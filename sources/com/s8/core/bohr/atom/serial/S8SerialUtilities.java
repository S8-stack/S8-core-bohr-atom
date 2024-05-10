package com.s8.core.bohr.atom.serial;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.s8.api.annotations.S8Serial;
import com.s8.api.exceptions.S8IOException;
import com.s8.api.serial.S8SerialPrototype;
import com.s8.api.serial.S8Serializable;

public class S8SerialUtilities {

	

	
	
	@SuppressWarnings("unchecked")
	public static <T extends S8Serializable> S8SerialPrototype<T> getPrototype(Class<?> c) throws S8IOException{
		
		/* <fields> */
		for(Field field : c.getFields()){
			
			if(field.isAnnotationPresent(S8Serial.class)) {
				int modifiers = field.getModifiers();
				if(!Modifier.isStatic(modifiers)) {
					throw new S8IOException("S8Serial prototype MUST be a static field");
				}
				if(!Modifier.isFinal(modifiers)) {
					throw new S8IOException("S8Serial prototype MUST be a final field");
				}
				if(!field.getType().equals(S8SerialPrototype.class)) {
					throw new S8IOException("S8Serial prototype MUST be of type S8Serializable.SerialPrototype");
				}
				try {
					return (S8SerialPrototype<T>) field.get(null);
				} 
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new S8IOException("Failed to retrieve S8Serial prototype due to: "+e.getMessage());
				}
			}
		}
		/* </fields> */
		
		/* <methods> */
		for(Method method : c.getMethods()){
			
			if(method.isAnnotationPresent(S8Serial.class)) {
				int modifiers = method.getModifiers();
				if(!Modifier.isStatic(modifiers)) {
					throw new S8IOException("S8Serial prototype method MUST be a static field");
				}
				if(!method.getReturnType().equals(S8SerialPrototype.class)) {
					throw new S8IOException("S8Serial prototype gen method MUST return an S8SerialPrototype");
				}
				try {
					return (S8SerialPrototype<T>) method.invoke(null, new Object[0]);
				} 
				catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					throw new S8IOException("Failed to retrieve S8Serial prototype due to: "+e.getMessage());
				}
			}
		}
		/* </methods> */
		
		
		
		throw new S8IOException("Failed to dinf the deserializer for class: "+c);
	}
	
	
	/**
	 * Utility method
	 * @param left
	 * @param right
	 * @return
	 */
	public static boolean hasDelta(int[] left, int[] right) {
		// check nulls
		if(left != null || right !=null) { 
			// check lengths
			int n0 = left.length;
			int n1 = right.length;
			if(n0 != n1) { return false; }
			for(int i = 0; i < n0; i++) { if(left[i] != right[i]) return true; }
			return false;
		}
		else if((left != null && right == null) || (left == null && right != null)) { 
			return true; 
		}
		else { // left == null && right == null
			return false;
		}
	}
	
	
	
	
	/**
	 * Utility method
	 * @param left
	 * @param right
	 * @return
	 */
	public static boolean hasDelta(long[] left, long[] right) {
		// check nulls
		if(left != null || right !=null) { 
			// check lengths
			int n0 = left.length;
			int n1 = right.length;
			if(n0 != n1) { return false; }
			for(int i = 0; i < n0; i++) { if(left[i] != right[i]) return true; }
			return false;
		}
		else if((left != null && right == null) || (left == null && right != null)) { 
			return true; 
		}
		else { // left == null && right == null
			return false;
		}
	}
	
	
	
	/**
	 * Utility method
	 * @param left
	 * @param right
	 * @return
	 */
	public static boolean hasDelta(float[] left, float[] right) {
		// check nulls
		if(left != null || right !=null) { 
			// check lengths
			int n0 = left.length;
			int n1 = right.length;
			if(n0 != n1) { return false; }
			for(int i = 0; i < n0; i++) { if(left[i] != right[i]) return true; }
			return false;
		}
		else if((left != null && right == null) || (left == null && right != null)) { 
			return true; 
		}
		else { // left == null && right == null
			return false;
		}
	}
	
	
	
	/**
	 * Utility method
	 * @param left
	 * @param right
	 * @return
	 */
	public static boolean hasDelta(double[] left, double[] right) {
		// check nulls
		if(left != null || right !=null) { 
			// check lengths
			int n0 = left.length;
			int n1 = right.length;
			if(n0 != n1) { return false; }
			for(int i = 0; i < n0; i++) { if(left[i] != right[i]) return true; }
			return false;
		}
		else if((left != null && right == null) || (left == null && right != null)) { 
			return true; 
		}
		else { // left == null && right == null
			return false;
		}
	}
	
	

	/**
	 * Utility method
	 * @param left
	 * @param right
	 * @return
	 */
	public static <T extends S8Serializable> boolean hasDelta(S8SerialPrototype<T> proto, T[] lefts, T[] rights) {
		// check nulls
		if(lefts != null || rights !=null) { 
			// check lengths
			int n0 = lefts.length;
			int n1 = rights.length;
			if(n0 != n1) { return false; }
			for(int i = 0; i < n0; i++) { 
				T left = lefts[i], right = rights[i];
				if(left != null && right != null && proto.hasDelta(left, right)) {
					return true; 
				}
				else if((left != null && right == null) || (left == null && right != null)) {
					return true;
				}
			}
			return false;
		}
		else if((lefts != null && rights == null) || (lefts == null && rights != null)) { 
			return true; 
		}
		else { // left == null && right == null
			return false;
		}
	}
}
