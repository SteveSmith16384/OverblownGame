package com.scs.splitscreenfps.game.gamemodes;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.DrawTextComponent;
import com.scs.splitscreenfps.game.components.HarmPlayerOnContactComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.components.UltimateAbilityComponent;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.TextEntity;
import com.scs.splitscreenfps.game.events.EventCollision;

public class ShootTagSystem implements ISystem {

	private Game game;
	private BasicECS ecs;
	private AbstractPlayersAvatar it_player;
	private AbstractEntity time_text;
	private float[] time_as_it;

	public ShootTagSystem(Game _game, BasicECS _ecs, long duration) {
		game = _game;
		ecs = _ecs;
		time_as_it = new float[4];
		for (int i=0 ; i<game.players.length ; i++) {
			this.time_as_it[i] = duration;
		}
	}


	@Override
	public void process() {
		if (game.game_stage != 0) {
			return;
		}

		if (it_player == null) {
			it_player = game.players[0];
			updatePlayerWeapons();
		}

		if (time_text == null) {
			time_text = new TextEntity(game.ecs, "", 37, 52, -1, Color.WHITE, 0, game.font_large, true);
			game.ecs.addEntity(time_text);
		}

		this.time_as_it[this.it_player.playerIdx] -= Gdx.graphics.getDeltaTime();
		if (this.time_as_it[this.it_player.playerIdx] <= 0) {
			PlayerData pdata = (PlayerData)this.it_player.getComponent(PlayerData.class);
			TextEntity text = new TextEntity(game.ecs, pdata.playerName + " HAS LOST!", 37, 52, -1, Settings.getColourForSide(this.it_player.playerIdx), 0, game.font_large, true);
			game.ecs.addEntity(text);
			game.playerHasLost(it_player);

		}
		
		DrawTextComponent dtd = (DrawTextComponent)time_text.getComponent(DrawTextComponent.class);
		//todo dtd.text = "Time Left: " + ((end_time-System.currentTimeMillis())/1000);

		checkForBulletHits();
	}


	private void checkForBulletHits() {
		for(AbstractPlayersAvatar player : game.players) {
			if (player == this.it_player) {
				continue;
			}
			List<AbstractEvent> colls = ecs.getEventsForEntity(EventCollision.class, player);
			for (AbstractEvent evt : colls) {
				EventCollision coll = (EventCollision)evt;
				HarmPlayerOnContactComponent harm = (HarmPlayerOnContactComponent)coll.entity2.getComponent(HarmPlayerOnContactComponent.class);
				if (harm == null) {
					continue;
				}
				if (coll.entity1 == harm.shooter) {
					continue;
				}

				this.it_player = (AbstractPlayersAvatar)coll.entity1;
				this.updatePlayerWeapons();
				break;
			}
		}
	}


	private void updatePlayerWeapons() {
		for(AbstractPlayersAvatar player : game.players) {
			WeaponSettingsComponent weapon = (WeaponSettingsComponent)player.getComponent(WeaponSettingsComponent.class);
			if (player == this.it_player) {
				weapon.weapon_type = WeaponSettingsComponent.HYPERSPHERES;
				PlayerData pdata = (PlayerData)this.it_player.getComponent(PlayerData.class);
				game.appendToLog(pdata.playerName + " is now IT!");
			} else {
				weapon.weapon_type = WeaponSettingsComponent.NONE;
			}
		}

	}
	
}
