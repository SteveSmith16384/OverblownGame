package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.components.HasModelComponent;

public class Ceiling extends AbstractEntity {

	public Ceiling(BasicECS ecs, String tex_filename, int mapOffX, int mapOffZ, int map_width, int map_height, boolean tile, float height) {
		super(ecs, Ceiling.class.getSimpleName());
		
		Texture tex = new Texture(tex_filename);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createRect(
				0f, 0f, (float) map_height,
				(float)map_width, 0f, (float)map_height,
				(float)map_width, 0f, 0f,
				0f,0f,0f,
				1f,1f,1f,
				white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		Matrix3 mat = new Matrix3();
		if (tile) {
			mat.scl(new Vector2(map_width, map_height));
		}
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor);
		instance.transform.translate(mapOffX, height, mapOffZ);
		instance.transform.rotate(Vector3.X, 180);
		instance.transform.translate(0, 0, -(float)map_width);
		instance.calculateTransforms();

		HasModelComponent model = new HasModelComponent("ceiling", instance);
		this.addComponent(model);
	}


	public Ceiling(BasicECS ecs, Pixmap pixmap, int mapOffX, int mapOffZ, int map_width, int map_height, float height) {
		super(ecs, Ceiling.class.getSimpleName());
		
		Texture tex = new Texture(pixmap);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createRect(
				0f, 0f, (float) map_height,
				(float)map_width, 0f, (float)map_height,
				(float)map_width, 0f, 0f,
				0f,0f,0f,
				1f,1f,1f,
				white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		Matrix3 mat = new Matrix3();
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor);
		instance.transform.translate(mapOffX, height, mapOffZ);
		instance.transform.rotate(Vector3.X, 180);
		instance.transform.translate(0, 0, -(float)map_width);
		instance.calculateTransforms();

		HasModelComponent model = new HasModelComponent("Ceiling", instance);
		this.addComponent(model);
	}

}
