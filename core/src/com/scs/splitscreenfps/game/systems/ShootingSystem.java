package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.BulletEntityFactory;

public class ShootingSystem extends AbstractSystem {

	private Game game;

	public ShootingSystem(BasicECS ecs, Game _game) {
		super(ecs, CanShoot.class);

		game = _game;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PlayerData playerData = (PlayerData)entity.getComponent(PlayerData.class);
		if (playerData.health <= 0) {
			return;
		}

		CanShoot cc = (CanShoot)entity.getComponent(CanShoot.class);
		if (cc.nextShotTime > System.currentTimeMillis()) {
			return;
		} 

		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		WeaponSettingsComponent weapon = (WeaponSettingsComponent)entity.getComponent(WeaponSettingsComponent.class);

		if (cc.reloading) {
			cc.reloading = false;
			cc.ammo = weapon.max_ammo;
			playerData.gunText = "Ammo: " + cc.ammo + "/" + weapon.max_ammo;
		}
		
		if (player.inputMethod.isShootPressed()) {
			cc.ammo--;
			cc.nextShotTime = System.currentTimeMillis() + weapon.shot_interval;
			Vector3 dir = new Vector3();
			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
			if (cc.shootInCameraDirection) {
				dir.set(player.camera.direction);
			} else {
				dir.set((float)Math.sin(Math.toRadians(posData.angle_y_degrees+90)), 0, (float)Math.cos(Math.toRadians(posData.angle_y_degrees+90)));
			}
			dir.nor();

			//Settings.p("Shoot dir = " + dir);

			Vector3 startPos = new Vector3();
			startPos.set(posData.position);
			startPos.mulAdd(dir, .2f);

			switch (weapon.weapon_type) {
			case WeaponSettingsComponent.RACER_PISTOLS:
			case WeaponSettingsComponent.BOWLINGBALL_GUN:
			case WeaponSettingsComponent.PIGGY_GUN:
				AbstractEntity bullet = BulletEntityFactory.createBullet(game, player, startPos, dir);
				game.ecs.addEntity(bullet);
				break;

			case WeaponSettingsComponent.BLOWPIPE:
			case WeaponSettingsComponent.HYPERSPHERES:
				AbstractEntity hs = BulletEntityFactory.createBouncingBullet(game, player, startPos, dir);
				game.ecs.addEntity(hs);
				break;

			case WeaponSettingsComponent.BOOMFIST_RIFLE:
				// Shoot 3-ways - need to do work to stop them colliding at the start!
				AbstractEntity b1 = BulletEntityFactory.createBullet(game, player, startPos, dir);
				game.ecs.addEntity(b1);
				
				dir.set((float)Math.sin(Math.toRadians(posData.angle_y_degrees+65)), dir.y, (float)Math.cos(Math.toRadians(posData.angle_y_degrees+65)));
				dir.nor();
				startPos.set(posData.position);
				startPos.mulAdd(dir, .3f); //.2f
				b1 = BulletEntityFactory.createBullet(game, player, startPos, dir);
				game.ecs.addEntity(b1);

				dir.set((float)Math.sin(Math.toRadians(posData.angle_y_degrees+115)), dir.y, (float)Math.cos(Math.toRadians(posData.angle_y_degrees+115)));
				dir.nor();
				startPos.set(posData.position);
				startPos.mulAdd(dir, .3f); //.2f
				b1 = BulletEntityFactory.createBullet(game, player, startPos, dir);
				game.ecs.addEntity(b1);
				
				break;
			case WeaponSettingsComponent.JUNKRAT_GRENADE_LAUNCHER:
				AbstractEntity g = BulletEntityFactory.createGrenade(game, player, startPos, dir);
				game.ecs.addEntity(g);
				break;

			case WeaponSettingsComponent.WEAPON_ROCKET_LAUNCHER:
				AbstractEntity r = BulletEntityFactory.createRocket(game, player, startPos, dir);
				game.ecs.addEntity(r);
				break;

			case WeaponSettingsComponent.WEAPON_PUNCH:
				AbstractEntity p = BulletEntityFactory.createFist(game, player, startPos, dir);
				game.ecs.addEntity(p);
				break;

			case WeaponSettingsComponent.BASTION_CANNON:
				AbstractEntity c = BulletEntityFactory.createCannonball(game, player, startPos, dir);
				game.ecs.addEntity(c);
				break;

			case WeaponSettingsComponent.NONE:
				break;
				
			default:
				throw new RuntimeException("Unknown weapon type: " + weapon.weapon_type);
			}

			if (weapon.kickback_force != 0) {
				PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
				pc.body.activate();				
				pc.getRigidBody().applyCentralImpulse(player.camera.direction.cpy().scl(-1 * weapon.kickback_force));
			}

			// Recoil
			/*Vector3 tmp = new Vector3();
			tmp.set(player.camera.direction).crs(player.camera.up).nor();
			player.camera.rotate(tmp, 2);*/

			if (cc.ammo <= 0) {
				//playerData.ability1text = "Reloading...";
				//BillBoardFPS_Main.audio.play("sfx/gun_reload_lock_or_click_sound.wav");
				//cc.ammo = weapon.max_ammo;
				cc.nextShotTime = System.currentTimeMillis() + weapon.reload_interval;
				cc.reloading = true;
			}
			if (cc.reloading) {
				playerData.gunText = "Reloading...";
			} else {
				playerData.gunText = "Ammo: " + cc.ammo + "/" + weapon.max_ammo;
			}
		} else if (player.inputMethod.isReloadPressed()) {
			if (cc.ammo < weapon.max_ammo) {
				//cc.ammo = weapon.max_ammo;
				cc.reloading = true;
				cc.nextShotTime = System.currentTimeMillis() + weapon.reload_interval;
				//playerData.gunText = "Ammo: " + cc.ammo + "/" + weapon.max_ammo;
				playerData.gunText = "Reloading...";
			}
		}

	}

}
