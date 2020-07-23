package com.scs.splitscreenfps.game.systems.dependencies;

import java.awt.Rectangle;

public interface IGetCurrentViewport {

	int getCurrentViewportIdx();
	
	Rectangle getCurrentViewportRect();
}
