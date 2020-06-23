package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.SecondaryAbilityComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class SecondaryAbilitySystem extends AbstractSystem {

	private Game game;

	public SecondaryAbilitySystem(BasicECS ecs, Game _game) {
		super(ecs, SecondaryAbilityComponent.class);
		
		game = _game;
	}

	
	@Override
	public void processEntity(AbstractEntity entity) {
		SecondaryAbilityComponent ability = (SecondaryAbilityComponent)entity.getComponent(SecondaryAbilityComponent.class);

		long interval = ability.interval;
		if (ability.lastShotTime + interval > System.currentTimeMillis()) {
			return;
		}

		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (player.inputMethod.isAbilityPressed()) {
			//Settings.p("Shoot at " + System.currentTimeMillis());
			ability.lastShotTime = System.currentTimeMillis();

			switch (ability.type) {
			case Boost:
				performBoost(entity, player);
				break;
			case Jump:
				performPowerJump(entity, player);
				break;
			default:
				throw new RuntimeException("Unknown ability: " + ability.type);
			}
			
			/*
			Vector3 dir = new Vector3();
			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
			if (cc.shootInCameraDirection) {
				dir.set(player.camera.direction);
			} else {
				dir.set((float)Math.sin(Math.toRadians(posData.angle_Y_degs+90)), 0, (float)Math.cos(Math.toRadians(posData.angle_Y_degs+90)));
			}
			dir.nor();

			Vector3 startPos = new Vector3();
			startPos.set(posData.position);
			startPos.mulAdd(dir, .5f);
			//startPos.y += .3f;

			switch (weapon.weapon_type) {
			case WeaponSettingsComponent.WEAPON_BULLET:
				AbstractEntity bullet = EntityFactory.createBullet(ecs, player, startPos, dir);
				game.ecs.addEntity(bullet);
				break;

			case WeaponSettingsComponent.WEAPON_GRENADE:
				AbstractEntity g = EntityFactory.createGrenade(ecs, player, startPos, dir);
				game.ecs.addEntity(g);
				break;

			case WeaponSettingsComponent.WEAPON_ROCKET:
				AbstractEntity r = EntityFactory.createRocket(ecs, player, startPos, dir);
				game.ecs.addEntity(r);
				break;

			default:
				throw new RuntimeException("Unknown weapon type: " + weapon.weapon_type);
			}
			
			if (weapon.kickback_force != 0) {
				PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
				pc.body.activate();				
				pc.body.applyCentralImpulse(player.camera.direction.cpy().scl(-1 * weapon.kickback_force));

			}
			*/
		}
	}
	
	
	private void performBoost(AbstractEntity entity, AbstractPlayersAvatar player) {
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		pc.body.activate();				
		pc.body.applyCentralImpulse(player.camera.direction.cpy().scl(30));
		
	}


	private void performPowerJump(AbstractEntity entity, AbstractPlayersAvatar player) {
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		//pc.body.activate();
		//pc.body.applyCentralForce(new Vector3(0, 500, 0));
		pc.body.applyCentralImpulse(new Vector3(0, 30, 0));
		Settings.p("Power jump!");
		
	}
}
