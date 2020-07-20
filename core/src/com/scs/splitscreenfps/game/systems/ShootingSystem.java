package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.BulletEntityFactory;

import ssmith.lang.NumberFunctions;

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

		if (player.inputMethod.isShootPressed()) {
			WeaponSettingsComponent weapon = (WeaponSettingsComponent)entity.getComponent(WeaponSettingsComponent.class);
			//Settings.p("Shoot at " + System.currentTimeMillis());
			cc.ammo--;
			cc.nextShotTime = System.currentTimeMillis() + weapon.shot_interval;
			Vector3 dir = new Vector3();
			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
			if (cc.shootInCameraDirection) {
				dir.set(player.camera.direction);
				float spread = .05f;
				dir.x += NumberFunctions.rndFloat(-spread,spread);
				dir.z += NumberFunctions.rndFloat(-spread, spread);
			} else {
				dir.set((float)Math.sin(Math.toRadians(posData.angle_y_degrees+90)), 0, (float)Math.cos(Math.toRadians(posData.angle_y_degrees+90)));
			}
			dir.nor();

			Vector3 startPos = new Vector3();
			startPos.set(posData.position);
			startPos.mulAdd(dir, .2f);
			//startPos.y += .3f;

			switch (weapon.weapon_type) {
			//case WeaponSettingsComponent.WEAPON_RIFLE:
			case WeaponSettingsComponent.BOOMFIST_RIFLE:
			case WeaponSettingsComponent.BOWLINGBALL_GUN:
				AbstractEntity bullet = BulletEntityFactory.createBullet(game, player, startPos, dir);
				game.ecs.addEntity(bullet);
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

			default:
				throw new RuntimeException("Unknown weapon type: " + weapon.weapon_type);
			}

			if (weapon.kickback_force != 0) {
				PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
				pc.body.activate();				
				pc.body.applyCentralImpulse(player.camera.direction.cpy().scl(-1 * weapon.kickback_force));
			}

			// Recoil
			/*Vector3 tmp = new Vector3();
			tmp.set(player.camera.direction).crs(player.camera.up).nor();
			player.camera.rotate(tmp, 2);*/

			if (cc.ammo <= 0) {
				//playerData.ability1text = "Reloading...";
				//BillBoardFPS_Main.audio.play("sfx/gun_reload_lock_or_click_sound.wav");
				cc.ammo = weapon.max_ammo;
				cc.nextShotTime = System.currentTimeMillis() + weapon.reload_interval;
			}

			playerData.gunText = "Ammo: " + cc.ammo + "/" + weapon.max_ammo;
		}

	}

}
