package com.s8.io.bohr.beryllium.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.s8.io.bohr.beryllium.fields.BeField;
import com.s8.io.bohr.beryllium.object.BeSerialException;


/**
 * 
 * @author pierreconvert
 *
 * @param <R>
 */
public class BeType {

	
	
	private final Class<?> baseType;
	
	String serialName;
	
	Constructor<?> constructor;
	
	Map<String, BeField> map;
	
	public BeType(Class<?> baseType) {
		super();
		this.baseType = baseType;
		this.map = new HashMap<>();
	}

	public BeField getField(String name) {
		return map.get(name);
	}
	

	/**
	 * 
	 * @return
	 */
	public String getSerialName() {
		return serialName;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws BeSerialException
	 */
	public Object createInstance() throws BeSerialException {
		try {
			return constructor.newInstance(new Object[]{});
		}
		catch (InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new BeSerialException("Failed to create new instance of type: "+baseType, e);
		}
	}
	
	

	public BeTypeIO generateIO() {
		int nFields = map.size();
		BeField[] fields = new BeField[nFields];
		int i=0;
		for(Entry<String, BeField> entry : map.entrySet()) {
			fields[i++] = entry.getValue();
		}
		return new BeTypeIO(this, fields);
	}
	
	
	
	/**
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws BeSerialException
	 */
	public boolean hasDiff(Object left, Object right, boolean isVerbose) throws BeSerialException {
		for(Entry<String, BeField> entry : map.entrySet()) {
			if(entry.getValue().hasDiff(left, right)) {
				if(isVerbose) {
					System.out.println("Diff for field: "+entry.getKey());
				}
				return true;
			}
		}
		return false;
		
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public Object DEBUG_print(String string) {
		return serialName;
	}

	
	/**
	 * 
	 * @return
	 */
	public Class<?> getBaseType() {
		return baseType;
	}
	

}
