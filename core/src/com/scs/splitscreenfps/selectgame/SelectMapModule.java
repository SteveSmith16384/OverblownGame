package com.scs.splitscreenfps.selectgame;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
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
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.levels.AbstractLevel;
import com.scs.splitscreenfps.pregame.PlayersJoinGameModule;
import com.scs.splitscreenfps.selectcharacter.GameSelectionData;
import com.scs.splitscreenfps.selectcharacter.SelectHeroModule;

public class SelectMapModule implements IModule {

	private static final long READ_INPUTS_INTERVAL = 100;

	private SpriteBatch batch2d;
	private BitmapFont font_small, font_large;
	private List<String> log = new LinkedList<String>();
	private FrameBuffer frameBuffer;
	private BillBoardFPS_Main main;
	public List<IInputMethod> inputs;
	private Sprite logo;
	private GameSelectionData gameSelectionData;
	public AssetManager assetManager = new AssetManager();
	private long next_input_check_time = 0;


	// Gfx pos data
	private int spacing_y;
	private Sprite arrow;

	public SelectMapModule(BillBoardFPS_Main _main, List<IInputMethod> _inputs) {
		super();

		main = _main;
		inputs = _inputs;

		this.gameSelectionData = new GameSelectionData(inputs.size());

		batch2d = new SpriteBatch();

		loadAssetsForResize();

		this.appendToLog("CHOOSE A MAP!");

		spacing_y = 30;//Settings.LOGICAL_SIZE_PIXELS / (AvatarFactory.MAX_CHARS+1);

		BillBoardFPS_Main.audio.startMusic("music/battleThemeA.mp3");
	}


	private void loadAssetsForResize() {
		batch2d = new SpriteBatch();

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.LOGICAL_SIZE_PIXELS, Settings.LOGICAL_SIZE_PIXELS, true);
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

		Texture tex = getTexture("arrow_right_white.png");
		arrow = new Sprite(tex);
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
			main.next_module = new PlayersJoinGameModule(main);//, inputs, this.gameSelectionData);
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

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		frameBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		batch2d.begin();

		if (logo != null) {
			logo.draw(batch2d);
		}

		font_small.setColor(1,  1,  1,  1);

		// Draw levels
		int x_pos = Settings.LOGICAL_SIZE_PIXELS/2;
		int y_pos = (int)(Gdx.graphics.getBackBufferHeight() * .6f);
		for (int i=0 ; i<AbstractLevel.MAX_LEVELS ; i++) {
			font_small.draw(batch2d, AbstractLevel.getName(i), x_pos, y_pos);
			y_pos -= spacing_y;
		}

		// Draw arrows
		x_pos = Settings.LOGICAL_SIZE_PIXELS/2 - 50;
		y_pos = (int)( Gdx.graphics.getBackBufferHeight() * .6f) - (gameSelectionData.level * spacing_y) - 20;
		arrow.setBounds(x_pos,  y_pos , 30, 30);
		arrow.draw(batch2d);

		// Draw log
		int y = (int)(Gdx.graphics.getHeight()*0.98);// - 220;
		for (String s :this.log) {
			font_small.draw(batch2d, s, 10, y);
			y -= this.font_small.getLineHeight();
		}

		if (Settings.TEST_SCREEN_COORDS) {
			font_small.draw(batch2d, "TL", 20, 20);
			font_small.draw(batch2d, "50", 50, 50);
			font_small.draw(batch2d, "150", 150, 150);
			font_small.draw(batch2d, "TR", Gdx.graphics.getBackBufferWidth()-20, 20);
			font_small.draw(batch2d, "BL", 10, Gdx.graphics.getBackBufferHeight()-20);
			font_small.draw(batch2d, "BR", Gdx.graphics.getBackBufferWidth()-20, Gdx.graphics.getBackBufferHeight()-20);
		}

		batch2d.end();

		frameBuffer.end();

		//Draw buffer and FPS
		batch2d.begin();
		batch2d.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		/*if (Settings.SHOW_FPS) {
			font.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, font.getLineHeight());
		}*/
		batch2d.end();
	}


	private boolean readInputs() {
		// Read inputs
		//boolean all_selected = true;
		IInputMethod input = this.inputs.get(0);
		if (input.isMenuUpPressed()) {
			main.audio.play("sfx/type2.mp3");
			this.gameSelectionData.level--;
			if (this.gameSelectionData.level < 0) {
				this.gameSelectionData.level = AbstractLevel.MAX_LEVELS-1;
			}
		} else if (input.isMenuDownPressed()) {
			main.audio.play("sfx/type2.mp3");
			this.gameSelectionData.level++;
			if (this.gameSelectionData.level >= AbstractLevel.MAX_LEVELS) {
				this.gameSelectionData.level = 0;
			}
		} else if (input.isMenuSelectPressed()) {
			return true;
		}
		return false;
	}


	private void startGame() {
		// Check all players have selected a character
		//main.next_module = new Game(main, inputs, gameSelectionData);
		main.next_module = new SelectHeroModule(main, inputs, this.gameSelectionData);
	}


	@Override
	public void dispose() {
		this.batch2d.dispose();
		this.frameBuffer.dispose();
		this.font_small.dispose();
		this.font_large.dispose();
		assetManager.dispose();
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


	@Override
	public void controlledAdded(Controller controller) {

	}


	@Override
	public void controlledRemoved(Controller controller) {

	}

}
