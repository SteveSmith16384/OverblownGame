package com.scs.splitscreenfps.game;

import java.awt.Rectangle;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.scs.splitscreenfps.game.decals.ShadedGroupStrategy;

public class ViewportData {

	public PerspectiveCamera camera;
	public Rectangle viewPos;
	public FrameBuffer frameBuffer;
	public DecalBatch decalBatch;

	public ViewportData(int idx, boolean full_screen, int total) {
		Rectangle d = this.getDimensions(idx, full_screen, total);

		camera = new PerspectiveCamera(65, d.width, d.height);
		camera.position.set(10f, 0, 10f);
		camera.lookAt(11f, 0, 10f);
		camera.near = 0.1f;
		camera.far = 30f;
		camera.update();

		ShadedGroupStrategy groupStrategy = new ShadedGroupStrategy(camera);
		decalBatch = new DecalBatch(groupStrategy);

		this.resize(idx, full_screen, total);
	}


	private Rectangle getDimensions(int idx, boolean full_screen, int total) {
		int x, y, w, h;
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
		} else {
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
			
		}
		return new Rectangle(x, y, w, h);
	}


	public void resize(int idx, boolean full_screen, int total) {
		/*int w = Gdx.graphics.getWidth()/2;
		int h = Gdx.graphics.getHeight()/2;
		if (full_screen) {
			w = Gdx.graphics.getBackBufferWidth()/2;
			h = Gdx.graphics.getBackBufferHeight()/2;
		}

		// Goes clockwise starting top-left
		switch (idx) {
		case 0:
			this.viewPos = new Rectangle(0, 0, w, h);
			break;
		case 1:
			this.viewPos = new Rectangle(w, 0, w, h);
			break;
		case 2:
			this.viewPos = new Rectangle(w, h, w, h);
			break;
		case 3:
			this.viewPos = new Rectangle(0, h, w, h);
			break;
		}*/
		
		this.viewPos = this.getDimensions(idx, full_screen, total);

		camera.viewportWidth = viewPos.width;
		camera.viewportHeight = viewPos.height;

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.LOGICAL_WIDTH_PIXELS, Settings.LOGICAL_HEIGHT_PIXELS, true);
		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 512, true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		if (Gdx.app.getType() != ApplicationType.WebGL) {
			//post = new PostProcessing(w, h);
		}
	}


	public void dispose() {
		decalBatch.dispose();
		frameBuffer.dispose();
	}

}
