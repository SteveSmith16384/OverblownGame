package com.scs.splitscreenfps.pregame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.ControllerManager;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.input.ControllerInputMethod;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.input.MouseAndKeyboardInputMethod;
import com.scs.splitscreenfps.game.input.NoInputMethod;

public class PreGameScreen implements IModule {

	private SpriteBatch batch2d;
	private BitmapFont font_small, font_large;
	private ControllerManager controllerManager = new ControllerManager(null, 3);
	private List<String> log = new LinkedList<String>();
	private FrameBuffer frameBuffer;
	private BillBoardFPS_Main main;
	private Sprite logo;
	private boolean keyboard_player_joined = false;

	public PreGameScreen(BillBoardFPS_Main _main) {
		super();

		main = _main;

		batch2d = new SpriteBatch();

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 1024, true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		loadAssetsForResize();

		this.appendToLog("Welcome to " + Settings.TITLE);

		this.appendToLog("v" + Settings.VERSION);
		/*if (Settings.RELEASE_MODE == false) {
			this.appendToLog("WARNING! Game in debug mode!");
		}*/
		//this.appendToLog("Looking for controllers...");
		this.appendToLog("Click mouse to play with keyboard/mouse");
		this.appendToLog("Press X to play with controller");
		this.appendToLog("F1 to toggle full-screen");
		this.appendToLog("To SPACE to start once all players have joined!");

		BillBoardFPS_Main.audio.startMusic("sfx/battleThemeA.mp3");
	}


	private void loadAssetsForResize() {
		batch2d = new SpriteBatch();

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 1024, true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SHOWG.TTF"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getBackBufferHeight()/30;
		//Settings.p("Font size=" + parameter.size);
		font_small = generator.generateFont(parameter); // font size 12 pixels
		parameter.size = Gdx.graphics.getBackBufferHeight()/10;
		//Settings.p("Font size=" + parameter.size);
		font_large = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		Texture logoTex = new Texture(Gdx.files.internal("logo1.png"));		
		logo = new Sprite(logoTex);
		logo.setBounds(0,  0 , Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
		logo.setColor(0.4f, 0.4f, 0.4f, 1);

	}


	@Override
	public void render() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			System.exit(0);
		}

		controllerManager.checkForControllers();

		if (Settings.AUTO_START) {
			List<IInputMethod> inputs = new ArrayList<IInputMethod>();
			inputs.add(new MouseAndKeyboardInputMethod());
			Array<Controller> allControllers = this.controllerManager.getAllControllers();
			for (Controller c : allControllers) {
				inputs.add(new ControllerInputMethod(c));
			}
			if (inputs.size() == 1) {
				inputs.add(new NoInputMethod());
			}
			main.next_module = new Game(main, inputs);
			return;
		}

		controllerManager.checkForControllers();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		frameBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		batch2d.begin();

		if (logo != null) {
			logo.draw(batch2d);
		}

		// Show controllers
		int y = Gdx.graphics.getHeight()-10;// - (int)this.font_large.getLineHeight()*1;
		Array<Controller> allControllers = this.controllerManager.getAllControllers();
		int idx = 1;
		for (Controller c : allControllers) {
			font_large.setColor(1,  1,  1,  1);
			font_large.draw(batch2d, "Controller " + idx, 10, y);

			if (this.controllerManager.isControllerInGame(c)) {
				font_large.setColor(0,  1,  0,  1);
				font_large.draw(batch2d, "IN GAME!", 10, y-this.font_large.getLineHeight());
			} else {
				font_large.setColor(1,  0,  0,  1);
				font_large.draw(batch2d, "Not in game - Press X to Join!", 10, y-this.font_large.getLineHeight());
			}
			y -= this.font_large.getLineHeight()*2;
			idx++;
		}
		if (allControllers.size == 0) {
			font_large.setColor(1,  1,  1,  1);
			font_large.draw(batch2d, "No Controllers Found", 10, y);
		}

		// Draw log
		font_small.setColor(1,  1,  1,  1);
		y = (int)(Gdx.graphics.getHeight()*0.4);// - 220;
		for (String s :this.log) {
			font_small.draw(batch2d, s, 10, y);
			y -= this.font_small.getLineHeight();
		}

		// Draw game options
		font_small.setColor(0,  1,  1,  1);
		int x = (int)(Gdx.graphics.getWidth() * 0.7f);
		y = (int)(Gdx.graphics.getHeight()*.3f);
		font_small.draw(batch2d, "PRESS SPACE TO START!", x, y);

		batch2d.end();

		frameBuffer.end();

		//Draw buffer and FPS
		batch2d.begin();
		batch2d.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		/*if (Settings.SHOW_FPS) {
			font.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, font.getLineHeight());
		}*/

		batch2d.end();

		readKeyboard();
	}


	private void readKeyboard() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && keyboard_player_joined == false) {
			this.keyboard_player_joined = true;
			this.appendToLog("Mouse/Keyboard player joined!");
			BillBoardFPS_Main.audio.play("sfx/Plug-in.wav");
		} else if (Gdx.input.isKeyJustPressed(Keys.S) || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			this.startGame();
		}
	}


	private void startGame() {
		List<IInputMethod> inputs = new ArrayList<IInputMethod>();
		if (keyboard_player_joined) {
			inputs.add(new MouseAndKeyboardInputMethod());
		}
		for (Controller c : controllerManager.getInGameControllers()) {
			if (inputs.size() <= 2) { // Max 2 players for now
				inputs.add(new ControllerInputMethod(c));
			}
		}
		if (inputs.size() > 0) {
			main.next_module = new Game(main, inputs);
		} else {
			this.appendToLog("No players have joined!");
		}
	}


	@Override
	public void dispose() {
		this.batch2d.dispose();
		this.frameBuffer.dispose();
		this.font_small.dispose();
		this.font_large.dispose();
	}


	@Override
	public void setFullScreen(boolean fullscreen) {
		batch2d.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}


	@Override
	public void resize(int w, int h) {
		this.loadAssetsForResize();
	}


	private void appendToLog(String s) {
		this.log.add(s);
		while (log.size() > 6) {
			log.remove(0);
		}
	}

}
