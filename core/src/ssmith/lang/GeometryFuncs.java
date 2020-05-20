package ssmith.lang;

public class GeometryFuncs {

	public static double distance(int x1, int y1, int x2, int y2) {
		//System.out.println(x1+","+y1+"  "+x2+","+y2);
		double side1 = 0;
		if (x1 != x2) {
			side1 = Math.pow( (double) (x2 - x1), 2);
		}
		//System.out.println("Side1: "+side1);
		double side2 = 0;
		if (y1 != y2) {
			side2 = Math.pow( (double) (y2 - y1), 2);
		}
		//System.out.println("Side2: "+side2);
		if (side1 == 0 && side2 == 0) {
			return 0;
		} else {
			double result = Math.sqrt(side1 + side2);
			//System.out.println("Distance: "+result);
			return result;
		}
	}


	public static float distance(float x1, float y1, float x2, float y2) {
		//System.out.println(x1+","+y1+"  "+x2+","+y2);
		double side1 = 0;
		if (x1 != x2) {
			float x3 = (x2 - x1);
			side1 = x3 * x3;
		}
		//System.out.println("Side1: "+side1);
		double side2 = 0;
		if (y1 != y2) {
			float y3 = (y2 - y1);
			side2 = y3 * y3;
		}
		double result = Math.sqrt(side1 + side2);
		return (float)result;
	}


	public static double distance(double x1, double y1, double x2, double y2) {
		//System.out.println(x1+","+y1+"  "+x2+","+y2);
		double side1 = 0;
		if (x1 != x2) {
			side1 = Math.pow( (x2 - x1), 2);
		}
		//System.out.println("Side1: "+side1);
		double side2 = 0;
		if (y1 != y2) {
			side2 = Math.pow( (y2 - y1), 2);
		}
		double result = Math.sqrt(side1 + side2);
		return result;
	}


	public static double distance(float x1, float y1, float z1, float x2, float y2, float z2) {
		double side1 = Math.pow( (double) (x2 - x1), 2);
		double side2 = Math.pow( (double) (y2 - y1), 2);
		double side3 = Math.pow( (double) (z2 - z1), 2);

		double result = Math.sqrt(side1 + side2 + side3);
		return result;
	}


}
