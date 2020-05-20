package com.scs.splitscreenfps.game.systems;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AudioSystem {

	private Music music;
	private HashMap<String, Sound> sounds; 
	//private float musicVolume;

	public AudioSystem() {
		sounds = new HashMap<String, Sound>();

		/*for(String s : preload) {
			String filename = "audio/" +s+".wav";
			Sound sfx = Gdx.audio.newSound(Gdx.files.internal(filename));
			sounds.put(s, sfx);
		}*/

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
		music = Gdx.audio.newMusic(Gdx.files.internal(filename));
		music.setLooping(true);
		try {
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
		if (sounds.containsKey(name)) {
			sounds.get(name).play();
		} else {
			String filename = name;// + ".wav";
			if (filename.indexOf(".") < 0) {
				filename = filename + ".wav";
			}
			Sound sfx = Gdx.audio.newSound(Gdx.files.internal(filename));
			sounds.put(name, sfx);
			//System.out.println("Sound " + name + " not preloaded");
			play(name); // Loop round to play the newly-added file.
		}

	}


	public void dipose() {
		for (Sound s : this.sounds.values()) {
			s.dispose();
		}
	}

}
