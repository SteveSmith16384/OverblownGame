package com.scs.splitscreenfps.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.Settings;

public class IntroModule implements IModule {

	private BillBoardFPS_Main main;
	public BasicECS ecs;
	private SpriteBatch batch2d;
	private BitmapFont font_small, font_large;
	private FrameBuffer frameBuffer;
	private Sprite logo;

	public IntroModule(BillBoardFPS_Main _main) {
		super();

		main = _main;
		batch2d = new SpriteBatch();

		loadAssetsForResize();

		BillBoardFPS_Main.audio.startMusic("music/megasong.mp3");
	}


	private void loadAssetsForResize() {
		batch2d = new SpriteBatch();

		//frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.LOGICAL_SIZE_PIXELS, Settings.LOGICAL_SIZE_PIXELS, true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		// todo - move all font stuff to BillboardFPS_Main
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SHOWG.TTF"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getBackBufferHeight()/30;
		//Settings.p("Font size=" + parameter.size);
		font_small = generator.generateFont(parameter); // font size 12 pixels
		parameter.size = Gdx.graphics.getBackBufferHeight()/10;
		//Settings.p("Font size=" + parameter.size);
		font_large = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		Texture weaponTex = new Texture("overblown_logo.png");
		//Texture weaponTex = new Texture(Gdx.files.internal("colours/red.png"));		
		logo = new Sprite(weaponTex);
		logo.setBounds(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

	}


	@Override
	public void render() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Settings.AUTO_START) {
			showPlayersJoinModule();
			return;
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		frameBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		batch2d.begin();

		if (logo != null) {
			logo.draw(batch2d);
		}

		// Draw game options
		font_small.setColor(1,  1,  1,  1);
		int x = (int)(Gdx.graphics.getWidth() * 0.45f);
		int y = (int)(Gdx.graphics.getHeight()*.3f);
		font_small.draw(batch2d, "PRESS SPACE TO START!", x, y);

		batch2d.end();

		frameBuffer.end();

		//Draw buffer and FPS
		batch2d.begin();
		batch2d.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		batch2d.end();

		readKeyboard();
	}


	private void readKeyboard() {
		if (Gdx.input.isKeyJustPressed(Keys.S) || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			this.showPlayersJoinModule();
		}
	}


	private void showPlayersJoinModule() {
		main.next_module = new PlayersJoinGameModule(main);
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

}
