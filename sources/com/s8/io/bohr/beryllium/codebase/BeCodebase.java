package com.s8.io.bohr.beryllium.codebase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.s8.io.bohr.beryllium.types.BeType;


/**
 * 
 * <h1>Codebase</h1>
 * <h2>S8 Model foundation</h2>
 * <p>
 * The S8 Model is base on a <code>GphCodebase</code> which is meant to be used
 * on massive scale. procedure is as follows
 * </p>
 * <ul>
 * <li>Step<b>#1</b>: Code your .jar with all classes annotated by
 * <code>S8Type</code> and <code>S8Field</code> (and <code>S8Getter</code>/
 * <code>S8Setter</code>). Names used in annotations MUST NOT be changed in the
 * future. Note that is a good idea to a prefixnig (suffixing) hash for
 * successive versions.</li>
 * <li>Step<b>#2</b>: (First-time) Compile your code using
 * <code>GphCodebase.compile()</code>.</li>
 * <li>Step<b>#3</b>: Your <code>GphCodebase</code> is now compiled (which means
 * that all names have been translated into codes with much more efficient
 * memory footprint. You must now save this compilation using <code>GphCodebase.compose()</code>.</li>
 * <li>Step<b>#4</b>: (Subsequent uses) When booting, server must generate <code>GphCodebase</code> using <code>GphCodebaseBuilder</code>, then
 * use the <code>GphCodebase.parse()</code> method to retrieve the proper encoding.</li>
 * </ul>
 * @author pc
 *
 */
public class BeCodebase {


	
	public final static byte[] OPENING_TAG = "<code:>".getBytes(StandardCharsets.US_ASCII);

	public final static byte[] CLOSING_TAG = "</code>".getBytes(StandardCharsets.US_ASCII);

	
	public long revision;
	
	private boolean isVerbose;

	/**
	 * 
	 */
	Map<String, BeType> typesBySerialName;
	
	
	Map<String, BeType> typesByRuntimeName;
	
	
	/**
	 * Note that types length is not equal to nTypes
	 */
	BeType[] types;
 	
	
	/**
	 * 
	 */
	int nTypes;
	
	int highestTypeCode = 0;
	

	/**
	 * 
	 * @param typesContext
	 */
	public BeCodebase(boolean isVerbose) {
		super();
		
		this.isVerbose = isVerbose;
		
		typesBySerialName = new HashMap<>();
		
		typesByRuntimeName = new HashMap<>();
		
	}

	
	public boolean isVerbose() {
		return isVerbose;
	}
	
	
	/**
	 * 
	 * @param type
	 */
	void put(BeType type){
		typesBySerialName.put(type.getSerialName(), type);	
		typesByRuntimeName.put(type.getBaseType().getName(), type);
	}

	
	/**
	 * (Persistency side)
	 * @param name
	 * @return
	 */
	public BeType getTypeBySerialName(String name) {
		return typesBySerialName.get(name);
	}
	
	
	/**
	 * (Runtime side)
	 * @param name
	 * @return
	 */
	public BeType getTypeByRuntimeName(String name) {
		return typesByRuntimeName.get(name);
	}
	
	/**
	 * (Runtime side)
	 * @param outboundTypeName
	 * @return
	 */
	public BeType getTypeByCode(int code) {
		return types[code];
	}

	/**
	 * Traverse all types
	 * (Compilation time only)
	 * @param consumer
	 */
	public void forEachObjectType(BiConsumer<String, BeType> consumer) {
		typesBySerialName.forEach(consumer);
	}
	

	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean isTypeKnown(Class<?> type) {
		return typesByRuntimeName.containsKey(type.getName());
	}

	
	/**
	 * 
	 * @param prototype
	 * @return
	 * @throws IOException 
	 */
	public BeType getType(Object object) {
		return typesByRuntimeName.get(object.getClass().getName());
	}
	
	
	
	public BeType getType(Class<?> c) {
		return typesByRuntimeName.get(c.getName());
	}


	/**
	 * 
	 */
	public void DEBUG_print() {
		typesBySerialName.forEach((name, type) -> type.DEBUG_print(""));
	}
	
	
}
