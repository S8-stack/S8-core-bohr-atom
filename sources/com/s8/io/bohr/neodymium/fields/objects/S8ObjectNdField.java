package com.s8.io.bohr.neodymium.fields.objects;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.atom.S8Field;
import com.s8.io.bohr.atom.S8Getter;
import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8Setter;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldBuilder;
import com.s8.io.bohr.neodymium.fields.NdFieldComposer;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.fields.NdFieldParser;
import com.s8.io.bohr.neodymium.fields.NdFieldPrototype;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties1T;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bohr.neodymium.type.GraphCrawler;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 * @author pc
 *
 */
public class S8ObjectNdField extends NdField {



	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype() {


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> fieldType = field.getType();
			if(S8Object.class.isAssignableFrom(fieldType)){
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.FIELD, fieldType);
					properties.setFieldAnnotation(annotation);
					return properties;	
				}
				else { return null; }
			}
			else { return null; }
		}


		@Override
		public NdFieldProperties captureSetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getParameterTypes()[0];
			S8Setter annotation = method.getAnnotation(S8Setter.class);
			if(annotation != null) {
				if(S8Object.class.isAssignableFrom(baseType)) {
					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, baseType);
					properties.setSetterAnnotation(annotation);
					return properties;
				}
				else {
					throw new NdBuildException("S8Annotated field of type List must have its "
							+"parameterized type inheriting from S8Object", method);
				}
			}
			else { return null; }
		}

		@Override
		public NdFieldProperties captureGetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getReturnType();

			S8Getter annotation = method.getAnnotation(S8Getter.class);
			if(annotation != null) {
				if(S8Object.class.isAssignableFrom(baseType)){
					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, baseType);
					properties.setGetterAnnotation(annotation);
					return properties;
				}
				else {
					throw new NdBuildException("S8Annotated field of type List must have its "
							+"parameterized type inheriting from S8Object", method);

				}
			}
			else { return null; }
		}


		@Override
		public NdFieldBuilder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new Builder(properties, handler);
		}
	};




	private static class Builder extends NdFieldBuilder {

		public Builder(NdFieldProperties properties, NdHandler handler) {
			super(properties, handler);
		}

		@Override
		public NdFieldPrototype getPrototype() {
			return PROTOTYPE;
		}

		@Override
		public NdField build(int ordinal) {
			return new S8ObjectNdField(ordinal, properties, handler);
		}
	}


	public S8ObjectNdField(int ordinal, NdFieldProperties properties, NdHandler handler) {
		super(ordinal, properties, handler);
	}





	@Override
	public void sweep(S8Object object, GraphCrawler crawler) {
		try {
			S8Object fieldObject = (S8Object) handler.get(object);
			if(fieldObject!=null) {
				crawler.accept(fieldObject);
			}
		} 
		catch (NdIOException cause) {
			cause.printStackTrace();
		}
	}


	@Override
	public void collectReferencedBlocks(S8Object object, Queue<String> references) {
		// No ext references
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (S8Object)");
	}

	@Override
	public void computeFootprint(S8Object object, MemoryFootprint weight) throws NdIOException {
		weight.reportReference();
	}


	@Override
	public void deepClone(S8Object origin, S8Object clone, BuildScope scope) throws NdIOException {
		S8Object value = (S8Object) handler.get(origin);
		if(value!=null) {
			S8Index index = value.S8_index;

			scope.appendBinding(new BuildScope.Binding() {

				@Override
				public void resolve(BuildScope scope) throws NdIOException {

					// no need to upcast to S8Object
					S8Object indexedObject = scope.retrieveObject(index);
					if(indexedObject==null) {
						throw new NdIOException("Fialed to retriev vertex");
					}
					handler.set(clone, indexedObject);
				}
			});
		}
		else {
			handler.set(clone, null);
		}
	}


	@Override
	public boolean hasDiff(S8Object base, S8Object update) throws NdIOException {
		S8Object baseValue = (S8Object) handler.get(base);
		S8Object updateValue = (S8Object) handler.get(update);
		if(baseValue == null && updateValue == null) {
			return false;
		}
		else if ((baseValue != null && updateValue == null) || (baseValue == null && updateValue != null)) {
			return true;
		}
		else {
			return !baseValue.S8_index.equals(updateValue.S8_index);
		}
	}



	@Override
	public S8ObjectNdFieldDelta produceDiff(S8Object object) throws NdIOException {
		S8Object value = (S8Object) handler.get(object);
		return new S8ObjectNdFieldDelta(this, value != null ? value.S8_index : S8Index.NULL);
	}


	@Override
	protected void printValue(S8Object object, Writer writer) throws IOException {
		S8Object value = (S8Object) handler.get(object);
		if(value!=null) {
			writer.write("(");
			writer.write(value.getClass().getCanonicalName());
			writer.write("): ");
			writer.write(value.S8_index.toString());	
		}
		else {
			writer.write("null");
		}
	}

	@Override
	public String printType() {
		return "S8Object";
	}

	public void setValue(Object object, S8Object struct) throws NdIOException {
		handler.set(object, struct);
	}





	@Override
	public boolean isValueResolved(S8Object object) {
		return true; // always resolved at resolve step in shell
	}



	/**
	 * 
	 * @author pierreconvert
	 *
	 * @param <T>
	 */
	private class ObjectBinding implements BuildScope.Binding {

		private Object object;

		private S8Index id;

		public ObjectBinding(Object object, S8Index id) {
			super();
			this.object = object;
			this.id = id;
		}

		@Override
		public void resolve(BuildScope scope) throws NdIOException {
			handler.set(object, scope.retrieveObject(id));
		}
	}

	/* <delta> */



	/* <IO-inflow-section> */

	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code){
		case BOHR_Types.S8OBJECT : return new Inflow();
		default: throw new NdIOException("Unsupported code: "+Integer.toHexString(code));
		}
	}


	private class Inflow extends NdFieldParser {

		@Override
		public void parseValue(S8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			S8Index id = S8Index.read(inflow);
			if(id != null) {
				/* append bindings */
				scope.appendBinding(new ObjectBinding(object, id));
			}
			else {
				// set list
				handler.set(object, null);	
			}
		}


		@Override
		public S8ObjectNdField getField() {
			return S8ObjectNdField.this;
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new S8ObjectNdFieldDelta(S8ObjectNdField.this, S8Index.read(inflow));
		}
	}


	/* </IO-inflow-section> */



	/* <IO-outflow-section> */

	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {
		case DEFAULT_FLOW_TAG: case "obj[]" : return new Outflow(code);
		default : throw new NdIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Outflow extends NdFieldComposer {

		public Outflow(int code) {
			super(code);
		}

		@Override
		public NdField getField() {
			return S8ObjectNdField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.S8OBJECT);
		}

		@Override
		public void composeValue(S8Object object, ByteOutflow outflow) throws IOException {
			S8Object value = (S8Object) handler.get(object);
			S8Index.write(value != null ? value.S8_index : null, outflow);
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			S8Index.write(((S8ObjectNdFieldDelta) delta).index, outflow);
		}
	}
	/* </IO-outflow-section> */
}
