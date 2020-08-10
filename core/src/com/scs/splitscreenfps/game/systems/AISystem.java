package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.ViewportData;
import com.scs.splitscreenfps.game.components.HasAIComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
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
		if (game.game_stage != 0) {
			return;
		}
		
		HasAIComponent ai = (HasAIComponent)ai_entity.getComponent(HasAIComponent.class);
		if (ai.target_entity == null) {
			PositionComponent aiPosData = (PositionComponent)ai_entity.getComponent(PositionComponent.class);
			float closest_dist = 9999f;
			for(AbstractPlayersAvatar player : game.players) {
				if (player != ai_entity) {
					PlayerData targetPlayerData = (PlayerData)player.getComponent(PlayerData.class);
					if (targetPlayerData.dead == false) {
						PositionComponent targetPosData = (PositionComponent)player.getComponent(PositionComponent.class);
						float dist = aiPosData.position.dst(targetPosData.position);
						if (dist < closest_dist) {
							ai.target_entity = player;
							closest_dist = dist;
						}
					}
				}
			}
		} else {
			// check if target is dead.
			PlayerData targetPlayerData = (PlayerData)ai.target_entity.getComponent(PlayerData.class);
			if (targetPlayerData.dead) {
				ai.target_entity = null;
				return;
			}
			// todo - find closest enemy

			ai.ai_input.move_fwd = false;
			ai.ai_input.turn_left = false;
			ai.ai_input.turn_right = false;
			ai.ai_input.look_up = false;
			ai.ai_input.look_down = false;
			ai.ai_input.shoot = false;

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
			float degs = (float)Math.toDegrees(angle);
			//Settings.p("Degs: " + degs);
			float ai_angle = (360-aiPosData.angle_y_degrees);
			degs -= ai_angle;
			while (degs < 0) {
				degs += 360;
			}
			while (degs > 360) {
				degs -= 360;
			}

			if (degs > 185 && degs < 355) {
				ai.ai_input.turn_left = true;
			} else if (degs > 5 && degs < 175) {
				ai.ai_input.turn_right = true;
			}

			Camera camera = game.viewports[1].camera;
			//Settings.p("Can see: " + can_see);
			if (can_see) {
				// Look up/down
				PlayerData playerData = (PlayerData)ai_entity.getComponent(PlayerData.class);
				ViewportData viewport = game.viewports[playerData.playerIdx];
				tmpVec.set(targetPosData.position);
				camera.project(tmpVec, viewport.viewRect.x, viewport.viewRect.y, viewport.viewRect.width, viewport.viewRect.height);

				float height_req = viewport.viewRect.y + (viewport.viewRect.height/2);
				//Settings.p("Y: " + pos.y);
				if (tmpVec.y > height_req+10) {
					ai.ai_input.look_up = true;
				} else if (tmpVec.y < height_req-10) {
					ai.ai_input.look_down = true;
				}

				ai.ai_input.shoot = true;
			} else {
				//camera.direction.y = 0;
				//camera.direction.nor();
			}

			/*if (camera.up.y != 1) {
				Settings.p("UP wrong!");
			}*/

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
