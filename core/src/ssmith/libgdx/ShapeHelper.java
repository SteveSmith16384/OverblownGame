package ssmith.libgdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

public class ShapeHelper {

	public static ModelInstance createRect(Texture tex, float w, float d) {
		//tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material material = new Material(TextureAttribute.createDiffuse(tex));		

		ModelBuilder modelBuilder = new ModelBuilder();
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
	

	public static ModelInstance createSphere(Texture tex, float x, float y, float z, float size) {
		//Texture tex = new Texture(tex_filename1);
		//tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createSphere(size, size, size, 5, 5, white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal);

		Matrix3 mat = new Matrix3();
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor, new Vector3(x, y, z));
		//instance.transform.translate(Game.UNIT/2, 0, Game.UNIT/2);
		//instance.calculateTransforms();

		return instance;
	}

	
	public static ModelInstance createCylinder(Texture tex, float x, float y, float z, float diam, float length) {
		//Texture tex = new Texture(tex_filename1);
		//tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createCylinder(diam, length, diam, 8, white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Normal);

		Matrix3 mat = new Matrix3();
		floor.meshes.get(0).transformUV(mat);

		ModelInstance instance = new ModelInstance(floor, new Vector3(x, y, z));
		//instance.transform.translate(Game.UNIT/2, 0, Game.UNIT/2);
		//instance.calculateTransforms();

		return instance;
	}

/*
	public static ModelInstance createCube(String tex_filename1, float w, float h, float d) {
		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture(tex_filename1)));
		ModelBuilder modelBuilder = new ModelBuilder();
		Model box_model = modelBuilder.createBox(w, h, d, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		ModelInstance instance = new ModelInstance(box_model);//, new Vector3(posX+(w/2), posY+(h/2), posZ+(d/2)));

		return instance;
	}
*/
}
