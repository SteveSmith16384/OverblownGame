package com.scs.splitscreenfps;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.scs.splitscreenfps.game.systems.AudioSystem;
import com.scs.splitscreenfps.pregame.IntroModule;

public class BillBoardFPS_Main extends ApplicationAdapter implements ControllerConnectionListener {

	public static final AudioSystem audio = new AudioSystem();

	private IModule current_module;
	public IModule next_module;
	private boolean fullscreen = false;

	public ControllerManager controllerManager;

	@Override
	public void create() {
		Settings.init();
		Bullet.init();

		if (Settings.RELEASE_MODE) {
			this.setFullScreen();
		}

		controllerManager = new ControllerManager(this, 4);

		current_module = new IntroModule(this);
		
	}


	@Override
	public void render() {
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			Gdx.input.setCursorCatched(true);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {// && Gdx.input.isCursorCatched()) {
			Gdx.input.setCursorCatched(false);
		}

		controllerManager.checkForControllers();

		if (next_module != null) {
			this.current_module.dispose();
			this.current_module = this.next_module;
			this.next_module = null;
		}


		if (current_module != null) {
			current_module.render();
		}

		audio.update();

		if (Gdx.input.isKeyJustPressed(Keys.F1)) {
			if (fullscreen) {
				Gdx.graphics.setWindowedMode(Settings.WINDOW_WIDTH_PIXELS, Settings.WINDOW_HEIGHT_PIXELS);
				fullscreen = false;
			} else {
				this.setFullScreen();
			}
			this.current_module.setFullScreen(fullscreen);
		} else if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			//Settings.p("F2");
			if (fullscreen) {
				Gdx.graphics.setUndecorated(true);
				Gdx.graphics.setWindowedMode(Settings.WINDOW_WIDTH_PIXELS, Settings.WINDOW_HEIGHT_PIXELS);
				fullscreen = false;
			} else {
				int w = Gdx.graphics.getDisplayMode().width;
				int h = Gdx.graphics.getDisplayMode().height;
				Gdx.graphics.setUndecorated(true);
				//Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				Gdx.graphics.setWindowedMode(w-20, h-20);
				fullscreen = true;
			}
		} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			//Gdx.app.exit();
		}


	}


	private void setFullScreen() {
		DisplayMode m = null;
		for(DisplayMode mode: Gdx.graphics.getDisplayModes()) {
			if (m == null) {
				m = mode;
			} else {
				if (m.width < mode.width) {
					m = mode;
				}
			}
		}

		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		fullscreen = true;
	}


	@Override
	public void resize(int width, int height) {
		//Settings.p("Resize() called");
		if (this.current_module != null) {
			this.current_module.resize(width, height);
		}
	}


	@Override
	public void dispose() {
		if (current_module != null) {
			current_module.dispose();
			current_module = null;
		}
		audio.dipose();
	}


	@Override
	public void connected(Controller controller) {
		this.current_module.controlledAdded(controller);
	}


	@Override
	public void disconnected(Controller controller) {
		this.current_module.controlledRemoved(controller);
	}

}

