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
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.input.ControllerInputMethod;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.input.MouseAndKeyboardInputMethod;
import com.scs.splitscreenfps.game.input.NoInputMethod;
import com.scs.splitscreenfps.selectcharacter.GameSelectionData;
import com.scs.splitscreenfps.selectgame.SelectMapModule;

public class PlayersJoinGameModule implements IModule {

	private final BillBoardFPS_Main main;
	private final SpriteBatch spriteBatch;
	private BitmapFont font_small, font_large;
	private List<String> log = new LinkedList<String>();
	private FrameBuffer frameBuffer;
	private Sprite logo;
	private boolean keyboard_player_joined = false;

	public PlayersJoinGameModule(BillBoardFPS_Main _main) {
		super();

		main = _main;

		spriteBatch = new SpriteBatch();

		loadAssetsForResize();

		this.appendToLog("Welcome to " + Settings.TITLE);

		//this.appendToLog("v" + Settings.VERSION);
		this.appendToLog("Click mouse to play with keyboard/mouse");
		this.appendToLog("Press X to play with a controller");
		this.appendToLog("F1 to toggle full-screen");
		//this.appendToLog("Press SPACE to start once all players have joined!");

		BillBoardFPS_Main.audio.startMusic("music/megasong.mp3");
	}


	private void loadAssetsForResize() {
		//batch2d = new SpriteBatch();

		this.font_small = main.font_small;
		this.font_large = main.font_large;
		
		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.LOGICAL_SIZE_PIXELS, Settings.LOGICAL_SIZE_PIXELS, true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
	}


	@Override
	public void render() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Settings.AUTO_START || Settings.USE_MAP_EDITOR) {
			List<IInputMethod> inputs = new ArrayList<IInputMethod>();
			inputs.add(new MouseAndKeyboardInputMethod());
			if (Settings.USE_MAP_EDITOR == false) {
				if (Settings.NUM_AUTOSTART_CHARACTERS > 1) {
					Array<Controller> allControllers = main.controllerManager.getAllControllers();
					for (Controller c : allControllers) {
						inputs.add(new ControllerInputMethod(c));
					}
					while (inputs.size() < Settings.NUM_AUTOSTART_CHARACTERS) {
						inputs.add(new NoInputMethod());
					}
				}
			}
			GameSelectionData gameSelectionData = new GameSelectionData(Settings.NUM_AUTOSTART_CHARACTERS);
			gameSelectionData.level = Settings.START_LEVEL;
			for (int i=0 ; i<Settings.NUM_AUTOSTART_CHARACTERS ; i++) {
				gameSelectionData.character[i] = Settings.AUTOSTART_CHARACTER;
			}
			main.next_module = new Game(main, inputs, gameSelectionData);
			return;
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		frameBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		spriteBatch.begin();

		if (logo != null) {
			logo.draw(spriteBatch);
		}

		// Show controllers
		int y = Gdx.graphics.getHeight()-10;// - (int)this.font_large.getLineHeight()*1;
		Array<Controller> allControllers = main.controllerManager.getAllControllers();
		int idx = 1;
		for (Controller c : allControllers) {
			font_large.setColor(1,  1,  1,  1);
			font_large.draw(spriteBatch, "Controller " + idx, 10, y);

			if (main.controllerManager.isControllerInGame(c)) {
				font_large.setColor(0,  1,  0,  1);
				font_large.draw(spriteBatch, "IN GAME!", 10, y-this.font_large.getLineHeight());
			} else {
				font_large.setColor(1,  0,  0,  1);
				font_large.draw(spriteBatch, "Not in game - Press X to Join!", 10, y-this.font_large.getLineHeight());
			}
			y -= this.font_large.getLineHeight()*2;
			idx++;
		}
		if (allControllers.size == 0) {
			font_large.setColor(1, 1, 1, 1);
			font_large.draw(spriteBatch, "No Controllers Found", 10, y);
		}

		// Draw log
		font_small.setColor(1,  1,  1,  1);
		y = (int)(Gdx.graphics.getHeight()*0.3);// - 220;
		for (String s :this.log) {
			font_small.draw(spriteBatch, s, 10, y);
			y -= this.font_small.getLineHeight();
		}

		// Draw game options
		font_small.setColor(0,  1,  1,  1);
		int x = (int)(Gdx.graphics.getWidth() * 0.7f);
		y = (int)(Gdx.graphics.getHeight()*.3f);
		font_small.draw(spriteBatch, "PRESS SPACE TO START!", x, y);

		spriteBatch.end();

		frameBuffer.end();

		//Draw buffer and FPS
		spriteBatch.begin();
		spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		/*if (Settings.SHOW_FPS) {
			font.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, font.getLineHeight());
		}*/

		spriteBatch.end();

		readInputs();
	}


	private void readInputs() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && keyboard_player_joined == false) {
			this.keyboard_player_joined = true;
			this.appendToLog("Mouse/Keyboard player joined!");
			//BillBoardFPS_Main.audio.play("sfx/Plug-in.wav");
			main.audio.play("sfx/controlpoint.mp3");
		} else if (Gdx.input.isKeyJustPressed(Keys.S) || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			this.startGame();
		}
	}


	private void startGame() {
		List<IInputMethod> inputs = new ArrayList<IInputMethod>();
		if (keyboard_player_joined) {
			inputs.add(new MouseAndKeyboardInputMethod());
		}
		for (Controller c : main.controllerManager.getInGameControllers()) {
			inputs.add(new ControllerInputMethod(c));
		}
		if (inputs.size() > 0) {
			main.next_module = new SelectMapModule(main, inputs);
		} else {
			this.appendToLog("No players have joined!");
		}
	}


	@Override
	public void dispose() {
		this.spriteBatch.dispose();
		this.frameBuffer.dispose();
	}


	@Override
	public void setFullScreen(boolean fullscreen) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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


	@Override
	public void controlledAdded(Controller controller) {
		// Do nothing
	}


	@Override
	public void controlledRemoved(Controller controller) {
		// Do nothing
	}

}
