package com.scs.splitscreenfps.game.input;

public interface IInputMethod {

	boolean isMouse(); // Mouse has extra features like capturing the window

	float getForwards();

	float getBackwards();

	float getStrafeLeft();

	float getStrafeRight();

	float getLookLeft();

	float getLookRight();

	boolean isMenuLeftPressed();

	boolean isMenuRightPressed();

	boolean isMenuUpPressed();

	boolean isMenuDownPressed();

	boolean isMenuSelectPressed();

	float getLookUp();

	float getLookDown();

	boolean isJumpPressed();

	boolean isShootPressed();

	boolean isReloadPressed();

	boolean isAbility1Pressed();

	boolean isUltimatePressed();

	boolean isPickupPressed();
}
