package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

public class DrawModelSystem extends AbstractSystem {

	private Game game;
	private ModelBatch modelBatch;
	private Environment environment;

	private Vector3 tmpOffset = new Vector3();
	private Matrix4 tmpMat = new Matrix4();

	public DrawModelSystem(Game _game, BasicECS ecs) {
		super(ecs, HasModelComponent.class);
		game = _game;

		this.modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

	}


	//@Override
	public void process(Camera cam) {
		this.modelBatch.begin(cam);

		Iterator<AbstractEntity> it = entities.iterator();
		while (it.hasNext()) {
			AbstractEntity entity = it.next();
			this.processEntity(entity, cam);
		}

		this.modelBatch.end();
	}


	//@Override
	public void processEntity(AbstractEntity entity, Camera camera) {
		HasModelComponent model = (HasModelComponent)entity.getComponent(HasModelComponent.class);
		if (model.dontDrawInViewId == game.currentViewId) {
			return;
		}
		if (model.onlyDrawInViewId >= 0) {
			if (model.onlyDrawInViewId != game.currentViewId) {
				return;
			}
		}

		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
		if (Settings.STRICT) {
			if (posData == null) {
				throw new RuntimeException(entity + " has no PositionComponent");
			}
		}
		if (pc != null) {
			pc.body.getWorldTransform(tmpMat);

			//tmpOffset.add(model.positionOffsetToOrigin); // Adjust model position for origin

			// Resets the matrix to avoid hangoffs
			if (model.scale == 1f) {
				model.model.transform.set(tmpMat);
			} else {
				tmpMat.getTranslation(tmpOffset);
				tmpOffset.y += model.yOff;
				model.model.transform.setToTranslation(tmpOffset);
				model.model.transform.scl(model.scale);

				// Set rotation
				if (pc.physicsControlsRotation == false) {
					// Typically Avatars
					model.model.transform.rotate(Vector3.Y, posData.angle_y_degrees+model.angleYOffsetToFwds);
				} else {
					Quaternion q = new Quaternion();
					tmpMat.getRotation(q);
					model.model.transform.rotate(q);
				}
			}

			// Set model position data based on physics data
			tmpMat.getTranslation(posData.position);

		} else { // Non-physics entity
			//tmpOffset.set(model.positionOffsetToOrigin);
			//tmpOffset.set(posData.position);
			model.model.transform.setToTranslation(posData.position);
			model.model.transform.scl(model.scale);
			model.model.transform.rotate(Vector3.Y, posData.angle_y_degrees+model.angleYOffsetToFwds);
		}


		// Only draw if in frustum 
		/*if (model.always_draw == false && !camera.frustum.sphereInFrustum(posData.position, 1f)) {
			//return; todo - check if bounds are in frustum!
		}
		/*} else {
			if (model.always_draw == false) {
				// Only draw if in frustum 
				if (model.bb == null) {
					model.bb = new BoundingBox();
					model.model.calculateBoundingBox(model.bb);
					model.bb.mul(model.model.transform);
				}
				if (!camera.frustum.boundsInFrustum(model.bb)) {
					//return; todo - fix
				}
			}
		}*/
		//}

		modelBatch.render(model.model, environment);
	}


	private void showModelDetails(Matrix4 mat) {
		Vector3 v = new Vector3();
		mat.getTranslation(v);
		mat.getScale(v);
		int dfgdfg = 454;
	}

}
