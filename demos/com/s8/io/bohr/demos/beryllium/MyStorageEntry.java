package com.s8.io.bohr.demos.beryllium;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8RowType;


/**
 * 
 * @author pierreconvert
 *
 */
public @S8RowType(name = "storage-entry") final class MyStorageEntry {

	
	@S8Field(name = "id") 
	public String id;
	
	
	
	public enum Category {
		FURNITURE, ELECTRICAL_APPLIANCES, POWER_TOOLS, GARDEN;
	}
	
	
	@S8Field(name = "category") 
	public Category category;
	
	
	@S8Field(name = "quantity")
	public int quantity;

	
	@S8Field(name = "is-stored")
	public boolean isStored;
	
	
	@S8Field(name = "size")
	public float size;

	
	@S8Field(name = "lattitude")
	public double lattitude;

	
	@S8Field(name = "longitude")
	public double longitude;
	
	
	@S8Field(name = "timestamp")
	public long timestamp;

	
	/**
	 * 
	 */
	public MyStorageEntry() {
		super();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static MyStorageEntry generateRandom() {
		MyStorageEntry entry = new MyStorageEntry();
		entry.shuffle();
		return entry;
	}
	
	
	/**
	 * 
	 */
	public void shuffle() {
		id = "CODE_TRGE_"+System.nanoTime()+ ((int) (256*Math.random()));
		category = Category.values()[(int) (4*Math.random())];
		quantity = (int) (1024*Math.random());
		isStored = Math.random()<0.5;
		size = (float) (Math.random()*1024);
		lattitude = 2*(Math.random()-0.5)*90;
		longitude = Math.random()*360;
		timestamp = System.currentTimeMillis();
	}

}
