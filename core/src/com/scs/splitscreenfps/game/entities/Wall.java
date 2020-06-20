package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;

public class Wall extends AbstractEntity {

	public Wall(BasicECS ecs, String name, String tex_filename, float posX, float posY, float posZ, float w, float h, float d, float mass_pre, int texRepeat) {
		this(ecs, name, tex_filename, posX, posY, posZ, w, h, d, mass_pre, 0, 0, 0, texRepeat);
	}
	
	
	// Note that the mass gets multiplied by the size
	public Wall(BasicECS ecs, String name, String tex_filename, float posX, float posY, float posZ, float w, float h, float d, float mass_pre, float degreesX, float degreesY, float degreesZ, int texRepeat) {
		super(ecs, name);

		Texture tex = new Texture(tex_filename);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material black_material = new Material(TextureAttribute.createDiffuse(tex));
		ModelBuilder modelBuilder = new ModelBuilder();
		//Model box_model = modelBuilder.createBox(w, h, d, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
		modelBuilder.begin();
		modelBuilder.part("front", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,-h/2,-d/2, -w/2,h/2,-d/2,  w/2,h/2,-d/2, w/2,-h/2,-d/2, 0,0,-1);
		modelBuilder.part("back", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,h/2,d/2, -w/2,-h/2,d/2,  w/2,-h/2,d/2, w/2,h/2,d/2, 0,0,1);
		modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,-h/2,d/2, -w/2,-h/2,-d/2,  w/2,-h/2,-d/2, w/2,-h/2,d/2, 0,-1,0);
		modelBuilder.part("top", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,h/2,-d/2, -w/2,h/2,d/2,  w/2,h/2,d/2, w/2,h/2,-d/2, 0,1,0);
		modelBuilder.part("left", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(-w/2,-h/2,d/2, -w/2,h/2,d/2,  -w/2,h/2,-d/2, -w/2,-h/2,-d/2, -1,0,0);
		modelBuilder.part("right", GL20.GL_TRIANGLES, attr, black_material)
		    .rect(w/2,-h/2,-d/2, w/2,h/2,-d/2,  w/2,h/2,d/2, w/2,-h/2,d/2, 1,0,0);
		Model box_model = modelBuilder.end();
		
		Matrix3 mat = new Matrix3();
		mat.scl(texRepeat);
		box_model.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(box_model, new Vector3(posX, posY, posZ));
		if (degreesX != 0) {
			instance.transform.rotate(Vector3.X, degreesX);
		}
		if (degreesY != 0) {
			instance.transform.rotate(Vector3.Y, degreesY);
		}
		if (degreesZ != 0) {
			instance.transform.rotate(Vector3.Z, degreesZ);
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

		//this.addComponent(new AffectedByExplosionComponent());
	}

}
