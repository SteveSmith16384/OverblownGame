package com.scs.splitscreenfps.game.input;

import com.badlogic.gdx.controllers.Controller;
import com.scs.splitscreenfps.Settings;

public class ControllerInputMethod implements IInputMethod {

	public Controller controller;

	public ControllerInputMethod(Controller _controller) {
		controller = _controller;
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
		// Code for testing buttons
		/*for (int i=0 ; i<16 ; i++) {
			if (this.controller.getButton(i)) {
				Settings.p("Pressed! " + i);
				break;
			}
		}*/
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


}
