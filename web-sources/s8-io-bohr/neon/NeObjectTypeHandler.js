import { ByteInflow } from "/s8-io-bytes/ByteInflow";

import { NeFieldHandler } from "./NeFieldHandler";
import { NeBranch } from "./NeBranch";
import { BOHR_Keywords } from "/s8-io-bohr/atom/BOHR_Protocol";
import { NeFieldEntry } from "./NeFieldEntry";
import { S8Object } from "../atom/S8Object";



export class NeObjectTypeHandler {


    /**
     * @type {NeBranch}
     */
    branch;


    /**
     * @type {string}
     */
    pathname = "(undefined)";


    /**
     * @type {string}
     */
    classname = "(undefined)";


    /**
     * @type {number}
     */
    code = -1;


    /**
     * @type {boolean}
     */
    isClassLoaded = false;


    /**
     * @type {Class}
     */
    _class = null;


    /**
     * @type {NeFieldHandler[]}
     * Fields by code (map encoded as an array)
     */
    fieldHandlers = new Array(4);


    /**
     * @type { Map<string, NeOrbitalTypeFieldHandler> }
     */
    fieldHandlersByName = new Map();



    /**
     * 
     * @param {NeBranch} branch 
     * @param {string} classPathname 
     */
    constructor(branch, classPathname, code) {
        this.branch = branch;
        this.parseClassPathname(classPathname); 
        this.code = code;
    }


    /**
     * 
     * @param {string} classPathname 
     */
    parseClassPathname(classPathname) {
        let lastSeparatorIndex = -1, i;
        while ((i = classPathname.indexOf("/", lastSeparatorIndex + 1)) >= 0) {
            lastSeparatorIndex = i;
        }
        if (lastSeparatorIndex == -1) {
            throw "Illformed classpath: " + classPathname;
        }
        let n = classPathname.length;

        // pathname (with last folder separator)
        this.pathname = classPathname.substring(0, lastSeparatorIndex + 1);
        this.classname = classPathname.substring(lastSeparatorIndex + 1, n);
    }


    getTargetClassPathname(){
        return this.pathname + this.classname;
    }


    /**
     * 
     * @param { Class } _class 
     */
    setClass(_class) {

        /* set class */
        this._class = _class;
        this.isClassLoaded = true;

        /* link fields */
        let nFields = this.fieldHandlers.length;
        let fieldHandler;
        for (let i = 0; i < nFields; i++) {
            if ((fieldHandler = this.fieldHandlers[i]) != null) { fieldHandler.link(this._class); }
        }
    }




    /**
     * 
     * @param {*} instance 
     * @param {number} code 
     * @param {ByteInflow} value 
     * @param {Array} bindings 
     */
    set(instance, code, inflow, bindings) {

        // retrieve field
        let field = this.fieldHandlers[code];

        // set value
        field.setValue(instance, inflow, bindings);
    }


    /**
     * 
     * @param {string} id 
     * @returns {S8Object}
     */
    createNewInstance(id) {
        
        /** @type {S8Object} */
        let object = new this._class();

        // assign id
        object.S8_id = id;

        // assign type handler
        object.S8_type = this;

        return object;
    }



    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry[]}
     */
    consumeEntries(inflow) {
        let entries = new Array();
        let code;
        while ((code = inflow.getUInt8()) != BOHR_Keywords.CLOSE_NODE) {
            switch (code) {
                case BOHR_Keywords.DECLARE_FIELD: this.consume_DECLARE_FIELD(inflow); break;
                case BOHR_Keywords.SET_VALUE: this.consume_SET_VALUE(entries, inflow); break;
                default: throw "Code not supported while crawiling node definition: " + code;
            }
        }
        return entries;
    }



    /**
     * 
     * @param {ByteInflow} inflow 
     */
    consume_DECLARE_FIELD(inflow) {

        /* retrieve name */
        let fieldName = inflow.getStringUTF8();

        /* build field */
        let field = NeFieldHandler.consumeFormat(inflow);

        /* retrieve code */
        let fieldCode = inflow.getUInt8();

        field.name = fieldName;
        field.code = fieldCode;

        /* link field immediately if possible */
        if (this.isClassLoaded) { field.link(this._class); }

        this.appendField(fieldCode, field);
    }


    /**
     * @param {NeFieldEntry[]} entries 
     * @param {ByteInflow} inflow 
     */
    consume_SET_VALUE(entries, inflow) {
        let fieldCode = inflow.getUInt8();
        let fieldHanlder = this.fieldHandlers[fieldCode];
        entries.push(fieldHanlder.retrieveValue(inflow));
    }


    appendField(code, field) {

        if (code >= this.fieldHandlers.length) {
            let n = this.fieldHandlers.length, m = 2 * n;

            // extend while code is not within range
            while (code >= m) { m = 2 * m; }

            let extendedFields = new Array(2 * n);

            for (let i = 0; i < n; i++) { extendedFields[i] = this.fieldHandlers[i]; }
            this.fieldHandlers = extendedFields;

        }

        this.fieldHandlers[code] = field;
    }




}



