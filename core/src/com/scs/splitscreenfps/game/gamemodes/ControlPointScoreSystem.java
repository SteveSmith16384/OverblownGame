package com.scs.splitscreenfps.game.gamemodes;

import java.util.Iterator;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;

public class ControlPointScoreSystem implements ISystem {

	private Game game;
	private int last_side_on_point = -1;
	private AbstractEntity controlpoint;
	
	public ControlPointScoreSystem(Game _game) {
		//super(game.ecs);
		game = _game;
		
		// Find the control point entity
		Iterator<AbstractEntity> it = game.ecs.getEntityIterator();
		while (it.hasNext()) {
			AbstractEntity e = it.next();
			if (e.tags.contains("controlpoint")) {
				this.controlpoint = e;
				break;
			}
		}
		
		if (this.controlpoint == null) {
			throw new RuntimeException("No control point entity found");
		}
	}

	
	@Override
	public void process() {
		
	}
	


}
