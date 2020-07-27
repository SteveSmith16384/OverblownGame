package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasAIComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class AISystem extends AbstractSystem {

	private Game game;

	public AISystem(Game _game, BasicECS ecs) {
		super(ecs,  HasAIComponent.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		HasAIComponent ai = (HasAIComponent)entity.getComponent(HasAIComponent.class);
		if (ai.target_entity == null) {
			for(AbstractPlayersAvatar player : game.players) {
				if (player != entity) {
					ai.target_entity = player;
					break;
				}
			}
		} else {
			ai.ai_input.move_fwd = true;
			ai.ai_input.turn_left = true;
			ai.ai_input.turn_right = true;
			// todo
		}
	}

}
