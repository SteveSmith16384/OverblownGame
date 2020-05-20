package ssmith.lang;

import java.util.Random;

public class NumberFunctions {

	private static Random random = new Random();

	public static int biggest(int a, int b) {
		return Math.max(a, b);
	}

	
	public static int rnd(int a, int b) {
		int var = b + 1 - a;
		return random.nextInt(var) + a;
	}

	
	public static byte rndByte(int a, int b) {
		assert a <= 127 && b <= 127;
		int var = b + 1 - a;
		return (byte)(random.nextInt(var) + a);
	}

	
	public static short rndShort(int a, int b) {
		int var = b + 1 - a;
		return (short)(random.nextInt(var) + a);
	}

	
	public static float rndFloat(float a, float b) {
		return (random.nextFloat() * (b - a)) + a;
	}

	
	public static double rndDouble(double a, double b) {
		return (random.nextDouble() * (b - a)) + a;
	}

	
	public static int sign(int a) {
		if (a == 0) {
			return 0;
		}
		else if (a > 0) {
			return 1;
		}
		else {
			return -1;
		}
	}

	
	public static int sign(float a) {
		if (a == 0) {
			return 0;
		}
		else if (a > 0) {
			return 1;
		}
		else {
			return -1;
		}
	}

	
	public static int sign(double a) {
		if (a == 0) {
			return 0;
		}
		else if (a > 0) {
			return 1;
		}
		else {
			return -1;
		}
	}

	
	public static float MakeSameSignAs(float num, float s) {
		if (sign(num) != sign(s)) {
			return num * -1;
		} else {
			return num;
		}
	}

	
	public static int mod(int x) {
		if (x >= 0) {
			return x;
		}
		else {
			return x * -1;
		}
	}

	
	public static float mod(float x) {
		if (x >= 0) {
			return x;
		}
		else {
			return x * -1;
		}
	}

	
	public static double mod(double x) {
		if (x >= 0) {
			return x;
		}
		else {
			return x * -1;
		}
	}

	
	public static int ParseInt(String s) {
		return ParseInt(s, true);
	}
	
	
	public static int ParseInt(String s, boolean error) {
		try {
			if (s == null && error == false) {
				s = "0";
			} else if (s.length() == 0) { // In case of "-1"
				s = "0";
			}
			s = s.trim();
			if (s.startsWith("+") && s.length() > 1) {
				s = s.substring(1);
			}
			return Integer.parseInt(s);
		} catch (java.lang.NumberFormatException ex) {
			if (error) {
				throw ex;
			} else {
				return 0;
			}
		}
	}
	
	
	public static byte ParseByte(String s) {
		return ParseByte(s, true);
	}
	
	
	public static byte ParseByte(String s, boolean error) {
		try {
			if (s == null && error == false) {
				s = "0";
			} else if (s.length() == 0) { // In case of "-1"
				s = "0";
			}
			return Byte.parseByte(s.trim());
		} catch (java.lang.NumberFormatException ex) {
			if (error) {
				throw new NumberFormatException("Error parsing '" + s + "'");
			} else {
				return 0;
			}
		}
	}
	
	
	public static short ParseShort(String s) {
		return ParseShort(s, true);
	}
	
	
	public static short ParseShort(String s, boolean error) {
		try {
			if (s == null && error == false) {
				s = "0";
			} else if (s.length() == 0) { // In case of "-1"
				s = "0";
			}
			return Short.parseShort(s.trim());
		} catch (java.lang.NumberFormatException ex) {
			if (error) {
				throw ex;
			} else {
				return 0;
			}
		}
	}
	

	public static boolean IsNumeric(String s) {
		try {
			NumberFunctions.ParseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	


}
