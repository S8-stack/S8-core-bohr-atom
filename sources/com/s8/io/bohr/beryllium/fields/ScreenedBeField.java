package com.s8.io.bohr.beryllium.fields;

import java.lang.reflect.Field;

import com.s8.io.bohr.beryllium.types.BeTypeBuildException;


/**
 * 
 * @author pierreconvert
 *
 */
public abstract class ScreenedBeField extends BeField {



	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static abstract class Prototype extends BeField.Prototype {
	
		
		public Prototype() {
			super();
		}
		
		/**
		 * 
		 * @return
		 */
		@Override
		public String getTypeName() {
			return "screened";
		}
		
		/**
		 * 
		 * @param field
		 * @return
		 * @throws GphSerialException
		 */
		public abstract BeField captureField(String name, long props, Field field) throws BeTypeBuildException;

	}
	


	@Override
	public abstract Prototype getPrototype();
	
	
	public ScreenedBeField(String name, long props, Field field) {
		super(name, props, field);
	}

	
}
