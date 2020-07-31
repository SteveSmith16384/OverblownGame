package com.scs.splitscreenfps.game.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.scs.basicecs.ISystem;

public class AudioSystem implements ISystem { // todo - rename to MusicSystem

	private Music music;
	private String music_filename;
	private HashMap<String, Sound> sounds;
	private List<ScheduledSoundData> scheduled_sounds = new ArrayList<ScheduledSoundData>();

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


	/*
	public void play(String name, long delay) {
		synchronized (scheduled_sounds) {
			this.scheduled_sounds.add(new ScheduledSoundData(name, System.currentTimeMillis() + delay));
		}
	}
	*/

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


	@Override
	public void process() {
		if (scheduled_sounds.size() > 0) {
			synchronized (scheduled_sounds) {
				for (int i=this.scheduled_sounds.size()-1 ; i>=0 ; i--) {
					//			for (ScheduledSoundData soundData : this.scheduled_sounds) {
					ScheduledSoundData soundData = this.scheduled_sounds.get(i);
					if (soundData.time_to_play < System.currentTimeMillis()) {
						this.scheduled_sounds.remove(i);
						this.play(soundData.filename);
					}
				}
			}
		}
	}

	class ScheduledSoundData {

		String filename;
		long time_to_play;

		public ScheduledSoundData(String _filename, long _time_to_play) {
			filename = _filename;
			time_to_play = _time_to_play;
		}
	}

}
