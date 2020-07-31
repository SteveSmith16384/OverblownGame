package com.scs.splitscreenfps.game.components;

public class AudioComponent {

	public String filename;
	public boolean remove_entity_on_play = true;
	public float volume = 1f;
	
	public AudioComponent(String _filename) {
		filename = _filename;
	}

	public AudioComponent(String _filename, float _vol) {
		filename = _filename;
		this.volume = _vol;
		
	}

}
