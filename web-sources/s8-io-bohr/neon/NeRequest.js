
import { NeOutflow } from './NeOutflow';

/* <methods> */

export const XeForkProtocol = {
/**
 * 
 */
 SIGN_UP_METHOD : 0x15,

 /**
  * 
  */
	LOG_IN_METHOD : 0x16,
 
 
 /**
  * 
  */
	CTRL_METHOD : 0x17,
 
 
 /**
  * 
  */
	DEBUG_BOOT_METHOD : 0x26,
}


export const BOHR_RequestProtocol = {

 
 /* </methods> */
 
 
 /* <targets> */
 
 /**
  * 
  */
	VIEW_INDEX : 0x21,
 
	METHOD_CODE : 0x24,
 
 /* </targets> */
 
 
 /* <parameters> */
 
 
 /** param0 */
	PARAMETER_CODES : [0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47],
 
 /* <parameters> */
 
 
 /* <termination code> */
	TERMINATED : 0x72,

};





/**
 * 
 */
export class NeRequest {

	constructor(context) {
		// create array buffer of given capacity
		this.context = context;


		this.isForkDefined = false;
		this.isViewIndexDefined = false;
		this.isMethodCodeDefined = false;

		this.isParameterDefined = new Array(8);
		for(let i=0; i<8; i++){
			this.isParameterDefined[i] = false;
		}

		this.parameterValues = new Array(8);
	}


	setFork(fork){
		this.fork = fork;
		this.isForkDefined = true;
	}

	setViewIndex(index) {
		this.isViewIndexDefined = true;
		this.viewIndex = index;
	}

	setMethodCode(code) {
		this.isMethodCodeDefined = true;
		this.methodCode = code;
	}

	setByteParam(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishByte(value);
	}

	setUInt8Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishUInt8(value);
	}

	setUInt16Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishUInt16(value);
	}

	setInt16Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishInt16(value);
	}

	setUInt32Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishUInt32(value);
	}

	setInt32Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishInt32(value);
	}

	setUInt64Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishUInt64(value);
	}

	setInt64Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishInt64(value);
	}

	setFloat32Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishFloat32(value);
	}

	setFloat64Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishFloat64(value);
	}

	setL8StringASCIIParam(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishL8StringASCII(value);
	}

	setL32StringUTF8Param(slot, value) {
		this.isParameterDefined[slot] = true;
		this.parameterValues[slot] = inflow => inflow.publishL32StringUTF8(value);
	}

	

	/**
	 * 
	 * @param {*} callback 
	 */
	send(capacity, callback) {

		let arraybuffer = new ArrayBuffer(capacity);
		let outflow = new NeOutflow(arraybuffer);

		if(this.isForkDefined){
			switch (this.fork) {
				case "sign-up": outflow.putUInt8(XeForkProtocol.SIGN_UP_METHOD); break;
				case "log-in": outflow.putUInt8(XeForkProtocol.LOG_IN_METHOD); break;
				case "ctrl": outflow.putUInt8(XeForkProtocol.CTRL_METHOD); break;
				case "debug-boot": outflow.putUInt8(XeForkProtocol.DEBUG_BOOT_METHOD); break;
				default: throw "No code for such fork: "+fork;
			}
		}
		else{
			throw "fork code is mandatory";
		}

		if(this.isViewIndexDefined){
			outflow.putUInt8(BOHR_RequestProtocol.VIEW_INDEX);
			outflow.putInt64(this.viewIndex);
		}

		if(this.isMethodCodeDefined){
			outflow.putUInt8(BOHR_RequestProtocol.METHOD_CODE);
			outflow.putUInt8(this.methodCode);
		}

		for(let i=0; i<8; i++){
			if(this.isParameterDefined[i]){
				outflow.putUInt8(BOHR_RequestProtocol.PARAMETER_CODES[i]);
				this.parameterValues[i](outflow);
			}
		}
		
		// close
		outflow.putUInt8(BOHR_RequestProtocol.TERMINATED);

		// send
		this.context.sendRequest_POST(outflow.compact(), callback);
	}
}