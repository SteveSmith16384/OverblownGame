package ssmith.libgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.scs.splitscreenfps.game.components.HasModelComponent;

public class GraphicsHelper {

	public GraphicsHelper() {
	}


	public static TextureRegion[][] createSheet(Texture tex, int numX, int numY){
		//Texture tex = new Texture(Gdx.files.internal(src));
		int w = tex.getWidth()/numX;
		int h = tex.getHeight()/numY;
		TextureRegion reg[][]  = new TextureRegion[numX][numY];

		for (int x = 0; x < numX; x++) {
			for (int y = 0; y < numY; y++) {
				reg[x][y] = new TextureRegion(tex, x*w, y*h, w, h);
			}
		}

		return reg;

	}


	public static Decal DecalHelper(Texture tex, float sizePcent) {
		//Texture tex = new Texture(Gdx.files.internal(filename));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
		Decal decal = Decal.newDecal(tr, true);
		decal.setScale(sizePcent / tr.getRegionWidth());
		return decal;
	}


	public static Decal DecalHelper(TextureRegion tr, float size) {
		Decal decal = Decal.newDecal(tr, true);
		decal.setScale(size / tr.getRegionWidth());
		return decal;
	}



}
