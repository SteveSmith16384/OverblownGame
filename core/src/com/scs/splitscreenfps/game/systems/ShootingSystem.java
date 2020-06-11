package com.scs.splitscreenfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.levels.GangBeastsLevel1;

public class ShootingSystem extends AbstractSystem {

	private Game game;
	private GangBeastsLevel1 level;

	public ShootingSystem(BasicECS ecs, Game _game, GangBeastsLevel1 _level) {
		super(ecs, CanShoot.class);

		game = _game;
		level = _level;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		CanShoot cc = (CanShoot)entity.getComponent(CanShoot.class);
		long interval = 300;
		if (cc.ammo == 0) {
			interval = 1500;
			//cc.ammo = 6;
		}
		if (cc.lastShotTime + interval > System.currentTimeMillis()) {
			//Settings.p("Too soon");
			return;
		}

		AbstractPlayersAvatar player = (AbstractPlayersAvatar)entity;

		if (player.inputMethod.isShootPressed()) {
			if (cc.ammo == 0) {
				BillBoardFPS_Main.audio.play("sfx/gun_reload_lock_or_click_sound.wav");			
				//Settings.p("Reloading");
				cc.ammo = 6;
			}
			//Settings.p("Shot!");

			cc.lastShotTime = System.currentTimeMillis();
			cc.ammo--;

			PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);

			
			Vector3 tmpBulletOffset = new Vector3();
			tmpBulletOffset.set((float)Math.sin(Math.toRadians(posData.angle_degs+90)), 0, (float)Math.cos(Math.toRadians(posData.angle_degs+90)));
			tmpBulletOffset.nor();
			//tmpBulletOffset.scl(8);

			Vector3 startPos = new Vector3();
			startPos.set(posData.position);
			startPos.add(tmpBulletOffset);//.nor().scl(2));
			startPos.y += .3f;

			AbstractEntity bullet = EntityFactory.createBullet(ecs, player, startPos, tmpBulletOffset);
			game.ecs.addEntity(bullet);

			//level.qlRecordAndPlaySystem.addEvent(new BulletFiredRecordData(this.level.qlPhaseSystem.getPhaseNum012(), this.level.getCurrentPhaseTime(), player, startPos, tmpBulletOffset));
			//level.qlRecordAndPlaySystem.addEvent(new BulletFiredRecordData(this.level.qlPhaseSystem.getPhaseNum012(), this.level.getCurrentPhaseTime(), player.playerIdx, startPos, tmpBulletOffset));
		}
	}


}
