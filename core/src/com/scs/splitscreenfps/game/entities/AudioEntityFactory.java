package com.scs.splitscreenfps.game.entities;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.AddComponentAfterTimeComponent;
import com.scs.splitscreenfps.game.components.AudioComponent;

public class AudioEntityFactory {

	public static AbstractEntity createSfxEntity(BasicECS ecs, String filename, float vol) {
		AbstractEntity e = new AbstractEntity(ecs, "Audio_" + filename);
		
		AudioComponent audio = new AudioComponent(filename, vol);
		e.addComponent(audio);
		return e;
	}
	

	public static AbstractEntity createSfxEntityWithDelay(BasicECS ecs, String filename, float vol, long delay) {
		AbstractEntity e = new AbstractEntity(ecs, "Audio_" + filename);
		
		AudioComponent audio = new AudioComponent(filename, vol);
		//e.addComponent(audio); No!

		e.addComponent(new AddComponentAfterTimeComponent(audio, delay));
		return e;
	}
	
}
