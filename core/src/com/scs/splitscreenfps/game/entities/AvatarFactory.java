package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Camera;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasAIComponent;
import com.scs.splitscreenfps.game.components.JunkratMineAbilityComponent;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent.SecondaryAbilityType;
import com.scs.splitscreenfps.game.components.UltimateAbilityComponent;
import com.scs.splitscreenfps.game.components.UltimateAbilityComponent.UltimateType;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.data.ExplosionData;
import com.scs.splitscreenfps.game.input.AIInputMethod;
import com.scs.splitscreenfps.game.input.IInputMethod;

public class AvatarFactory {

	public static final int CHAR_PHARTAH = 1;
	public static final int CHAR_BOOMFIST = 2;
	public static final int CHAR_BOWLING_BALL = 3;
	public static final int CHAR_RACER = 4;
	public static final int CHAR_ASSASSIN = 5;
	public static final int CHAR_PIGGY = 6;
	public static final int CHAR_VICTIM = 7;
	public static final int CHAR_WHAT_THE_BALL = 8;
	public static final int CHAR_RUBBISHRODENT = 9;
	public static final int CHAR_TOBLERONE = 10;
	public static final int CHAR_BOUNCING_BALL = 11;
	public static final int CHAR_WEAK = 12;

	public static final int CHAR_WINSTON = 96;
	public static final int CHAR_BASTION = 97;


	public static String getName(int id) {
		switch (id) {
		case CHAR_PHARTAH: return "Phartah";
		case CHAR_BOOMFIST: return "Boomfist";
		case CHAR_BOWLING_BALL: return "Bowling Ball";
		case CHAR_RACER: return "Racer";
		case CHAR_WINSTON: return "Winston";
		case CHAR_BASTION: return "Bastion";
		case CHAR_RUBBISHRODENT: return "Rubbish Rodent";
		case CHAR_TOBLERONE: return "Toblerone";
		case CHAR_WEAK: return "WEAK";

		case CHAR_PIGGY: return "The Hunter";
		case CHAR_VICTIM: return "INNOCENT VICTIM";
		case CHAR_ASSASSIN: return "ASSASSIN";
		case CHAR_BOUNCING_BALL: return "Bouncing Ball";

		default:
			throw new RuntimeException("Unhandled character id: " + id);
		}
	}


	private static int getHealth(int id) {
		switch (id) {
		case CHAR_PHARTAH: return 100;
		case CHAR_BOOMFIST: return 200;
		case CHAR_BOWLING_BALL: return 300;
		case CHAR_RACER: return 150;
		case CHAR_WINSTON: return 300;
		case CHAR_BASTION: return 150;
		case CHAR_RUBBISHRODENT: return 150;
		case CHAR_TOBLERONE: return 150;

		case CHAR_PIGGY: return 9999;
		case CHAR_VICTIM: return 1;
		case CHAR_ASSASSIN: return 1;
		case CHAR_WHAT_THE_BALL: return 100;
		case CHAR_BOUNCING_BALL: return 100;
		case CHAR_WEAK: return 100;

		default:
			throw new RuntimeException("Unhandled character id: " + id);
		}
	}


	private static float getSpeed(int id) {
		switch (id) {
		case CHAR_VICTIM:
			return Settings.DEF_MOVE_SPEED * 1.1f;
		default:
			return Settings.DEF_MOVE_SPEED;
		}
	}


	public static AbstractPlayersAvatar createAvatar(Game _game, int playerIdx, Camera camera, IInputMethod _inputMethod, int hero_id) {
		AbstractPlayersAvatar avatar = null;
		if (hero_id == CHAR_BOWLING_BALL || hero_id == CHAR_WHAT_THE_BALL) {
			avatar = new PlayerAvatar_Ball(_game, playerIdx, camera, hero_id, _inputMethod, getHealth(hero_id), getSpeed(hero_id));
		} else if (hero_id == CHAR_BOUNCING_BALL) {
			avatar = new PlayerAvatar_BouncingBall(_game, playerIdx, camera, hero_id, _inputMethod, getHealth(hero_id), getSpeed(hero_id));
		} else {
			avatar = new PlayerAvatar_Person(_game, playerIdx, camera, hero_id, _inputMethod, getHealth(hero_id), getSpeed(hero_id));
		}

		avatar.setAvatarColour();

		if (_inputMethod instanceof AIInputMethod) {
			avatar.addComponent(new HasAIComponent((AIInputMethod)_inputMethod));

		}

		WeaponSettingsComponent weapon = null;
		int weapon_type = -1;
		switch (hero_id) {
		case CHAR_PHARTAH:
			weapon_type = WeaponSettingsComponent.WEAPON_ROCKET_LAUNCHER;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.JetPac, 10));
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.RocketBarrage, 60));
			break;

		case CHAR_BOOMFIST:
			weapon_type = WeaponSettingsComponent.BOOMFIST_RIFLE;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.PowerPunch, 4, .5f));
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.CraterStrike, 50));
			break;

		case CHAR_BOWLING_BALL:
			weapon_type = WeaponSettingsComponent.BOWLINGBALL_GUN;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.JumpUp, 5));
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.Minefield, 60));
			break;

		case CHAR_RACER:
			weapon_type = WeaponSettingsComponent.RACER_PISTOLS;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.Blink, 6, 3));
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.TraceyBomb, 50));
			break;

		case CHAR_WINSTON:
			weapon_type = WeaponSettingsComponent.JUNKRAT_GRENADE_LAUNCHER;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.JumpForwards, 5));
			break;

		case CHAR_RUBBISHRODENT:
			weapon_type = WeaponSettingsComponent.JUNKRAT_GRENADE_LAUNCHER;
			avatar.addComponent(new JunkratMineAbilityComponent(4));
			break;

		case CHAR_BASTION:
			weapon_type = WeaponSettingsComponent.BASTION_CANNON;
			break;

		case CHAR_TOBLERONE:
			weapon_type = WeaponSettingsComponent.TOBLERONE_GUN;
			avatar.addComponent(new UltimateAbilityComponent(UltimateType.SprayLava, 50));
			break;

		case CHAR_ASSASSIN:
			weapon_type = WeaponSettingsComponent.BLOWPIPE;
			avatar.addComponent(new SecondaryAbilityComponent(SecondaryAbilityType.Invisible_Mine, 20));
			break;

		case CHAR_PIGGY:
			weapon_type = WeaponSettingsComponent.PIGGY_GUN;
			break;

		case CHAR_VICTIM:
			weapon_type = WeaponSettingsComponent.NONE;
			break;

		case CHAR_WHAT_THE_BALL:
			weapon_type = WeaponSettingsComponent.BOWLINGBALL_GUN;
			break;

		case CHAR_BOUNCING_BALL:
			weapon_type = WeaponSettingsComponent.NONE;
			break;
			
		case CHAR_WEAK:
			weapon_type = WeaponSettingsComponent.WEAK_PISTOL;
			break;
			
		default:
			throw new RuntimeException("Unhandled character: " + hero_id);
		}
		

		switch (weapon_type) {
		case WeaponSettingsComponent.NONE:
			weapon = new WeaponSettingsComponent(weapon_type, 100000, 100000, 0, 0, 
					0, 0, 0, null);
			break;
		case WeaponSettingsComponent.WEAPON_ROCKET_LAUNCHER: 
			ExplosionData explData2 = new ExplosionData(1f, 30, 3f);
			weapon = new WeaponSettingsComponent(weapon_type, 750, 1500, 6, 50, 
					60, 0, 0, explData2);
			weapon.kickback_force = 5f;
			break;

		case WeaponSettingsComponent.BOOMFIST_RIFLE:
			weapon = new WeaponSettingsComponent(weapon_type, 300, 1200, 20, 30, 
					40, 15, 1, null);
			//weapon.kickback_force = 1f;
			break;

		case WeaponSettingsComponent.BOWLINGBALL_GUN:
			weapon = new WeaponSettingsComponent(weapon_type, 100, 2100, 80, 25, 
					12, 15, .2f, null);
			weapon.kickback_force = .5f;
			break;

		case WeaponSettingsComponent.RACER_PISTOLS:
			weapon = new WeaponSettingsComponent(weapon_type, 100, 1150, 20, 20, 
					12, 13, .5f, null);
			break;

		case WeaponSettingsComponent.JUNKRAT_GRENADE_LAUNCHER:
			ExplosionData explData = new ExplosionData(2f, 40, 2f);
			weapon = new WeaponSettingsComponent(weapon_type, 667, 1500, 5, 100, 
					50, 0, 0, explData);
			weapon.kickback_force = 1f;
			break;

		case WeaponSettingsComponent.WEAPON_PUNCH:
			weapon = new WeaponSettingsComponent(weapon_type, 500, 500, 1000, 0.3f, 
					60, 0, 0, null);
			break;

		case WeaponSettingsComponent.BASTION_CANNON:
			weapon = new WeaponSettingsComponent(weapon_type, 300, 500, 1500, 20, 
					60, 0, 0, null);
			break;

		case WeaponSettingsComponent.BLOWPIPE:
			weapon = new WeaponSettingsComponent(weapon_type, 4000, 0, 999, 999, 
					999, 0, 0, null);
			break;

		case WeaponSettingsComponent.PIGGY_GUN:
			weapon = new WeaponSettingsComponent(weapon_type, 500, 0, 999, 999, 
					1, 0, 0, null); // Damage must be 1 for shoot-tag
			break;

		case WeaponSettingsComponent.TOBLERONE_GUN:
			weapon = new WeaponSettingsComponent(weapon_type, 700, 1500, 20, 100, 
					50, 0, 0, null);
			break;

		case WeaponSettingsComponent.WEAK_PISTOL:
			weapon = new WeaponSettingsComponent(weapon_type, 700, 1500, 20, 100, 
					50, 0, 0, null);
			break;

		default:
			throw new RuntimeException("Unknown weapon: " + weapon_type);
		}

		if (weapon != null) {
			avatar.addComponent(weapon);
		}

		return avatar;
	}
}
