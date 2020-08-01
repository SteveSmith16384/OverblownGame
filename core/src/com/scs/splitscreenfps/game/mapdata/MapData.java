package com.scs.splitscreenfps.game.mapdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapData {

	public String name;
	public String filename;
	public HashMap<Integer, String> textures;
	public List<MapBlockComponent> blocks;
	
	public MapData() {
		blocks = new ArrayList<MapBlockComponent>();
		this.filename = "maps/map_editor.json";
	}
}
