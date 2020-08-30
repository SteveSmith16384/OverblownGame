package com.scs.splitscreenfps.game.input;

import com.badlogic.gdx.controllers.Controller;
import com.scs.splitscreenfps.Settings;

public class ControllerInputMethod implements IInputMethod {

	private static final int PICKUP_DROP_BUTTON = 2;

	public Controller controller;
	private boolean pickup_released = true;

	public ControllerInputMethod(Controller _controller) {
		controller = _controller;
	}

	@Override
	public void process() {
		if (pickup_released == false) {
			if (this.controller.getButton(PICKUP_DROP_BUTTON) == false) {
				this.pickup_released = true;
			}
		}
	}

	@Override
	public float getForwards() {
		return -controller.getAxis(1);
	}

	@Override
	public float getBackwards() {
		return controller.getAxis(1);
	}

	@Override
	public float getStrafeLeft() {
		return -controller.getAxis(0);
	}

	@Override
	public float getStrafeRight() {
		return controller.getAxis(0);
	}

	@Override
	public boolean isMouse() {
		return false;
	}

	@Override
	public float getLookLeft() {
		return -controller.getAxis(2);
	}

	@Override
	public float getLookRight() {
		return controller.getAxis(2);
	}

	@Override
	public float getLookUp() {
		return -controller.getAxis(3);
	}

	@Override
	public float getLookDown() {
		return controller.getAxis(3);
	}

	@Override
	public boolean isJumpPressed() {
		return this.controller.getButton(0);
	}


	@Override
	public boolean isShootPressed() {
		/*
		// Code for testing buttons
		for (int i=0 ; i<16 ; i++) {
			if (this.controller.getButton(i)) {
				Settings.p("Pressed! " + i);
				break;
			}
		}*/

		//Settings.p("Pressed! " + this.controller.getAxis(5));

		return this.controller.getAxis(5) > 0.5f;
	}


	@Override
	public boolean isAbility1Pressed() {
		return this.controller.getAxis(4) > 0.5f;
	}


	@Override
	public boolean isMenuLeftPressed() {
		return this.controller.getButton(13);
	}


	@Override
	public boolean isMenuRightPressed() {
		// Code for testing buttons
		/*for (int i=0 ; i<16 ; i++) {
			if (this.controller.getButton(i)) {
				Settings.p("Pressed! " + i);
				break;
			}
		}*/
		return this.controller.getButton(14);
	}


	@Override
	public boolean isMenuSelectPressed() {
		// Code for testing buttons
		/*for (int i=0 ; i<16 ; i++) {
			if (this.controller.getButton(i)) {
				Settings.p("Pressed! " + i);
				break;
			}
		}*/
		return this.controller.getButton(0); // X
	}

	@Override
	public boolean isUltimatePressed() {
		/*for (int i=0 ; i<16 ; i++) {
		if (this.controller.getButton(i)) {
			Settings.p("Pressed! " + i);
			break;
		}
	}*/
		return this.controller.getButton(3); // Triangle
	}


	@Override
	public boolean isMenuUpPressed() {
		/*for (int i=0 ; i<16 ; i++) {
			if (this.controller.getButton(i)) {
				Settings.p("Pressed! " + i);
				break;
			}
		}*/
		return this.controller.getButton(11);
	}


	@Override
	public boolean isMenuDownPressed() {
		return this.controller.getButton(12);
	}

	@Override
	public boolean isReloadPressed() {
		return this.controller.getButton(2); // Square
	}


	@Override
	public boolean isPickupDropPressed() {
		/*for (int i=0 ; i<16 ; i++) {
		if (this.controller.getButton(i)) {
			Settings.p("Pressed! " + i);
			break;
		}
	}*/
		boolean b = this.pickup_released && this.controller.getButton(PICKUP_DROP_BUTTON); // Square
		if (b) {
		pickup_released = false;
		}
		/*this.pickup_released = !b;
		if (!pickup_released) {
			Settings.p("pickup_released=" + pickup_released);
		}*/
		return b;
	}


	@Override
	public boolean isThrowPressed() {
		return this.isShootPressed();
	}

}
