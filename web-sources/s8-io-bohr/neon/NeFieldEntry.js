
import { NeFieldHandler } from "./NeFieldHandler.js";
import { NeVertex } from "./NeVertex.js";



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
	 * @param {NeVertex} vertex 
	 */
	set(vertex){
		this.fieldHandler.setValue(vertex, this.value);
	}
}
