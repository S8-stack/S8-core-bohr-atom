package com.s8.io.bohr.beryllium.fields;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.beryllium.fields.lists.StringListBeField;
import com.s8.io.bohr.beryllium.fields.objects.EnumBeField;
import com.s8.io.bohr.beryllium.fields.primitives.BooleanBeField;
import com.s8.io.bohr.beryllium.fields.primitives.DoubleBeField;
import com.s8.io.bohr.beryllium.fields.primitives.FloatBeField;
import com.s8.io.bohr.beryllium.fields.primitives.IntegerBeField;
import com.s8.io.bohr.beryllium.fields.primitives.LongBeField;
import com.s8.io.bohr.beryllium.fields.primitives.ShortBeField;
import com.s8.io.bohr.beryllium.fields.primitives.StringBeField;
import com.s8.io.bohr.beryllium.types.BeTypeBuildException;

/**
 * 
 * @author pierreconvert
 *
 */
public class BeFieldFactory {

	
	public final static MappedBeField.Prototype[] MAPPED = new MappedBeField.Prototype[] {
			BooleanBeField.PROTOTYPE,
			ShortBeField.PROTOTYPE,
			IntegerBeField.PROTOTYPE,
			LongBeField.PROTOTYPE,
			FloatBeField.PROTOTYPE,
			DoubleBeField.PROTOTYPE,
			StringBeField.PROTOTYPE
	};
	
	public final static ScreenedBeField.Prototype[] SCREENED = new ScreenedBeField.Prototype[] {
			EnumBeField.PROTOTYPE,
			StringListBeField.PROTOTYPE
	};
	

	
	public final Map<String, MappedBeField.Prototype> map;
	
	
	/**
	 * 
	 */
	public BeFieldFactory() {
		super();
		this.map = new HashMap<>();
		for(MappedBeField.Prototype prototype : MAPPED) {
			map.put(prototype.getTypeName(), prototype);
		}
	}
	
	public BeField create(Field field) throws BeTypeBuildException {
		S8Field annotation = field.getAnnotation(S8Field.class);
		if(annotation!=null) {
			Class<?> fieldType = field.getType();
			
			// try mappedd
			MappedBeField.Prototype mappedPrototype;
			if((mappedPrototype = map.get(fieldType.getName()))!=null) {
				return mappedPrototype.createField(
						annotation.name(), 
						annotation.props(), 
						field);
			}
			else {
				BeField fieldHandler;
				for(ScreenedBeField.Prototype screenedPrototype : SCREENED) {
					fieldHandler = screenedPrototype.captureField(
							annotation.name(), 
							annotation.props(), 
							field);
					if(fieldHandler!=null) {
						return fieldHandler;
					}
				}
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	
}
