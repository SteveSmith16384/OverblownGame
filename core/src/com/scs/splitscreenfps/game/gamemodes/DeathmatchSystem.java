package com.scs.splitscreenfps.game.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.TextEntity;

public class DeathmatchSystem extends AbstractSystem {

	private Game game;

	public DeathmatchSystem(Game _game, BasicECS ecs) {
		super(ecs, AbstractPlayersAvatar.class);
		game = _game;

		
	}
	
	
	public void processEntity(AbstractEntity entity) {
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (playerData.damage_caused > 1000) {
			TextEntity text = new TextEntity(game.ecs, playerData.playerName + " HAS WON!", 37, 52, -1, Color.WHITE, 0, game.font_med, true);
			game.ecs.addEntity(text);
			game.playerHasWon(entity);
		}
	}


}
