package com.scs.splitscreenfps.game.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.components.CanShoot;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.WeaponSettingsComponent;
import com.scs.splitscreenfps.game.components.WillRespawnComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;

public class RespawnPlayerSystem implements ISystem {

	private List<AbstractEntity> entities = new ArrayList<AbstractEntity>();

	public RespawnPlayerSystem() {
	}


	public void addEntity(AbstractEntity e, Vector3 spawnPoint) {
		// todo - check they aren't already in the list
		e.addComponent(new WillRespawnComponent(spawnPoint));
		this.entities.add(e);
	}	


	@Override
	public void process() {
		for (int i=this.entities.size()-1 ; i>= 0 ; i--) {
			AbstractEntity e = this.entities.get(i);
			WillRespawnComponent wrc = (WillRespawnComponent)e.getComponent(WillRespawnComponent.class);
			if (wrc.respawn_time < System.currentTimeMillis()) {
				//Settings.p("Respawning " + e);
				
				PhysicsComponent md = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
				Matrix4 mat = new Matrix4();
				mat.setTranslation(wrc.respawnPoint);
				md.body.setWorldTransform(mat);
				md.body.activate();
				md.getRigidBody().setAngularVelocity(Vector3.Zero);
				md.getRigidBody().setLinearVelocity(Vector3.Zero);

				CanShoot cc = (CanShoot)e.getComponent(CanShoot.class);
				WeaponSettingsComponent weapon = (WeaponSettingsComponent)e.getComponent(WeaponSettingsComponent.class);
				cc.ammo = weapon.max_ammo;
				
				// Reset health
				PlayerData playerData = (PlayerData)e.getComponent(PlayerData.class);
				playerData.health = playerData.max_health;
				playerData.invincible_until = System.currentTimeMillis() + 4000;
				playerData.dead = false;
				
				AbstractPlayersAvatar player = (AbstractPlayersAvatar)e;
				Vector3 dir = new Vector3(15, 1, 15).sub(wrc.respawnPoint);
				dir.y = 0;
				dir.nor();
				player.camera.direction.set(dir);//1, 0, 1);//.lookAt(new Vector3(15, .5f, 15)); player.camera.rotate(Vector3.X, -90);
				player.camera.up.x = 0;
				player.camera.up.y = 1;
				player.camera.up.z = 0;
				player.camera.update();
				
				HasModelComponent model = (HasModelComponent)player.getComponent(HasModelComponent.class);
				model.dontDrawInViewId = playerData.playerIdx; // Since we changed it to draw the corpse
				model.invisible = false;
				
				e.removeComponent(WillRespawnComponent.class);
				this.entities.remove(i);
			}
		}
	}
	
	
}