package com.scs.splitscreenfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
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

import ssmith.libgdx.WireframeShader;

public class DrawModelSystem extends AbstractSystem {

	private Game game;
	private ModelBatch modelBatch;
	private ModelBatch wireframeBatch;
	private Environment environment;

	private Matrix4 tmpMat = new Matrix4();
	private Vector3 tmpVec = new Vector3();

	private DirectionalShadowLight shadowLight;
	private ModelBatch shadowBatch;

	public AbstractEntity wireframe_entity;

	private int num_objects_drawn;
	private BoundingBox tmpBB = new BoundingBox();

	public DrawModelSystem(Game _game, BasicECS ecs) {
		super(ecs, HasModelComponent.class);
		game = _game;

		this.modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		//environment.set(new ColorAttribute(ColorAttribute.Diffuse, 0.6f, 0.6f, 0.6f, 1f));
		//environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.5f, -1f));

		environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 15, 15, 1f, 100f))
				.set(0.9f, 0.8f, 0.8f, 
						1f, -.5f, 1f));
		environment.shadowMap = shadowLight;
		shadowBatch = new ModelBatch(new DepthShaderProvider());

		wireframeBatch = new ModelBatch(new DefaultShaderProvider() {
			@Override
			protected Shader createShader(Renderable renderable) {
				return new WireframeShader(renderable, config);
			}
		});
	}


	//@Override
	public void process(Camera cam, boolean shadows) {
		long start = System.currentTimeMillis();
		num_objects_drawn = 0;

		if (!shadows) {
			this.modelBatch.begin(cam);

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

		if (wireframe_entity != null) {
			if (wireframe_entity.isMarkedForRemoval()) {
				wireframe_entity = null;
			} else {
				wireframeBatch.begin(cam);
				this.renderEntity(wireframe_entity, wireframeBatch, false);
				wireframeBatch.end();
			}
		}

		long duration = System.currentTimeMillis() - start;
		this.total_time += duration;
		if (shadows == false) {
			//Settings.p("num_objects_drawn=" + num_objects_drawn);
		}
	}


	//@Override
	public void renderEntity(AbstractEntity entity, ModelBatch batch, boolean drawing_shadows) {
		HasModelComponent model = (HasModelComponent)entity.getComponent(HasModelComponent.class);

		PositionComponent posData = (PositionComponent)entity.getComponent(PositionComponent.class);
		if (Settings.STRICT) {
			if (posData == null) {
				throw new RuntimeException(entity + " has no PositionComponent");
			}
		}

		PhysicsComponent pc = (PhysicsComponent)entity.getComponent(PhysicsComponent.class);
		if (game.currentViewId == 0 && drawing_shadows == false) {
			// Calc position.  Only need to do this bit once per game loop!
			if (pc != null && pc.enable_physics) {
				if (pc.position_dirty || pc.getRigidBody().getInvMass() != 0 || Settings.USE_MAP_EDITOR) {
					pc.position_dirty = false;
					pc.body.getWorldTransform(tmpMat);
					// Resets the matrix to avoid hangoffs
					if (model.scale == 1f) {
						model.model.transform.set(tmpMat);
					} else {
						tmpMat.getTranslation(tmpVec);
						tmpVec.y += model.yOff;
						model.model.transform.setToTranslation(tmpVec);
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
				}
			}
		}

		if (drawing_shadows == false && model.dontDrawInViewId == game.currentViewId && Settings.TEST_3RD_PERSON == false) {
			return;
		}
		if (model.onlyDrawInViewId >= 0) {
			if (model.onlyDrawInViewId != game.currentViewId) {
				return;
			}
		}

		if (model.invisible) {
			return;
		}

		if (drawing_shadows && model.cast_shadow == false) {
			return;
		}

		if (pc == null || pc.enable_physics == false) { // Non-physics entity
			// Position model
			if (model.keep_player_in_centre) { // i.e. a skybox
				model.model.transform.setToTranslation(batch.getCamera().position);
			} else {
				model.model.transform.setToTranslation(posData.position);
				model.model.transform.scl(model.scale);
				model.model.transform.rotate(Vector3.X, posData.angle_x_degrees);
				model.model.transform.rotate(Vector3.Y, posData.angle_y_degrees+model.angleYOffsetToFwds);
			}
		}

		// Only draw if in frustum 
		if (model.always_draw == false) {
			if (model.centre == null) {
				Vector3 dimensions = new Vector3();
				model.model.calculateBoundingBox(tmpBB);
				tmpBB.mul(model.model.transform);
				tmpBB.getDimensions(dimensions);
				model.radius = dimensions.len();
				model.centre = new Vector3();
				tmpBB.getCenter(model.centre);
			}
			tmpVec.set(posData.position);
			tmpVec.sub(model.centre);
			if (!batch.getCamera().frustum.sphereInFrustum(tmpVec, model.radius)) {
				//todo return;
			}
		}

		batch.render(model.model, environment);

		if (drawing_shadows == false) {
			num_objects_drawn++;
		}
	}


	@Override
	public void dispose() {
		this.modelBatch.dispose();
		this.shadowBatch.dispose();
	}

}
