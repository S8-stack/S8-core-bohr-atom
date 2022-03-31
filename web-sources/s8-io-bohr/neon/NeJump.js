import { ByteInflow } from "/s8-io-bytes/ByteInflow.js";
import { BOHR_Keywords } from "/s8-io-bohr/atom/BOHR_Protocol.js";
import { NeBranch } from "./NeBranch.js";
import { NeBranchInbound } from "./NeBranchInbound.js";
import { NeFieldEntry } from "./NeFieldEntry.js";
import { NeObjectTypeHandler } from "./NeObjectTypeHandler.js";
import { S8Object } from "../atom/S8Object.js";




/**
 * 
 * @param {NeBranchInbound} branchInbound 
 * @param {ByteInflow} inflow 
 * @param {Function} onBuilt 
 */
export const jump = function (branchInbound, inflow, onBuilt) {
    let jumpScope = new NeJumpScope(branchInbound);
    jumpScope.consume(inflow, onBuilt);
}

class NeJumpScope {

    /**
     * @type {NeBranchInbound}
     */
    branchInbound;

    /**
     * Object types for which this jump is waiting loading completion
     * @type {Map<number, NeObjectTypeHandlerLoading>}
     */
    typeLoadings = new Map();


    /**
     * @type {NeObjectHandler[]}
     */
    objectHandlers = new Array();


    /**
     * @type {ExposeNeSlotHandler[]}
     */
    slotHandlers = new Array();


    isConsumed = false;

    isBuilt = false;

    /**
     * @type {NeBranchInbound}
     */
    constructor(branchInbound) {
        this.branchInbound = branchInbound
    }

    /**
     * 
     * @param {ByteInflow} inflow 
     * @param {Function} onBuilt
     */
    consume(inflow, onBuilt) {
        let code = 0;
        while (!this.isConsumed) {
            switch (code = inflow.getUInt8()) {

                case BOHR_Keywords.DECLARE_TYPE: this.consume_DECLARE_TYPE(inflow, onBuilt); break;

                case BOHR_Keywords.CREATE_NODE: this.consume_CREATE_NODE(inflow); break;

                case BOHR_Keywords.UPDATE_NODE: this.consume_UPDATE_NODE(inflow); break;

                case BOHR_Keywords.EXPOSE_NODE: this.consume_EXPOSE_NODE(inflow); break;

                case BOHR_Keywords.CLOSE_JUMP: this.isConsumed = true; break;

                default: "Code not supported for jump: " + code;
            }
        }

        // try to build
        this.build(onBuilt);
    }


    /**
     * 
     * @param {ByteInflow} inflow 
     * @param {Function} onBuilt
     */
    consume_DECLARE_TYPE(inflow, onBuilt) {
        let objectType = this.branchInbound.declareType(inflow);

        /* append to type loadings */
        let typeLoading = new NeObjectTypeHandlerLoading(this, objectType);
        this.typeLoadings.set(objectType.code, typeLoading);

        // immediately trigger loading
        typeLoading.load(onBuilt);
    }



    /**
     * 
     * @param {ByteInflow} inflow 
     */
    consume_CREATE_NODE(inflow) {
        let typeCode = inflow.getUInt7x();
        let id = inflow.getStringUTF8();

        /** @type {NeObjectTypeHandler} */
        let objectType = this.branchInbound.objectTypes.get(typeCode);

        let entries = objectType.consumeEntries(inflow);

        let createObjectHandler = new CreateNeObjectHandler(id, entries);

        /* test if a type loading is in progress */
        let typeLoading = this.typeLoadings.get(typeCode);
        if (typeLoading != undefined) {
            /* will ultimately trigger object instantiation */
            typeLoading.addInstantiable(createObjectHandler);
        }

        this.objectHandlers.push(createObjectHandler);
    }


    /**
     * 
     * @param {ByteInflow} inflow 
     */
    consume_UPDATE_NODE(inflow) {
        let id = inflow.getStringUTF8();
        let object = this.branchInbound.branch.getObject(id);
        let objectType = object.S8_type;
        let entries = objectType.consumeEntries(inflow);

        let updateObjectHandler = new UpdateNeObjectHandler(id, entries);

        /* retrieve objet */
        let branch = this.branchInbound.branch;
        updateObjectHandler.resolve(branch);

        this.objectHandlers.push(updateObjectHandler);
    }


    consume_EXPOSE_NODE(inflow) {
        let id = inflow.getStringUTF8();
        let slot = inflow.getUInt8();
        this.slotHandlers.push(new ExposeNeSlotHandler(id, slot));
    }


    areAllTypesLoadingsTerminated() {
        let areTerminated = true;
        this.typeLoadings.forEach(loading => {
            if (!loading.isTerminated) { areTerminated = false; }
        });
        return areTerminated;
    }


    /**
     * @param {Function} onBuilt
     */
    build(onBuilt) {
        if (!this.isBuilt && this.isConsumed && this.areAllTypesLoadingsTerminated()) {
            let branch = this.branchInbound.branch;

            /* set field values */
            this.objectHandlers.forEach(handler => handler.setFieldValues(branch));

            /* exposed */
            this.slotHandlers.forEach(handler => handler.setSlot(branch));

            /* render all objects */
            this.objectHandlers.forEach(handler => handler.render());

            this.isBuilt = true;

            onBuilt();
        }
    }
}



class NeObjectTypeHandlerLoading {


    /**
     * @type {NeJumpScope}
     */
    jump;


    /**
     * @type { NeObjectTypeHandler }
     */
    typeHandler;



    /**
     * @type { CreateNeObjectHandler[] }
     */
    instantiables = new Array();


    /**
     * 0 = unresolved, 1 = loading-in-progres, 2 = building, 3 = built
     * @type {boolean}
     */
    isLoaded = false;


    isTerminated = false;

    constructor(jump, typeHandler) {
        this.jump = jump;
        this.typeHandler = typeHandler;
    }

    /**
     * Can only be launched by a consume_DECLARE_TYPE statement
     * @param {NeJumpScope} jump 
     * @param {Function} onBuilt
     */
    load(onBuilt) {

        /* unresolved */
        if (!this.isLoaded && !this.typeHandler.isClassLoaded) {

            // so that jump can track loadings
            this.jump.typeLoadings.set(this.code, this);

            let _this = this;
            let typeHandler = _this.typeHandler;

            let onClassLoaded = function (_class) {

                // handler
                typeHandler.setClass(_class);

                // set as loaded
                _this.isLoaded = true;

                /* instantiate what's missing */
                let branch = _this.jump.branchInbound.branch;
                _this.instantiables.forEach(handler => handler.instantiate(branch, typeHandler));

                _this.isTerminated = true;

                /* try to trigger build */
                _this.jump.build(onBuilt);
            }

            // trigger loading
            import(typeHandler.getTargetClassPathname()).
                then(module => onClassLoaded(module[typeHandler.classname])).
                catch(function (reason) {
                    console.error(`[NEON] Failed to load: ${typeHandler.getTargetClassPathname()}, due to: ${reason}`);
                });
        }
        else if (this.typeHandler.isClassLoaded) {
            this.isLoaded = true;
            this.isTerminated = true;
        }
    }

    /**
     * 
     * @param {CreateNeObjectHandler} objectHandler 
     */
    addInstantiable(objectHandler) {
        if (!this.isLoaded) {
            this.instantiables.push(objectHandler);
        }
        else {
            objectHandler.instantiate(this.jump);
        }
    }

}




/**
 * 
 */
class NeObjectHandler {

    /**
     * @type {string}
     */
    id;


    /**
     * @type {NeObjectTypeHandler}
     */
    typeHandler;


    /**
     * @type {NeFieldEntry[]}
     */
    entries;

    /**
     * @type {S8Object}
     */
    object;


    /**
     *  
     * @param {string} id 
     * @param {NeFieldEntry[]} entries 
     */
    constructor(id, entries) {
        this.id = id;
        this.entries = entries;
    }


    /**
     * 
     * @param {NeBranch} branch 
     */
    setFieldValues(branch) {
        let object = this.object;
        this.entries.forEach(entry => { entry.set(object, branch); });
    }

    render() {
        this.object.S8_render();
    }
}

/**
 * 
 */
class CreateNeObjectHandler extends NeObjectHandler {


    /**
     * @type {boolean}
     */
    isInstantiated;

    /**
	
     * @param {NeFieldEntry[]} entries 
     */
    constructor(id, entries) {
        super(id, entries);
        this.isInstantiated = false;
    }


    /**
     * 
     * @param {NeBranch} branch 
     * @param {NeObjectTypeHandler} typeHandler 
     */
    instantiate(branch, typeHandler) {
        if (!this.isInstantiated) {
            this.object = typeHandler.createNewInstance(this.id);

            branch.setObject(this.id, this.object);

            this.isInstantiated = true;
        }
    }

}

/**
 * 
 */
class UpdateNeObjectHandler extends NeObjectHandler {

    constructor(id, entries) {
        super(id, entries);
    }


    /**
     * 
     * @param {NeBranch} branch 
     */
    resolve(branch) {
        this.object = branch.getObject(this.id);
    }

}


class ExposeNeSlotHandler {

    /**
     * @type {string}
     */
    id;

    /**
   * @type {number}
   */
    slot;

    constructor(id, slot) {
        this.id = id;
        this.slot = slot;
    }


    /**
     * 
     * @param {NeBranch} branch 
     */
    setSlot(branch) {
        branch.expose(this.id, this.slot);
    }

}