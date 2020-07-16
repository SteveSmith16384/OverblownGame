package com.scs.splitscreenfps;

import com.badlogic.gdx.controllers.Controller;

public interface ControllerConnectionListener {

	public void connected (Controller controller);

	public void disconnected (Controller controller);

	
}
