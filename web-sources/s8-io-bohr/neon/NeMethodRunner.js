
import { ByteOutflow } from "/s8-io-bytes/ByteOutflow.js";

import { BOHR_Keywords, BOHR_Types } from "/s8-io-bohr/atom/BOHR_Protocol.js";

import { NeObjectTypeHandler } from "./NeObjectTypeHandler.js";

export class NeMethodRunner {



    /**
     * @type {NeObjectTypeHandler}
     */
    type;

    /**
     * @type {string}
     */
    name;

    /**
     * @type {number}
     */
    code;

    /**
     * @type {boolean}
     */
    isPublished = false;

    
    constructor(name, code) {
        this.name = name;
        this.code = code;
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     */
    publish_DECLARE_METHOD(outflow){
        if(!this.isPublished){
            
            // declare method
            outflow.putUInt8(BOHR_Keywords.DECLARE_METHOD);

            // type code
            outflow.putUInt7x(this.type.code);

            // method name
            outflow.putStringUTF8(this.name);

            // method signature
            this.produceFormat(outflow);
            
            // method code
            outflow.putUInt8(this.code);
            
            this.isPublished = true;
        }
    }

}



class PrimitiveNeMethodRunner extends NeMethodRunner {

    /**
     * 
     * @param {string} name 
     * @param {number} code 
     */
    constructor(name, code) {
        super(name, code);
    }

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



export class VoidNeMethodRunner extends PrimitiveNeMethodRunner {
    /**
        * 
        * @param {string} name 
        * @param {number} code 
        */
    constructor(name, code) {
        super(name, code);
    }

    getType() { return "void"; }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.VOID);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue() {
        // do nothing
    }
}

export class Bool8NeMethodRunner extends PrimitiveNeMethodRunner {
    /**
        * 
        * @param {string} name 
        * @param {number} code 
        */
    constructor(name, code) {
        super(name, code);
    }

    getType() { return "bool8"; }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.BOOL8);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putBool8(value);
    }
}

export class Bool8ArrayNeMethodRunner extends PrimitiveNeMethodRunner {
    /**
        * 
        * @param {string} name 
        * @param {number} code 
        */
    constructor(name, code) {
        super(name, code);
    }

    getType() { return "bool8[]"; }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.BOOL8);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putBool8(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}



export class UInt8NeMethodRunner extends PrimitiveNeMethodRunner {

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    getType() { return "uint8"; }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.UINT8);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putUInt8(value);
    }
}


export class UInt8ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    getType() { return "uint8[]"; }

    /** 
        * @param {ByteOutflow} outflow 
        */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.UINT8);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putUInt8(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class UInt16NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "uint16"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.UINT16);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putUInt16(value);
    }
}


export class UInt16ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "uint16[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.UINT16);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putUInt16(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class UInt32NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "uint32"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.UINT32);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putUInt32(value);
    }
}


export class UInt32ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "uint32[]"; }
    /**
        * 
        * @param {string} name 
        * @param {number} code 
        */
    constructor(name, code) {
        super(name, code);
    }

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.UINT32);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putUInt32(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class UInt64NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "uint64"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.UINT64);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putUInt64(value);
    }
}


export class UInt64ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "uint64[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.UINT64);
    }

    /**
    * 
    * @param {ByteOutflow} outflow 
    * @param {Array} value
    */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putUInt64(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class Int8NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int8"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
     * @param {ByteOutflow} outflow 
     */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.INT8);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putInt8(value);
    }
}



export class Int8ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int8[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.INT8);
    }

    /**
    * 
    * @param {ByteOutflow} outflow 
    * @param {Array} value
    */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putInt8(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class Int16NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int16"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
       * @param {ByteOutflow} outflow 
       */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.INT16);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putInt16(value);
    }
}


export class Int16ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int16"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.INT16);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putInt16(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class Int32NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int32"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
   * @param {ByteOutflow} outflow 
   */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.INT32);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putInt32(value);
    }
}


export class Int32ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int32[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }


    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.INT32);
    }

    /**
      * 
      * @param {ByteOutflow} outflow 
      * @param {Array} value
      */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putInt32(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class Int64NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int64"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }


    /** 
   * @param {ByteOutflow} outflow 
   */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.INT64);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putInt64(value);
    }
}


export class Int64ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "int64[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.INT64);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putInt64(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}




export class Float32NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "float32"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
   * @param {ByteOutflow} outflow 
   */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.FLOAT32);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putFloat32(value);
    }
}


export class Float32ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "float32[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }


    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.FLOAT32);
    }

    /**
    * 
    * @param {ByteOutflow} outflow 
    * @param {Array} value
    */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putFloat32(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class Float64NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "float64"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }


    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.FLOAT64);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putFloat64(value);
    }
}




export class Float64ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "float64[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }


    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.FLOAT64);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putFloat64(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}



export class StringUTF8NeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "string-UTF8"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }


    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.STRING_UTF8);
    }


    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {*} value
     */
    produceValue(outflow, value) {
        outflow.putStringUTF8(value);
    }
}




export class StringUTF8ArrayNeMethodRunner extends PrimitiveNeMethodRunner {

    getType() { return "string-UTF8[]"; }

    /**
    * 
    * @param {string} name 
    * @param {number} code 
    */
    constructor(name, code) {
        super(name, code);
    }

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.STRING_UTF8);
    }

    /**
     * 
     * @param {ByteOutflow} outflow 
     * @param {Array} value
     */
    produceValue(outflow, value) {
        if (value != null) {
            let length = value.length;
            outflow.putUInt7x(length);
            for (let i = 0; i < length; i++) { outflow.putStringUTF8(value[i]); }
        }
        else {
            outflow.putUInt7x(-1); // special code
        }
    }
}


export class S8ObjectNeMethodRunner extends NeMethodRunner {


    /** 
        * @param {ByteOutflow} outflow 
        */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.S8OBJECT);
    }

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



export class S8ObjectArrayNeMethodRunner extends NeMethodRunner {

    /** 
    * @param {ByteOutflow} outflow 
    */
    produceFormat(outflow) {
        outflow.putUInt8(BOHR_Types.ARRAY);
        outflow.putUInt8(BOHR_Types.S8OBJECT);
    }

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
                array[i] = id != null ? branch.getObject(id) : null;
            }
            this.setter.call(object, array);
        }
        else {
            this.setter.call(object, null);
        }
    }
}
