package com.s8.io.bohr.neon.core;

import java.util.List;

public class NeObject {


	public final NeVertex vertex;


	public NeObject(NeBranch branch, String typeName) {
		super();

		// create vertex
		vertex = new NeVertex(branch, typeName, this);
	}


	public interface VoidLambda {
		public void operate();
	}

	

	public interface Bool8Lambda {
		public void operate(boolean arg);
	}

	public interface Bool8ArrayLambda {
		public void operate(boolean[] arg);
	}

	public interface UInt8Lambda {
		public void operate(int arg);
	}
	
	public interface UInt8ArrayLambda {
		public void operate(int[] arg);
	}

	public interface UInt16Lambda {
		public void operate(int arg);
	}
	
	public interface UInt16ArrayLambda {
		public void operate(int[] arg);
	}
	
	

	public interface UInt32Lambda {
		public void operate(int arg);
	}
	
	public interface UInt32ArrayLambda {
		public void operate(int[] arg);
	}


	public interface UInt64Lambda {
		public void operate(long arg);
	}
	
	public interface UInt64ArrayLambda {
		public void operate(long[] arg);
	}

	public interface Int8Lambda {
		public void operate(int arg);
	}
	
	public interface Int8ArrayLambda {
		public void operate(int[] arg);
	}

	public interface Int16Lambda {
		public void operate(int arg);
	}
	
	public interface Int16ArrayLambda {
		public void operate(int[] arg);
	}
	
	public interface Int32Lambda {
		public void operate(int arg);
	}
	
	public interface Int32ArrayLambda {
		public void operate(int[] arg);
	}

	public interface Int64Lambda {
		public void operate(long arg);
	}

	public interface Int64ArrayLambda {
		public void operate(long[] arg);
	}



	public interface Float32Lambda {
		public void operate(float arg);
	}
	
	public interface Float32ArrayLambda {
		public void operate(float[] arg);
	}


	public interface Float64Lambda {
		public void operate(double arg);
	}
	
	public interface Float64ArrayLambda {
		public void operate(double[] arg);
	}


	public interface StringUTF8Lambda {
		public void operate(String arg);
	}
	
	public interface StringUTF8ArrayLambda {
		public void operate(String[] arg);
	}

	
	public interface ListLambda<T extends NeObject> {
		public void operate(List<T> arg);
	}
}
