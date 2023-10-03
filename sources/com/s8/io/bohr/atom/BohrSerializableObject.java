package com.s8.io.bohr.atom;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.s8.api.bytes.ByteInflow;
import com.s8.api.bytes.ByteOutflow;
import com.s8.api.bytes.MemoryFootprint;
import com.s8.api.exceptions.S8IOException;
import com.s8.api.objects.annotations.S8Serial;



/**
 * Can parse from a <code>ByteInflow</code> and compose to a <code>ByteOutflow</code>.
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
@Deprecated
public interface BohrSerializableObject {
	
	
	public final static String KEYWORD = "BOHR_PROTO";


	/**
	 * <p>
	 * <b>Important explanation on why it's coded that way</b>.
	 * </p>
	 * <p>
	 * Assume you have a -slightly- complex ByteOutflowable object: it's called
	 * <code>ThFluid</code> and it contains all properties of a given fluid. Let's
	 * ay that they are limite nb of fluids (air, water, methane, etc.) so that you
	 * can simply assign a number to them (CAS number in real life). The trick is
	 * that there is multiple types of fluids (like for instance
	 * <code>ImcompressibleFluid</code> or <code>PerfetGasThFluid</code>, eah
	 * bringing it's own formula, equation of state and so on. So when reading the
	 * encoding of the fluid, there is a rot switch that will call sub-factories (on
	 * specialized on perfect gas, another one in incompressible fluid, etc.).
	 * </p>
	 * <p>
	 * That being said, say you are now building a parsing/composing engine for
	 * serilization/persistency or whatever. You stumble on a field which has a type
	 * that actually implementts the <code>ByteOutflowable</code> interface, but it
	 * MUST have a method to indicate the PROPER factory to be used for
	 * deserializing this type of field. In the case described in previous
	 * paragraph, the factory cannot be a constant of for instance
	 * <code>ImcompressibleFluid</code> since type parsing may need to be done at an
	 * higher level (for instance <code>ThFluid</code>) to enable the possibility to
	 * switch dynamically between differnt types of fluid with a unified encoding
	 * (for instance first type gives the type of fluid and allows to switch to a
	 * give factory, then each factory will continue to read bytes, each one havong
	 * it's own byte pattern). If relying on constant fields, override is extremly
	 * painful since we cannot select from a specific class the right factory. For
	 * instance tf the type of the fiekd is simply Impcoressible, we might want to
	 * restrain encoding to simply the one give by this specific factory.
	 * </p>
	 */
	public static abstract class Prototype<S extends BohrSerializableObject> {

		public Prototype(String signature) {
			super();
		}



		/**
		 * 
		 * @param type
		 * @return
		 * @throws IOException 
		 */
		public abstract S deserialize(ByteInflow inflow) throws IOException;

		
		/**
		 * 
		 * @param object
		 * @return
		 */
		public abstract void computeFootprint(S object, MemoryFootprint footprint);
	}



	/**
	 * 
	 * @param outflow
	 * @throws IOException
	 */
	public void serialize(ByteOutflow outflow) throws IOException;
	
	
	/**
	 * 
	 * @param <S>
	 * @return
	 */
	public abstract Prototype<?> getBohrPrototype();
	
	
	/**
	 * return a proxy of memory footprint
	 * @return
	 */
	public long computeFootprint();
	
	
	/**
	 * 
	 * @return
	 */
	public BohrSerializableObject deepClone();

	
	
	public static Prototype<?> getDeserializer(Class<?> c) throws S8IOException{
		for(Field field : c.getFields()){
			
			if(field.isAnnotationPresent(S8Serial.class)) {
				int modifiers = field.getModifiers();
				if(!Modifier.isStatic(modifiers)) {
					throw new S8IOException("S8Serial prototype MUST be a static field");
				}
				if(!Modifier.isFinal(modifiers)) {
					throw new S8IOException("S8Serial prototype MUST be a final field");
				}
				if(!field.getType().equals(BohrSerializableObject.Prototype.class)) {
					throw new S8IOException("S8Serial prototype MUST be of type S8Serializable.SerialPrototype");
				}
				try {
					return (Prototype<?>) field.get(null);
				} 
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new S8IOException("Failed to retrieve S8Serial prototype due to: "+e.getMessage());
				}
			}
		}
		throw new S8IOException("Failed to dinf the deserializer for class: "+c);
	}
}