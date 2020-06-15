package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.AffectedByExplosionComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;

public class Wall extends AbstractEntity {

	public Wall(BasicECS ecs, String name, String tex_filename, float posX, float posY, float posZ, float w, float h, float d, float mass_pre) {
		this(ecs, name, tex_filename, posX, posY, posZ, w, h, d, mass_pre, null, 0);
	}
	
	
	// Note that the mass gets multiplied by the size
	public Wall(BasicECS ecs, String name, String tex_filename, float posX, float posY, float posZ, float w, float h, float d, float mass_pre, Vector3 axis, float degrees) {
		super(ecs, name);

		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture(tex_filename)));
		ModelBuilder modelBuilder = new ModelBuilder();
		Model box_model = modelBuilder.createBox(w, h, d, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		//ModelInstance instance = new ModelInstance(box_model, new Vector3(posX+(w/2), posY+(h/2), posZ+(d/2)));
		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));
		if (axis != null) {
			instance.transform.rotate(axis, degrees);
		}
		
		HasModelComponent model = new HasModelComponent(instance);
		this.addComponent(model);

		float mass = mass_pre * w * h * d; 

		btBoxShape boxShape = new btBoxShape(new Vector3(w/2, h/2, d/2));
		Vector3 local_inertia = new Vector3();
		boxShape.calculateLocalInertia(mass, local_inertia);
		btRigidBody groundObject = new btRigidBody(mass, null, boxShape, local_inertia);
		groundObject.userData = this;
		groundObject.setRestitution(.2f);
		groundObject.setCollisionShape(boxShape);
		groundObject.setWorldTransform(instance.transform);
		this.addComponent(new PhysicsComponent(groundObject));

		this.addComponent(new AffectedByExplosionComponent());


	}

}
