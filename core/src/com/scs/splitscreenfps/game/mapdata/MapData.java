package com.scs.splitscreenfps.game.mapdata;

import java.util.ArrayList;
import java.util.List;

public class MapData {

	public String filename;
	public List<MapBlockComponent> blocks;
	
	public MapData() {
		blocks = new ArrayList<MapBlockComponent>();
		this.filename = "maps/default_map.json";
	}
}
