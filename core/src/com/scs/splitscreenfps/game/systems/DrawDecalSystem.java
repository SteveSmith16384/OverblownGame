package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class DrawDecalSystem extends AbstractSystem {

	private Vector3 tmp = new Vector3();
	private Game game;

	public DrawDecalSystem(Game _game, BasicECS ecs) {
		super(ecs, HasDecal.class);

		game = _game;
	}


	@Override
	public void process() {
		int viewId = game.currentViewId;
		Camera camera = game.players[viewId].camera;
		DecalBatch batch = game.viewports[viewId].decalBatch;

		Iterator<AbstractEntity> it = entities.iterator();
		while (it.hasNext()) {
			AbstractEntity entity = it.next();
			this.processEntity(entity, camera, batch);
		}

		batch.flush();
	}


	//@Override
	public void processEntity(AbstractEntity entity, Camera camera, DecalBatch batch) {
		HasDecal hasDecal = (HasDecal)entity.getComponent(HasDecal.class);

		updateTransform(entity, camera, hasDecal);

		if (game.currentViewId == 0) { // Only need to do this once!
			PositionComponent pos = (PositionComponent)entity.getComponent(PositionComponent.class);
			hasDecal.decal.setPosition(pos.position);
		}

		/*if(!camera.frustum.sphereInFrustum(hasPosition.position, 1f)) {
			return;
		}*/

		batch.add(hasDecal.decal);
	}


	private void updateTransform(AbstractEntity entity, Camera cam, HasDecal hasDecal) {
		if (hasDecal.faceCamera) {
			tmp.set(cam.direction).scl(-1);
			if(hasDecal.dontLockYAxis == false) {
				tmp.y = 0;
			}
			hasDecal.decal.setRotation(tmp, Vector3.Y);
			hasDecal.decal.rotateY(hasDecal.rotation);
		} else {
			hasDecal.decal.setRotationY(hasDecal.rotation);
		}

		/*
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		if (pc != null) {
			pc.body.getWorldTransform().getTranslation(tmp);
			hasDecal.decal.setPosition(tmp);
		} else {
			PositionComponent pos = (PositionComponent)entity.getComponent(PositionComponent.class);
			hasDecal.decal.setPosition(pos.position);
		}
*/
	}


}
