package com.scs.splitscreenfps.game.systems;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AudioSystem {

	private Music music;
	private String music_filename;
	private HashMap<String, Sound> sounds;

	public AudioSystem() {
		sounds = new HashMap<String, Sound>();
	}


	public void startMusic(String filename) {
		if (filename.equals(music_filename)) {
			return;
		}

		if (music != null) {
			music.stop();
			music.dispose();
		}
		try {
			music = Gdx.audio.newMusic(Gdx.files.internal(filename));
			music.setLooping(true);
			music.play();
			music.setVolume(1f);
			music_filename = filename;
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
			if (name.isEmpty()) {
				return;
			}
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


	public void dispose() {
		for (Sound s : this.sounds.values()) {
			s.dispose();
		}
	}

}
