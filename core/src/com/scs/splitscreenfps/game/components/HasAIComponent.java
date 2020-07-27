package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.input.AIInputMethod;

public class HasAIComponent {

	public AIInputMethod ai_input;
	public AbstractEntity target_entity;
	
	public HasAIComponent(AIInputMethod _ai_input) {
		ai_input = _ai_input;
	}
	
}
