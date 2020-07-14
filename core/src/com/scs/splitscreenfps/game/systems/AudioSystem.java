package com.scs.splitscreenfps.game.systems;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.scs.splitscreenfps.Settings;

public class AudioSystem {

	private Music music;
	private HashMap<String, Sound> sounds; 

	public AudioSystem() {
		sounds = new HashMap<String, Sound>();

		//music = Gdx.audio.newMusic(Gdx.files.internal("audio/orbital_colossus.mp3"));
		//music.setLooping(true);
	}


	public void update() {
	}


	public void startMusic(String filename) {
		if (music != null) {
			music.stop();
			music.dispose();
		}
		try {
			music = Gdx.audio.newMusic(Gdx.files.internal(filename));
			music.setLooping(true);
			music.play();
			music.setVolume(1f);
		} catch (GdxRuntimeException ex) {
			ex.printStackTrace();
		}
	}


	public void stopMusic() {
		if (music != null) {
			music.stop();
		}
	}


	public void play(String name) {
		try {
			if (sounds.containsKey(name)) {
				sounds.get(name).play();
			} else {
				String filename = name;// + ".wav";
				if (filename.indexOf(".") < 0) {
					filename = filename + ".wav";
				}
				Sound sfx = Gdx.audio.newSound(Gdx.files.internal(filename));
				sounds.put(name, sfx);
				play(name); // Loop round to play the newly-added file.
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			sounds.remove(name);
		}
	}


	public void dipose() {
		for (Sound s : this.sounds.values()) {
			s.dispose();
		}
	}

}
