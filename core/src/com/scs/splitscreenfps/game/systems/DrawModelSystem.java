package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.entities.SkyboxCube;

public class DrawModelSystem extends AbstractSystem {

	private Game game;
	private ModelBatch modelBatch;
	private Environment environment;

	private Vector3 tmpOffset = new Vector3();
	private Matrix4 tmpMat = new Matrix4();

	private DirectionalShadowLight shadowLight;
	private ModelBatch shadowBatch;

	private SkyboxCube skybox;

	public DrawModelSystem(Game _game, BasicECS ecs) {
		super(ecs, HasModelComponent.class);
		game = _game;

		this.modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		//environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 15, 15, 1f, 100f))
				.set(0.8f, 0.8f, 0.8f, 
						1f, -.5f, 1f));
		environment.shadowMap = shadowLight;
		shadowBatch = new ModelBatch(new DepthShaderProvider());

		skybox = new SkyboxCube(ecs, "Skybox", "", 30, 30, 30);
		//skybox = new SkyboxSphere(ecs, "Skybox", "", 20);

	}


	//@Override
	public void process(Camera cam, boolean shadows) {
		if (!shadows) {
			this.modelBatch.begin(cam);

			this.renderEntity(skybox, modelBatch, false);
			//HasModelComponent model = (HasModelComponent)skybox.getComponent(HasModelComponent.class);
			//modelBatch.render(model.model, environment_skybobx);

			Iterator<AbstractEntity> it = entities.iterator();
			while (it.hasNext()) {
				AbstractEntity entity = it.next();
				this.renderEntity(entity, modelBatch, false);
			}
			this.modelBatch.end();
		} else {
			shadowLight.begin(Vector3.Zero, cam.direction);
			shadowBatch.begin(shadowLight.getCamera());
			Iterator<AbstractEntity> it2 = entities.iterator();
			while (it2.hasNext()) {
				AbstractEntity entity = it2.next();
				this.renderEntity(entity, shadowBatch, true);
			}
			shadowBatch.end();
			shadowLight.end();
		}
	}


	//@Override
	public void renderEntity(AbstractEntity entity, ModelBatch batch, boolean shadow) {
		HasModelComponent model = (HasModelComponent)entity.getComponent(HasModelComponent.class);
		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);

		if (pc != null) {
			pc.body.getWorldTransform(tmpMat);

			// Set model position data based on physics data - regardless of whether we draw them
			tmpMat.getTranslation(posData.position);
		}
		
		if (model.dontDrawInViewId == game.currentViewId) {
			return;
		}
		if (model.onlyDrawInViewId >= 0) {
			if (model.onlyDrawInViewId != game.currentViewId) {
				return;
			}
		}

		if (shadow && model.cast_shadow == false) {
			return;
		}

		if (Settings.STRICT) {
			if (posData == null) {
				throw new RuntimeException(entity + " has no PositionComponent");
			}
		}
		if (pc != null) {
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

		} else { // Non-physics entity
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

		batch.render(model.model, environment);
	}


}
