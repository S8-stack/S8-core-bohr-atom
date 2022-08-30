
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


    /**
     * 
     * @param {string} fieldName 
     * @param {string} fieldValue 
     */
    fire(fieldName, fieldValue){

    }

    setVoid(fieldName){

    }

	/**
	 * 
	 * @param {string} fieldName 
     * @param {boolean} fieldValue 
	 */
	setBool8(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
	setBool8Array(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
	setUInt8(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
	setUInt8Array(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
	setUInt16(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
	setUInt16Array(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
	setUInt32(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
	setUInt32Array(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
	setUInt64(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
	setUInt64Array(fieldName, fieldValue) {
		
	}


	
	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setInt8(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
    setInt8Array(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setInt16(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
    setInt16Array(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setInt32(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setInt32Array(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setInt64(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setInt64Array(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setFloat32(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
    setFloat32Array(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setFloat64(fieldName, fieldValue) {
		
	}



	/**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
    setFloat64Array(fieldName, fieldValue) {
		
	}


	/**
	 * 
	 * @param {string} fieldName 
     * @param {number} fieldValue 
	 */
    setStringUTF8(fieldName, fieldValue) {
		
	}

	
    /**
	 * 
	 * @param {string} fieldName 
     * @param {Array} fieldValue 
	 */
    setStringUTF8Array(fieldName, fieldValue) {
		
	}

}