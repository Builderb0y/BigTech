package builderb0y.bigtech.util;

public class BigTechMath {

	//////////////////////////////// modulus ////////////////////////////////

	//in java, the default % operator is fucking useless when a < 0 || b <= 0.
	//these methods are how you do modulus operations PROPERLY.
	//these implementations make the following guarantees:
	//	if (b > 0), then (a mod b) >= 0.
	//	if (b < 0), then (a mod b) <= 0.
	//	if (b == 0), then (a mod b) == 0.
	//		for floats and doubles, if (b == 0), then (a mod b) = 0 with the same sign as b.
	//	for floats and doubles, if (b is NaN), then (a mod b) = b.
	//	for floats and doubles, the sign bit of (a mod b) equals the sign bit of b.

	/** returns (a mod b) when nothing is known in advance about the signs of a or b. */
	public static int modulus(int a, int b) {
		if      (b > 0) return modulus_BP(a, b);
		else if (b < 0) return modulus_BN(a, b);
		else            return 0; //lim[b -> 0] (a mod b) = 0 for all values of a.
	}

	/**
	returns (a mod b) when b is known in advance to be positive (> 0).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static int modulus_BP(int a, int b) {
		return (a %= b) < 0 ? a + b : a;
	}

	/**
	returns (a mod b) when b is known in advance to be negative (< 0).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static int modulus_BN(int a, int b) {
		return (a %= -b) > 0 ? a + b : a;
	}

	/** returns (a mod b) when nothing is known in advance about the signs of a or b. */
	public static long modulus(long a, long b) {
		if      (b > 0L) return modulus_BP(a, b);
		else if (b < 0L) return modulus_BN(a, b);
		else             return 0L; //lim[b -> 0] (a mod b) = 0 for all values of a.
	}

	/**
	returns (a mod b) when b is known in advance to be positive (> 0L).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static long modulus_BP(long a, long b) {
		return (a %= b) < 0L ? a + b : a;
	}

	/**
	returns (a mod b) when b is known in advance to be negative (< 0L).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static long modulus_BN(long a, long b) {
		return (a %= -b) > 0L ? a + b : a;
	}

	/** returns (a mod b) when nothing is known in advance about the signs of a or b. */
	public static float modulus(float a, float b) {
		if      (b > 0.0F) return modulus_BP(a, b);
		else if (b < 0.0F) return modulus_BN(a, b);
		else               return b; //+0.0, -0.0, and NaN.
	}

	/**
	returns (a mod b) when b is known in advance to be positive (> 0.0F).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static float modulus_BP(float a, float b) {
		float mod = a % b;
		if (mod < 0.0F) mod += b;
		//convert -0.0F to +0.0F.
		mod = Float.intBitsToFloat(Float.floatToRawIntBits(mod) & 0x7FFF_FFFF);
		return mod;
	}

	/**
	returns (a mod b) when b is known in advance to be negative (< 0.0F).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static float modulus_BN(float a, float b) {
		float mod = a % -b;
		//b is negative, so this acts as subtraction, not addition.
		if (mod > 0.0F) mod += b;
		//convert +0.0F to -0.0F.
		mod = Float.intBitsToFloat(Float.floatToRawIntBits(mod) | 0x8000_0000);
		return mod;
	}

	/** returns (a mod b) when nothing is known in advance about the signs of a or b. */
	public static double modulus(double a, double b) {
		if      (b > 0.0D) return modulus_BP(a, b);
		else if (b < 0.0D) return modulus_BN(a, b);
		else               return b; //+0.0, -0.0, and NaN.
	}

	/**
	returns (a mod b) when b is known in advance to be positive (> 0.0D).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static double modulus_BP(double a, double b) {
		double mod = a % b;
		if (mod < 0.0D) mod += b;
		//convert -0.0D to +0.0D.
		mod = Double.longBitsToDouble(Double.doubleToRawLongBits(mod) & 0x7FFF_FFFF_FFFF_FFFFL);
		return mod;
	}

	/**
	returns (a mod b) when b is known in advance to be negative (< 0.0D).
	this method makes no assumptions about a.
	*/
	@SuppressWarnings("UseOfRemainderOperator")
	public static double modulus_BN(double a, double b) {
		double mod = a % -b;
		//b is negative, so this acts as subtraction, not addition.
		if (mod > 0.0D) mod += b;
		//convert +0.0D to -0.0.
		mod = Double.longBitsToDouble(Double.doubleToRawLongBits(mod) | 0x8000_0000_0000_0000L);
		return mod;
	}
}