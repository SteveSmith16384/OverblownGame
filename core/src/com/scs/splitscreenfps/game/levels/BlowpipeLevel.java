package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.components.UltimateAbilityComponent;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

public class BlowpipeLevel extends AbstractLevel {

	private ISystem deathmatchSystem;
	private boolean players_edited = false;

	public BlowpipeLevel(Game _game) {
		super(_game);

		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, false);

	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/factory.json", false);
			//super.loadJsonFile("maps/map_editor.json", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void update() {
		if (players_edited == false) {
			players_edited = true;
			for(AbstractPlayersAvatar player : game.players) {
				PlayerData pdata = (PlayerData)player.getComponent(PlayerData.class);
				pdata.health = 1;
				pdata.max_health = 1;

				player.removeComponent(UltimateAbilityComponent.class);
				player.removeComponent(SecondaryAbilityComponent.class);

				WeaponSettingsComponent weapon = (WeaponSettingsComponent)player.getComponent(WeaponSettingsComponent.class);
				weapon.reload_interval = 5000;
				weapon.max_ammo = 1;
			}
		}

		deathmatchSystem.process();
	}


}
