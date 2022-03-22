import { NeObjectTypeHandler } from "./NeObjectTypeHandler";



/**
 * 
 */
export class NeObject {


    /**
     * @type {string}
     */
    id;


    /**
     * @type {NeObjectTypeHandler}
     */
    type;


    /**
     * 
     * @param {string} id 
     * @param {NeObjectTypeHandler} type 
     */
    constructor(id, type){
        this.id = id;
        this.type = type;
    }

}
