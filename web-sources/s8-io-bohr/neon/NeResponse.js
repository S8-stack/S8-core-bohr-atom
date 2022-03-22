

import { NeInflow } from "./NeInflow";
import { CreateNeOrbitalHandler, UpdateNeOrbitalHandler } from "./NeLexicon";
import { NeSphere } from "./NeBranch";



/**
 * 
 */
export class NeResponse {


    /**
     * 
     * @param {NeSphere} sphere 
     * @param {NeInflow} inflow 
     */
    constructor(sphere, inflow) {
        this.sphere = sphere;

        this.inflow = inflow;

        this.objectHandlers = new Array();
        this.nInstantiated = 0;

        // create bindings
        this.bindings = new Array();

        // root definition
        this.isRootRedefined = false;
        this.rootIndex;

        this.isTerminated = false;
    }


    /**
     * Step 0:
     * 
     */
    parse() {


        // code
        if (this.inflow.getUInt8() != BOHR_ResponseProtocol.OPEN_DELTA) {
            throw "Inconsistent delta open tag";
        }

        let isClosed = false;
        while (!isClosed) {
            let code = this.inflow.getUInt8();
            switch (code) { // retrieve code

                case BOHR_ResponseProtocol.REDEFINE_ROOT:
                    this.redefineRoot();
                    break;

                // create a new node
                case BOHR_ResponseProtocol.DECLARE_TYPE_AND_CREATE_NODE:
                    this.declareTypeAndCreateNode();
                    break;

                // create a new node
                case BOHR_ResponseProtocol.CREATE_NODE:
                    this.createNode();
                    break;

                // update an already existing one
                case BOHR_ResponseProtocol.UPDATE_NODE:
                    this.updateNode();
                    break;

                // safely delete an already existing one
                case BOHR_ResponseProtocol.DELETE_NODE:
                    this.deleteNode();
                    break;

                // close delta 
                case BOHR_ResponseProtocol.CLOSE_DELTA:
                    isClosed = true;
                    break;

                default: throw "Failed to match Bohr Protocol prompter, code= " + code;
            }
        }

        this.instantiate();

    }


    redefineRoot() {
        this.isRootRedefined = true;
        this.rootIndex = this.inflow.retrieve();
    }

    declareTypeAndCreateNode() {

        // declarator
        let typeCode = this.inflow.retrieve();
        let typeClasspath = this.inflow.retrieve();
        let typeHandler = this.sphere.BOHR_lexicon.define(typeCode, typeClasspath);

        // retrieve index
        let id = this.inflow.retrieve();

        // parse node field changes
        let entries = this.parseViewBody();

        // store object handler
        this.objectHandlers.push(new CreateNeOrbitalHandler(id, typeHandler, entries));
    }


    createNode() {

        // retrieve type
        let typeCode = this.inflow.retrieve();
        let typeHandler = this.sphere.BOHR_lexicon.get(typeCode);

        // retrieve index
        let id = this.inflow.retrieve();

        // parse node field changes
        let entries = this.parseViewBody();

        // store object handler
        this.objectHandlers.push(new CreateNeOrbitalHandler(id, typeHandler, entries));
    }


    updateNode() {

        // index
        let id = this.inflow.retrieve();
        let object = this.sphere.getOrbital(id);
        if (object == undefined) {
            throw "Failed to retrieve node for hashcode: " + id;
        }

        // parse node field changes
        let entries = this.parseViewBody();

        // store object handler
        this.objectHandlers.push(new UpdateNeOrbitalHandler(object, entries));
    }


    
    deleteNode() {
        // index
        let id = this.inflow.retrieve();
        this.sphere.deleteNode(id);
    }


    parseViewBody() {

        let isScopeActive = true;
        let entries = [];
        while (isScopeActive) {
            let code = this.inflow.getUInt8();
            switch (code) {

                case BOHR_ResponseProtocol.SET_FIELD_VALUE:
                    entries.push(this.parseField());
                    break;

                case BOHR_ResponseProtocol.CLOSE_NODE:
                    isScopeActive = false;
                    break;

                default: throw "Failed to match switch code";
            }
        }
        return entries;
    }


    parseField() {

        let entry = {};

        // retrieve field code
        entry.code = this.inflow.getUInt8();

        // retrieve field value
        this.inflow.retrieveToTarget(entry, this.bindings);

        // set value
        //view.set(fieldCode, fieldValue, bindings);
        return entry;
    }


    /**
     * Step 1:
     */
    instantiate() {
        // declare and register a new object
        let _this = this;

        this.objectHandlers.forEach(objectHandler => {

            objectHandler.onInstantiated = function () {
                _this.nInstantiated += 1;
                _this.compile();
            };

            objectHandler.instantiate(this.sphere);
        });
    }


    areObjectsInstantiated() {
        if (this.nInstantiated == this.objectHandlers.length) {
            for (let objectHandler of this.objectHandlers) {
                if (!objectHandler.isInstantiated) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Step 2:
     */
    compile() {
        if (!this.isTerminated && this.areObjectsInstantiated()) {

            // release bindings
            this.bindings.forEach(binding => { binding.bind(this.sphere); });

            // set fields
            this.objectHandlers.forEach(objectHandler => objectHandler.setFields());

            // change root if applicable
            if (this.isRootRedefined) {
                let view = this.sphere.getOrbital(this.rootIndex);
                if (view != undefined && view != null) {
                    this.sphere.setRoot(view.getEnvelope());
                }
                else {
                    throw "Cannot change root";
                }
            }
            this.isTerminated = true;

            // render fields
            this.objectHandlers.forEach(objectHandler => objectHandler.render());
        }
    }
}