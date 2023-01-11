

import { S8 } from '/s8-io-bohr/atom/S8.js';
import { BOHR_Keywords } from '/s8-io-bohr/atom/BOHR_Protocol.js';

import { NeObjectTypeHandler } from './NeObjectTypeHandler.js';
import { NeVertex } from './NeVertex.js';
import { NeObject } from './NeObject.js';

import { jump } from "./NeJump.js";


export class NeBranch {

	/**
	 * @type {Map<string, NeObjectTypeHandler>}
	 */
	objectTypesByName = new Map();


	/**
	 * @type {Map<number, NeObjectTypeHandler>}
	 */
	objectTypes = new Map();


	/**
	 * @type {Map<string, NeVertex>}
	 */
	vertices = new Map();


	/**
	 * Out of thin air!
	 */
	constructor() {

		/** views */
		this.vertices.set("NULL", null);

		/* <screen> */
		this.screenNode = document.createElement("div");
		document.body.appendChild(this.screenNode);
		/* </screen> */

		// create branch inbound
		//this.inbound = new NeBranchInbound(this);

		this.isVerbose = false;
	}

	/**
	 * 
	 * @param {ByteInflow} inflow 
	 */
	consume(inflow) {
		let code = inflow.getUInt8();
		if (code != BOHR_Keywords.OPEN_JUMP) {
			throw "Error: Can only start with a JUMP!!";
		}

		// perform jump
		jump(this, inflow);

	}


	/**
	 * 
	 * @param {string} id 
	 * @returns {NeVertex}
	 */
	getVertex(id) {
		return this.vertices.get(id);
	}

	/**
	 * 
	 * @param {string} id 
	 * @returns {NeObject}
	 */
	getObject(id) {
		let vertex = this.vertices.get(id);
		if (vertex == undefined) { throw `Failed to retrieve vertex for id=${id}`; }
		return vertex.object;
	}

	/**
	 * 
	 * @param {string} id 
	 * @param {NeVertex} node 
	 */
	setVertex(id, vertex) {
		if (!this.vertices.has(id)) {
			this.vertices.set(id, vertex);
		}
		else {
			throw "HARD overriding for node with index = " + id;
		}
	}

	deleteVertex(id) {
		let vertex = this.vertices.get(id);
		if (vertex) {
			let object = vertex.object;
			object.S8_dispose();
			this.vertices.delete(id);
		}
	}


	resolve(pathname) {
		return this.origin + pathname;
	}


	/**
	 * 
	 * @param {string} id 
	 * @param {number} slot 
	 */
	expose(id, slot) {
		if (slot != 0) { throw "Only slot 0 is available!"; }

		let vertex = this.getVertex(id);

		if (vertex == undefined) { throw `No object for id=${id}`; }
		let object = vertex.object;

		/* clear screen */
		S8.removeChildren(this.screenNode);

		/* redraw screen */
		this.screenNode.appendChild(object.getEnvelope());
	}


	/**
	 * 
	 * @param {ByteInflow} inflow 
	 * @returns {NeObjectTypeHandler}
	 */
	declareType(inflow) {
		let name = inflow.getStringUTF8();
		let code = inflow.getUInt7x();

		let objectType = new NeObjectTypeHandler(this.branch, name, code);

		// register by name
		this.objectTypesByName.set(name, objectType);

		// register by code
		this.objectTypes.set(code, objectType);

		return objectType;
	}



	render(){

		// render all controlled object
		this.vertices.forEach(vertex => {
			if(vertex != null){ // a NULL vertex is defined by default
				vertex.object.S8_render()
			}
		});
	}


}

