package com.scs.splitscreenfps.game.systems;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.AudioComponent;

public class AudioSystem2 extends AbstractSystem {

	private HashMap<String, Sound> sounds;

	public AudioSystem2(BasicECS ecs) {
		super(ecs, AudioComponent.class);

		sounds = new HashMap<String, Sound>();
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		AudioComponent rc = (AudioComponent)entity.getComponent(AudioComponent.class);
		this.play(rc.filename, rc.volume);

		if (rc.remove_entity_on_play && entity.getComponents().size() == 1) {
			entity.remove();
		}
	}


	private void play(String name, float vol) {
		try {
			if (name.isEmpty()) {
				return;
			}
			if (sounds.containsKey(name)) {
				Sound s = sounds.get(name);
				long id = s.play();
				s.setVolume(id, vol);
			} else {
				String filename = name;// + ".wav";
				if (filename.indexOf(".") < 0) {
					filename = filename + ".wav";
				}
				Sound sfx = Gdx.audio.newSound(Gdx.files.internal(filename));
				sounds.put(name, sfx);
				play(name, vol); // Loop round to play the newly-added file.
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			sounds.remove(name);
		}
	}


	@Override
	public void dispose() {
		for (Sound s : this.sounds.values()) {
			s.dispose();
		}
	}

}
