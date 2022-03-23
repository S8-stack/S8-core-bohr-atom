
import { NeObjectTypeHandler } from './NeObjectTypeHandler';
import { S8Object } from '../atom/S8Object';
import { S8 } from '../atom/S8.js';
import { NeBranchInbound } from './NeBranchInbound.js';

export class NeBranch {



	/**
	 * @type {Map<string, NeObjectTypeHandler>}
	 */
	 objectTypes = new Map();

	/**
	 * @type {Map<string, S8Object>}
	 */
	objects = new Map();


	/**
	 * @type {NeBranchInbound}
	 */
	inbound;


	/**
	 * Out of thin air!
	 */
	constructor() {

		/** views */
		this.objects.set("NULL", null);

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
	 * @returns {S8Object}
	 */
	getObject(id) {
		return this.objects.get(id);
	}


	/**
	 * 
	 * @param {string} id 
	 * @param {S8Object} node 
	 */
	setObject(id, node) {
		if (!this.objects.has(id)) {
			this.objects.set(id, node);
		}
		else {
			throw "HARD overriding for node with index = " + id;
		}
	}

	deleteObject(id) {
		let object = this.objects.get(id);
		if (object) {
			object.S8_dispose();
			this.objects.delete(id);
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
	expose(id, slot){
		if(slot != 0) { throw "Only slot 0 is available!"; }
		let node = this.objects.get(id);
		S8.removeChildren(this.screenNode);
		this.screenNode.appendChild(node.getEnvelope());
	}

}

