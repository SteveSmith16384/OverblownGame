package com.scs.splitscreenfps.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasAIComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
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
			ai.ai_input.move_fwd = false;
			ai.ai_input.turn_left = false;
			ai.ai_input.turn_right = false;

			PositionComponent ourPosData = (PositionComponent)entity.getComponent(PositionComponent.class);
			PositionComponent targetPosData = (PositionComponent)ai.target_entity.getComponent(PositionComponent.class);
			
			float x_diff = targetPosData.position.x-ourPosData.position.x;
			float z_diff = targetPosData.position.z-ourPosData.position.z;
			double angle = Math.atan2(z_diff, x_diff);
			float degs = (float)Math.toDegrees(angle)-ourPosData.angle_y_degrees;
			while (degs < 0) {
				degs += 360;
			}
			while (degs > 360) {
				degs -= 360;
			}

			Settings.p("Angle: " + degs);
			if (degs > 51) {
				//ai.ai_input.turn_right = true;
			} else if (degs < 51) {
				//ai.ai_input.turn_left = true;
			}
			// todo
		}
	}

}
