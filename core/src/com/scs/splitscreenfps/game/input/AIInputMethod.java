package com.scs.splitscreenfps.game.input;

public class AIInputMethod implements IInputMethod {

	private static final float TURN_SPEED = 1f;
	private static final float LOOK_UPDOWN_SPEED = 0.3f;

	public boolean move_fwd;
	public boolean shoot;
	public boolean turn_left, turn_right;
	public boolean look_up, look_down;

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
		return shoot;
	}

	@Override
	public boolean isMouse() {
		return false;
	}

	@Override
	public float getLookLeft() {
		return turn_left ? TURN_SPEED : 0;//.21f;
	}

	@Override
	public float getLookRight() {
		return turn_right ? TURN_SPEED : 0;//.21f;
	}

	@Override
	public float getLookUp() {
		return look_up ? LOOK_UPDOWN_SPEED : 0;
	}

	@Override
	public float getLookDown() {
		return look_down ? LOOK_UPDOWN_SPEED : 0;	
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

	@Override
	public boolean isPickupPressed() {
		return false;
	}
}
