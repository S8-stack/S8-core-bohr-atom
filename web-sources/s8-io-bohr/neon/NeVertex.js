

import { ByteInflow } from "/s8-io-bytes/ByteInflow.js";
import { ByteOutflow } from "/s8-io-bytes/ByteOutflow.js";

import { S8 } from "/s8-io-bohr/atom/S8.js";
import { BOHR_Keywords, BOHR_Methods } from "/s8-io-bohr/atom/BOHR_Protocol.js";

import { NeBranch } from "./NeBranch.js";
import { NeObject } from "./NeObject.js";
import { NeObjectTypeHandler } from "./NeObjectTypeHandler.js";
import { NeMethodRunner } from "./NeMethodRunner.js";

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




    runVoid(methodName){
        let methodRunner = this.type.getVoidMethodRunner(methodName);
        this.shoot(methodRunner);
    }

	/**
	 * 
	 * @param {string} methodName 
     * @param {boolean} argValue 
	 */
	runBool8(methodName, argValue) {
		let methodRunner = this.type.getBool8MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	runBool8Array(methodName, argValue) {
		let methodRunner = this.type.getBool8ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	runUInt8(methodName, argValue) {
		let methodRunner = this.type.getUInt8MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	runUInt8Array(methodName, argValue) {
		let methodRunner = this.type.getUInt8ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	runUInt16(methodName, argValue) {
		let methodRunner = this.type.getUInt16MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	runUInt16Array(methodName, argValue) {
		let methodRunner = this.type.getUInt16ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	runUInt32(methodName, argValue) {
		let methodRunner = this.type.getUInt32MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	runUInt32Array(methodName, argValue) {
		let methodRunner = this.type.getUInt32ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
	runUInt64(methodName, argValue) {
		let methodRunner = this.type.getUInt64MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
	runUInt64Array(methodName, argValue) {
		let methodRunner = this.type.getUInt64ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	
	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runInt8(methodName, argValue) {
		let methodRunner = this.type.getInt8MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    runInt8Array(methodName, argValue) {
		let methodRunner = this.type.getInt8ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runInt16(methodName, argValue) {
		let methodRunner = this.type.getInt16MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    runInt16Array(methodName, argValue) {
		let methodRunner = this.type.getInt16ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runInt32(methodName, argValue) {
		let methodRunner = this.type.getInt32MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runInt32Array(methodName, argValue) {
		let methodRunner = this.type.getInt16ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runInt64(methodName, argValue) {
		let methodRunner = this.type.getInt64ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runInt64Array(methodName, argValue) {
		let methodRunner = this.type.getInt64MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runFloat32(methodName, argValue) {
		let methodRunner = this.type.getFloat32MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    runFloat32Array(methodName, argValue) {
		let methodRunner = this.type.getFloat32ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runFloat64(methodName, argValue) {
		let methodRunner = this.type.getFloat64MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}



	/**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    runFloat64Array(methodName, argValue) {
		let methodRunner = this.type.getFloat64ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


	/**
	 * 
	 * @param {string} methodName 
     * @param {number} argValue 
	 */
    runStringUTF8(methodName, argValue) {
		let methodRunner = this.type.getStringUTF8MethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}

	
    /**
	 * 
	 * @param {string} methodName 
     * @param {Array} argValue 
	 */
    runStringUTF8Array(methodName, argValue) {
		let methodRunner = this.type.getStringUTF8ArrayMethodRunner(methodName);
        this.shoot(methodRunner, argValue);
	}


    /**
     * 
     * @param {NeMethodRunner} method 
     * @param {*} arg 
     */
    shoot(method, arg) {

        let requestArrayBuffer = new ArrayBuffer(64);
        let outflow = new ByteOutflow(requestArrayBuffer);
        outflow.putUInt8(BOHR_Methods.WEB_RUN_FUNC);
        
        
		/* <declare-method> */
		
		// publish method if necessary
        method.publish_DECLARE_METHOD(outflow);
		/* <declare-method> */
    
		/* <run-method> */

        // shoot id
        outflow.putUInt8(BOHR_Keywords.RUN_METHOD);
        
        // object/vertex id
        outflow.putStringUTF8(this.id);
    
        // method code
        outflow.putUInt8(method.code);
    
        // pass args
        method.produceValue(outflow, arg);
    
        outflow.putUInt8(BOHR_Keywords.CLOSE_JUMP);
    
        S8.sendRequest_HTTP2_POST(requestArrayBuffer, function(responseArrayBuffer){
            let inflow = new ByteInflow(responseArrayBuffer);
            S8.branch.consume(inflow);
        });
		/* </run-method> */

    };
}