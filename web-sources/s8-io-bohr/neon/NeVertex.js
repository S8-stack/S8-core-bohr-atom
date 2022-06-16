
import { NeBranch } from "./NeBranch.js";
import { NeObjectTypeHandler } from "./NeObjectTypeHandler.js";
import { NeObject } from "./NeObject.js";

/**
 * 
 */
export class NeVertex {

    /**
     * @type {NeBranch}
     */
    branch;

    /**
     * Automatically assigned by NeObjectTypeHandler
     * 
     * @type {string}
     */
    id;

    /**
     * @type {NeObjectTypeHandler}
     */
    type;


    /**
     * @type {NeObject}
     */
    object;

    /**
     * 
     * @param {NeBranch} branch 
     * @param {string} id 
     * @param {NeObjectTypeHandler} type
     * @param {NeObject} object
     */
    constructor(branch, id, type, object) {
        this.branch = branch;
        this.id = id;
        this.type = type;
        this.object = object;
    }

}