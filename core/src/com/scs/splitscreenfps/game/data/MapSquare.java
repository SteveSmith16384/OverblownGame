package com.scs.splitscreenfps.game.data;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;

public class MapSquare {

	//public boolean blocked = false;
	public boolean spawn_point = false;
	public AbstractEntity entity;
	
	public MapSquare() {

	}

/*
	public MapSquare(boolean _blocked) {
		blocked = _blocked;
	}
*/

	/**
	 * Call this constructor if you're planning to add components to the mapsquares.
	 * @param ecs
	 */
	public MapSquare(BasicECS ecs) {
		this.entity = new AbstractEntity(ecs, "MapSquare");
	}
	
}
