package com.scs.splitscreenfps.game;

import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.decals.ShadedGroupStrategy;

public class ViewportData {

	public int idx;
	public PerspectiveCamera camera;
	public Rectangle viewRect;
	public FrameBuffer frameBuffer;
	public DecalBatch decalBatch;

	public ViewportData(int _idx, boolean full_screen, int total) {
		idx = _idx;

		Rectangle d = this.getDimensions(idx, full_screen, total);

		camera = new PerspectiveCamera(65, d.width, d.height);
		camera.lookAt(10f, 0, 10f);
		camera.near = 0.01f;
		camera.far = 100f;
		camera.update();

		decalBatch = new DecalBatch(new ShadedGroupStrategy(camera));

		this.resize(idx, full_screen, total);
	}


	private Rectangle getDimensions(int idx, boolean full_screen, int total) {
		int x=0, y=0, w=0, h=0;
		if (total == 1) {
			x = 0;
			y = 0;
			if (full_screen) {
				w = Gdx.graphics.getBackBufferWidth();
				h = Gdx.graphics.getBackBufferHeight();
			} else {
				w = Gdx.graphics.getWidth();
				h = Gdx.graphics.getHeight();
			}
		} else if (total == 2) {
			if (idx == 0) {
				x = 0;
				y = 0;
			} else {
				x = 0;
				y = Gdx.graphics.getHeight()/2;
			}
			if (full_screen) {
				w = Gdx.graphics.getBackBufferWidth();
				h = Gdx.graphics.getBackBufferHeight()/2;
			} else {
				w = Gdx.graphics.getWidth();
				h = Gdx.graphics.getHeight()/2;
			}
		} else if (total == 3 || total == 4) {
			if (full_screen) {
				w = Gdx.graphics.getBackBufferWidth()/2;
				h = Gdx.graphics.getBackBufferHeight()/2;
			} else {
				w = Gdx.graphics.getWidth()/2;
				h = Gdx.graphics.getHeight()/2;
			}

			if (idx == 0) {
				x = 0;
				y = 0;
			} else if (idx == 1) {
				x = w;
				y = 0;
			} else if (idx == 2) {
				x = w;
				y = h;
			} else {
				x = 0;
				y = h;
			}
		} else if (total == 5 || total == 6) {
			// todo
		} else if (total == 7 || total == 8 || total == 9) {
			if (full_screen) {
				w = Gdx.graphics.getBackBufferWidth()/3;
				h = Gdx.graphics.getBackBufferHeight()/3;
			} else {
				w = Gdx.graphics.getWidth()/3;
				h = Gdx.graphics.getHeight()/3;
			}

			if (idx == 0) {
				x = 0;
				y = 0;
			} else if (idx == 1) {
				x = w;
				y = 0;
			} else if (idx == 2) {
				x = w*2;
				y = 0;
			} else if (idx == 3) {
				x = 0;
				y = h;
			} else if (idx == 4) {
				x = w;
				y = h;
			} else if (idx == 5) {
				x = w*2;
				y = h;
			} else if (idx == 6) {
				x = 0;
				y = h*2;
			} else if (idx == 7) {
				x = w;
				y = h*2;
			} else if (idx == 8) {
				x = w*2;
				y = h*2;
			}

		} else {
			throw new RuntimeException("Cannot handle " + total + " viewports");
		}

		return new Rectangle(x, y, w, h);
	}


	public void resize(int idx, boolean full_screen, int total) {
		this.viewRect = this.getDimensions(idx, full_screen, total);

		camera.viewportWidth = viewRect.width;
		camera.viewportHeight = viewRect.height;

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, true);
		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.LOGICAL_SIZE_PIXELS, Settings.LOGICAL_SIZE_PIXELS, true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, viewRect.width, viewRect.height, true);

		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
	}


	public void dispose() {
		decalBatch.dispose();
		frameBuffer.dispose();
	}

}
