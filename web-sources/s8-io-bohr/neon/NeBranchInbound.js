
import { ByteInflow } from "s8-io-bytes/ByteInflow.js";
import { BOHR_Keywords } from "s8-io-bohr/atom/BOHR_Protocol";
import { NeBranch } from "./NeBranch";
import { CreateNeObjectHandler } from "./NeObjectHandler";
import { NeObjectTypeHandler } from "./NeObjectTypeHandler";



export class NeBranchInbound {

	/**
	 * @type {NeBranch} reference to parent branch
	 */
	branch;



	/**
	 * @type {Map<number, NeObjectTypeHandler>}
	 */
	objectTypes = new Map();


	/**
	 * 
	 * @param {NeBranch} branch 
	 */
	constructor(branch) {
		this.branch = branch;
	}


	/**
	 * 
	 * @param {ByteInflow} inflow 
	 */
	consume(inflow) {

		
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
		this.branch.objectTypes.set(name, objectType);

		// register by code
		this.objectTypes.set(code, objectType);

		return objectType;
	}

	
}


