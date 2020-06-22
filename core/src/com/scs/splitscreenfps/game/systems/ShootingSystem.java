package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class ShootingSystem extends AbstractSystem {

	private Game game;

	public ShootingSystem(BasicECS ecs, Game _game) {
		super(ecs, CanShoot.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		CanShoot cc = (CanShoot)entity.getComponent(CanShoot.class);
		WeaponSettingsComponent weapon = (WeaponSettingsComponent)entity.getComponent(WeaponSettingsComponent.class);

		long interval = weapon.shot_interval;//300;
		if (cc.ammo == 0) {
			interval = weapon.reload_interval;//1500;
		}
		
		if (cc.lastShotTime + interval > System.currentTimeMillis()) {
			return;
		}

		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (player.inputMethod.isShootPressed()) {
			//Settings.p("Shoot at " + System.currentTimeMillis());
			if (cc.ammo == 0) {
				BillBoardFPS_Main.audio.play("sfx/gun_reload_lock_or_click_sound.wav");
				cc.ammo = weapon.max_ammo;
			}

			cc.lastShotTime = System.currentTimeMillis();
			cc.ammo--;

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
		}
	}

}
