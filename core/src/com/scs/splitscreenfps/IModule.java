package com.scs.splitscreenfps;

import com.badlogic.gdx.controllers.Controller;

public interface IModule {

	void render();
	
	void dispose();
	
	void setFullScreen(boolean fullscreen);
	
	void resize(int w, int h);
	
	void controlledAdded(Controller controller);
	
	void controlledRemoved(Controller controller);

}
