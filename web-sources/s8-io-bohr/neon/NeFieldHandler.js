

import { ByteInflow } from "/s8-io-bytes/ByteInflow.js";

import { BOHR_Types } from "/s8-io-bohr/atom/BOHR_Protocol.js";
import { NeFieldEntry } from "./NeFieldEntry";
import { S8Object } from "../atom/S8Object";
import { NeBranch } from "./NeBranch.js";



export class NeFieldHandler {


    /**
     * @type {string}
     */
    name = "(undefined)";


    /**
     * @type {number}
     */
    code = -1;


    /**
     * @type {boolean}
     */
    isLinked = false;


    /**
     * @type {Function}
     */
    setter = null;




    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldHandler}
     */
    static consumeFormat(inflow) {

        let code = inflow.getUInt8();

        switch (code) {

            /* <structure> */
            case BOHR_Types.ARRAY: return NeFieldHandler.buildArray(inflow);
            /* </structure> */

            /* <bytes> */
            case BOHR_Types.SERIAL: throw "Unsupported serial";
            /* </bytes> */


            /* <booleans> */
            case BOHR_Types.BOOL8: return new Bool8NeFieldHandler();
            /* </booleans> */

            /* <unsigned-integers> */
            case BOHR_Types.UINT8: return new UInt8NeFieldHandler();
            case BOHR_Types.UINT16: return new UInt16NeFieldHandler();
            case BOHR_Types.UINT32: return new UInt32NeFieldHandler();
            case BOHR_Types.UINT64: return new UInt64NeFieldHandler();
            /* </unsigned-integers> */

            /* <signed-integers> */
            case BOHR_Types.INT8: return new Int8NeFieldHandler();
            case BOHR_Types.INT16: return new Int16NeFieldHandler();
            case BOHR_Types.INT32: return new Int32NeFieldHandler();
            case BOHR_Types.INT64: return new Int64NeFieldHandler();
            /* </signed-integers> */

            /* <float> */
            case BOHR_Types.FLOAT32: return new Float32NeFieldHandler();
            case BOHR_Types.FLOAT64: return new Float64NeFieldHandler();
            /* </float> */

            /* <string> */
            case BOHR_Types.STRING_UTF8: return new StringUTF8NeFieldHandler();
            /* </string> */

            /* <object> */
            case BOHR_Types.S8OBJECT: return new S8ObjectNeFieldHandler();
            /* </object> */

            default: throw "Unsupported BOHR type code: " + code;
        }
    }

    static buildArray(inflow) {
        let code = inflow.getUInt8();
        switch (code) {

            case BOHR_Types.BOOL8: return new Bool8ArrayNeFieldHandler();

            case BOHR_Types.UINT8: return new UInt8ArrayNeFieldHandler();
            case BOHR_Types.UINT16: return new UInt16ArrayNeFieldHandler();
            case BOHR_Types.UINT32: return new UInt32ArrayNeFieldHandler();
            case BOHR_Types.UINT64: return new UInt64ArrayNeFieldHandler();

            case BOHR_Types.INT8: return new Int8ArrayNeFieldHandler();
            case BOHR_Types.INT16: return new Int16ArrayNeFieldHandler();
            case BOHR_Types.INT32: return new Int32ArrayNeFieldHandler();
            case BOHR_Types.INT64: return new Int64ArrayNeFieldHandler();

            case BOHR_Types.FLOAT32: return new Float32ArrayNeFieldHandler();
            case BOHR_Types.FLOAT64: return new Float64ArrayNeFieldHandler();

            case BOHR_Types.STRING_UTF8: return new StringUTF8ArrayNeFieldHandler();
            case BOHR_Types.S8OBJECT: return new S8ObjectArrayNeFieldHandler();

            default: throw "Unsupported BOHR ARRAY type code: " + code;
        }
    }



    static buildSerial(inflow) {
        let code = inflow.getUInt8();
        switch (inflow.getUInt8()) {
            default: throw "Unsupported BOHR SERIAL type code: " + code;
        }
    }



    /**
     * 
     * @param {Class} objectClass 
     */
    link(objectClass) {
        if(!this.isLinked){
            this.type = objectClass;

            // resolve setter
            let setMethod = this.type.prototype["S8_set_" + this.name];
            if(setMethod == undefined){
                throw "Failed to link against method for "+this.name+" in "+objectClass;
            }
            this.setter = setMethod;
            this.isLinked = true;
        }
    }

}



class PrimitiveNeFieldHandler extends NeFieldHandler {

   /**
    * 
    * @param {S8Object} object 
    * @param { * } value 
    */
    setValue(object, value) {
        this.setter.call(object, value);
    }
}



class Bool8NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getBool8());
    }

}

class Bool8ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getBool8(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}



class UInt8NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt8());
    }
}


class UInt8ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getUInt8(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class UInt16NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt16());
    }
}


class UInt16ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getUInt16(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class UInt32NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt32());
    }
}


class UInt32ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getUInt32(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class UInt64NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt64());
    }
}


class UInt64ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getUInt64(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Int8NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt8());
    }
}



class Int8ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getInt8(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Int16NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt16());
    }
}


class Int16ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getInt16(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Int32NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt32());
    }
}


class Int32ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getInt32(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Int64NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt64());
    }
}


class Int64ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getInt64(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}




class Float32NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getFloat32());
    }
}


class Float32ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getFloat32(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Float64NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getFloat64());
    }
}




class Float64ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getFloat64(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}



class StringUTF8NeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getStringUTF8());
    }
}




class StringUTF8ArrayNeFieldHandler extends PrimitiveNeFieldHandler {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getStringUTF8(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class S8ObjectNeFieldHandler extends NeFieldHandler {

    /**
    * 
    * @param {ByteInflow} inflow 
    * @returns {NeFieldEntry}
    */
    retrieveValue(inflow) {
        let index = inflow.getStringUTF8();
        // index can be null
        return new NeFieldEntry(this, index);
    }

    /**
     * 
     * @param {*} object 
     * @param {string} id 
     * @param {NeBranch} branch 
     */
    setValue(object, id, branch) {
        let value = (id != null) ? branch.getObject(id) : null;
        this.setter.call(object, value);
    }
}



class S8ObjectArrayNeFieldHandler extends NeFieldHandler {

    /**
    * 
    * @param {ByteInflow} inflow 
    * @returns {NeFieldEntry}
    */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let indices = new Array(length);
            for (let i = 0; i < length; i++) { indices[i] = inflow.getStringUTF8(); }
            return new NeFieldEntry(this, indices);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }


    /**
    * 
    * @param {*} object 
    * @param {string[]} index 
    * @param {NeBranch} branch 
    */
    setValue(object, indices, branch) {

        if (indices != null) {
            let length = indices.length;
            let array = new Array(length);
            for (let i = 0; i < length; i++) { 
                let id = indices[i];
                array[i] = id!=null ? branch.getObject(id) : null; 
            }
            this.setter.call(object, array);
        }
        else {
            this.setter.call(object, null);
        }
    }
}
