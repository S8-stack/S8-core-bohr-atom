
import { NeFieldParser } from "./NeFieldParser.js";
import { NeVertex } from "./NeVertex.js";



export class NeFieldEntry {


	/**
	 * @type {NeFieldParser} 
	 */
	fieldParser;

	/**
	 * @type { * }
	 */
	value;

	/**
	 * 
	 * @param {NeFieldParser} handler 
	 * @param {*} value 
	 */
    constructor(fieldHandler, value){
		this.fieldParser = fieldHandler;
        this.value = value;
    }

	/**
	 * @param {NeVertex} vertex 
	 */
	set(vertex){
		this.fieldParser.setValue(vertex, this.value);
	}
}
