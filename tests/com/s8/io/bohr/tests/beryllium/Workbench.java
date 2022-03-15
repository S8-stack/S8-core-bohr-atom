package com.s8.io.bohr.tests.beryllium;

import java.io.IOException;
import java.nio.file.Path;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.beryllium.codebase.BeCodebase;
import com.s8.io.bohr.beryllium.codebase.BeCodebaseBuilder;
import com.s8.io.bohr.beryllium.tables.BerylTable;
import com.s8.io.bohr.beryllium.types.BeType;
import com.s8.io.bohr.beryllium.types.BeTypeBuildException;
import com.s8.io.bohr.demos.beryllium.MyStorageEntry;


/**
 * 
 * @author pierreconvert
 *
 * @param <R>
 */
public class Workbench {



	public final BeType type;

	public final BeCodebase codebase;

	public Workbench(Class<?> rawType) throws BeTypeBuildException {
		super();
		BeCodebaseBuilder.UpperLevel upperLevel = new BeCodebaseBuilder.UpperLevel() {

			@Override
			public void pushObjectType(Class<?> type) throws S8BuildException {
			}
		};
		BeCodebaseBuilder builder = new BeCodebaseBuilder(upperLevel, false);
		builder.pushRowType(MyStorageEntry.class);
		codebase = builder.finish();
		type = codebase.getTypeByRuntimeName(rawType.getName());
	}




	/**
	 * 
	 * @param <T>
	 * @param type
	 * @param address
	 * @param pathname
	 * @return
	 * @throws S8Exception
	 * @throws IOException
	 */
	public BerylTable createSerie(String address, Path path, Class<?> rawType) 
			throws S8Exception, IOException {


		/*
		BerylTable table = new BerylTable(address, path, codebase);
		M3Config config = new M3Config();
		config.maxNbLoaded = 65536;
		table.init(config, rawType);
		return table;
		 */

		return null;

	}




}
