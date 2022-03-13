package com.s8.io.bohr.demos.repo0;



import static com.s8.io.bohr.demos.repo0.MyUnit0.Type.IMPERIAL;
import static com.s8.io.bohr.demos.repo0.MyUnit0.Type.SI;
import static com.s8.io.bohr.demos.repo0.MyUnit0.Type.US;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


public class MyUnit0 {


	public final static int CLASS_MAP_CAPACITY = 256;

	public final static int UNITS_MAP_CAPACITY = 256;

	private static boolean isInitialized = false;

	private static HomogeneityClass[] CLASSES;

	private static Map<String, MyUnit0> ABBREVIATIONS_MAP;


	public static HomogeneityClass getClass(int code) {
		initialize();
		return CLASSES[code];
	}


	public enum Type {
		SI, IMPERIAL, US, NONE, UNKNOWN;
	}


	/*
		PICO("p", 1e-12),
		NANO("n", 1e-9),
		MICRO("μu", 1e-6),
		MILLI("m", 1e-3),
		CENTI("c", 1e-2),
		DECI("d", 1e-1),

		DECA("da", 1e1),
		HECTO("h", 1e2),
		KILO("k", 1e3),
		MEGA("M", 1e6),
		GIGA("G", 1e9),
		TERA("T", 1e12),
		PETA("P", 1e15);
	 */

	public static enum HomogeneityClass {


		// special
		NONE(0x00, new MyUnit0[] {
				new MyUnit0(0x00, SI, "<no-unit>", "-", 1, 0)
		}),


		// pure -> 0x1*

		LENGTH(0x11, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Meter", "m", 1, 0), // SI-base

				new MyUnit0(0x11, SI, "Decimeter", "dm", 1e-1, 0),
				new MyUnit0(0x12, SI, "Centimeter", "cm", 1e-2, 0),
				new MyUnit0(0x13, SI, "Millimeter", "mm", 1e-3, 0),
				new MyUnit0(0x14, SI, "Micrometer", "μu", 1e-6, 0),
				new MyUnit0(0x15, SI, "Nanometer", "nm", 1e-9, 0),
				new MyUnit0(0x16, SI, "Picometer", "pm", 1e-12, 0),
				new MyUnit0(0x17, SI, "Femtometer", "fm", 1e-15, 0),

				new MyUnit0(0x21, SI, "Decameter", "dam", 1e1, 0),
				new MyUnit0(0x21, SI, "Hectometer", "hm", 1e2, 0),
				new MyUnit0(0x23, SI, "Kilometer", "km", 1e3, 0),

				new MyUnit0(0x30, IMPERIAL, "Inch", "in", 0.0254, 0), // exactly 2.54 cm

				new MyUnit0(0x40, IMPERIAL, "Mile", "mi", 1609.344, 0), // exactly

		}),

		TIME(0x12, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Second", "s", 1, 0), // SI-base

				new MyUnit0(0x12, SI, "Picosecond", "ps", 1e-12, 0),
				new MyUnit0(0x13, SI, "Nanosecond", "ns", 1e-9, 0),
				new MyUnit0(0x14, SI, "Microsecond", "μs", 1e-6, 0),
				new MyUnit0(0x15, SI, "Millisecond", "ms", 1e-3, 0),

				new MyUnit0(0x20, SI, "Minute", "min", 60, 0),

				new MyUnit0(0x30, SI, "Hour", "h", 3600, 0),		
		}),

		MASS(0x13, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Kilogram", "kg", 1, 0), // SI-base

				new MyUnit0(0x11, SI, "Femtogram", "fg", 1e-18, 0),
				new MyUnit0(0x12, SI, "Picogram", "pg", 1e-15, 0),
				new MyUnit0(0x13, SI, "Nanogram", "ng", 1e-12, 0),
				new MyUnit0(0x14, SI, "Microgram", "μg", 1e-9, 0),
				new MyUnit0(0x15, SI, "Milligram", "mg", 1e-6, 0),
				new MyUnit0(0x16, SI, "Centigram", "cg", 1e-5, 0),
				new MyUnit0(0x17, SI, "Decigram", "dg", 1e-4, 0),
				new MyUnit0(0x18, SI, "Gram", "dg", 1e-3, 0),

				new MyUnit0(0x20, SI, "Ton", "T", 1000, 0),
				new MyUnit0(0x21, SI, "Kiloton", "kT", 1e6, 0),
				new MyUnit0(0x22, SI, "Megaton", "MT", 1e9, 0)
		}),

		FORCE(0x14, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Newton", "N", 1, 0), // SI-base

				new MyUnit0(0x11, SI, "KiloNewton", "kN", 1000.0, 0),
		}),

		TEMPERATURE(0x15, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Kelvin", "K", 1, 0), // SI-base

				new MyUnit0(0x10, SI, "MilliKelvin", "mK", 1e-3, 0),
				new MyUnit0(0x10, SI, "CentiKelvin", "cK", 1e-3, 0),

				new MyUnit0(0x20, SI, "Celsius Degree", "°C", 1, 273.15),
				new MyUnit0(0x30, SI, "Fahrenheit Degree", "°F", 5.0/9.0, 273.15-32*5.0/9.0),
		}),

		POWER(0x16, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Watt", "W", 1, 0), // SI-base

				new MyUnit0(0x11, SI, "FemtoWatt", "fW", 1e-15, 0),
				new MyUnit0(0x12, SI, "PicoWatt", "pW", 1e-12, 0),
				new MyUnit0(0x13, SI, "NanoWatt", "nW", 1e-9, 0),
				new MyUnit0(0x14, SI, "MicroWatt", "μW", 1e-6, 0),
				new MyUnit0(0x15, SI, "MilliWatt", "mW", 1e-3, 0),
				new MyUnit0(0x16, SI, "KilomWatt", "kW", 1e3, 0),
				new MyUnit0(0x17, SI, "MegaWatt", "MW", 1e6, 0),
				new MyUnit0(0x18, SI, "GigaWatt", "GW", 1e9, 0),
				new MyUnit0(0x19, SI, "TeraWatt", "TW", 1e12, 0),
				new MyUnit0(0x1a, SI, "PetaWatt", "PW", 1e15, 0),

				new MyUnit0(0x20, IMPERIAL, "Mechanical Horsepower", "hp", 745.69987158, 0),
				new MyUnit0(0x21, IMPERIAL, "British Thermal Unit per hour", "BTU/h", 0.29307107, 0)

		}),

		MOLE(0x17, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Mole", "mol", 1, 0), // SI-base

				new MyUnit0(0x10, SI, "KiloMole", "kmol", 1, 0)
		}),



		// exponents >= 2 only -> 0x2*

		AREA(0x21, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Square Meter", "m2", 1, 0), // SI-base

				new MyUnit0(0x11, SI, "Square Femtometer", "fm2", 1e-30, 0),
				new MyUnit0(0x12, SI, "Square Picometer", "pm2", 1e-24, 0),
				new MyUnit0(0x13, SI, "Square Nanometer", "nm2", 1e-18, 0),
				new MyUnit0(0x14, SI, "Square Micrometer", "μu2", 1e-12, 0),
				new MyUnit0(0x15, SI, "Square Millimeter", "mm2", 1e-6, 0),
				new MyUnit0(0x16, SI, "Square Centimeter", "cm2", 1e-4, 0),
				new MyUnit0(0x17, SI, "Square Decimeter", "dm2", 1e-2, 0),
				new MyUnit0(0x18, SI, "Square Decameter", "dam2", 1e2, 0),
				new MyUnit0(0x19, SI, "Acre", "ha", 1e4, 0),
				new MyUnit0(0x1a, SI, "Square Kilometer", "km", 1e6, 0),

				new MyUnit0(0x20, IMPERIAL, "Square Inch", "in2", 0.0254*0.0254, 0), // exactly 2.54 cm

				new MyUnit0(0x30, IMPERIAL, "Square Mile", "mi2", 1609.344*1609.344, 0), // exactly
		}),

		VOLUME(0x22, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Cubic Meter", "m3", 1, 0), // SI-base

				new MyUnit0(0x11, SI, "Cubic Femtometer", "fm3", 1e-45, 0),
				new MyUnit0(0x12, SI, "Cubic Picometer", "pm3", 1e-36, 0),
				new MyUnit0(0x13, SI, "Cubic Nanometer", "nm3", 1e-27, 0),
				new MyUnit0(0x14, SI, "Cubic Micrometer", "μu3", 1e-18, 0),
				new MyUnit0(0x15, SI, "Cubic Millimeter", "mm3", 1e-9, 0),
				new MyUnit0(0x16, SI, "Cubic Centimeter", "cm3", 1e-6, 0),
				new MyUnit0(0x17, SI, "Cubic Decimeter", "dm3", 1e-3, 0),
				new MyUnit0(0x18, SI, "Cubic Decameter", "dam3", 1e2, 0),
				new MyUnit0(0x1a, SI, "Cubic Kilometer", "km3", 1e6, 0),

				new MyUnit0(0x20, IMPERIAL, "Cubic Inch", "in2", 0.0254*0.0254*0.0254, 0), // exactly 2.54 cm

				new MyUnit0(0x30, IMPERIAL, "Cubic Mile", "mi2", 1609.344*1609.344*1609.344, 0), // exactly

				new MyUnit0(0x40, SI, "Liter", "L", 1e-3, 0),
				new MyUnit0(0x41, SI, "Milliliter", "mL", 1e-6, 0),
				new MyUnit0(0x42, SI, "Centiliter", "cL", 1e-6, 0),
				new MyUnit0(0x43, SI, "Deciiliter", "dL", 1e-6, 0),

				new MyUnit0(0x50, IMPERIAL, "Gallon", "gal", 0.00454609, 0),
				new MyUnit0(0x51, US, "Gallon US", "gal(US)", 0.003785411784, 0)
		}),


		PER_LENGTH_UNIT(0x23, new MyUnit0[] {
				new MyUnit0(0x00, SI, "per meter", "/m", 1, 0),
		}),



		// simple compounds -> 0x3*



		VELOCITY(0x30, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Meter per Second", "m.s-1", 1, 0), // SI-base

				new MyUnit0(0x20, SI, "Kilometer per hour", "km.h-1", 1000/3600, 0),
				new MyUnit0(0x21, IMPERIAL, "Mile per hour", "mph", 1609.344/3600, 0)
		}),


		ROTATIONAL_SPEED(0x31, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Radian per Second", "rad.s-1", 1, 0), // SI-base

				new MyUnit0(0x20, SI, "Revolution per minute", "RPM", (2.0*Math.PI)/60.0, 0),
		}),

		ACCELERATION(0x32, new MyUnit0[] {
				new MyUnit0(0x10, SI, "Meter per Second^2", "m.s-2", 1, 0) // SI-base
		}),

		VOLUME_FLOW(0x33, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Cubic Meter per Second", "m3.s-1", 1, 0), // SI-base

				new MyUnit0(0x11, SI, "Cubic Meter per Minute", "m3.min-1", 1.0/60, 0),
				new MyUnit0(0x12, SI, "Cubic Meter per Hour", "m3.h-1", 1.0/3600, 0),

				new MyUnit0(0x20, SI, "Liter per Second", "L.s-1", 1-3, 0),
				new MyUnit0(0x21, SI, "Deciliter per Second", "dL.s-1", 1-4, 0),
				new MyUnit0(0x22, SI, "Centiliter per Second", "cL.s-1", 1-5, 0),
				new MyUnit0(0x23, SI, "Milliliter per Second", "mL.s-1", 1-6, 0)
		}),

		TORQUE(0x34, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Newton Meter", "Nm", 1, 0), // SI-base
				new MyUnit0(0x11, SI, "KiloNewton Meter", "kNm", 1000.0, 0),
		}),

		DENSITY(0x35, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Kilogram per Cubic Meter", "kg.m-3", 1, 0)// SI-base
		}),

		SPECIFIC_VOLUME(0x36, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Cubic Meter per Kilogram", "m3.kg-1", 1, 0) // SI-base
		}),

		MASS_FLOW(0x37, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Kilogram per Second", "kg.s-1", 1, 0), // SI_base

				new MyUnit0(0x20, SI, "Kilogram per Hour", "kg.h-1", 1.0/3600, 0),
				new MyUnit0(0x30, SI, "Ton per Hour", "T.h-1", 1000.0/3600, 0)
		}),

		PRESSURE(0x38, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Pascal", "Pa", 1, 0), // SI base

				new MyUnit0(0x11, SI, "Pascal (absolute)", "Pa(a)", 1, 0),
				new MyUnit0(0x12, SI, "Pascal (gauge)", "Pa(a)", 1, 1e5),
				new MyUnit0(0x13, SI, "Pascal (vaccum)", "Pa(v)", -1, 1e5),
				new MyUnit0(0x14, SI, "MegaPascal", "MPa", 1e6, 0),
				new MyUnit0(0x15, SI, "GigaPascal", "GPa", 1e9, 0),

				new MyUnit0(0x20, SI, "KiloPascal", "kPa", 1e3, 0),
				new MyUnit0(0x21, SI, "KiloPascal (absolute)", "kPa(a)", 1e3, 0),
				new MyUnit0(0x22, SI, "KiloPascal (gauge)", "kPa(g)", 1e3, 0),
				new MyUnit0(0x23, SI, "KiloPascal (vaccum)", "kPa(v)", 1e3, 0),

				new MyUnit0(0x30, SI, "Bar", "bar", 1e5, 0),
				new MyUnit0(0x31, SI, "Bar (absolute)", "bar(a)", 1e5, 0),
				new MyUnit0(0x32, SI, "Bar (gauge)", "bar(g)", 1e5, 1e5),

				new MyUnit0(0x40, SI, "Millibar", "mbar", 1e2, 0),
				new MyUnit0(0x41, SI, "Millibar (absolute)", "mbar(a)", 1e2, 0),
				new MyUnit0(0x42, SI, "Millibar (gauge)", "mbar(g)", 1e2, 1e5),

				new MyUnit0(0x52, IMPERIAL, "PSI", "psi", 6894.75729317831, 0.0),
				new MyUnit0(0x53, IMPERIAL, "PSI (absolute)", "psi", 6894.75729317831, 0.0),
				new MyUnit0(0x54, IMPERIAL, "PSI (gauge)", "psi(g)", 6894.75729317831, 1.0e5)

		}),


		ENERGY(0x39, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Joule", "J", 1, 0), // SI base

				new MyUnit0(0x11, SI, "FemtoJoule", "fJ", 1e-15, 0),
				new MyUnit0(0x12, SI, "PicoJoule", "pJ", 1e-12, 0),
				new MyUnit0(0x13, SI, "NanoJoule", "nJ", 1e-9, 0),
				new MyUnit0(0x14, SI, "MicroJoule", "μJ", 1e-6, 0),
				new MyUnit0(0x15, SI, "MilliJoule", "mJ", 1e-3, 0),
				new MyUnit0(0x16, SI, "KiloJoule", "kJ", 1e3, 0),
				new MyUnit0(0x17, SI, "MegaJoule", "MJ", 1e6, 0),
				new MyUnit0(0x18, SI, "GigaJoule", "GJ", 1e9, 0),
				new MyUnit0(0x19, SI, "TeraJoule", "TJ", 1e12, 0),
				new MyUnit0(0x1a, SI, "PetaJoule", "PJ", 1e15, 0),

				new MyUnit0(0x10, SI, "Watt hour", "Wh", 1, 0),

				new MyUnit0(0x15, SI, "MilliWatt hour", "mWh", 1e-3, 0),
				new MyUnit0(0x16, SI, "KiloWatt hour", "kWh", 1e3, 0),
				new MyUnit0(0x17, SI, "MegaWatt hour", "MWh", 1e6, 0),
				new MyUnit0(0x18, SI, "GigaWatt hour", "GWh", 1e9, 0),
				new MyUnit0(0x19, SI, "TeraWatt hour", "TWh", 1e12, 0),
				new MyUnit0(0x1a, SI, "PetaWatt hour", "PWh", 1e15, 0),

				new MyUnit0(0x1a, SI, "BTU (IT)", "BTU", 1055.05585262, 0),
				new MyUnit0(0x1a, SI, "KiloBTU (IT)", "kBTU", 1055055.85262, 0),
				new MyUnit0(0x1a, SI, "MegaBTU (IT)", "MBTU", 1055055852.62, 0),
		}),

		MOLAR_MASS(0x37, new MyUnit0[] {
				new MyUnit0(0x00, SI, "KiloGramPerMole", "g.mol-1", 1, 0), // SI-base
				new MyUnit0(0x10, SI, "GramPerMole", "g.mol-1", 0.001, 0),
		}),


		// complex compounds / thermodynamics -> 0x4*


		MASS_FLUX(0x42, new MyUnit0[] {
				new MyUnit0(0x10, SI, "Kilogram per Second per square Meter", "kg.s-1.m-2", 1, 0)
		}),


		SPECIFIC_ENERGY(0x43, new MyUnit0[]{ // for instance Enthalpy, etc.
				new MyUnit0(0x00, SI, "Joule per Kilogram", "J.kg-1", 1, 0),
				new MyUnit0(0x12, SI, "Kilo Joule per Kilogram", "kJ.kg-1", 1000, 0)
		}),


		SPECIFIC_HEAT(0x44, new MyUnit0[] { // 
				new MyUnit0(0x00, SI, "Joule per Kilogram per Kelvin", "J.kg-1.K-1", 1, 0),
				new MyUnit0(0x12, SI, "Kilo Joule per Kilogram per Kelvin", "kJ.kg-1.K-1", 1000, 0)
		}),

		THERMAL_CONDUCTIVITY(0x45, new MyUnit0[] { // 
				new MyUnit0(0x00, SI, "Watt per meter per Kelvin", "W.m-1.K-1", 1, 0),
				new MyUnit0(0x12, SI, "Milliwatt per meter per Kelvin", "mW.m-1.K-1", 0.001, 0)
		}),


		HEAT_TRANSFER_COEFFICIENT (0x46, new MyUnit0[] { // 
				new MyUnit0(0x00, SI, "Watt per square meters per Kelvin", "W.m-2.K-1", 1, 0)
		}),

		THERMAL_RESISTANCE (0x47, new MyUnit0[] { // 
				new MyUnit0(0x00, SI, "Kelvin per Watt", "K.W-1", 1, 0)
		}),

		THERMAL_RESISTIVITY (0x48, new MyUnit0[] { // 
				new MyUnit0(0x00, SI, "Kelvin square meters per Watt", "K.m2.W-1", 1, 0)
		}),

		KINEMATIC_VISCOSITY(0x49, new MyUnit0[] { // Thermal diffusivity
				new MyUnit0(0x00, SI, "Square meters per second", "m2.s-1", 1, 0),
				new MyUnit0(0x12, SI, "Stockes", "St", 1e-4, 0),
				new MyUnit0(0x13, SI, "CentiStockes", "cSt", 1e-6, 0)
		}),

		DYNAMIC_VISCOSITY(0x50, new MyUnit0[] {
				new MyUnit0(0x00, SI, "Kilograms per meter per second", "kg.m-1.s-1", 1, 0),
				new MyUnit0(0x01, SI, "Pascal second", "Pa.s", 1, 0),
				new MyUnit0(0x12, SI, "Milli Pascal second", "mPa.s", 1e-3, 0),
				new MyUnit0(0x13, SI, "Micro Pascal second", "μPa.s", 1e-6, 0)
		});

		public int code;

		public MyUnit0[] units;

		private MyUnit0[] codeMap;

		private HomogeneityClass(int code, MyUnit0[] units) {
			this.code = code;
			this.units = units;

			// map
			codeMap = new MyUnit0[UNITS_MAP_CAPACITY];
			for(MyUnit0 unit : units) {
				unit.homogeneityClass = this;
				codeMap[unit.code] = unit;
			}
		}


		public MyUnit0 getUnitByCode(int code) {
			return codeMap[code];
		}

		public MyUnit0 read(ByteInflow inflow) throws IOException {
			return codeMap[inflow.getUInt8()];
		}
	}


	public final static String REGEX = " ?([0-9\\.\\-eE]*) ?([a-zA-Z°\\-1-9\\.]*) ?";
	public final static Pattern PATTERN = Pattern.compile(REGEX);


	private HomogeneityClass homogeneityClass;

	private int code;

	private Type type;

	private String name;

	private String abbreviation;

	/**
	 * primary scalar factor for conversion UNIT:x -> SI:y=a*x+b
	 */
	private double a;

	/**
	 * primary scalar offset for conversion UNIT:x -> SI:y=a*x+b
	 */
	private double b;

	/**
	 * Scalar factor for conversion SI:x -> UNIT:y=c*(x-b) 
	 * note: multiplication is MUCH faster than division on most processor at the time of writing
	 * c acts as a a^-1 cache.
	 */
	private double c;

	/**
	 * 
	 * @param code
	 * @param name
	 * @param abbreviation
	 * @param a
	 * @param b
	 */
	public MyUnit0(int code, Type type, String name, String abbreviation, double a, double b) {
		super();
		this.code = code;
		this.type = type;
		this.name = name;
		this.abbreviation = abbreviation;
		this.a = a;
		this.b = b;
		this.c = 1.0/a;
	}


	public HomogeneityClass getHomogeneityClass() {
		return homogeneityClass;
	}

	public int getCode() {
		return code;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}


	/**
	 * 
	 * @param value
	 * @return the SI value converted into this unit
	 */
	public double convert(double value) {
		/*
		 *  note: multiplication is MUCH faster than division on most processor at the time of writing
		 */
		return c*(value-b);
	}

	/**
	 * Convert the value passed as argument back into the SI unit system.
	 * 
	 * @param SI_value
	 * @return the SI unit retrieved from the value passed as argument (which in <code>this</code> unit)
	 */
	public double revert(double value) {
		return a*value+b;
		// reverse is (si-b)/a;
	}


	public static MyUnit0 getUnitByAbbreviation(String abbreviation) {
		initialize();
		return ABBREVIATIONS_MAP.get(abbreviation);
	}

	public static MyUnit0 read(ByteInflow inflow) throws IOException {
		int homogeneityCode = inflow.getUInt8();
		HomogeneityClass homogeneityClass = CLASSES[homogeneityCode];
		if(homogeneityClass!=null) {
			int unitCode = inflow.getUInt8();
			return homogeneityClass.getUnitByCode(unitCode);		
		}
		else {
			return null;
		}
	}


	public void write(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(homogeneityClass.code);
		outflow.putUInt8(code);
	}



	/**
	 * read a value and convert it in IS
	 */
	public static double read(String measure){
		Matcher m = PATTERN.matcher(measure);
		if(m.find()) {
			//System.out.println(Unit.get(m.group(2)).toString());
			String value = m.group(1);
			String abbreviation = m.group(2);
			MyUnit0 unit = MyUnit0.getUnitByAbbreviation(abbreviation);
			if(unit==null) {
				throw new RuntimeException("Failed to retrieve unit: "+abbreviation);
			}
			return unit.revert(Double.valueOf(value));
		}
		else{
			throw new RuntimeException("Measure cannot be read (invalid format).");
		}
	}


	private static void initialize() {
		if(!isInitialized) {
			CLASSES = new HomogeneityClass[CLASS_MAP_CAPACITY];
			for(HomogeneityClass homogeneityClass : HomogeneityClass.values()) {
				CLASSES[homogeneityClass.code] = homogeneityClass;
			}

			ABBREVIATIONS_MAP = new HashMap<>();
			for(HomogeneityClass homogeneityClass : HomogeneityClass.values()) {
				for(MyUnit0 unit : homogeneityClass.units) {
					ABBREVIATIONS_MAP.put(unit.getAbbreviation(), unit);
				}
			}

			// true
			isInitialized = true;
		}
	}


}
