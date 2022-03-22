import { NeBranch } from "./NeBranch";
import { NeFieldHandler } from "./NeFieldHandler";
import { NeObject } from "./NeObject";



export class NeFieldEntry {


	/**
	 * @type {NeFieldHandler} 
	 */
	fieldHandler;

	/**
	 * @type { * }
	 */
	value;

	/**
	 * 
	 * @param {NeFieldHandler} handler 
	 * @param {*} value 
	 */
    constructor(fieldHandler, value){
		this.fieldHandler = fieldHandler;
        this.value = value;
    }

	/**
	 * @param {NeBranch} branch  
	 * @param {NeObject} object 
	 */
	set(object, branch){
		this.fieldHandler.setValue(object, this.value, branch);
	}
}
