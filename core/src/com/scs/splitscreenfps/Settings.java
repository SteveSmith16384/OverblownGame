package com.scs.splitscreenfps;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.scs.splitscreenfps.game.entities.AvatarFactory;

public class Settings {

	public static final boolean RELEASE_MODE = false || new File("../../debug_mode.tmp").exists() == false;

	public static final String VERSION = "1.01";

	// Debugging Hacks
	public static boolean DEBUG_GUI_SPRITES = !RELEASE_MODE && false;
	public static boolean DRAW_PHYSICS = !RELEASE_MODE && false;
	public static final boolean AUTO_START = !RELEASE_MODE && true;
	public static final int AUTOSTART_CHARACTER = AvatarFactory.CHAR_BOOMFIST;
	public static final boolean TWO_AUTOSTART_CHARACTERS = false;
	public static final boolean DEBUG_PUNCH = !RELEASE_MODE && false;
	public static final boolean TEST_SCREEN_COORDS = !RELEASE_MODE && false;
	public static final boolean SHOW_FPS = !RELEASE_MODE && false;
	public static final boolean STRICT = !RELEASE_MODE && true;

	// Game settings
	public static final int START_HEALTH = 100;

	// Other settings
	public static final float MIN_AXIS = 0.2f; // Movement less than this is ignored
	public static final float CAM_OFFSET = -0.2f;//0.14f;
	public static final String TITLE = "Gang Beasts FPS";
	public static final int WINDOW_WIDTH_PIXELS = RELEASE_MODE ? 1024 : 1024;
	public static final int WINDOW_HEIGHT_PIXELS = (int)(WINDOW_WIDTH_PIXELS * .68);
	public static final int LOGICAL_SIZE_PIXELS = 1024;

	public static Properties prop;

	public static Random random = new Random();

	private Settings() {

	}


	public static void init() {
		// load any settings file
		prop = new Properties();
		try {
			prop.load(new FileInputStream("config.txt"));
		} catch (Exception e) {
			//e.printStackTrace();
		}

	}


	public static final void p(String s) {
		System.out.println(s);
	}


	public static final void pe(String s) {
		System.err.println(s);
	}

}
