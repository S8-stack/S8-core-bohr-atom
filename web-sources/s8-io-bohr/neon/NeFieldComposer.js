

import { BOHR_Types } from "/s8-io-bohr/atom/BOHR_Protocol.js";
import { ByteOutflow } from "/s8-io-bytes/ByteOutflow.js";


export class NeFieldComposer {


    /**
     * @type {string}
     */
    name = "(undefined)";


    /**
     * @type {number}
     */
    code = -1;

}



class PrimitiveNeFieldComposer extends NeFieldComposer {

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



export class Bool8NeFieldComposer extends PrimitiveNeFieldComposer {

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

class Bool8ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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



class UInt8NeFieldComposer extends PrimitiveNeFieldComposer {

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


class UInt8ArrayNeFieldComposer extends PrimitiveNeFieldComposer {
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


class UInt16NeFieldComposer extends PrimitiveNeFieldComposer {

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


class UInt16ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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


class UInt32NeFieldComposer extends PrimitiveNeFieldComposer {

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


class UInt32ArrayNeFieldHandler extends PrimitiveNeFieldComposer {

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


class UInt64NeFieldComposer extends PrimitiveNeFieldComposer {

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


class UInt64ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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


class Int8NeFieldComposer extends PrimitiveNeFieldComposer {

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



class Int8ArrayNeFieldHandler extends PrimitiveNeFieldComposer {

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


class Int16NeFieldComposer extends PrimitiveNeFieldComposer {
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


class Int16ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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


class Int32NeFieldComposer extends PrimitiveNeFieldComposer {

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


class Int32ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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


class Int64NeFieldComposer extends PrimitiveNeFieldComposer {

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


class Int64ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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




class Float32NeFieldComposer extends PrimitiveNeFieldComposer {

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


class Float32ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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


class Float64NeFieldComposer extends PrimitiveNeFieldComposer {

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




class Float64ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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



class StringUTF8NeFieldComposer extends PrimitiveNeFieldComposer {

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




class StringUTF8ArrayNeFieldComposer extends PrimitiveNeFieldComposer {

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


class S8ObjectNeFieldComposer extends NeFieldComposer {


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



class S8ObjectArrayNeFieldComposer extends NeFieldComposer {

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
