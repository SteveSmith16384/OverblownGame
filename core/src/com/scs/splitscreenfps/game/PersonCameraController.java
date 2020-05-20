package com.scs.splitscreenfps.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.input.NoInputMethod;

public class PersonCameraController {

	private Camera camera;
	private Vector3 tmp = new Vector3();

	private float rotSpeedX = 220f;
	private float rotSpeedY = 140f;

	private float cursorSpeed = .15f;

	private IInputMethod input;

	public PersonCameraController(Camera cam, IInputMethod _input) {
		camera = cam;
		input = _input;
	}

	public void update() {
		if (this.input.isMouse()) {
			/*if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				Gdx.input.setCursorCatched(true);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
				Gdx.input.setCursorCatched(false);
			}*/

			if (Gdx.input.isCursorCatched()) {
				float rx = Gdx.input.getDeltaX();
				float ry = Gdx.input.getDeltaY();

				tmp.set(camera.direction).crs(camera.up).nor();
				if ((ry>0 && camera.direction.y>-0.95) || (ry<0 && camera.direction.y < 0.95)) {
					camera.rotate(tmp, -cursorSpeed * ry);
				}
				camera.rotate(Vector3.Y, -cursorSpeed  * rx);
			}
		} else if (input instanceof NoInputMethod) {
			// Look at player 1
			//PositionComponent posData = (PositionComponent)game.players[0].getComponent(PositionComponent.class);
			//camera.lookAt(posData.position);
		} else {
			//Rotation
			float dt = Gdx.graphics.getDeltaTime();

			if (input.getLookUp() > Settings.MIN_AXIS) { //if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				if(camera.direction.y < 0.95) {
					tmp.set(camera.direction).crs(camera.up).nor();
					camera.rotate(tmp, rotSpeedY * input.getLookUp() * dt);
				}

			} else if (input.getLookDown() > Settings.MIN_AXIS) { // Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				if(camera.direction.y>-0.95) {
					tmp.set(camera.direction).crs(camera.up).nor();
					camera.rotate(tmp, -rotSpeedY * input.getLookDown() * dt);
				}
			}
			if (input.getLookLeft() > Settings.MIN_AXIS) {//Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				camera.rotate(Vector3.Y, rotSpeedX * input.getLookLeft() * dt);
			} else if (input.getLookRight() > Settings.MIN_AXIS) {
				camera.rotate(Vector3.Y, -rotSpeedX * input.getLookRight() * dt);
			}
		}
		camera.update();
	}

}
