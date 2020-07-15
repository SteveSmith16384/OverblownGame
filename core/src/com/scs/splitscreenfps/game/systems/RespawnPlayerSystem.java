package com.scs.splitscreenfps.game.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.WillRespawnComponent;

public class RespawnPlayerSystem implements ISystem {

	private List<AbstractEntity> entities = new ArrayList<AbstractEntity>();
	private BasicECS ecs;

	public RespawnPlayerSystem(BasicECS _ecs) {
		ecs = _ecs;
	}


	public void addEntity(AbstractEntity e, Vector3 spawnPoint) {
		e.addComponent(new WillRespawnComponent(spawnPoint));
		this.entities.add(e);
		//e.remove();
	}	


	@Override
	public void process() {
		for (int i=this.entities.size()-1 ; i>= 0 ; i--) {
			AbstractEntity e = this.entities.get(i);
			WillRespawnComponent wrc = (WillRespawnComponent)e.getComponent(WillRespawnComponent.class);
			if (wrc.respawn_time < System.currentTimeMillis()) {
				Settings.p("Respawning " + e);
				
				PhysicsComponent md = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
				Matrix4 mat = new Matrix4();
				mat.setTranslation(wrc.respawnPoint);
				md.body.setWorldTransform(mat);
				md.body.activate();
				//md.body.clearForces();
				md.body.setAngularVelocity(Vector3.Zero);
				md.body.setLinearVelocity(Vector3.Zero);

				// Reset health
				PlayerData playerData = (PlayerData)e.getComponent(PlayerData.class);
				playerData.health = playerData.max_health;

				e.removeComponent(WillRespawnComponent.class);
				this.entities.remove(i);
			}
		}
	}

}