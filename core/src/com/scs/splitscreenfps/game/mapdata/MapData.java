package com.scs.splitscreenfps.game.mapdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class MapData {

	public String filename;
	public HashMap<Integer, String> textures;
	public List<MapBlockComponent> blocks;
	public HashMap<Integer, Vector3> start_positions;
	
	public MapData() {
		blocks = new ArrayList<MapBlockComponent>();
		this.filename = "maps/default_map.json";
	}
}
