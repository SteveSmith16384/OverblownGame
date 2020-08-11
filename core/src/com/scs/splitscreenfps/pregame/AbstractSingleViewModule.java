package com.scs.splitscreenfps.pregame;

import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.systems.dependencies.IGetCurrentViewport;

public abstract class AbstractSingleViewModule implements IModule, IGetCurrentViewport {

	protected final BillBoardFPS_Main main;
	protected Rectangle viewRect;
	protected AssetManager assetManager = new AssetManager();
	protected FrameBuffer frameBuffer;

	public AbstractSingleViewModule(BillBoardFPS_Main _main) {
		main = _main;
	}
	
	protected void loadAssetsForResize() {
		//this.viewRect = new Rectangle(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
		this.viewRect = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, true);//Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.LOGICAL_SIZE_PIXELS, Settings.LOGICAL_SIZE_PIXELS, true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

	}
	
	@Override
	public void controlledAdded(Controller controller) {

	}


	@Override
	public void controlledRemoved(Controller controller) {

	}



	@Override
	public int getCurrentViewportIdx() {
		return 0;
	}


	@Override
	public Rectangle getCurrentViewportRect() {
		return viewRect;
	}


	@Override
	public void dispose() {
		assetManager.dispose();

	}


}
