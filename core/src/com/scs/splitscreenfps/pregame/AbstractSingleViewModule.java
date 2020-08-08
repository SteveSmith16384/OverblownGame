package com.scs.splitscreenfps.pregame;

import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.game.systems.dependencies.IGetCurrentViewport;

public abstract class AbstractSingleViewModule implements IModule, IGetCurrentViewport {

	protected final BillBoardFPS_Main main;
	protected Rectangle viewRect;
	protected AssetManager assetManager = new AssetManager();

	public AbstractSingleViewModule(BillBoardFPS_Main _main) {
		main = _main;
	}
	
	protected void loadAssetsForResize() {
		this.viewRect = new Rectangle(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
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
