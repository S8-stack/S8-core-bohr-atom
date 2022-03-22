



export class S8FieldTypeHandler {

}







class S8TypeHandler {

    constructor(props, inherited){
        super();
        this.props = props;
        this.inherited = inherited;
    }

    
}


export function TYPE(props, inherited){
    return new ObjectS8TypeHandler(props, inherited);
}



class ObjectS8FieldTypeHandler extends S8FieldTypeHandler {

    constructor(type, setter){
        super();
        this.type = type;
        this.setter = setter;
    }

    
}


export function OBJECT(type, setter){
    return new ObjectS8FieldTypeHandler(type, setter);
}