package ssmith.libgdx;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;
import com.scs.splitscreenfps.Settings;

public class ModelFunctions {

	private ModelFunctions() {
	}


	public static ModelInstance loadModel(String filename, boolean removeMaterials, Vector3 adj, float scale) {
		Model model = null;
		if (filename.endsWith(".obj")) {
			ModelLoader loader = new ObjLoader();
			model = loader.loadModel(Gdx.files.internal(filename));
		} else if (filename.endsWith(".g3db")) {
			G3dModelLoader g3dbModelLoader = new G3dModelLoader(new UBJsonReader());
			//MyModelLoader g3dbModelLoader = new MyModelLoader(new UBJsonReader(), adj, scale);
			model = g3dbModelLoader.loadModel(Gdx.files.absolute(filename));
		} else {
			throw new RuntimeException("Unhandled model format: " + filename);
		}
		
		ModelInstance instance = new ModelInstance(model);

		if (removeMaterials) {
			for (Material mat : instance.materials) {
				mat.remove(BlendingAttribute.Type);
			}
		}

		return instance;
	}


	public static float getScaleForHeight(ModelInstance model, float height) {
		BoundingBox bb = new BoundingBox();
		model.calculateBoundingBox(bb);
		bb.mul(model.transform);

		return height / bb.getHeight();
	}


	public static float getScaleForWidth(ModelInstance model, float width) {
		BoundingBox bb = new BoundingBox();
		model.calculateBoundingBox(bb);
		bb.mul(model.transform);

		return width / bb.getWidth();
	}


	public static void getOrigin(ModelInstance model, Vector3 out) {
		BoundingBox bb = new BoundingBox();
		model.calculateBoundingBox(bb);

		if (Settings.STRICT) {
			Vector3 vec = new Vector3();
			model.transform.getTranslation(vec);
			if (vec.len() != 0) {
				throw new RuntimeException("Model must be at 0,0,0 to get correct origin!");
			}
		}

		bb.mul(model.transform);
		bb.getCenter(out);
		// NO! Origin must be in middle for physics -  out.y -= bb.getHeight()/2; // Make origin at the bottom of the model
	}


	public static Vector3 getOrigin(ModelInstance model) {
		Vector3 out = new Vector3();
		getOrigin(model, out);
		return out;
	}

}
