package ssmith.libgdx;

import java.io.Serializable;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Same as GridPoint2, but prevents values being changed.
 * @author StephenCS
 *
 */
public class GridPoint2Static implements Serializable {
	private static final long serialVersionUID = -4019969926331717380L;

	public final int x;
	public final int y;

	/** Constructs a new 2D grid point.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate */
	public GridPoint2Static(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/** Copy constructor
	 * 
	 * @param point The 2D grid point to make a copy of. */
	public GridPoint2Static(GridPoint2Static point) {
		this.x = point.x;
		this.y = point.y;
	}


	/**
	 * @param other The other point
	 * @return the squared distance between this point and the other point.
	 */
	public float dst2 (GridPoint2 other) {
		int xd = other.x - x;
		int yd = other.y - y;

		return xd * xd + yd * yd;
	}

	/**
	 * @param x The x-coordinate of the other point
	 * @param y The y-coordinate of the other point
	 * @return the squared distance between this point and the other point.
	 */
	public float dst2 (int x, int y) {
		int xd = x - this.x;
		int yd = y - this.y;

		return xd * xd + yd * yd;
	}

	/**
	 * @param other The other point
	 * @return the distance between this point and the other vector.
	 */
	public float dst (GridPoint2 other) {
		int xd = other.x - x;
		int yd = other.y - y;

		return (float)Math.sqrt(xd * xd + yd * yd);
	}

	/**
	 * @param x The x-coordinate of the other point
	 * @param y The y-coordinate of the other point
	 * @return the distance between this point and the other point.
	 */
	public float dst (int x, int y) {
		int xd = x - this.x;
		int yd = y - this.y;

		return (float)Math.sqrt(xd * xd + yd * yd);
	}

	/**
	 * @return a copy of this grid point
	 */
	public GridPoint2Static cpy () {
		return new GridPoint2Static(this);
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || o.getClass() != this.getClass()) return false;
		GridPoint2 g = (GridPoint2)o;
		return this.x == g.x && this.y == g.y;
	}

	@Override
	public int hashCode () {
		final int prime = 53;
		int result = 1;
		result = prime * result + this.x;
		result = prime * result + this.y;
		return result;
	}

	@Override
	public String toString () {
		return "(" + x + ", " + y + ")";
	}
}
