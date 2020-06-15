package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;

import ssmith.libgdx.ShapeHelper;

public class Floor extends AbstractEntity {


	public Floor(BasicECS ecs, String name, String tex_filename1, float x, float y, float z, float w, float d) {
		super(ecs, name);

		/*
		Texture tex = new Texture(tex_filename1);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createRect(
				0f,0f, (float) d,
				(float)w, 0f, (float)d,
				(float)w, 0f, 0f,
				0f,0f,0f,
				1f,1f,1f,
				white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		Matrix3 mat = new Matrix3();
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor, new Vector3(x, y, z));
*/
		
		ModelInstance instance = ShapeHelper.createRect(tex_filename1, w, d);
		instance.transform.setTranslation(x, y, z);
		
		HasModelComponent model = new HasModelComponent(instance);
		this.addComponent(model);
		
		btBoxShape groundShape = new btBoxShape(new Vector3(w/2, 0.5f, d/2)); // todo - use infinite plane
		btRigidBody groundObject = new btRigidBody(0f, null, groundShape);
		groundObject.userData = this;
		groundObject.setRestitution(.9f);
		groundObject.setCollisionShape(groundShape);
		groundObject.setWorldTransform(instance.transform);
		groundObject.translate(new Vector3(0, -.5f, 0));
		//game.dynamicsWorld.addRigidBody(groundObject);
		this.addComponent(new PhysicsComponent(groundObject));

	}

}
