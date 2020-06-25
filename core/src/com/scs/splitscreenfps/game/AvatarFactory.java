package com.scs.splitscreenfps.game;

import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent.Type;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.PlayersAvatar_Person;
import com.scs.splitscreenfps.game.input.IInputMethod;

public class AvatarFactory {

	public static final int CHAR_PHARTA = 0;
	public static final int CHAR_BOOMFIST = 1;
	public static final int CHAR_WINSTON = 2;
	public static final int CHAR_TRACY = 3;

	public static final int MAX_CHARS = 4;

	public static String getName(int id) {
		switch (id) {
		case CHAR_PHARTA: return "Pharta";
		case CHAR_BOOMFIST: return "Boomfist";
		case CHAR_WINSTON: return "Winston";
		case CHAR_TRACY: return "Tracey";
		default:
			throw new RuntimeException("Todo");
		}
	}


	public static AbstractPlayersAvatar createAvatar(Game _game, int playerIdx, ViewportData _viewportData, IInputMethod _inputMethod, int character) {
		AbstractPlayersAvatar avatar = new PlayersAvatar_Person(_game, playerIdx, _viewportData, _inputMethod);

		WeaponSettingsComponent weapon;
		int weapon_type = -1;
		switch (character) {
		case CHAR_PHARTA:
			weapon_type = WeaponSettingsComponent.WEAPON_ROCKET;
			break;
		case CHAR_BOOMFIST:
			weapon_type = WeaponSettingsComponent.WEAPON_PUNCH;
			avatar.addComponent(new SecondaryAbilityComponent(Type.Boost, 1000));
			break;
		case CHAR_WINSTON:
			weapon_type = WeaponSettingsComponent.WEAPON_GRENADE;
			avatar.addComponent(new SecondaryAbilityComponent(Type.Jump, 1000));
			break;
		case CHAR_TRACY:
			weapon_type = WeaponSettingsComponent.WEAPON_BULLET;
			break;
		default:
			throw new RuntimeException("Todo");
		}

		switch (weapon_type) {
		case WeaponSettingsComponent.WEAPON_BULLET:
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_BULLET, 300, 1200, 20, 20, 10, 0f, 0f);
			weapon.kickback_force = 1f;
			break;

		case WeaponSettingsComponent.WEAPON_GRENADE:
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_GRENADE, 600, 1500, 12, 20, 20, 3f, 6f);
			weapon.kickback_force = 1f;
			break;

		case WeaponSettingsComponent.WEAPON_ROCKET:
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_ROCKET, 900, 2000, 6, 20, 30, 2f, 15f);
			weapon.kickback_force = 5f;
			break;

		case WeaponSettingsComponent.WEAPON_PUNCH:
			weapon = new WeaponSettingsComponent(WeaponSettingsComponent.WEAPON_PUNCH, 500, 500, 1000, 1, 60, 0f, 0f);
			//weapon.kickback_force = 5f;
			break;

		default:
			throw new RuntimeException("Unknown weapon: " + weapon_type);
		}

		avatar.addComponent(weapon);

		return avatar;
	}
}
