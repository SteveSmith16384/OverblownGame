package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
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

	private Vector3 tmpVec = new Vector3();

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
			ai.ai_input.move_fwd = false;
			ai.ai_input.turn_left = false;
			ai.ai_input.turn_right = false;

			PositionComponent aiPosData = (PositionComponent)ai_entity.getComponent(PositionComponent.class);
			PositionComponent targetPosData = (PositionComponent)ai.target_entity.getComponent(PositionComponent.class);

			float dist = aiPosData.position.dst(targetPosData.position);
			boolean can_see = this.canSee(ai.target_entity, aiPosData, targetPosData);

			if (dist > 2 || can_see == false) {
				ai.ai_input.move_fwd = true;
			}
			float x_diff = targetPosData.position.x - aiPosData.position.x;
			float z_diff = targetPosData.position.z - aiPosData.position.z;
			double angle = Math.atan2(z_diff, x_diff);
			float degs = (float)Math.toDegrees(angle);// - ourPosData.angle_y_degrees;
			//Settings.p("Degs: " + degs);
			float ai_angle = (360-aiPosData.angle_y_degrees);
			degs -= ai_angle;//aiPosData.angle_y_degrees;
			while (degs < 0) {
				degs += 360;
			}
			while (degs > 360) {
				degs -= 360;
			}

			//Settings.p("AI Angle: " + ai_angle);
			//Settings.p("Diff Angle: " + degs);

			if (degs > 185 && degs < 355) {
				ai.ai_input.turn_left = true;
			} else if (degs > 5 && degs < 175) {
				ai.ai_input.turn_right = true;
			}

			//Settings.p("Can see: " + can_see);
			if (can_see) {
				ai.ai_input.shoot = true;
			}

		}
	}


	private boolean canSee(AbstractEntity target, PositionComponent aiPosData, PositionComponent targetPosData) {
		tmpVec.set(targetPosData.position).sub(aiPosData.position);
		ClosestRayResultCallback results = game.rayTestByDir(aiPosData.position, tmpVec, 10);
		if (results != null) {
			btCollisionObject obj = results.getCollisionObject();
			boolean can_see = (obj.userData == target);
			return can_see;
		}
		return true;
	}

}
