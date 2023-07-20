

export const BOHR_Keywords = {


    OPEN_SEQUENCE: 0x02,
    CLOSE_SEQUENCE: 0x0f,

    OPEN_JUMP: 0x22,

    DEFINE_JUMP_TIMESTAMP: 0x24,
    DEFINE_JUMP_COMMENT: 0x25,
    DEFINE_JUMP_VERSION: 0x26,

    CLOSE_JUMP: 0x2f,



    /* <types> */


    /**
     * 
     */
    DECLARE_TYPE: 0x32,

    /* </types> */


    /* <nodes> */


    /**
     * (Must be closed)
     */
    CREATE_NODE : 0x42,

    /**
     * (must be closed)
     */
    UPDATE_NODE : 0x43,


    /**
     * no-closing required
     */
    EXPOSE_NODE : 0x44,


    /**
     * no-closing required
     */
    REMOVE_NODE : 0x45,

    /**
     * 
     */
    CLOSE_NODE : 0x4f,
    /* </nodes> */


    /* <fields> */



    /**
     * declare field and set value
     */
    DECLARE_FIELD: 0x52,
    /* </fields> */

    /* <value> */

    /**
     * 
     */
    SET_VALUE: 0x64,

    /* </value> */



    DECLARE_METHOD: 0x72,



    RUN_METHOD: 0x74,
    
    
    DECLARE_PROVIDER: 0x82,


    RUN_PROVIDER: 0x84,

};

export const BOHR_Types = {


	/* <core> */
	
	VOID: 0x02,
	
	/* </core> */
	

    /* <structure> */
    TUPLE: 0x12,

    ARRAY: 0x14,

    HASH_MAP: 0x16,

    TREE_MAP: 0x17,

    /* </structure> */


    /* <byte> */
    BYTE: 0x22,


    /**
     * Open new vista
     */
    SERIAL: 0x24,


    /*</byte> */



    /* <boolean> */
    BOOL8: 0x32,
    BOOL8_BIT_ARRAY: 0x33,
    BOOL64: 0x38,
    /* </boolean> */


    /* <unsigned-integer> */
    UINT8: 0x41,
    UINT16: 0x42,
    UINT32: 0x44,
    UINT64: 0x48,
    /* </unsigned-integer> */


    /* <signed-integer> */
    INT8: 0x51,
    INT16: 0x52,
    INT32: 0x54,
    INT64: 0x58,
    /* </signed-integer> */


    /* <float> */
    FLOAT32: 0x62,
    FLOAT64: 0x64,
    /* </float> */

    /* <string> */
    STRING_UTF8: 0x72,
    /* </string> */



    /* <object> */

    /**
     * 
     */
    S8OBJECT: 0x82,

    S8REF: 0x84,

    S8TABLE: 0x86,
    /* </object> */
};


export const BOHR_SerialTypes = {



    /* <SERIAL> */
    SOUND: 0x20,

    IMAGE: 0x22,

    /**
     * <h1>Engineering (SERIAL - level 1)</h1>
     * Engineering
     */
    NG: 0x64,

    /* <SERIAL> */


    /**
     * <h1>Math (SERIAL > NG - level 2)</h1>
     * Math for engineering
     */
    LINEAR_MATH: 0x24,

    /**
     * <h1>Vector (SERIAL > NG > LINEAR_MATH - level 3)</h1>
     * Math for engineering
     */
    VECTOR2: 0x22,
    MATRIX2: 0x23,
    AFFINE2: 0x24,

    VECTOR3: 0x32,
    MATRIX3: 0x33,
    AFFINE3: 0x34,

    VECTOR4: 0x42,
    MATRIX4: 0x43,
    AFFINE4: 0x44,

    VECTORN: 0x52,
    MATRIXN: 0x53,
    AFFINEN: 0x54,

    /**
     * <h1>Math (SERIAL > NG - level 2)</h1>
     * Math for engineering
     */
    THERMODYNAMICS: 0x32,


    TH_FLUID: 0x22,
    TH_STATE: 0x24,


    TURBOMACHINES: 0x36,
    TX_FLOW: 0x12,

}


