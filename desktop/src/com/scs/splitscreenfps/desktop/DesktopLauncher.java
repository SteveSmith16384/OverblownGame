package com.scs.splitscreenfps.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = Settings.TITLE;
		config.width = Settings.WINDOW_WIDTH_PIXELS;
		config.height = Settings.WINDOW_HEIGHT_PIXELS;
		config.vSyncEnabled = false;
		config.resizable = false;

		config.foregroundFPS = 0;
		config.backgroundFPS = 0;

		new LwjglApplication(new BillBoardFPS_Main(), config);
	}
}
