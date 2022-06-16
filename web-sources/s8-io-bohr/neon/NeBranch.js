
import { NeObjectTypeHandler } from './NeObjectTypeHandler.js';
import { S8 } from '../atom/S8.js';
import { NeBranchInbound } from './NeBranchInbound.js';
import { NeVertex } from './NeVertex.js';
import { NeObject } from './NeObject.js';


export class NeBranch {



	/**
	 * @type {Map<string, NeObjectTypeHandler>}
	 */
	objectTypes = new Map();

	/**
	 * @type {Map<string, NeVertex>}
	 */
	vertices = new Map();


	/**
	 * @type {NeBranchInbound}
	 */
	inbound;


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
		this.inbound = new NeBranchInbound(this);

		this.isVerbose = false;
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
		if(vertex == undefined){ throw `Failed to retrieve vertex for id=${id}`; }
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

}

