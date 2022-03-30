
import { ByteInflow } from "/s8-io-bytes/ByteInflow.js";
import { BOHR_Keywords } from "/s8-io-bohr/atom/BOHR_Protocol.js";
import { NeBranch } from "./NeBranch.js";
import { NeObjectTypeHandler } from "./NeObjectTypeHandler.js";
import { jump } from "./NeJump.js";



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
		let code = inflow.getUInt8();
		if(code != BOHR_Keywords.OPEN_JUMP){
			throw "Error: Can only start with a JUMP!!";
		}

		// perform jump
		jump(this, inflow, function(){});
		
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


