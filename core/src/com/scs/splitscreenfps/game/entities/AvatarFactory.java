package com.scs.splitscreenfps.game.entities;

import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.ViewportData;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent.SecondaryAbilityType;
import com.scs.splitscreenfps.game.components.UltimateAbilityComponent;
import com.scs.splitscreenfps.game.components.UltimateAbilityComponent.UltimateType;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.data.ExplosionData;
import com.scs.splitscreenfps.game.input.IInputMethod;

public class AvatarFactory {

	public static final int CHAR_PHARTAH = 0;
	public static final int CHAR_BOOMFIST = 1;
	public static final int CHAR_BOWLING_BALL = 2;
	public static final int CHAR_WINSTON = 3;
	public static final int CHAR_BASTION = 4;
	public static final int CHAR_JUNKRAT = 5;

	public static final int MAX_CHARS = 3;

	public static String getName(int id) {
		switch (id) {
		case CHAR_PHARTAH: return "Phartah";
		case CHAR_BOOMFIST: return "Boomfist";
		case CHAR_WINSTON: return "Winston";
		case CHAR_BASTION: return "Bastion";
		case CHAR_JUNKRAT: return "Junkrat";
		case CHAR_BOWLING_BALL: return "Bowling Ball";
		default:
			throw new RuntimeException("Unhandled character id: " + id);
		}
	}


	private static int getHealth(int id) {
		switch (id) {
		case CHAR_PHARTAH: return 200;
		case CHAR_BOOMFIST: return 300;
		case CHAR_WINSTON: return 300;
		case CHAR_BASTION: return 200;
		case CHAR_JUNKRAT: return 200;
		case CHAR_BOWLING_BALL: return 300;
		default:
			throw new RuntimeException("Unhandled character id: " + id);
		}
	}


	public static AbstractPlayersAvatar createAvatar(Game _game, int playerIdx, ViewportData _viewportData, IInputMethod _inputMethod, int character) {
		AbstractPlayersAvatar avatar = null;
		if (character == CHAR_BOWLING_BALL) {
			avatar = new PlayerAvatar_Ball(_game, playerIdx, _viewportData, _inputMethod, getHealth(character));
		} else {
			avatar = new PlayersAvatar_Person(_game, playerIdx, _viewportData, _inputMethod, getHealth(character));
		}
		
		WeaponSettingsComponent weapon;
		int weapon_type = -1;
		switch (character) {
		case CHAR_PHARTAH:
			weapon_type = WeaponSettingsComponent.WEAPON_ROCKET_LAUNCHER;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.JetPac, 10000));
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.RocketBarrage, 60));
			break;
			
		case CHAR_BOOMFIST:
			weapon_type = WeaponSettingsComponent.WEAPON_RIFLE;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.PowerPunch, 6000, .5f));
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.CraterStrike, 50));
			break;
			
		case CHAR_BOWLING_BALL:
			weapon_type = WeaponSettingsComponent.WEAPON_RIFLE;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.JumpUp, 5000));
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.Minefield, 60));
			break;
			
		case CHAR_WINSTON:
			weapon_type = WeaponSettingsComponent.WEAPON_GRENADE_LAUNCHER;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.JumpForwards, 5000));
			break;
			
		case CHAR_JUNKRAT:
			weapon_type = WeaponSettingsComponent.WEAPON_GRENADE_LAUNCHER;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.StickyMine, 4000));
			break;
			
		case CHAR_BASTION:
			weapon_type = WeaponSettingsComponent.WEAPON_CANNON;
			break;
			
		default:
			throw new RuntimeException("Unhandled character: " + character);
		}

		switch (weapon_type) {
		case WeaponSettingsComponent.WEAPON_RIFLE:
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_RIFLE, 300, 1200, 20, 20, 10, null);
			weapon.kickback_force = 1f;
			break;

		case WeaponSettingsComponent.WEAPON_GRENADE_LAUNCHER:
			ExplosionData explData = new ExplosionData(1f, 10f, 2f);
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_GRENADE_LAUNCHER, 600, 1500, 12, 20, 20, explData);
			weapon.kickback_force = 1f;
			break;

		case WeaponSettingsComponent.WEAPON_ROCKET_LAUNCHER: 
			ExplosionData explData2 = new ExplosionData(1f, 10f, 3f);
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_ROCKET_LAUNCHER, 800, 2000, 6, 20, 30, explData2);
			weapon.kickback_force = 5f;
			break;

		case WeaponSettingsComponent.WEAPON_PUNCH:
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_PUNCH, 500, 500, 1000, 0.3f, 60, null);
			break;

		case WeaponSettingsComponent.WEAPON_CANNON:
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_CANNON, 300, 500, 1500, 20, 60, null);
			break;

		default:
			throw new RuntimeException("Unknown weapon: " + weapon_type);
		}

		avatar.addComponent(weapon);

		return avatar;
	}
}
