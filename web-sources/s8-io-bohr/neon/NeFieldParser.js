

import { ByteInflow } from "/s8-io-bytes/ByteInflow.js";

import { BOHR_Types } from "/s8-io-bohr/atom/BOHR_Protocol.js";
import { NeFieldEntry } from "./NeFieldEntry.js";
import { NeVertex } from "./NeVertex.js";



export class NeFieldParser {


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
     * @returns {NeFieldParser}
     */
    static consumeFormat(inflow) {

        let code = inflow.getUInt8();

        switch (code) {

            /* <structure> */
            case BOHR_Types.ARRAY: return NeFieldParser.buildArray(inflow);
            /* </structure> */

            /* <bytes> */
            case BOHR_Types.SERIAL: throw "Unsupported serial";
            /* </bytes> */


            /* <booleans> */
            case BOHR_Types.BOOL8: return new Bool8NeFieldParser();
            /* </booleans> */

            /* <unsigned-integers> */
            case BOHR_Types.UINT8: return new UInt8NeFieldParser();
            case BOHR_Types.UINT16: return new UInt16NeFieldParser();
            case BOHR_Types.UINT32: return new UInt32NeFieldParser();
            case BOHR_Types.UINT64: return new UInt64NeFieldParser();
            /* </unsigned-integers> */

            /* <signed-integers> */
            case BOHR_Types.INT8: return new Int8NeFieldParser();
            case BOHR_Types.INT16: return new Int16NeFieldParser();
            case BOHR_Types.INT32: return new Int32NeFieldParser();
            case BOHR_Types.INT64: return new Int64NeFieldParser();
            /* </signed-integers> */

            /* <float> */
            case BOHR_Types.FLOAT32: return new Float32NeFieldParser();
            case BOHR_Types.FLOAT64: return new Float64NeFieldParser();
            /* </float> */

            /* <string> */
            case BOHR_Types.STRING_UTF8: return new StringUTF8NeFieldParser();
            /* </string> */

            /* <object> */
            case BOHR_Types.S8OBJECT: return new S8ObjectNeFieldParser();
            /* </object> */

            default: throw "Unsupported BOHR type code: " + code;
        }
    }

    static buildArray(inflow) {
        let code = inflow.getUInt8();
        switch (code) {

            case BOHR_Types.BOOL8: return new Bool8ArrayNeFieldParser();

            case BOHR_Types.UINT8: return new UInt8ArrayNeFieldParser();
            case BOHR_Types.UINT16: return new UInt16ArrayNeFieldParser();
            case BOHR_Types.UINT32: return new UInt32ArrayNeFieldHandler();
            case BOHR_Types.UINT64: return new UInt64ArrayNeFieldParser();

            case BOHR_Types.INT8: return new Int8ArrayNeFieldHandler();
            case BOHR_Types.INT16: return new Int16ArrayNeFieldParser();
            case BOHR_Types.INT32: return new Int32ArrayNeFieldParser();
            case BOHR_Types.INT64: return new Int64ArrayNeFieldParser();

            case BOHR_Types.FLOAT32: return new Float32ArrayNeFieldParser();
            case BOHR_Types.FLOAT64: return new Float64ArrayNeFieldParser();

            case BOHR_Types.STRING_UTF8: return new StringUTF8ArrayNeFieldParser();
            case BOHR_Types.S8OBJECT: return new S8ObjectArrayNeFieldParser();

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



class PrimitiveNeFieldParser extends NeFieldParser {

   /**
    * 
    * @param {NeVertex} vertex 
    * @param { * } value 
    */
    setValue(vertex, value) {
        let object = vertex.object;
        this.setter.call(object, value);
    }
}



class Bool8NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getBool8());
    }

}

class Bool8ArrayNeFieldParser extends PrimitiveNeFieldParser {

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



class UInt8NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt8());
    }
}


class UInt8ArrayNeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Uint8Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getUInt8(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class UInt16NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt16());
    }
}


class UInt16ArrayNeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Uint16Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getUInt16(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class UInt32NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt32());
    }
}


class UInt32ArrayNeFieldHandler extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Uint32Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getUInt32(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class UInt64NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getUInt64());
    }
}


class UInt64ArrayNeFieldParser extends PrimitiveNeFieldParser {

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


class Int8NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt8());
    }
}



class Int8ArrayNeFieldHandler extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Int8Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getInt8(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Int16NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt16());
    }
}


class Int16ArrayNeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Int16Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getInt16(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Int32NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt32());
    }
}


class Int32ArrayNeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Int32Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getInt32(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Int64NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getInt64());
    }
}


class Int64ArrayNeFieldParser extends PrimitiveNeFieldParser {

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




class Float32NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getFloat32());
    }
}


class Float32ArrayNeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Float32Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getFloat32(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}


class Float64NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getFloat64());
    }
}




class Float64ArrayNeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        let length = inflow.getUInt7x();
        if (length >= 0) {
            let array = new Float64Array(length);
            for (let i = 0; i < length; i++) { array[i] = inflow.getFloat64(); }
            return new NeFieldEntry(this, array);
        }
        else {
            return new NeFieldEntry(this, null);
        }
    }
}



class StringUTF8NeFieldParser extends PrimitiveNeFieldParser {

    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry} 
     */
    retrieveValue(inflow) {
        return new NeFieldEntry(this, inflow.getStringUTF8());
    }
}




class StringUTF8ArrayNeFieldParser extends PrimitiveNeFieldParser {

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


class S8ObjectNeFieldParser extends NeFieldParser {

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
     * @param {NeVertex} vertex 
     * @param {string} id 
     */
    setValue(vertex, id) {
        let branch = vertex.branch;
        let object = vertex.object;
        let value = (id != null) ? branch.getObject(id) : null;
        this.setter.call(object, value);
    }
}



class S8ObjectArrayNeFieldParser extends NeFieldParser {

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
    * @param {NeVertex} vertex 
    * @param {string[]} index 
    */
    setValue(vertex, indices) {
        let branch = vertex.branch;
        let object = vertex.object;

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
