package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;

public class CastleLevel extends AbstractLevel {

	private static final float SECTION_MASS = 5f;
	private static final float FLOOR_SIZE = 15f;

	private AbstractEntity wall_test;
	
	public CastleLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(1, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, 1));

		//textures/seamlessTextures2/clover.jpg
		Wall floor = new Wall(game, "Floor", "colours/white.png", FLOOR_SIZE/2, -0.1f, FLOOR_SIZE/2, 
				FLOOR_SIZE, .2f, FLOOR_SIZE, 
				0f, true, false);
		game.ecs.addEntity(floor);

		/*wall_test = EntityFactory.createModelAndPhysicsBox(game.ecs, "CastlePart", "models/kenney/castle/wallCorner.g3db", 
				5, 2, 5, 
				0, SECTION_MASS,
				new Vector3(-0.5f, -0.55f, 0.5f), .1f);
		game.ecs.addEntity(wall_test);
*/
		AbstractEntity crate = EntityFactory.createCrate(game.ecs, "colours/red.png", 3, 3, 3, 1, 1, 1);
		game.ecs.addEntity(crate);

		FileHandle file = Gdx.files.local("maps/castle1.csv");
		String csv = file.readString();
		String rows[] = csv.split("\n");
		for (int row=0 ; row<rows.length ; row++) {
			String cols[] = rows[row].split("\t");
			for (int col=0 ; col<cols.length ; col++) {
				String tokens[] = cols[col].split(",");
				for (int t=0 ; t<tokens.length ; t++) {
					String items[] = tokens[t].split("-");
					int code = Integer.parseInt(items[0].trim());
					int angle = 0;
					if (items.length > 1) {
						angle = Integer.parseInt(items[1].trim());
					}
					//addItem(col, row, code, angle);
				}
			}
		}
	}


	private void addItem(int col, int row, int code, int angle) {
		switch (code) {
		case 0:
			// Do nothing
			break;
		case 1: // Edge
			AbstractEntity entity = EntityFactory.createModelAndPhysicsBox(game.ecs, "CastlePart", "models/kenney/castle/wall.g3db", col, 0, row, angle, SECTION_MASS, new Vector3(-0.5f, -0.55f, 0.5f), .1f);
			game.ecs.addEntity(entity);
			break;
		case 2:  // Corner
			AbstractEntity corner = EntityFactory.createModelAndPhysicsBox(game.ecs, "CastlePart", "models/kenney/castle/wallCorner.g3db", col, 0, row, angle, SECTION_MASS, new Vector3(-0.5f, -0.55f, 0.5f), .1f);
			//game.ecs.addEntity(corner);
			break;
		default:
			//throw new RuntimeException("Unknown code: " + code);
		}
	}


	@Override
	public void update() {
		//PhysicsComponent phys = (PhysicsComponent)wall_test.getComponent(PhysicsComponent.class);
		//phys.body.applyTorque(new Vector3(1, 10, 0));
		//phys.body.activate();
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {

	}



}
