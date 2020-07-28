package com.scs.splitscreenfps.game.data;

public class GameSelectionData {

	public int[] selected_character_id;
	public boolean[] has_selected_character;
	public int level;
	
	public GameSelectionData() {//int num_players) {
		this.selected_character_id = new int[4]; // Always have 4 in case we need to add some AI
		this.has_selected_character = new boolean[4];
	}
	
}
