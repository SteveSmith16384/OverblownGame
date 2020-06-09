package com.scs.splitscreenfps.game.input;

public interface IInputMethod {
	
	boolean isMouse(); // Mouse has extra features like capturing the window

	float getForwards();

	float getBackwards();

	float getStrafeLeft();

	float getStrafeRight();
	
	float getLookLeft();

	float getLookRight();

	float getLookUp();

	float getLookDown();

	/*
	boolean isCirclePressed();
	
	boolean isCrossPressed();
	
	boolean isTrianglePressed();
	
	boolean isSquarePressed();
	
	boolean isL1ressed();

	boolean isR1Pressed();
	
	boolean isR2Pressed();
	
	boolean isKeyJustPressed(int key);

	boolean isKeyPressed(int key);
	*/
	
	boolean isJumpPressed();

	boolean isShootPressed();
}
