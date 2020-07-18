package com.scs.splitscreenfps.game.gamemodes;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class DeathmatchSystem extends AbstractSystem {

	private Game game;

	public DeathmatchSystem(Game _game, BasicECS ecs) {
		super(ecs, AbstractPlayersAvatar.class);
		game = _game;

		
	}
	
	
	public void processEntity(AbstractEntity entity) {
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (playerData.damage_caused > 1000) {
			game.playerHasWon(entity);
		}
	}


}
