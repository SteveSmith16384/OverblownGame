package com.scs.splitscreenfps;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.levels.AbstractLevel;

public class Settings {

	public static final boolean RELEASE_MODE = false || new File("../../debug_mode.tmp").exists() == false;

	public static final String VERSION = "1.12";

	// Debugging Hacks
	public static final boolean AUTO_START = !RELEASE_MODE && false;
	public static final int AUTOSTART_LEVEL = AbstractLevel.LEVEL_AI_TEST;
	public static final int NUM_AUTOSTART_CHARACTERS = 1;
	public static final int AUTOSTART_CHARACTER = AvatarFactory.CHAR_RUBBISHRODENT;
	public static boolean USE_MAP_EDITOR = !RELEASE_MODE && false;
	
	public static final boolean DISABLE_SHADOWS = !RELEASE_MODE && false;
	public static final boolean TEST_3RD_PERSON = !RELEASE_MODE && false;
	public static final boolean DEBUG_ULTIMATES = !RELEASE_MODE && false;
	public static boolean DRAW_PHYSICS = !RELEASE_MODE && false;
	public static boolean DEBUG_GUI_SPRITES = !RELEASE_MODE && false;
	public static final boolean DEBUG_PUNCH = !RELEASE_MODE && false;
	public static final boolean TEST_SCREEN_COORDS = !RELEASE_MODE && false;
	public static final boolean SHOW_FPS = !RELEASE_MODE && true;
	public static final boolean DISABLE_POST_EFFECTS = !RELEASE_MODE && false;
	public static final boolean STRICT = !RELEASE_MODE && true;

	// Other settings
	public static final float MIN_AXIS = 0.1f;//0.2f; // Movement less than this is ignored
	public static final float CAM_OFFSET = -0.05f;//-0.2f;//0.14f;
	public static final String TITLE = "Overblown";
	public static final int WINDOW_WIDTH_PIXELS = RELEASE_MODE ? 1024 : 1024;
	public static final int WINDOW_HEIGHT_PIXELS = (int)(WINDOW_WIDTH_PIXELS * .68);
	public static final int LOGICAL_SIZE_PIXELS = 1024;
	public static float DEF_MOVE_SPEED = 15;//20;//25;//1.5f;

	public static Properties prop;

	public static Random random = new Random();

	private Settings() {

	}


	public static Color getColourForSide( int idx) {
		switch (idx) {
		case 0:
			return Color.GREEN;
		case 1:
			return Color.YELLOW;
		case 2:
			return Color.RED;
		case 3:
			return Color.MAGENTA;
		default:
			throw new RuntimeException("Unknown side: " + idx);
		}
	}


	public static final void p(String s) {
		if (Settings.RELEASE_MODE == false) {
			System.out.println(s);
		}
	}


	public static final void pe(String s) {
		System.err.println(s);
	}

}
