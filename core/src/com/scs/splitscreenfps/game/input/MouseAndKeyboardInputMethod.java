package com.scs.splitscreenfps.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

public class MouseAndKeyboardInputMethod implements IInputMethod {

	public MouseAndKeyboardInputMethod() {
	}

	@Override
	public float getForwards() {
		return Gdx.input.isKeyPressed(Keys.W) ? 1 : 0;
	}

	@Override
	public float getBackwards() {
		return Gdx.input.isKeyPressed(Keys.S) ? 1 : 0;
	}

	@Override
	public float getStrafeLeft() {
		return Gdx.input.isKeyPressed(Keys.A) ? 1 : 0;
	}

	@Override
	public float getStrafeRight() {
		return Gdx.input.isKeyPressed(Keys.D) ? 1 : 0;
	}

	@Override
	public boolean isJumpPressed() {
		return Gdx.input.isKeyPressed(Keys.SPACE);
	}

	@Override
	public boolean isMouse() {
		return true;
	}

	@Override
	public float getLookLeft() {
		// Not used in this implementation
		return 0;
	}

	@Override
	public float getLookRight() {
		// Not used in this implementation
		return 0;
	}

	@Override
	public float getLookUp() {
		// Not used in this implementation
		return 0;
	}

	@Override
	public float getLookDown() {
		// Not used in this implementation
		return 0;
	}


	@Override
	public boolean isShootPressed() {
		return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
	}


	@Override
	public boolean isAbilityPressed() {
		return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
	}


	public boolean isKeyJustPressed(int key) {
		return Gdx.input.isKeyJustPressed(key);
	}


	public boolean isKeyPressed(int key) {
		return Gdx.input.isKeyPressed(key);
	}

	
	public boolean isMouseClicked() {
		return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
	}

	@Override
	public boolean isMenuLeftPressed() {
		return this.getStrafeLeft() > 0.5f;
	}

	@Override
	public boolean isMenuRightPressed() {
		return this.getStrafeRight() > 0.5f;
	}

	@Override
	public boolean isMenuSelectPressed() {
		return Gdx.input.isKeyPressed(Keys.SPACE);
	}

	@Override
	public boolean isUltimatePressed() {
		return Gdx.input.isKeyPressed(Keys.U);
	}


}
