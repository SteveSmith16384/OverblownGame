package ssmith.libgdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

public class ShapeHelper {
	
	public static Model createCube(ModelBuilder modelBuilder, float w, float h, float d, Material black_material) {
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
		modelBuilder.begin();

		MeshPartBuilder mb = modelBuilder.part("front", GL20.GL_TRIANGLES, attr, black_material);
		mb.rect(-w/2,-h/2,-d/2, -w/2,h/2,-d/2,  w/2,h/2,-d/2, w/2,-h/2,-d/2, 0,0,-1);
		//modelBuilder.part("back", GL20.GL_TRIANGLES, attr, black_material)
		mb.rect(-w/2,h/2,d/2, -w/2,-h/2,d/2,  w/2,-h/2,d/2, w/2,h/2,d/2, 0,0,1);
		//modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, black_material)
		mb.rect(-w/2,-h/2,d/2, -w/2,-h/2,-d/2,  w/2,-h/2,-d/2, w/2,-h/2,d/2, 0,-1,0);
		//modelBuilder.part("top", GL20.GL_TRIANGLES, attr, black_material)
		mb.rect(-w/2,h/2,-d/2, -w/2,h/2,d/2,  w/2,h/2,d/2, w/2,h/2,-d/2, 0,1,0);
		//modelBuilder.part("left", GL20.GL_TRIANGLES, attr, black_material)
		mb.rect(-w/2,-h/2,d/2, -w/2,h/2,d/2,  -w/2,h/2,-d/2, -w/2,-h/2,-d/2, -1,0,0);
		//modelBuilder.part("right", GL20.GL_TRIANGLES, attr, black_material)
		mb.rect(w/2,-h/2,-d/2, w/2,h/2,-d/2,  w/2,h/2,d/2, w/2,-h/2,d/2, 1,0,0);

		Model box_model = modelBuilder.end();

		return box_model;
	}

	
	public static ModelInstance createRect(ModelBuilder modelBuilder, Texture tex, float w, float d) {
		//tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material material = new Material(TextureAttribute.createDiffuse(tex));		

		//ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createRect(
				(float)-w/2, 0f, (float)d/2,
				(float)w/2, 0f, (float)d/2,
				(float)w/2, 0f, (float)-d/2,
				(float)-w/2, 0f,(float)-d/2,
				
				1f,1f,1f,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal);

		Matrix3 mat = new Matrix3();
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor);//, new Vector3(x, y, z));
		return instance;
	}
	

	public static ModelInstance createSphere(ModelBuilder modelBuilder, Texture tex, float x, float y, float z, float size) {
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		Model floor = modelBuilder.createSphere(size, size, size, 5, 5, white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal);

		Matrix3 mat = new Matrix3();
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor, new Vector3(x, y, z));
		//instance.transform.translate(Game.UNIT/2, 0, Game.UNIT/2);
		//instance.calculateTransforms();

		return instance;
	}

	
	public static ModelInstance createCylinder(ModelBuilder modelBuilder, Texture tex, float x, float y, float z, float diam, float length) {
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		//ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createCylinder(diam, length, diam, 8, white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal);

		Matrix3 mat = new Matrix3();
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor, new Vector3(x, y, z));
		//instance.transform.translate(Game.UNIT/2, 0, Game.UNIT/2);
		//instance.calculateTransforms();

		return instance;
	}


}
