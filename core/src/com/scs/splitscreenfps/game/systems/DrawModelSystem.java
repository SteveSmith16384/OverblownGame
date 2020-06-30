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

			// Get bb for model
			BoundingBox bb = new BoundingBox();
			model.model.calculateBoundingBox(bb);
			tmpMat.scl(model.scale);
			bb.mul(tmpMat);

			// Resets the matrix to avoid hangoffs
			model.model.transform.idt();//set(tmpMat);
			
			// Move model to origin
			Vector3 v = new Vector3();
			bb.getCenter(v).scl(-1);
			model.model.transform.setTranslation(v.x, v.y, v.z);
			
			model.model.transform.scl(model.scale);
			
			//tmpOffset.set(model.positionOffsetToOrigin).scl(1.5f); // Adjust model position for origin

			// Set model rotation
			Quaternion q  = new Quaternion();
			tmpMat.getRotation(q);
			model.model.transform.rotate(q);


			/*model.model.transform.set(tmpOffset.x, tmpOffset.y, tmpOffset.z,
					q.x, q.y, q.z, q.w,
					model.scale, model.scale, model.scale);
*/
			// Set scale
			//model.model.transform.scl(model.scale); // Scale is not stored in RigidBody transform!

			// Set rotation
			if (pc.physicsControlsRotation == false) {
				model.model.transform.rotate(Vector3.Y, posData.angle_y_degrees+model.angleYOffsetToFwds);
			}
			
			//showModelDetails(model);
			
			// Now put back to correct position
			tmpMat.getTranslation(tmpOffset);
			tmpOffset.add(model.positionOffsetToOrigin); // Adjust model position for origin
			//model.model.transform.setTranslation(tmpOffset);

			//showModelDetails(model);
			
			// Set model position data based on physics data
			tmpMat.getTranslation(posData.position);

		} else { // Non-physics entity
			tmpOffset.set(model.positionOffsetToOrigin);
			tmpOffset.add(posData.position);
			model.model.transform.setToTranslation(tmpOffset);
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

	
	private void showModelDetails(HasModelComponent model) {
		if (Settings.DEBUG_MISSING_MODEL) {
			Vector3 v = new Vector3();
			model.model.transform.getTranslation(v);
			model.model.transform.getScale(v);
			int dfgdfg = 454;
		}
		
		
	}
}
