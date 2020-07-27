package com.scs.splitscreenfps.selectcharacter;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.ChangeColourComponent;
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.TextEntity;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.systems.ChangeColourSystem;
import com.scs.splitscreenfps.game.systems.DrawGuiSpritesSystem;
import com.scs.splitscreenfps.game.systems.DrawTextSystem;
import com.scs.splitscreenfps.game.systems.dependencies.IGetCurrentViewport;
import com.scs.splitscreenfps.selectgame.SelectMapModule;

public class SelectHeroModule implements IModule, IGetCurrentViewport {

	private static final long READ_INPUTS_INTERVAL = 100;

	private final SpriteBatch spriteBatch;
	private BitmapFont font_small, font_large;
	private final List<String> log = new LinkedList<String>();
	private FrameBuffer frameBuffer;
	private final BillBoardFPS_Main main;
	public List<IInputMethod> inputs;
	private GameSelectionData gameSelectionData;
	public final AssetManager assetManager = new AssetManager();
	private long earliest_input_time;
	private long next_input_check_time = 0;
	private final BasicECS ecs;
	private Rectangle viewRect;
	
	// Systems
	private DrawGuiSpritesSystem drawGuiSpritesSystem;
	private ChangeColourSystem colChangeSystem;
	private DrawTextSystem drawTextSystem;

	// Gfx pos data
	private int spacing_y;
	private Sprite[] arrows;

	public SelectHeroModule(BillBoardFPS_Main _main, List<IInputMethod> _inputs, GameSelectionData _gameSelectionData) {
		super();

		main = _main;
		inputs = _inputs;

		this.gameSelectionData = _gameSelectionData;//new GameSelectionData(inputs.size());

		for (int i=0 ; i<this.gameSelectionData.has_selected_character.length ; i++) {
			this.gameSelectionData.has_selected_character[i] = false;
			this.gameSelectionData.character[i] = 1; // First character
		}

		spriteBatch = new SpriteBatch();

		ecs = new BasicECS();
		drawGuiSpritesSystem = new DrawGuiSpritesSystem(ecs, this, spriteBatch);
		colChangeSystem = new ChangeColourSystem(ecs);
		this.drawTextSystem = new DrawTextSystem(ecs, this, this.spriteBatch);
		
		// Logo
		AbstractEntity entity = new AbstractEntity(ecs, "Logo");
		Texture weaponTex = this.getTexture("overblown_logo.png");
		Sprite sprite = new Sprite(weaponTex);
		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_FILTER, new com.badlogic.gdx.math.Rectangle(0.1f, 0.7f, .5f, .2f));
		entity.addComponent(hgsc);
		ecs.addEntity(entity);

		// Text
		TextEntity text = new TextEntity(ecs, "CHOOSE YOUR HEROES!", 50, 20, -1, Color.WHITE, 0, main.font_large, true);
		text.addComponent(new ChangeColourComponent(Color.WHITE, Color.GRAY, 300));
		ecs.addEntity(text);
		loadAssetsForResize();

		//this.appendToLog("CHOOSE A HERO!");

		spacing_y = 30;//Settings.LOGICAL_SIZE_PIXELS / (AvatarFactory.MAX_CHARS+1);

		BillBoardFPS_Main.audio.startMusic("music/battleThemeA.mp3");

		earliest_input_time = System.currentTimeMillis() + 1000;
	}


	private void loadAssetsForResize() {
		this.viewRect = new Rectangle(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());

		font_small = main.font_small;
		//this.font_med = main.font_med;
		this.font_large = main.font_large;

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.LOGICAL_SIZE_PIXELS, Settings.LOGICAL_SIZE_PIXELS, true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Texture tex = getTexture("arrow_right_white.png");
		arrows = new Sprite[this.inputs.size()];
		for (int playerIdx=0 ; playerIdx<this.inputs.size() ; playerIdx++) {
			arrows[playerIdx] = new Sprite(tex);
			arrows[playerIdx].setColor(Settings.getColourForSide(playerIdx));
		}
	}


	public Texture getTexture(String tex_filename) {
		assetManager.load(tex_filename, Texture.class);
		assetManager.finishLoading();
		Texture tex = assetManager.get(tex_filename);
		return tex;
	}


	@Override
	public void render() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.next_module = new SelectMapModule(main, inputs);
			return;
		}

		if (next_input_check_time < System.currentTimeMillis()) {
			this.next_input_check_time = System.currentTimeMillis() + READ_INPUTS_INTERVAL;
			boolean all_selected = this.readInputs();
			if (all_selected) {
				this.startGame();
				return;
			}
		}

		ecs.addAndRemoveEntities();
		colChangeSystem.process();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		frameBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		spriteBatch.begin();

		drawGuiSpritesSystem.process();
		drawTextSystem.process();

		font_small.setColor(1,  1,  1,  1);

		// Draw heroes
		int x_pos = Settings.LOGICAL_SIZE_PIXELS/2;
		int y_pos = (int)(Gdx.graphics.getBackBufferHeight() * .6f);
		for (int i=1 ; i<=AvatarFactory.MAX_CHAR_ID ; i++) {
			font_small.draw(spriteBatch, AvatarFactory.getName(i), x_pos, y_pos);
			y_pos -= spacing_y;
		}

		// Draw arrows
		for (int playerIdx=0 ; playerIdx<this.inputs.size() ; playerIdx++) {
			x_pos = Settings.LOGICAL_SIZE_PIXELS/2 - ((playerIdx+1) * 35);
			//y_pos = y_pos + (30*playerIdx);
			int y_pos_start = (int)(Gdx.graphics.getBackBufferHeight() * .6f);
			y_pos = y_pos_start - (spacing_y * (this.gameSelectionData.character[playerIdx]));
			arrows[playerIdx].setBounds(x_pos,  y_pos , 30, 30);
			arrows[playerIdx].draw(spriteBatch);
		}

		// Draw log
		int y = (int)(Gdx.graphics.getHeight()*0.4);// - 220;
		for (String s :this.log) {
			font_small.draw(spriteBatch, s, 10, y);
			y -= this.font_small.getLineHeight();
		}

		spriteBatch.end();

		frameBuffer.end();

		//Draw buffer and FPS
		spriteBatch.begin();
		spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		/*if (Settings.SHOW_FPS) {
			font.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, font.getLineHeight());
		}*/

		spriteBatch.end();
	}


	private boolean readInputs() {
		// Read inputs
		boolean all_selected = true;
		for (int playerIdx=0 ; playerIdx<this.inputs.size() ; playerIdx++) {
			IInputMethod input = this.inputs.get(playerIdx);
			if (this.gameSelectionData.has_selected_character[playerIdx] == false) {
				all_selected = false;
				if (input.isMenuUpPressed()) {
					main.audio.play("sfx/type2.mp3");
					this.gameSelectionData.character[playerIdx]--;
					if (this.gameSelectionData.character[playerIdx] < 1) {
						this.gameSelectionData.character[playerIdx] = AvatarFactory.MAX_CHAR_ID;
					}
				} else if (input.isMenuDownPressed()) {
					main.audio.play("sfx/type2.mp3");
					this.gameSelectionData.character[playerIdx]++;
					if (this.gameSelectionData.character[playerIdx] > AvatarFactory.MAX_CHAR_ID) {
						this.gameSelectionData.character[playerIdx] = 1;
					}
				} 
			}
			if (input.isMenuSelectPressed()) {
				if (System.currentTimeMillis() > earliest_input_time) {
					main.audio.play("sfx/controlpoint.mp3");
					this.gameSelectionData.has_selected_character[playerIdx] = true;
					this.appendToLog("Player " + playerIdx + " has selected " + AvatarFactory.getName(this.gameSelectionData.character[playerIdx]));
				}
			}
		}
		if (all_selected) {
			this.appendToLog("All Players have selected their hero!");
		}
		return all_selected;
	}


	private void startGame() {
		// Check all players have selected a character
		main.next_module = new Game(main, inputs, gameSelectionData);
	}


	@Override
	public void dispose() {
		this.spriteBatch.dispose();
		this.frameBuffer.dispose();
		assetManager.dispose();
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
	
}
