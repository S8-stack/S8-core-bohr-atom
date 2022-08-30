
import { NeBranch } from "./NeBranch.js";
import { NeObjectTypeHandler } from "./NeObjectTypeHandler.js";
import { NeObject } from "./NeObject.js";
import { fire } from "./NeFire.js";

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
     * @param {string} methodName 
     * @param {string} argValue 
     */
    fire(methodName, argValue){

    }

    setVoid(methodName){

    }

	/**
	 * 
	 * @param {string} methodName 
     * @param {boolean} argValue 
	 */
	runBool8(methodName, argValue) {
		let methodRunner = this.type.getBool8MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	runBool8Array(methodName, argValue) {
		let methodRunner = this.type.getBool8ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	runUInt8(methodName, argValue) {
		let methodRunner = this.type.getUInt8MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	setUInt8Array(methodName, argValue) {
		let methodRunner = this.type.getUInt8ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	setUInt16(methodName, argValue) {
		let methodRunner = this.type.getUInt16MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	setUInt16Array(methodName, argValue) {
		let methodRunner = this.type.getUInt16ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	setUInt32(methodName, argValue) {
		let methodRunner = this.type.getUInt32MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	setUInt32Array(methodName, argValue) {
		let methodRunner = this.type.getUInt32ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	setUInt64(methodName, argValue) {
		let methodRunner = this.type.getUInt64MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	setUInt64Array(methodName, argValue) {
		let methodRunner = this.type.getUInt64ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	
	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setInt8(methodName, argValue) {
		let methodRunner = this.type.getInt8MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    setInt8Array(methodName, argValue) {
		let methodRunner = this.type.getInt8ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setInt16(methodName, argValue) {
		let methodRunner = this.type.getInt16MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    setInt16Array(methodName, argValue) {
		let methodRunner = this.type.getInt16ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setInt32(methodName, argValue) {
		let methodRunner = this.type.getInt32MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setInt32Array(methodName, argValue) {
		let methodRunner = this.type.getInt16ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setInt64(methodName, argValue) {
		let methodRunner = this.type.getInt64ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setInt64Array(methodName, argValue) {
		let methodRunner = this.type.getInt64MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setFloat32(methodName, argValue) {
		let methodRunner = this.type.getFloat32MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    setFloat32Array(methodName, argValue) {
		let methodRunner = this.type.getFloat32ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setFloat64(methodName, argValue) {
		let methodRunner = this.type.getFloat64MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    setFloat64Array(methodName, argValue) {
		let methodRunner = this.type.getFloat64ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    setStringUTF8(methodName, argValue) {
		let methodRunner = this.type.getStringUTF8MethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}

	
    /**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    setStringUTF8Array(methodName, argValue) {
		let methodRunner = this.type.getStringUTF8ArrayMethodRunner(methodName);
        fire(id, methodRunner, argValue);
	}

}