package com.scs.splitscreenfps.pregame;

import java.awt.Rectangle;
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
import com.scs.splitscreenfps.game.components.ChangeColourComponent;
import com.scs.splitscreenfps.game.components.HasGuiSpriteComponent;
import com.scs.splitscreenfps.game.data.GameSelectionData;
import com.scs.splitscreenfps.game.entities.TextEntity;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.levels.AbstractLevel;
import com.scs.splitscreenfps.game.systems.ChangeColourSystem;
import com.scs.splitscreenfps.game.systems.DrawGuiSpritesSystem;
import com.scs.splitscreenfps.game.systems.DrawTextSystem;
import com.scs.splitscreenfps.game.systems.dependencies.IGetCurrentViewport;

public class SelectMapModule implements IModule, IGetCurrentViewport {

	private static final long READ_INPUTS_INTERVAL = 100;

	private final SpriteBatch spriteBatch;
	private BitmapFont font_small, font_large;
	private FrameBuffer frameBuffer;
	private final BillBoardFPS_Main main;
	public final List<IInputMethod> inputs;
	private GameSelectionData gameSelectionData;
	public AssetManager assetManager = new AssetManager();
	private long next_input_check_time = 0;
	private Rectangle viewRect;
	private final BasicECS ecs;
	private long earliest_input_time;
	
	private DrawGuiSpritesSystem drawGuiSpritesSystem;
	private ChangeColourSystem colChangeSystem;
	private DrawTextSystem drawTextSystem;

	// Gfx pos data
	private int spacing_y;
	private Sprite arrow;

	public SelectMapModule(BillBoardFPS_Main _main, List<IInputMethod> _inputs) {
		super();

		main = _main;
		inputs = _inputs;
		this.gameSelectionData = new GameSelectionData();//inputs.size());
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
		TextEntity text = new TextEntity(ecs, "SELECT MAP", 50, 20, -1, Color.WHITE, 0, main.font_large, true);
		text.addComponent(new ChangeColourComponent(Color.WHITE, Color.GRAY, 300));
		ecs.addEntity(text);

		loadAssetsForResize();

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
		arrow = new Sprite(tex);

		drawGuiSpritesSystem.rescaleSprites();
		drawTextSystem.rescaleText();
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
			main.next_module = new PlayersJoinGameModule(main);
			return;
		}

		if (next_input_check_time < System.currentTimeMillis()) {
			this.next_input_check_time = System.currentTimeMillis() + READ_INPUTS_INTERVAL;
			boolean all_selected = this.readInputs();
			if (all_selected) {
				main.audio.play("sfx/controlpoint.mp3");
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

		// Draw levels
		int x_pos = Settings.LOGICAL_SIZE_PIXELS/2;
		int y_pos = (int)(Gdx.graphics.getBackBufferHeight() * .6f);
		for (int i=0 ; i<=AbstractLevel.MAX_LEVEL_ID ; i++) {
			font_small.draw(spriteBatch, AbstractLevel.getName(i), x_pos, y_pos);
			y_pos -= spacing_y;
		}

		// Draw arrows
		x_pos = Settings.LOGICAL_SIZE_PIXELS/2 - 50;
		y_pos = (int)( Gdx.graphics.getBackBufferHeight() * .6f) - (gameSelectionData.level * spacing_y) - 20;
		arrow.setBounds(x_pos,  y_pos , 30, 30);
		arrow.draw(spriteBatch);

		spriteBatch.end();

		frameBuffer.end();

		//Draw buffer
		spriteBatch.begin();
		spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		spriteBatch.end();
	}


	private boolean readInputs() {
		IInputMethod input = this.inputs.get(0);
		if (input.isMenuUpPressed()) {
			main.audio.play("sfx/type2.mp3");
			this.gameSelectionData.level--;
			if (this.gameSelectionData.level < 0) {
				this.gameSelectionData.level = AbstractLevel.MAX_LEVEL_ID;
			}
		} else if (input.isMenuDownPressed()) {
			main.audio.play("sfx/type2.mp3");
			this.gameSelectionData.level++;
			if (this.gameSelectionData.level > AbstractLevel.MAX_LEVEL_ID) {
				this.gameSelectionData.level = 0;
			}
		} else if (input.isMenuSelectPressed() || input.isShootPressed()) {
			if (System.currentTimeMillis() > earliest_input_time) {
				return true;
			}
		}
		return false;
	}


	private void startGame() {
		main.next_module = new SelectHeroModule(main, inputs, this.gameSelectionData);
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
