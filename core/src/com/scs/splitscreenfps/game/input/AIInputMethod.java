package com.scs.splitscreenfps.game.input;

public class AIInputMethod implements IInputMethod {

	public boolean move_fwd;
	public boolean turn_left, turn_right;
	
	public AIInputMethod() {
	}

	@Override
	public float getForwards() {
		return move_fwd ? 1 : 0;
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
		return turn_left ? .5f : 0;//.21f;
	}

	@Override
	public float getLookRight() {
		return turn_right ? .5f : 0;//.21f;
	}

	@Override
	public float getLookUp() {
		return 0;
	}

	@Override
	public float getLookDown() {
		return 0;
	}


	@Override
	public boolean isAbility1Pressed() {
		return false;
	}

	@Override
	public boolean isMenuLeftPressed() {
		return false;
	}

	@Override
	public boolean isMenuRightPressed() {
		return false;
	}

	@Override
	public boolean isMenuSelectPressed() {
		return false;
	}

	@Override
	public boolean isUltimatePressed() {
		return false;
	}

	@Override
	public boolean isMenuUpPressed() {
		return false;
	}

	@Override
	public boolean isMenuDownPressed() {
		return false;
	}

	@Override
	public boolean isReloadPressed() {
		return false;
	}
}
