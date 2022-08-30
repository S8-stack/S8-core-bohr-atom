



export class NeBranchOutbound {


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