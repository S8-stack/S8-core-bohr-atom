
import { ByteInflow } from 's8-io-bytes/ByteInflow';
import { BOHR_Keywords } from '../atom/BOHR_Protocol';
import { NeInflow } from './NeInflow';
import { NeLexicon } from './NeLexicon';
import { NeObjectTypeHandler } from './NeObjectTypeHandler';
import { NeRequest } from './NeRequest';
import { NeObject } from './NeObject';
import { CreateNeObjectHandler } from './NeObjectHandler';

export class NeBranch {



	/**
	 * @type {Map<string, NeObjectTypeHandler>}
	 */
	 objectTypes = new Map();

	/**
	 * @type {Map<string, NeObject>}
	 */
	nodes = new Map();


	/**
	 * Out of thin air!
	 */
	constructor() {

		// BOHR
		this.BOHR_lexicon = new NeLexicon(this);

		/** views */
		this.nodes.set("NULL", null);

		/* <screen> */
		this.screenNode = document.createElement("div");
		document.body.appendChild(this.screenNode);
		/* </screen> */


		this.isVerbose = false;
	}


	/**
	 * 
	 * @param {string} id 
	 * @returns {NeObject}
	 */
	getNode(id) {
		return this.nodes.get(id);
	}


	setNode(id, node) {
		if (!this.nodes.has(id)) {
			this.nodes.set(id, node);
		}
		else {
			throw "HARD overriding for node with index = " + id;
		}
	}

	deleteNode(id) {
		let orbital = this.nodes.get(id);
		if (orbital) {
			orbital.S8_dispose();
			this.nodes.delete(id);
		}
	}


	resolve(pathname) {
		return this.origin + pathname;
	}



}

