package com.scs.splitscreenfps.game.mapdata;

public class TextureData {

	public String filename;
	public int max_x, max_y;
	public int x_pos, y_pos;
	
	
	public TextureData(String _filename, int _max_x, int _max_y, int _x_pos, int _y_pos) {
		this.filename = _filename;
		max_x = _max_x;
		max_y = _max_y;
		x_pos = _x_pos;
		y_pos = _y_pos;
	}
	
}
