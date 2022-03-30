import { NeBranch } from "./NeBranch.js";
import { NeFieldHandler } from "./NeFieldHandler.js";
import { S8Object } from "../atom/S8Object.js";



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
	 * @param {S8Object} object 
	 */
	set(object, branch){
		this.fieldHandler.setValue(object, this.value, branch);
	}
}
