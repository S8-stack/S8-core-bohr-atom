package com.s8.io.bohr.beryllium.fields;

import java.lang.reflect.Field;

import com.s8.io.bohr.beryllium.types.BeTypeBuildException;

public abstract class MappedBeField extends BeField {



	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static abstract class Prototype extends BeField.Prototype {
		
		
		public final Class<?> baseType;
		
		
		public Prototype(Class<?> baseType) {
			super();
			this.baseType = baseType;
		}
		
		/**
		 * 
		 * @return
		 */
		@Override
		public String getTypeName() {
			return baseType.getName();
		}
		
		/**
		 * 
		 * @param field
		 * @return
		 * @throws GphSerialException
		 */
		public abstract BeField createField(String name, long props, Field field) throws BeTypeBuildException;

	}
	


	@Override
	public abstract Prototype getPrototype();
	
	
	public MappedBeField(String name, long props, Field field) {
		super(name, props, field);
	}


	
}
