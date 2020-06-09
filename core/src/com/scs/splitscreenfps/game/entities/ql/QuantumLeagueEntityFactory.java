package com.scs.splitscreenfps.game.entities.ql;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.components.AutoMoveComponent;
import com.scs.splitscreenfps.game.components.CollidesComponent;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.MovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.ql.IsBulletComponent;
import com.scs.splitscreenfps.game.components.ql.QLPlayerData;

import ssmith.libgdx.GraphicsHelper;

public class QuantumLeagueEntityFactory {

	public static AbstractEntity createBullet(BasicECS ecs, AbstractEntity shooter, Vector3 start, Vector3 offset) {
		AbstractEntity e = new AbstractEntity(ecs, "Bullet");

		PositionComponent pos = new PositionComponent(start);
		e.addComponent(pos);

		QLPlayerData playerData = (QLPlayerData)shooter.getComponent(QLPlayerData.class);

		HasDecal hasDecal = new HasDecal();
		if (playerData.side == 0) {
			if (playerData.health > 0) {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red.png", 0.1f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_red_desync.png", 0.1f);
			}
		} else if (playerData.side == 1) {
			if (playerData.health > 0) {
			hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue.png", 0.1f);
			} else {
				hasDecal.decal = GraphicsHelper.DecalHelper("laser_bolt_blue_desync.png", 0.1f);
			}
		} else {
			throw new RuntimeException("Invalid side: " + playerData.side);
		}
		hasDecal.decal.setPosition(pos.position);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = false;
		e.addComponent(hasDecal);

		//MovementData md = new MovementData();
		//md.must_move_x_and_z = true;
		//e.addComponent(md);

		e.addComponent(new AutoMoveComponent(offset));

		CollidesComponent cc = new CollidesComponent(false, .1f);
		cc.dont_collide_with = shooter;
		e.addComponent(cc);

		e.addComponent(new IsBulletComponent(shooter, playerData.side));

		//No! Fire and forget e.addComponent(new IsRecordable("Bullet", e));
		BillBoardFPS_Main.audio.play("sfx/Futuristic Shotgun Single Shot.wav");
		
		return e;
	}

}
