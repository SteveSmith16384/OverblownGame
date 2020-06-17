package com.scs.splitscreenfps.game;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractEvent;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.IModule;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.components.AffectedByExplosionComponent;
import com.scs.splitscreenfps.game.components.ExplodeAfterTimeSystem;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.components.PlayerData;
import com.scs.splitscreenfps.game.components.PlayerMovementData;
import com.scs.splitscreenfps.game.components.PositionComponent;
import com.scs.splitscreenfps.game.components.RemoveEntityAfterTimeComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.GraphicsEntityFactory;
import com.scs.splitscreenfps.game.entities.PlayersAvatar_Person;
import com.scs.splitscreenfps.game.entities.TextEntity;
import com.scs.splitscreenfps.game.input.IInputMethod;
import com.scs.splitscreenfps.game.levels.AbstractLevel;
import com.scs.splitscreenfps.game.levels.RollingBallLevel;
import com.scs.splitscreenfps.game.systems.AnimationSystem;
import com.scs.splitscreenfps.game.systems.BulletSystem;
import com.scs.splitscreenfps.game.systems.CycleThroughModelsSystem;
import com.scs.splitscreenfps.game.systems.CycleThruDecalsSystem;
import com.scs.splitscreenfps.game.systems.DrawDecalSystem;
import com.scs.splitscreenfps.game.systems.DrawGuiSpritesSystem;
import com.scs.splitscreenfps.game.systems.DrawModelSystem;
import com.scs.splitscreenfps.game.systems.DrawTextIn3DSpaceSystem;
import com.scs.splitscreenfps.game.systems.DrawTextSystem;
import com.scs.splitscreenfps.game.systems.PhysicsSystem;
import com.scs.splitscreenfps.game.systems.PlayerInputSystem;
import com.scs.splitscreenfps.game.systems.PlayerMovementSystem;
import com.scs.splitscreenfps.game.systems.ProcessCollisionSystem;
import com.scs.splitscreenfps.game.systems.RemoveEntityAfterTimeSystem;
import com.scs.splitscreenfps.game.systems.RespawnPlayerSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;
import com.scs.splitscreenfps.pregame.PreGameScreen;

/**
 * This is the main game, where the players move about n'stuff.
 *
 */
public class Game implements IModule {

	private BillBoardFPS_Main main;
	private SpriteBatch batch2d;
	public BitmapFont font_small, font_med, font_large;
	public final ViewportData[] viewports;

	public AbstractPlayersAvatar[] players;
	public List<IInputMethod> inputs;
	public BasicECS ecs;
	public AbstractLevel currentLevel; // todo - use ILevelInterface

	private int game_stage;
	private long restartTime;
	private List<AbstractEntity> losers = new ArrayList<AbstractEntity>();

	// Specific systems 
	private DrawModelSystem drawModelSystem;
	private PhysicsSystem physicsSystem;
	private RespawnPlayerSystem respawnSystem;
	
	public int currentViewId;
	public AssetManager assetManager = new AssetManager();
	private ProcessCollisionSystem coll;
	private btDefaultCollisionConfiguration collisionConfig;
	private btSequentialImpulseConstraintSolver constraintSolver;
	private final ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3(), new Vector3());

	private DebugDrawer debugDrawer;
	private btBroadphaseInterface broadphase;
	private btCollisionDispatcher dispatcher;
	public btDiscreteDynamicsWorld dynamicsWorld;

	private long startPhysicsTime;

	public Game(BillBoardFPS_Main _main, List<IInputMethod> _inputs) {
		main = _main;
		inputs = _inputs;

		game_stage = 0;
		batch2d = new SpriteBatch();
		this.createECS();

		viewports = new ViewportData[4];
		players = new AbstractPlayersAvatar[inputs.size()];
		for (int i=0 ; i<players.length ; i++) {
			this.viewports[i] = new ViewportData(i, false, players.length);
		}

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
		if (Settings.DEBUG_PHYSICS) {
			debugDrawer = new DebugDrawer();
			debugDrawer.setDebugMode(DebugDrawer.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
			dynamicsWorld.setDebugDrawer(debugDrawer);
		}
		coll = new ProcessCollisionSystem(this);
		new MyContactListener(coll);

		//currentLevel = new GangBeastsLevel1(this);
		currentLevel = new RollingBallLevel(this);


		for (int i=0 ; i<players.length ; i++) {
			players[i] = new PlayersAvatar_Person(this, i, viewports[i], inputs.get(i), i);
			ecs.addEntity(players[i]);
		}	

		loadLevel();
		this.loadAssetsForRescale(); // Need this to load font

		for (int i=0 ; i<players.length ; i++) {
			//this.currentLevel.setupAvatars(this.players[i], i);

			// Todo - move to where we know size of the map
			Camera cam = players[i].camera;
			cam.lookAt(7,  0.4f,  7);
			cam.update();
		}

		startPhysicsTime = System.currentTimeMillis() + 500; // Don't start physics straight away.
	}


	private void loadAssetsForRescale() {
		//this.currentLevel.loadAssets();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SHOWG.TTF"));

		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getBackBufferHeight()/30;
		//Settings.p("Font size=" + parameter.size);
		font_small = generator.generateFont(parameter);

		parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getBackBufferHeight()/20;
		//Settings.p("Font size=" + parameter.size);
		font_med = generator.generateFont(parameter);

		parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getBackBufferHeight()/10;
		//Settings.p("Font size=" + parameter.size);
		font_large = generator.generateFont(parameter);

		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		DrawGuiSpritesSystem sys = (DrawGuiSpritesSystem)this.ecs.getSystem(DrawGuiSpritesSystem.class);
		sys.rescaleSprites();
	}


	public void resizeViewports(boolean full_screen) {
		for (int i=0 ; i<players.length ; i++) {
			this.viewports[i].resize(i, false, players.length);
		}
	}


	private void createECS() {
		ecs = new BasicECS();
		ecs.addSystem(new PlayerInputSystem(this));
		ecs.addSystem(new DrawDecalSystem(this, ecs));
		ecs.addSystem(new CycleThruDecalsSystem(ecs));
		ecs.addSystem(new CycleThroughModelsSystem(ecs));
		ecs.addSystem(new PlayerMovementSystem(this, ecs));
		ecs.addSystem(new RemoveEntityAfterTimeSystem(ecs));
		ecs.addSystem(new DrawTextSystem(ecs, this, batch2d));
		ecs.addSystem(new AnimationSystem(ecs));
		ecs.addSystem(new DrawGuiSpritesSystem(ecs, this, this.batch2d));
		ecs.addSystem(new ExplodeAfterTimeSystem(this, ecs));
		ecs.addSystem(new BulletSystem(ecs, this));
		ecs.addSystem(new ShootingSystem(ecs, this));
		this.drawModelSystem = new DrawModelSystem(this, ecs);
		ecs.addSystem(this.drawModelSystem);
		ecs.addSystem(new DrawTextIn3DSpaceSystem(ecs, this, batch2d));
		physicsSystem = new PhysicsSystem(this, ecs);
		ecs.addSystem(physicsSystem);
		this.respawnSystem = new RespawnPlayerSystem(ecs);
	}


	private void loadLevel() {
		currentLevel.load();

		// Set start position of players
		for (int idx=0 ; idx<players.length  ; idx++) {
			PositionComponent posData = (PositionComponent)this.players[idx].getComponent(PositionComponent.class);
			Vector3 start_pos = currentLevel.getPlayerStartPoint(idx);

			PlayerMovementData md = (PlayerMovementData)this.players[idx].getComponent(PlayerMovementData.class);
			Matrix4 mat = new Matrix4();
			mat.setTranslation(start_pos.x + 0.5f, start_pos.y, start_pos.z + 0.5f);
			md.characterController.setWorldTransform(mat);

			// Look down the z-axis
			this.viewports[idx].camera.direction.x = 0;
			this.viewports[idx].camera.direction.z = 1;
			this.viewports[idx].camera.update();
		}
	}


	@Override
	public void render() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (Settings.AUTO_START) {
				System.exit(0);
				return;
			}
			//BillBoardFPS_Main.audio.stopMusic();
			this.main.next_module = new PreGameScreen(main);
		}

		if (this.game_stage == 1) {
			if (this.restartTime < System.currentTimeMillis()) {
				this.main.next_module = new Game(main, this.inputs);
				return;
			}
		}

		this.currentLevel.update();

		this.ecs.events.clear();
		this.respawnSystem.process();
		this.ecs.getSystem(RemoveEntityAfterTimeSystem.class).process();
		this.ecs.addAndRemoveEntities();
		this.ecs.getSystem(PlayerInputSystem.class).process();
		this.ecs.getSystem(PlayerMovementSystem.class).process();
		
		if (System.currentTimeMillis() > startPhysicsTime) { // Don't start straight away
			// This must be run after the player has made inputs, but before the systems that process collisions!
			final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
			dynamicsWorld.stepSimulation(delta, 5, 1f/60f);
		}

		this.ecs.processSystem(BulletSystem.class);
		this.ecs.processSystem(ShootingSystem.class);
		this.ecs.getSystem(PhysicsSystem.class).process();
		this.ecs.getSystem(AnimationSystem.class).process();
		this.ecs.getSystem(CycleThruDecalsSystem.class).process();
		this.ecs.getSystem(CycleThroughModelsSystem.class).process();
		this.ecs.getSystem(ExplodeAfterTimeSystem.class).process();

		for (currentViewId=0 ; currentViewId<players.length ; currentViewId++) {
			ViewportData viewportData = this.viewports[currentViewId];

			Gdx.gl.glViewport(viewportData.viewPos.x, viewportData.viewPos.y, viewportData.viewPos.width, viewportData.viewPos.height);

			viewportData.frameBuffer.begin();

			this.currentLevel.setBackgroundColour();
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

			this.drawModelSystem.process(viewportData.camera);
			this.ecs.getSystem(DrawDecalSystem.class).process();

			if (Settings.DEBUG_PHYSICS) {
				debugDrawer.begin(viewportData.camera);
				dynamicsWorld.debugDrawWorld();
				debugDrawer.end();
			}

			batch2d.begin();
			this.ecs.getSystem(DrawTextIn3DSpaceSystem.class).process();
			this.ecs.getSystem(DrawTextSystem.class).process();
			this.ecs.getSystem(DrawGuiSpritesSystem.class).process();

			//font_white.draw(batch2d, "Screen " + this.currentViewId, 10, 250);
			/*if (this.game_stage == 1) {
				if (this.losers.contains(this.players[this.currentViewId])) {
					font.setColor(0, 1, 0, 1);
					font.draw(batch2d, "YOU HAVE LOST!", 10, Gdx.graphics.getBackBufferHeight()/2);
				} else {
					font.setColor(0, 1, 1, 1);
					font.draw(batch2d, "YOU HAVE WON!", 10, Gdx.graphics.getBackBufferHeight()/2);
				}
			}*/

			//currentLevel.renderUI(batch2d, currentViewId);
			float yOff = font_med.getLineHeight() * 1.2f;

			font_med.setColor(1, 1, 1, 1);

			PlayerData playerData = (PlayerData)players[currentViewId].getComponent(PlayerData.class);
			font_med.draw(batch2d, "Health: " + (int)(playerData.health), 10, (yOff*4));
			//font_med.draw(batch2d, this.scoreSystem.getHudText(playerData.side), 10, (yOff*5));

			/*if (Settings.TEST_SCREEN_COORDS) {
				font.draw(batch2d, "TL", 20, 20);
				font.draw(batch2d, "50", 50, 50);
				font.draw(batch2d, "150", 150, 150);
				font.draw(batch2d, "TR", Gdx.graphics.getBackBufferWidth()-20, 20);
				font.draw(batch2d, "BL", 10, Gdx.graphics.getBackBufferHeight()-20);
				font.draw(batch2d, "BR", Gdx.graphics.getBackBufferWidth()-20, Gdx.graphics.getBackBufferHeight()-20);
			}*/

			batch2d.end();

			viewportData.frameBuffer.end();

			//Draw buffer and FPS
			batch2d.begin();
			batch2d.draw(viewportData.frameBuffer.getColorBufferTexture(), viewportData.viewPos.x, viewportData.viewPos.y+viewportData.viewPos.height, viewportData.viewPos.width, -viewportData.viewPos.height);
			if (Settings.SHOW_FPS) {
				if (font_small != null) {
					font_small.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, font_small.getLineHeight()*2);
				}
			}
			batch2d.end();
		}
	}


	@Override
	public void resize(int w, int h) {
		this.loadAssetsForRescale();
	}


	@Override
	public void dispose() {
		for (currentViewId=0 ; currentViewId<players.length ; currentViewId++) {
			ViewportData viewportData = this.viewports[currentViewId];
			viewportData.dispose();
		}
		if (font_small != null) {
			font_small.dispose();
		}
		if (font_med != null) {
			font_med.dispose();
		}
		if (font_large != null) {
			font_large.dispose();
		}
		batch2d.dispose();
	}


	@Override
	public void setFullScreen(boolean fullscreen) {
		batch2d.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.resizeViewports(true);
	}


	public void playerHasLost(AbstractEntity avatar) {
		if (this.game_stage != 0) {
			return;
		}

		this.losers.clear();
		this.losers.add(avatar);
		loadWinLoseText();
	}


	public void playerHasWon(AbstractEntity winner) {
		if (this.game_stage != 0) {
			return;
		}

		this.losers.clear();
		for(AbstractEntity player : this.players) {
			if (player != winner) {
				this.losers.add(player);
			}
		}
		loadWinLoseText();
	}


	private void loadWinLoseText() {
		for (int i=0 ; i<this.players.length ; i++) {
			if (this.losers.contains(this.players[i])) {
				TextEntity te = new TextEntity(ecs, "YOU HAVE LOST!", Gdx.graphics.getBackBufferHeight()/2, 5, new Color(0, 0, 1, 1), i, 2);
				ecs.addEntity(te);
			} else {
				TextEntity te = new TextEntity(ecs, "YOU HAVE WON!", Gdx.graphics.getBackBufferHeight()/2, 5, new Color(0, 1, 0, 1), i, 1);
				ecs.addEntity(te);
			}
		}
		this.game_stage = 1;
		this.restartTime = System.currentTimeMillis() + 3000;
	}


	public void startSpecificLevel(int level) {
		this.game_stage = 1;
		this.restartTime = 0;

	}


	public List<AbstractEntity> getCollidedEntities(AbstractEntity e) {
		List<AbstractEntity> list = new ArrayList<AbstractEntity>();
		Iterator<AbstractEvent> it = ecs.events.iterator();
		while (it.hasNext()) {
			AbstractEvent evt = it.next();
			if (evt.getClass().equals(EventCollision.class)) {
				EventCollision coll = (EventCollision)evt;
				if (coll.entity1 == e) {
					list.add(coll.entity2);
				} else if (coll.entity2 == e) {
					list.add(coll.entity1);
				}
			}
		}
		return list;
	}


	public void playerDamaged(AbstractEntity player, PlayerData playerHitData, float amt) {
		if (playerHitData.health <= 0) {
			// Already dead
			return;
		}
		
		Settings.p("Player " + playerHitData.playerIdx + " damaged " + amt);
		playerHitData.health -= amt;//bullet.settings.damage;

		AbstractEntity redfilter = GraphicsEntityFactory.createRedFilter(ecs, playerHitData.playerIdx);
		redfilter.addComponent(new RemoveEntityAfterTimeComponent(1));
		ecs.addEntity(redfilter);

		if (playerHitData.health <= 0) {
			playerDied(player, playerHitData);
		}

	}


	public void playerDied(AbstractEntity player, PlayerData playerData) {
		this.respawnSystem.addEntity(player, this.currentLevel.getPlayerStartPoint(playerData.playerIdx));
		// todo - show "died"
	}


	public void explosion(final Vector3 pos, float range, float force, float width_height) {
		//Settings.p("Explosion at " + pos);

		AbstractEntity expl = GraphicsEntityFactory.createNormalExplosion(ecs, pos, width_height);
		ecs.addEntity(expl);

		// Temp vars
		Matrix4 mat = new Matrix4();
		Vector3 vec = new Vector3();

		Iterator<AbstractEntity> it = this.physicsSystem.getEntityIterator();
		while (it.hasNext()) {
			AbstractEntity e = it.next();
			if (e.isMarkedForRemoval()) {
				continue;
			}
			AffectedByExplosionComponent aff = (AffectedByExplosionComponent)e.getComponent(AffectedByExplosionComponent.class);
			if (aff != null) {
				PhysicsComponent pc = (PhysicsComponent)e.getComponent(PhysicsComponent.class);
				pc.body.getWorldTransform(mat);
				mat.getTranslation(vec);
				float distance = vec.dst(pos);
				if (distance <= range) {
					// Todo - check the explosion can see the target?
					pc.body.activate();
					pc.body.applyCentralImpulse(vec.cpy().sub(pos).nor().scl(force));
					//Settings.p("Moving " + e.name);
				}
			}
		}
	}


	//private final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);
	public btCollisionObject rayTestByDir(Vector3 ray_from, Vector3 dir, float range) {
		Vector3 ray_to = new Vector3(ray_from).mulAdd(dir, range);

		callback.setCollisionObject(null);
		callback.setClosestHitFraction(1f);

		Vector3 v1 = new Vector3();
		callback.getRayFromWorld(v1);
		v1.set(ray_from);

		Vector3 v2 = new Vector3();
		callback.getRayToWorld(v2);
		v2.set(ray_to);

		this.dynamicsWorld.rayTest(ray_from, ray_to, callback);
		if (callback.hasHit()) {
			return callback.getCollisionObject();
		}

		return null;
	}


	class MyContactListener extends ContactListener {

		private ProcessCollisionSystem coll;

		public MyContactListener(ProcessCollisionSystem _coll) {
			coll = _coll;
		}

		@Override
		public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
			return true;
		}

		@Override
		public void onContactStarted (btCollisionObject ob1, btCollisionObject ob2) {
			try {
				//Settings.p(ob1.userData + " collided with " + ob2.userData);
				AbstractEntity e1 = (AbstractEntity)ob1.userData;
				AbstractEntity e2 = (AbstractEntity)ob2.userData;

				coll.processCollision(e1, e2);
			} catch (Exception ex) {
				Settings.pe(ex.getMessage());
			}
		}

		@Override
		public void onContactProcessed (int userValue0, int userValue1) {
			//Settings.p("Here");
		}

	}

}

