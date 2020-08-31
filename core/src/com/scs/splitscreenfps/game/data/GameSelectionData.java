package com.scs.splitscreenfps.game.data;

import com.scs.splitscreenfps.Settings;

public class GameSelectionData {

	public int[] selected_character_id;
	public boolean[] has_selected_character;
	public int level_type;
	public int level_num = 1;
	
	public GameSelectionData() {
		this.selected_character_id = new int[Settings.MAX_PLAYERS]; // Always have 4 in case we need to add some AI
		this.has_selected_character = new boolean[Settings.MAX_PLAYERS];
	}
	
}
