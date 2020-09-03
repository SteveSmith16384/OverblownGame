package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

public class CSGODust2Level extends AbstractLevel {

	private ISystem deathmatchSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);		
		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void load() {
		// todo - improve these
		this.startPositions.add(new Vector3(9, 2f, 0));
		this.startPositions.add(new Vector3(9, 2f, 0));
		this.startPositions.add(new Vector3(9, 2f, 0));
		this.startPositions.add(new Vector3(9, 2f, 0));

		EntityFactory.createStaticModelsForLargeModel(game.ecs, "Dust2", "maps/Dust2/Dust2.g3db", 0, 0, 0, -90, 0);
	}


	@Override
	public void update() {
		deathmatchSystem.process();
	}


	@Override
	public String getName() {
		return "CS:GO Dust2 Deathmatch";
	}


}
