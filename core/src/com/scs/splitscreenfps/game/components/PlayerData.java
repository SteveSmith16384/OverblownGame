package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;

public class PlayerData {

	public int health, max_health;// = Settings.START_HEALTH;
	public int playerIdx;
	public String playerName;
	public long restartTime;
	public int points;
	public AbstractEntity last_person_to_hit_them;
	public String ability1text = "";
	public String ability2text = "";
	public String ultimateText = "";
	public boolean performing_power_punch;
	public boolean has_been_punched;
	public int damage_caused, num_kills;

	public PlayerData(int _playerIdx, int _health) {
		playerIdx = _playerIdx;
		health = _health;
		this.max_health = health;
		this.playerName = getName(playerIdx);

	}

	
	public static String getName(int p) {
		switch (p) {
		case 0:
			return "GREEN";
		case 1:
			return "YELLOW";
		case 2:
			return "RED";
		case 3:
			return "PURPLE";
		default:
			throw new RuntimeException("Unknown side: " + p);
		}

	}
}
