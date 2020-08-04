package com.scs.splitscreenfps.game.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.TextEntity;

public class DeathmatchSystem implements ISystem {

	private static final int DAMAGE = 1000;
	private static final int KILLS = 10;
	
	private Game game;
	private boolean by_health;
	private boolean setup = false;
	private TextEntity time_text;
	
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
		if (setup == false) {
			setup = true;

			time_text = new TextEntity(game.ecs, "", 37, 52, -1, Color.GREEN, 0, game.font_med, true);
			game.ecs.addEntity(time_text);

			DrawTextComponent dtd = (DrawTextComponent)time_text.getComponent(DrawTextComponent.class);
			dtd.dirty = true;
			if (by_health) {
				dtd.text = "First player to cause " + DAMAGE + " damage";
			} else {
				dtd.text = "First player to get " + KILLS + " kills";
			}

		}
		for(AbstractPlayersAvatar player : game.players) {
			checkPlayer(player);
		}
		
	}


	private void checkPlayer(AbstractPlayersAvatar entity) {
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (by_health) {
			if (playerData.damage_caused >= DAMAGE) {
				this.time_text.remove();
				TextEntity text = new TextEntity(game.ecs, playerData.playerName + " HAS WON!", 37, 52, -1, Settings.getColourForSide(playerData.playerIdx), 0, game.font_large, true);
				game.ecs.addEntity(text);
				game.playerHasWon(entity);
			}
		} else {
			if (playerData.num_kills >= KILLS) {
				this.time_text.remove();
				TextEntity text = new TextEntity(game.ecs, playerData.playerName + " HAS WON!", 37, 52, -1, Settings.getColourForSide(playerData.playerIdx), 0, game.font_large, true);
				game.ecs.addEntity(text);
				game.playerHasWon(entity);
			}
		}
	}


}
