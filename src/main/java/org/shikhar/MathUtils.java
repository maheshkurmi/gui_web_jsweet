package org.shikhar;


import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class MathUtils {
	static boolean chkIrrational=true;

    // constants
	
	static double ROOT_2=Math.sqrt(2);
	static double ROOT_3=Math.sqrt(3);
	static double ROOT_5=Math.sqrt(5);
	static double ROOT_6=Math.sqrt(6);
	static double ROOT_7=Math.sqrt(7);
	static String S_ROOT_2="\u221A"+2;
	static String S_ROOT_3="\u221A"+3;
	static String S_ROOT_5="\u221A"+5;
	static String S_ROOT_6="\u221A"+6;
	static String S_ROOT_7="\u221A"+7;

    static final double sq2p1 = 2.414213562373095048802e0;
    static final double sq2m1  = .414213562373095048802e0;
    static final double p4  = .161536412982230228262e2;
    static final double p3  = .26842548195503973794141e3;
    static final double p2  = .11530293515404850115428136e4;
    static final double p1  = .178040631643319697105464587e4;
    static final double p0  = .89678597403663861959987488e3;
    static final double q4  = .5895697050844462222791e2;
    static final double q3  = .536265374031215315104235e3;
    static final double q2  = .16667838148816337184521798e4;
    static final double q1  = .207933497444540981287275926e4;
    static final double q0  = .89678597403663861962481162e3;
    static final double PIO2 = 1.5707963267948966135E0;
    static final double PI2 = 2*Math.PI;
    static public Random random = new Random();

    static final double nan = (0.0/0.0);
    
	static public final float FLOAT_ROUNDING_ERROR = 0.000001f; // 32 bits
	static private final Matcher matcher_trail_0s = Pattern.compile("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$").matcher("");
    /*
	static public final double PI = 3.1415927f;
	static public final double PI2 = PI * 2;
	static public final double E = 2.7182818f;

	static private final int SIN_BITS = 10; // 16KB. Adjust for accuracy.
	static private final int SIN_MASK = ~(-1 << SIN_BITS);
	static private final int SIN_COUNT = SIN_MASK + 1;

	static private final double radFull = PI * 2;
	static private final double degFull = 360;
	static private final double radToIndex = SIN_COUNT / radFull;
	static private final double degToIndex = SIN_COUNT / degFull;

	/** multiply by this to convert from radians to degrees 
	static public final double radiansToDegrees = 180f / PI;
	static public final double radDeg = radiansToDegrees;
	/** multiply by this to convert from degrees to radians 
	static public final double degreesToRadians = PI / 180;
	static public final double degRad = degreesToRadians;
	
	static private class Sin {
		static final double[] table = new double[SIN_COUNT];
		static {
			for (int i = 0; i < SIN_COUNT; i++)
				table[i] = Math.sin((i + 0.5f) / SIN_COUNT * radFull);
			for (int i = 0; i < 360; i += 90)
				table[(int)(i * degToIndex) & SIN_MASK] = Math.sin(i * degreesToRadians);
		}
	}

	/** Returns the sine in radians from a lookup table. 
	static public double sin (double radians) {
		return Sin.table[(int)(radians * radToIndex) & SIN_MASK];
	}

	/** Returns the cosine in radians from a lookup table. 
	static public double cos (double radians) {
		return Sin.table[(int)((radians + PI / 2) * radToIndex) & SIN_MASK];
	}

	/** Returns the sine in radians from a lookup table. 
	static public double sinDeg (double degrees) {
		return Sin.table[(int)(degrees * degToIndex) & SIN_MASK];
	}

	/** Returns the cosine in radians from a lookup table. 
	static public double cosDeg (double degrees) {
		return Sin.table[(int)((degrees + 90) * degToIndex) & SIN_MASK];
	}


	/** Returns atan2 in radians, faster but less accurate than Math.atan2. Average error of 0.00231 radians (0.1323 degrees),
	 * largest error of 0.00488 radians (0.2796 degrees). 
	static public double atan2 (double y, double x) {
		if (x == 0f) {
			if (y > 0f) return PI / 2;
			if (y == 0f) return 0f;
			return -PI / 2;
		}
		final double atan, z = y / x;
		if (Math.abs(z) < 1f) {
			atan = z / (1f + 0.28f * z * z);
			if (x < 0f) return y < 0f ? atan - PI : atan + PI;
			return atan;
		}
		atan = PI / 2 - z / (z * z + 0.28f);
		return y < 0f ? atan - PI : atan;
	}
	*/
	

  
    

    /**
     * Returns value rounded off to decimal places as in preference
     * @param value
     * @return
     */
    public static double roundOffToSigFigures(double value, int n)
    {
     	if(value == 0) {
    	        return 0;
    	}

    	final double d = Math.ceil(Math.log10(value < 0 ? -value: value));
    	final int power = n - (int) d;

    	double magnitude = Math.pow(10, power);
    	//if(power>0)magnitude=Math.round(magnitude);
    	final long shifted = Math.round(value*magnitude);
    	return  shifted/magnitude;
    }

	

	public static double clamp (double value, double min, double max) {
		if (value < min) return min;
		if (value > max) return max;
		return value;
	}
	
	/** Returns the next power of two. Returns the specified value if the value is already a power of two. */
	public static int nextPowerOfTwo (int value) {
		if (value == 0) return 1;
		value--;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}
	
	static public boolean isPowerOfTwo (int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	/** Linearly interpolates between fromValue to toValue on progress position. */
	static public float lerp (float fromValue, float toValue, float progress) {
		return fromValue + (toValue - fromValue) * progress;
	}
	
	/**
	   * Normalize an angle in a 2&pi wide interval around a center value.
	   * This method has three main uses:
	   * <ul>
	   *   <li>normalize an angle between 0 and 2&pi;:<br/>
	   *       <code>a = MathUtils.normalizeAngle(a, Math.PI);</code></li>
	   *   <li>normalize an angle between -&pi; and +&pi;<br/>
	   *       <code>a = MathUtils.normalizeAngle(a, 0.0);</code></li>
	   *   <li>compute the angle between two defining angular positions:<br>
	   *       <code>angle = MathUtils.normalizeAngle(end, start) - start;</code></li>
	   * </ul>
	   * Note that due to numerical accuracy and since &pi; cannot be represented
	   * exactly, the result interval is <em>closed</em>, lit cannot be half-closed
	   * as would be more satisfactory in a purely mathematical view.
	   * @param a angle to normalize in radians
	   * @param center center of the desired 2&pi in radians; interval for the result
	   * @return a-2k&pi; with integer k and center-&pi; &lt;= a-2k&pi; &lt;= center+&pi;
	   */
	   public static double normalizeAngle(double a, double center) {
	       return a - PI2 * Math.floor((a + Math.PI - center) / PI2);
	   }
	   
	   
	   
	
}
