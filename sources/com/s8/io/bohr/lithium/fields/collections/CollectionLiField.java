package com.s8.io.bohr.lithium.fields.collections;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;


public abstract class CollectionLiField extends LiField {

	

	public final static byte[] SEQUENCE = new byte[] {
			(byte) BOHR_Types.ARRAY,
			(byte) BOHR_Types.S8OBJECT
	};
	


	public CollectionLiField(int ordinal, LiFieldProperties properties, LiHandler handler) {
		super(ordinal, properties, handler);
	}






	public interface ItemConsumer {

		public void consume(S8Object item) throws IOException;
	}


	public abstract void forEach(Object iterable, ItemConsumer consumer) throws IOException;




	private class Printer implements ItemConsumer {

		private boolean isInitialized = false;
		private Writer writer;

		public Printer(Writer writer) {
			super();
			this.writer = writer;
		}

		@Override
		public void consume(S8Object item) throws IOException {
			if(isInitialized) {
				writer.write(" ,");	
			}
			else {
				isInitialized = true;
			}

			if(item!=null) {
				writer.write("(");
				writer.write(item.getClass().getCanonicalName());
				writer.write("): ");
				writer.write(item.S8_index.toString());	
			}
			else {
				writer.write("null");
			}
		}
	}


	@Override
	protected void printValue(S8Object object, Writer writer) throws IOException {
		S8Object[] array = (S8Object[]) handler.get(object);
		if(array!=null) {
			writer.write('[');
			forEach(object, new Printer(writer));
			writer.write(']');
		}
		else {
			writer.write("null");
		}
	}
}