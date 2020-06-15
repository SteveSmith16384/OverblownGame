package ssmith.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class GraphicsHelper {

	public GraphicsHelper() {
	}


	public static TextureRegion[][] createSheet(String src, int numX, int numY){
		Texture tex = new Texture(Gdx.files.internal(src));
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


	public static Decal DecalHelper(String filename, float sizePcent) {
		Texture tex = new Texture(Gdx.files.internal(filename));
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
