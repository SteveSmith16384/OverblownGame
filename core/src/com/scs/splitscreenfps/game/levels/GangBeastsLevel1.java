package com.scs.splitscreenfps.game.levels;

import java.util.Properties;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.MapData;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.data.MapSquare;
import com.scs.splitscreenfps.game.entities.Floor;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.gamemodes.IScoreSystem;
import com.scs.splitscreenfps.game.gamemodes.LastPlayerOnPointScoreSystem;

import ssmith.libgdx.GridPoint2Static;

public class GangBeastsLevel1 extends AbstractLevel {

	public static Properties prop;

	public IScoreSystem scoreSystem;

	public GangBeastsLevel1(Game _game) {
		super(_game);

		scoreSystem = new LastPlayerOnPointScoreSystem(game);

		prop = new Properties();
	}


	@Override
	public void setupAvatars(AbstractEntity player, int playerIdx) {
		player.addComponent(new PlayerData(playerIdx));

		// Todo - move to where we know size of the map
		Camera cam = game.players[playerIdx].camera;
		cam.lookAt(7,  0.4f,  7);
		cam.update();

		// Add crosshairs
		/*Texture weaponTex = new Texture(Gdx.files.internal("quantumleague/crosshairs.png"));		
		Sprite sprite = new Sprite(weaponTex);
		sprite.setPosition((Gdx.graphics.getWidth()-sprite.getWidth())/2, 0);		
		HasGuiSpriteComponent hgsc = new HasGuiSpriteComponent(sprite, HasGuiSpriteComponent.Z_CARRIED, new Rectangle(0.45f, 0.45f, 0.1f, 0.1f));
		game.players[playerIdx].addComponent(hgsc);
		 */
	}


	@Override
	public void setBackgroundColour() {
		int winning_side = this.scoreSystem.getWinningPlayer();
		if (winning_side == 0) {
			Gdx.gl.glClearColor(1f, .3f, .3f, 1);
		} else if (winning_side == 1) {
			Gdx.gl.glClearColor(.3f, .3f, 1, 1);
		} else {
			Gdx.gl.glClearColor(.6f, .6f, 1, 1);
		}
	}


	@Override
	public void load() {
		// Random crates
		for (int i=0 ; i<10 ; i++) {
			int col = 5;//NumberFunctions.rnd(1,  10);
			int row = 5;//NumberFunctions.rnd(1,  10);
			AbstractEntity crate = EntityFactory.createCrate(game.ecs, "textures/crate.png", col, i+3, row, .4f, .4f, .4f);
			game.ecs.addEntity(crate);
		}

		loadMapFromFile("map1.csv");
		
		// Add platform
		Wall wall1 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 1, 1.1f, 1, .3f, 2f, .3f, 2f);
		game.ecs.addEntity(wall1);
		Wall wall2 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 3f, 1.1f, 1f, .3f, 2f, .3f, 2f);
		game.ecs.addEntity(wall2);
		Wall wall3 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 1, 1.1f, 3, .3f, 2f, .3f, 2f);
		game.ecs.addEntity(wall3);
		Wall wall4 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 3, 1.1f, 3, .3f, 2f, .3f, 2f);
		game.ecs.addEntity(wall4);
		
		Wall top = new Wall(game.ecs, "Top", "textures/set3_example_1.png", 1.5f, 2.1f, 1.5f, 3f, .2f, 3f, 2f);
		game.ecs.addEntity(top);
	}


	private void loadMapFromFile(String file) {
		String str = Gdx.files.internal(file).readString();
		String[] str2 = str.split("\n");

		this.map_width = str2[0].split("\t").length;
		this.map_height = str2.length;

		game.mapData = new MapData(map_width, map_height);

		int row = 0;
		for (String s : str2) {
			s = s.trim();
			if (s.length() > 0 && s.startsWith("#") == false) {
				String cells[] = s.split("\t");
				for (int col=0 ; col<cells.length ; col++) {
					game.mapData.map[col][row] = new MapSquare(game.ecs);

					String cell = cells[col];
					String tokens[] = cell.split(Pattern.quote("+"));
					for (String token : tokens) {
						if (token.equals("S")) { // Start pos
							this.startPositions.add(new GridPoint2Static(col, row));
							//Floor floor = new Floor(game.ecs, "quantumleague/textures/corridor.jpg", col, row, 1, 1, false);
							//game.ecs.addEntity(floor);
						} else if (token.equals("W")) { // Wall
							//game.mapData.map[col][row].blocked = true;
							//Wall wall = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", col, 0, row, 1, 1, 1);
							//game.ecs.addEntity(wall);

							AbstractEntity crate = EntityFactory.createCrate(game.ecs, "textures/crate.png", col, 3, row, .3f, .3f, .3f);
							game.ecs.addEntity(crate);
						} else if (token.equals("C")) { // Chasm
							//game.mapData.map[col][row].blocked = true;
						} else if (token.equals("F")) { // Floor
							if ((col-1) % 4 == 0 && (row-1)  % 4 == 0) {
								Floor floor = new Floor(game.ecs, "Floor", "textures/floor006.png", col, 0, row, 4, 4);
								game.ecs.addEntity(floor);
							}
						} else if (token.equals("G")) { // Goal point
							//Floor floor = new Floor(game.ecs, "Centre", "textures/centre.png", col, .01f, row, 1, 1);
							//game.ecs.addEntity(floor);
						} else if (token.equals("B")) { // Border
							//game.mapData.map[col][row].blocked = true;
							//Wall wall = new Wall(game.ecs, "BorderWall", "textures/mjst_metal_beamwindow_diffuse.png", col, 0, row, 1, 1, 1, true);
							//game.ecs.addEntity(wall);
						} else {
							throw new RuntimeException("Unknown cell type: " + token);
						}
					}
				}
				row++;
			}
		}

	}


	public void renderUI(SpriteBatch batch2d, int viewIndex) {
		float yOff = game.font_med.getLineHeight() * 1.2f;

		game.font_med.setColor(1, 1, 1, 1);

		PlayerData playerData = (PlayerData)game.players[viewIndex].getComponent(PlayerData.class);
		game.font_med.draw(batch2d, "Health: " + (int)(playerData.health), 10, (yOff*4));
		game.font_med.draw(batch2d, this.scoreSystem.getHudText(playerData.side), 10, (yOff*5));
	}


	public static void setAvatarColour(AbstractEntity e, boolean alive) {
		// Reset player colours
		HasModelComponent hasModel = (HasModelComponent)e.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			if (alive) {
				instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.BLACK));
				instance.materials.get(i).set(ColorAttribute.createAmbient(Color.BLACK));
			} else {
				instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.WHITE));
				instance.materials.get(i).set(ColorAttribute.createAmbient(Color.WHITE));
			}
		}
	}


	@Override
	public void startGame() {
		//BillBoardFPS_Main.audio.play("sfx/AirHorn.wav");

		BillBoardFPS_Main.audio.startMusic("sfx/fight.wav");
	}

}