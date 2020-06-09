package com.scs.splitscreenfps.game.input;

public class NoInputMethod implements IInputMethod {

	public NoInputMethod() {
	}

	@Override
	public float getForwards() {
		return 0;
	}

	@Override
	public float getBackwards() {
		return 0;
	}

	@Override
	public float getStrafeLeft() {
		return 0;
	}

	@Override
	public float getStrafeRight() {
		return 0;
	}

	@Override
	public boolean isJumpPressed() {
		return false;
	}

	@Override
	public boolean isShootPressed() {
		return false;
	}

	@Override
	public boolean isMouse() {
		return false;
	}

	@Override
	public float getLookLeft() {
		return 0;
	}

	@Override
	public float getLookRight() {
		return 0;//.1f;
	}

	@Override
	public float getLookUp() {
		return 0;
	}

	@Override
	public float getLookDown() {
		return 0;
	}
/*
	@Override
	public boolean isCrossPressed() {
		return false;
	}

	@Override
	public boolean isL1ressed() {
		return false;
	}

	@Override
	public boolean isR1Pressed() {
		return false;
	}

	@Override
	public boolean isTrianglePressed() {
		return false;
	}


	@Override
	public boolean isKeyJustPressed(int key) {
		return false;
	}

	
	@Override
	public boolean isSquarePressed() {
		return false;
	}

	@Override
	public boolean isKeyPressed(int key) {
		return false;
	}

	@Override
	public boolean isR2Pressed() {
		return false;
	}
	
	*/
}
