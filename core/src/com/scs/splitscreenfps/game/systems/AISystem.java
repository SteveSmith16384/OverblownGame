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
	public void processEntity(AbstractEntity ai_entity) {
		HasAIComponent ai = (HasAIComponent)ai_entity.getComponent(HasAIComponent.class);
		if (ai.target_entity == null) {
			for(AbstractPlayersAvatar player : game.players) {
				if (player != ai_entity) {
					ai.target_entity = player;
					break;
				}
			}
		} else {
			ai.ai_input.move_fwd = true;
			ai.ai_input.turn_left = false;
			ai.ai_input.turn_right = false;

			PositionComponent aiPosData = (PositionComponent)ai_entity.getComponent(PositionComponent.class);
			PositionComponent targetPosData = (PositionComponent)ai.target_entity.getComponent(PositionComponent.class);

			float x_diff = targetPosData.position.x - aiPosData.position.x;
			float z_diff = targetPosData.position.z - aiPosData.position.z;
			double angle = Math.atan2(z_diff, x_diff);
			float degs = (float)Math.toDegrees(angle);// - ourPosData.angle_y_degrees;
			//Settings.p("Degs: " + degs);
			//degs = 360-degs;
			float ai_angle = (360-aiPosData.angle_y_degrees);
			degs -= ai_angle;//aiPosData.angle_y_degrees;
			while (degs < 0) {
				degs += 360;
			}
			while (degs > 360) {
				degs -= 360;
			}

			//Settings.p("AI Angle: " + ai_angle);
			Settings.p("Diff Angle: " + degs);
			//ai.ai_input.turn_right = true; // reduces angle
			//ai.ai_input.turn_left = true;
			
			if (degs > 185 && degs < 355) {
				ai.ai_input.turn_left = true;
			} else if (degs > 5 && degs < 175) {
				ai.ai_input.turn_right = true;
			}

			// todo
		}
	}

}
