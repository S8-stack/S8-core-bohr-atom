import { ByteOutflow } from "/s8-io-bytes/ByteOutflow.js";

import { BOHR_Keywords } from '/s8-io-bohr/atom/BOHR_Protocol.js';
import { BOHR_Methods } from '/s8-io-bohr/atom/BOHR_Protocol.js';

import { NeMethodRunner } from "./NeMethodRunner.js";






/**
 * @param {string} id
 * @param {NeMethodRunner} method 
 * @param {*} arg 
 */
export const fire = function(id, method, arg) {

    let requestArrayBuffer = new ArrayBuffer(64);
    let outflow = new ByteOutflow(requestArrayBuffer);
    outflow.putUInt8(BOHR_Methods.WEB_RUN_FUNC);
    
    // publish method if necessary
    method.publish_DECLARE_METHOD();

    // shoot id
    outflow.putUInt8(BOHR_Keywords.RUN_METHOD);
    
    // object/vertex id
    outflow.putStringUTF8(id);

    // method code
    outflow.putUInt8(method.code);

    // pass args
    method.produceValue(outflow, arg);

    outflow.putUInt8(BOHR_Keywords.CLOSE_JUMP);

    S8.sendRequest_HTTP2_POST(requestArrayBuffer, function(responseArrayBuffer){
        let inflow = new ByteInflow(responseArrayBuffer);
        S8.branch.consume(inflow);
    });

};