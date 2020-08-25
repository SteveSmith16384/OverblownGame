package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btGhostObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasDecal;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.IsCollectableComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.systems.CollectableSystem;

import ssmith.libgdx.GraphicsHelper;
import ssmith.libgdx.ModelFunctions;
import ssmith.libgdx.ShapeHelper;

public class EntityFactory {

	public static AbstractEntity createBall(Game game, Texture tex, float posX, float posY, float posZ, float diam, float mass_pre) {
		AbstractEntity ball = new AbstractEntity(game.ecs, "Ball");

		Material black_material = new Material(TextureAttribute.createDiffuse(tex));

		Model sphere_model = game.modelBuilder.createSphere(diam,  diam,  diam, 10, 10, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		ModelInstance instance = new ModelInstance(sphere_model, new Vector3(posX, posY, posZ));

		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		ball.addComponent(model);

		float volume = (float)((4/3) * Math.PI * ((diam/2) * (diam/2) * (diam/2)));
		float mass = mass_pre * volume;

		btSphereShape sphere_shape = new btSphereShape(diam/2);
		Vector3 local_inertia = new Vector3();
		sphere_shape.calculateLocalInertia(mass, local_inertia);
		btRigidBody groundObject = new btRigidBody(mass, null, sphere_shape, local_inertia);
		groundObject.userData = ball;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(sphere_shape);
		groundObject.setWorldTransform(instance.transform); // todo - set from posdata!
		ball.addComponent(new PhysicsComponent(groundObject));

		ball.addComponent(new PositionComponent());

		return ball;
	}


	public static AbstractEntity createStaticModel(BasicECS ecs, String name, String filename, float posX, float posY, float posZ, float rot_y, boolean alignToY) {
		AbstractEntity entity = new AbstractEntity(ecs, name);

		ModelInstance instance = ModelFunctions.loadModel(filename, false, 1f);

		// Calc min on Y axis
		BoundingBox tmpBB = new BoundingBox();
		if (alignToY) {
			instance.calculateBoundingBox(tmpBB);
			tmpBB.mul(instance.transform);
		}
		instance.transform.setTranslation(posX, posY-tmpBB.min.y, posZ);

		if (rot_y != 0) {
			instance.transform.rotate(Vector3.Y, rot_y);
		}

		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		entity.addComponent(model);

		btCollisionShape shape = Bullet.obtainStaticNodeShape(instance.nodes);
		btRigidBody rigidBody = new btRigidBody(0, null, shape, new Vector3());
		rigidBody.userData = entity;
		//rigidBody.setRestitution(.2f);
		rigidBody.setCollisionShape(shape);
		rigidBody.setWorldTransform(instance.transform); // todo - set from posdata!
		entity.addComponent(new PhysicsComponent(rigidBody));

		entity.addComponent(new PositionComponent());

		return entity;
	}


	public static void createStaticModelsForLargeModel(BasicECS ecs, String name, String filename, float posX, float posY, float posZ, float rot_x, float rot_y) {
		AbstractEntity entity = new AbstractEntity(ecs, name);

		ModelInstance instance = ModelFunctions.loadModel(filename, false, 1f);

		// Calc min on Y axis
		instance.transform.setTranslation(posX, posY, posZ);

		if (rot_x != 0) {
			instance.transform.rotate(Vector3.X, rot_x); // todo - I think this line does nothing since it gets changed by PositionComponent
		}
		if (rot_y != 0) {
			instance.transform.rotate(Vector3.Y, rot_y); // todo - I think this line does nothing since it gets changed by PositionComponent
		}

		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		entity.addComponent(model);

		PositionComponent posData = new PositionComponent();
		posData.position.y = posY;
		posData.angle_x_degrees = rot_x;
		entity.addComponent(posData);

		ecs.addEntity(entity);

		int count = 0;
		for(Node node : instance.nodes) {
			count++;
			//if (count == 146) { // crashes?
				//Settings.p("Adding e " + count);

				entity = new AbstractEntity(ecs, name);

				btCollisionShape shape = Bullet.obtainStaticNodeShape(node, true);
				btRigidBody rigidBody = new btRigidBody(0, null, shape, new Vector3());
				rigidBody.userData = entity;
				//rigidBody.setRestitution(.2f);
				rigidBody.setCollisionShape(shape);
				rigidBody.setWorldTransform(instance.transform); // todo - set from posdata!
				entity.addComponent(new PhysicsComponent(rigidBody));

				entity.addComponent(new PositionComponent());

				ecs.addEntity(entity);

				//Settings.p("Added e " + count + "/" + instance.nodes.size);
			//}
		}
	}


	public static AbstractEntity createDynamicModel(BasicECS ecs, String name, String filename, float posX, float posY, float posZ, float rot_y, float mass, boolean alignToY) {
		AbstractEntity entity = new AbstractEntity(ecs, name);

		ModelInstance instance = ModelFunctions.loadModel(filename, false, 1f);

		// Calc min on Y axis
		BoundingBox tmpBB = new BoundingBox();
		instance.calculateBoundingBox(tmpBB);
		tmpBB.mul(instance.transform);
		if (alignToY) {
			instance.transform.setTranslation(posX, posY-tmpBB.min.y, posZ);
		} else {
			instance.transform.setTranslation(posX, posY, posZ);
		}

		if (rot_y != 0) {
			instance.transform.rotate(Vector3.Y, rot_y);
		}

		/*for(Node n : instance.nodes) {
			n.localTransform.setTranslation(-tmpBB.getCenterX(), -10, -tmpBB.getCenterZ());
		}*/

		for (int i = 0; i < instance.nodes.size; i++) {
			String id = instance.nodes.get(i).id;
			Node node = instance.getNode(id);

			node.translation.set(-tmpBB.getCenterX(), -tmpBB.getCenterY(), -tmpBB.getCenterZ());
			node.scale.set(1,1,1);
			node.rotation.idt();
			instance.calculateTransforms();
		}

		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		entity.addComponent(model);

		Vector3 ext = new Vector3();
		tmpBB.getDimensions(ext);
		ext.scl(.5f);

		btBoxShape shape = new btBoxShape(ext);

		Vector3 local_inertia = new Vector3();
		if (mass > 0) {
			shape.calculateLocalInertia(mass, local_inertia);
		}
		btRigidBody rigidBody = new btRigidBody(mass, null, shape, local_inertia);
		rigidBody.userData = entity;
		//rigidBody.setRestitution(.2f);
		rigidBody.setCollisionShape(shape);
		rigidBody.setWorldTransform(instance.transform); // todo - set from posdata!
		entity.addComponent(new PhysicsComponent(rigidBody));

		entity.addComponent(new PositionComponent());

		return entity;
	}


	public static AbstractEntity createCylinder(Game game, Texture tex, float x, float y, float z, float diam, float length, float mass_pre) {
		AbstractEntity cylinder = new AbstractEntity(game.ecs, "Cylinder");

		ModelInstance instance = ShapeHelper.createCylinder(game.modelBuilder, tex, x, y, z, diam, length);

		HasModelComponent model = new HasModelComponent(instance, 1, true);
		cylinder.addComponent(model);

		//if (mass_pre >= 0) {
		btCylinderShape cylinderShape = new btCylinderShape(new Vector3(diam/2, length/2, diam/2));
		Vector3 local_inertia = new Vector3();
		float mass = (float)(Math.PI * (diam/2) * (diam/2) + length) * mass_pre;
		cylinderShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody body = new btRigidBody(mass, null, cylinderShape, local_inertia);
		body.userData = cylinder;
		body.setRestitution(.5f);
		body.setCollisionShape(cylinderShape);
		body.setWorldTransform(instance.transform); // todo - set from posdata!
		cylinder.addComponent(new PhysicsComponent(body));
		//}

		cylinder.addComponent(new PositionComponent());

		return cylinder;
	}


	public static AbstractEntity createPlane(Game game, Texture tex, float x, float y, float z, float w, float d) {
		AbstractEntity plane = new AbstractEntity(game.ecs, "Plane");

		ModelInstance instance = ShapeHelper.createRect(game.modelBuilder, tex, w, d);

		HasModelComponent model = new HasModelComponent(instance, 1, false);
		plane.addComponent(model);

		PositionComponent pos = new PositionComponent();
		pos.position.set(x, y, z);
		plane.addComponent(pos);

		return plane;
	}


	public static AbstractEntity createHealthPack(Game game, Vector3 start) {
		AbstractEntity e = new AbstractEntity(game.ecs, "HealthPack");

		e.addComponent(new IsCollectableComponent(CollectableSystem.CollectableType.HealthPack));

		e.addComponent(new PositionComponent());

		HasDecal hasDecal = new HasDecal();
		hasDecal.decal = GraphicsHelper.DecalHelper(game.getTexture("textures/PowerUp/PowerUp_06.png"), 0.4f);
		hasDecal.faceCamera = true;
		hasDecal.dontLockYAxis = true;
		e.addComponent(hasDecal);

		// Add physics
		btBoxShape shape = new btBoxShape(new Vector3(.1f, .1f, .1f));
		btGhostObject body = new btGhostObject();//0, null, shape);
		body.setCollisionFlags(CollisionFlags.CF_NO_CONTACT_RESPONSE);
		body.userData = e;
		body.setCollisionShape(shape);
		Matrix4 mat = new Matrix4();
		mat.setTranslation(start);
		body.setWorldTransform(mat); // todo - set from posdata!
		PhysicsComponent pc = new PhysicsComponent(body);
		//pc.disable_gravity = true; Not available for ghost objects
		e.addComponent(pc);

		return e;
	}


	public static AbstractEntity createLootBox(Game game, float posX, float posY, float posZ, CollectableSystem.CollectableType type) {
		AbstractEntity lootbox = new AbstractEntity(game.ecs, "LootBox");

		float w = .5f;
		float h = .5f;
		float d = .5f;

		lootbox.addComponent(new IsCollectableComponent(type));

		TextureRegion tex = game.getTexture("textures/spritesforyou.png", 8, 8, 0, 4);
		Material black_material = new Material(TextureAttribute.createDiffuse(tex));
		ModelBuilder modelBuilder = game.modelBuilder;

		Model box_model = ShapeHelper.createCube(modelBuilder, w, h, d, black_material);

		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));

		HasModelComponent model = new HasModelComponent(instance, 1f, true);
		lootbox.addComponent(model);

		lootbox.addComponent(new PositionComponent());

		btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
		Vector3 local_inertia = new Vector3();
		boxShape.calculateLocalInertia(1f, local_inertia);
		btRigidBody groundObject = new btRigidBody(w*h*d, null, boxShape, local_inertia);
		groundObject.userData = lootbox;
		groundObject.setRestitution(.5f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform); // todo - set from posdata!
		lootbox.addComponent(new PhysicsComponent(groundObject));

		return lootbox;
	}


	public static AbstractEntity createOriginMarker(Game game) {
		AbstractEntity originMarker = new AbstractEntity(game.ecs, "OriginMarker");

		//Texture tex = game.getTexture("colours/yellow.png");
		Material material = new Material();//TextureAttribute.createDiffuse(tex));

		//Model box_model = ShapeHelper.createLine(modelBuilder, new Vector3(), new Vector3(0, 1, 0), material);

		int attr = VertexAttributes.Usage.Position;// | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
		ModelBuilder modelBuilder = game.modelBuilder;
		modelBuilder.begin();
		MeshPartBuilder mb = modelBuilder.part("front", GL20.GL_LINES, attr, material);
		mb.setColor(Color.WHITE);
		mb.line(new Vector3(), new Vector3(0, 100, 0));
		mb.line(new Vector3(), new Vector3(100, 0, 0));
		mb.line(new Vector3(), new Vector3(0, 0, 100));
		Model box_model = modelBuilder.end();

		ModelInstance instance = new ModelInstance(box_model);

		HasModelComponent model = new HasModelComponent(instance, 1f, false);
		originMarker.addComponent(model);

		originMarker.addComponent(new PositionComponent());

		return originMarker;
	}


}
