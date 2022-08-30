
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



	
	
}


