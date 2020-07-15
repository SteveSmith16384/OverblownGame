package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;

public class PlayerData {

	public float health = Settings.START_HEALTH;
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

	public PlayerData(int _playerIdx) {
		playerIdx = _playerIdx;

		switch (playerIdx) {
		case 0:
			playerName = "GREEN";
			break;
		case 1:
			playerName = "YELLOW";
			break;
		case 2:
			playerName = "RED";
			break;
		case 3:
			playerName = "PURPLE";
			break;
		default:
			throw new RuntimeException("Unknown side: " + playerIdx);
		}

	}

}
