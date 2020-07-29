package com.scs.splitscreenfps.game.gamemodes;

import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.TextEntity;

public class DeathmatchSystem implements ISystem {

	private Game game;
	private boolean by_health;

	public DeathmatchSystem(Game _game, BasicECS ecs, boolean _by_health) {
		game = _game;
		by_health = _by_health;
		
		if (this.by_health) {
			game.show_damage = true;
		} else {
			game.show_kills = true;
		}

	}


	@Override
	public void process() {
		if (game.game_stage != 0) {
			return;
		}
		for(AbstractPlayersAvatar player : game.players) {
			checkPlayer(player);
		}
		
	}


	private void checkPlayer(AbstractPlayersAvatar entity) {
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (by_health) {
			if (playerData.damage_caused >= 1000) {
				TextEntity text = new TextEntity(game.ecs, playerData.playerName + " HAS WON!", 37, 52, -1, Settings.getColourForSide(playerData.playerIdx), 0, game.font_large, true);
				game.ecs.addEntity(text);
				game.playerHasWon(entity);
			}
		} else {
			if (playerData.num_kills >= 10) {
				TextEntity text = new TextEntity(game.ecs, playerData.playerName + " HAS WON!", 37, 52, -1, Settings.getColourForSide(playerData.playerIdx), 0, game.font_large, true);
				game.ecs.addEntity(text);
				game.playerHasWon(entity);
			}
		}
	}


}
