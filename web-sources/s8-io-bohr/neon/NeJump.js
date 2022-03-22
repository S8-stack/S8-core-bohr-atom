import { ByteInflow } from "s8-io-bytes/ByteInflow";
import { BOHR_Keywords } from "s8-io-bohr/atom/BOHR_Protocol";
import { NeBranch } from "./NeBranch";
import { NeBranchInbound } from "./NeBranchInbound";
import { NeFieldEntry } from "./NeFieldEntry";
import { NeObjectTypeHandler } from "./NeObjectTypeHandler";


export class NeJump {


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


    isConsumed = false;

    isBuilt = false;

    /**
     * @type {Function}
     */
    onBuilt;


    /**
     * 
     * @param {Function} onBuilt 
     */
    constructor(onBuilt) {
        this.onBuilt = onBuilt;
    }

    /**
     * 
     * @param {ByteInflow} inflow 
     */
    consume(inflow) {
        let code = 0;
        while (!this.isConsumed) {
            switch (code = inflow.getUInt8()) {

                case BOHR_Keywords.DECLARE_TYPE: this.consume_DECLARE_TYPE(inflow); break;

                case BOHR_Keywords.CREATE_NODE: this.consume_CREATE_NODE(inflow); break;

                case BOHR_Keywords.UPDATE_NODE: this.consume_UPDATE_NODE(inflow); break;

                case BOHR_Keywords.CLOSE_JUMP: this.isConsumed = true; break;

                default: "Code not supported for jump: " + code;
            }
        }

     

        // try to build
        this.build();
    }


    /**
     * 
     * @param {ByteInflow} inflow 
     */
    consume_DECLARE_TYPE(inflow) {
        let objectType = this.branchInbound.declareType(inflow);

        /* append to type loadings */
        let typeLoading = new NeObjectTypeHandlerLoading(this, objectType);
        this.typeLoadings.set(objectType.code, typeLoading);

        // immediately trigger loading
        typeLoading.load();
    }



    /**
     * 
     * @param {ByteInflow} inflow 
     */
    consume_CREATE_NODE(inflow) {
        let typeCode = inflow.getUInt7x();
        let id = inflow.getStringUTF8();

        let objectType = this.branchInbound.objectTypes.get(typeCode);
        let createObjectHandler = new CreateNeObjectHandler(id, entries);
        let entries = objectType.consume(createObjectHandler, inflow);


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
        let object = this.branchInbound.branch.getNode(id);
        let objectType = object.type;
        let entries = objectType.consume(inflow);

        let updateObjectHandler = new UpdateNeObjectHandler(id, entries);

        /* retrieve objet */
        let branch = this.branchInbound.branch;
        updateObjectHandler.resolve(branch);

        this.objectHandlers.push(updateObjectHandler);
    }


    areAllTypesLoadingsTerminated() {
        let areTerminated = true;
        this.typeLoadings.forEach(loading => {
            if (!loading.isTerminated) { areTerminated = false; }
        });
        return areTerminated;
    }


    build() {
        if (!this.isBuilt && this.isConsumed && this.areAllTypesLoaded()) {
            let branch = this.branchInbound.branch;

            /* set field values */
            this.objectHandlers.forEach(handler => handler.setFieldValues(branch));

            /* render all objects */
            this.objectHandlers.forEach(handler => handler.render());

            this.isBuilt = true;

            this.onBuilt();
        }
    }
}



export class NeObjectTypeHandlerLoading {


    /**
     * @type {NeJump}
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
     * @param {NeJump} jump 
     */
    load() {

        /* unresolved */
        if (!this.isLoaded && !this.typeHandler.isClassLoaded) {

            // so that jump can track loadings
            this.jump.typeLoadings.add(this.code);

            let _this = this;
            
            let onClassLoaded = function (_constructor) {

                // handler
                _this.typeHandler.setClass(_class);

                // set as loaded
                _this.isLoaded = true;

                /* instantiate what's missing */
			    let branch = _this.jump.branchInbound.branch;
                _this.instantiables.forEach(handler => handler.instantiate(branch, _this.typeHandler));

                _this.isTerminated = true;

                /* try to trigger build */
                _this.jump.build();
            }

            // trigger loading
            import(this.getTargetClassPathname()).then(module => onClassLoaded(module[classname]));
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
	 * @type {NeObject}
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
		if(!this.isInstantiated){
			this.object = typeHandler.createNewInstance(this.id);

			branch.setNode(this.id, this.object);

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
		this.object = branch.getNode(this.id);
	}

}