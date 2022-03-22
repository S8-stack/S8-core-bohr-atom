import { ByteInflow } from "s8-io-bytes/ByteInflow";

import { Types } from "s8-io-bohr/BOHR_Protocol";
import { NeFieldHandler } from "./NeFieldHandler";
import { NeBranch } from "./NeBranch";
import { NeObjectHandler } from "./NeObjectHandler";
import { BOHR_Keywords } from "s8-io-bohr/atom/BOHR_Protocol";
import { NeFieldEntry } from "./NeFieldEntry";
import { NeJump } from "./NeJump";
import { NeObject } from "./NeObject";



export class NeObjectTypeHandler {


    /**
     * @type {NeBranch}
     */
    branch;


    /**
     * @type {string}
     */
    classPathname = "(undefined)";


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
        this.classPathname = classPathname;
        this.code = code;
    }


    /**
     * 
     * @returns {string} the target classpath for server module loading
     */
    getTargetClassPathname() {
        let lastSeparatorIndex = -1, i;
        while ((i = this.classPathname.indexOf("/", lastSeparatorIndex + 1)) >= 0) {
            lastSeparatorIndex = i;
        }
        if (lastSeparatorIndex == -1) {
            throw "Illformed classpath: " + this.classPathname;
        }
        let n = this.classPathname.length;

        // pathname (with last folder separator)
        let pathname = this.classPathname.substring(0, lastSeparatorIndex + 1);

        let classname = this.classPathname.substring(lastSeparatorIndex + 1, n);

        return pathname + classname;
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



    declareFieldCode(name, code) {
        let fieldHandler = this.fieldHandlersByName.get(name);
        if (fieldHandler != undefined) {
            this.fieldHandlersByCode.set(code, fieldHandler);
        }
        else {
            throw "[NeLexicon] Failed to match type for name: " + name;
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
     * @returns {NeObject}
     */
    createNewInstance(id) {
        return new this._class(id, this);
    }



    /**
     * 
     * @param {ByteInflow} inflow 
     * @returns {NeFieldEntry[]}
     */
    consume(inflow) {
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
        let classPathname = inflow.getStringUTF8();

        /* build field */
        let field = NeFieldHandler.consumeFormat(inflow);

        /* retrieve code */
        let code = inflow.getUInt8();

        field.classPathname = classPathname;
        field.code = code;

        /* link field immediately if possible */
        if (this.isClassLoaded) { field.link(this._class); }

        this.appendField(code, field);
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



