
import { ByteInflow } from "/s8-io-bytes/ByteInflow.js";

import { BOHR_Keywords } from "/s8-io-bohr/atom/BOHR_Protocol.js";


import { NeFieldParser } from "./NeFieldParser.js";
import { NeFieldComposer } from "./NeFieldComposer.js";
import { NeBranch } from "./NeBranch.js";
import { NeFieldEntry } from "./NeFieldEntry.js";
import { NeObject } from "./NeObject.js";



export class NeObjectTypeHandler {


    static JS_EXTENSION = ".js";

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
     * @type {NeFieldParser[]}
     * Fields by code (map encoded as an array)
     */
    fieldParsers = new Array(4);


    /**
     * @type {Map<string, NeFieldComposer>}
     */
    fieldComposers = new Map();


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
        return this.pathname + this.classname + NeObjectTypeHandler.JS_EXTENSION ;
    }


    /**
     * 
     * @param { Class } _class 
     */
    setClass(_class) {

        /* set class */
        this._class = _class;
        this.isClassLoaded = true;


        /* search for exigible methods */
        let renderMethod = _class.prototype["S8_render"];
        if(renderMethod == undefined){ throw this.classname+" is missing an S8_render() method."; }

        let disposeMethod = _class.prototype["S8_dispose"];
        if(disposeMethod == undefined){ throw this.classname+" is missing an S8_dispose() method."; }

        /* link fields */
        let nFields = this.fieldParsers.length;
        let fieldHandler;
        for (let i = 0; i < nFields; i++) {
            if ((fieldHandler = this.fieldParsers[i]) != null) { fieldHandler.link(this._class); }
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
        let field = this.fieldParsers[code];

        // set value
        field.setValue(instance, inflow, bindings);
    }


    /**
     * 
     * @param {string} id 
     * @returns {NeObject}
     */
    createNewInstance(id) {
        
        /** @type {NeObject} */
        let object = new this._class();

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
        let field = NeFieldParser.consumeFormat(inflow);

        /* retrieve code */
        let fieldCode = inflow.getUInt8();

        field.name = fieldName;
        field.code = fieldCode;

        /* link field immediately if possible */
        if (this.isClassLoaded) { field.link(this._class); }

        this.appendFieldParser(fieldCode, field);
    }


    /**
     * @param {NeFieldEntry[]} entries 
     * @param {ByteInflow} inflow 
     */
    consume_SET_VALUE(entries, inflow) {
        let fieldCode = inflow.getUInt8();
        let fieldParser = this.fieldParsers[fieldCode];
        entries.push(fieldParser.retrieveValue(inflow));
    }


    appendFieldParser(code, field) {

        if (code >= this.fieldParsers.length) {
            let n = this.fieldParsers.length, m = 2 * n;

            // extend while code is not within range
            while (code >= m) { m = 2 * m; }

            let extendedFields = new Array(2 * n);

            for (let i = 0; i < n; i++) { extendedFields[i] = this.fieldParsers[i]; }
            this.fieldParsers = extendedFields;

        }

        this.fieldParsers[code] = field;
    }




}



