package com.pacSON.scene;

import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pacSON.AI.ArtificialIntelligence;
import com.pacSON.base.BaseScene;
import com.pacSON.entity.GhostBot;
import com.pacSON.entity.LabyrinthBackground;
import com.pacSON.entity.Player;
import com.pacSON.entity.Star;
import com.pacSON.entity.Wall;
import com.pacSON.entity.collisions.PlayerCollisionHandler;
import com.pacSON.entity.collisions.effects.PlayerWithEnemyCollisionEffect;
import com.pacSON.entity.collisions.effects.PlayerWithStarCollisionEffect;
import com.pacSON.entity.modifiers.GhostBotMoveManager;
import com.pacSON.entity.modifiers.MovesReadyListener;
import com.pacSON.hud.PacHud;
import com.pacSON.labyrinth.LabyrinthManager;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;
import com.pacSON.manager.SceneManager.SceneType;
import com.pacSON.physic.gravity.AccelerometerSensor;
import com.pacSON.physic.gravity.GravitySensor;
import com.pacSON.physic.gravity.OnGravityChangedListener;

public class GameScene extends BaseScene<Boolean> // implements IOnSceneTouchListener
{
	private PhysicsWorld mPhysicsWorld;
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private int BLOCK_X_COUNT;
	private int BLOCK_Y_COUNT;

	private GravitySensor sensor;
	private LabyrinthManager lb;
	private ArtificialIntelligence ai;
	private Player player;
	private GhostBot[] ghostBots;
	private Star[] stars;
	private PacHud hud;
	private GhostBotMoveManager manager;

	private static final int BLOCK_WIDTH = 60;
	private static final int BLOCK_HEIGHT = 60;
	private static final int AREA_WIDTH = 1560;
	private static final int AREA_HEIGHT = 960;
	private static final float GRAVITY_FACTOR = 2f;
	private static final float GRAVITY_NOISE = 0.5f;
	private static final int GRAVITY_IMPULSE_SKIP = 1;
	private static final int UPDATE_RATE = 60;
	
	public GameScene(boolean reset)
	{
		super(reset);
	}

	public void createEngineOptions()
	{
		setUpCamera();
		setUpSensor();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		if (ResourcesManager.gamePaused)
		{
			if (resourcesManager.isAudioOn())
				resourcesManager.music.pause();
			sensor.stop();
			super.onManagedUpdate(0);
		} else
		{
			if (resourcesManager.isAudioOn())
				resourcesManager.music.resume();
			sensor.start();
			super.onManagedUpdate(pSecondsElapsed);
		}
	}

	@Override
	public void createScene(Boolean reset)
	{
		if (reset)
			GameManager.getInstance().reset();
		else GameManager.getInstance().levelUp();
		createEngineOptions();
		loadResources();
		onCreateScene();
	}

	protected void onCreateScene()
	{
		setUpAudio();
		final VertexBufferObjectManager vertexBufferObjectManager = resourcesManager.activity
				.getVertexBufferObjectManager();

		setUpBackground();

		spawnPlayer();
		createWalls(vertexBufferObjectManager);
		createStars(vertexBufferObjectManager);
		createBots(vertexBufferObjectManager);
		
		// LIVES HUD SETUP
		GameManager.getInstance().getPlayerStats().registerLivesChangedListener(
				hud.getLivesHud().getStatsChangedListener());

		// STARS HUD SETUP
		GameManager.getInstance().getPlayerStats().registerStarsChangedListener(
				hud.getStarsHud().getStatsChangedListener());

		camera.setChaseEntity(player.getSprite());
		camera.setHUD(hud);
	}
	
	@Override
	public void onSceneSet()
	{
		registerUpdateHandler(mPhysicsWorld);
		manager.start();
		sensor.start();
	}
	
	@Override
	public void onSceneUnset()
	{
		disposeScene();
		sensor.stop();
	}
	
	@Override
	public void onBackKeyPressed()
	{
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene()
	{
		camera.setHUD(null);
		camera.setChaseEntity(null);
		camera.setCenter(
				resourcesManager.menu_background_region.getWidth() / 2,
				resourcesManager.menu_background_region.getHeight() / 2);
		resourcesManager.music.stop();
		// TODO code responsible for disposing scene
		// removing all game scene objects.
	}

	protected void loadResources()
	{
		lb = new LabyrinthManager();
		lb.Generate_Labyrinth(BLOCK_X_COUNT, BLOCK_Y_COUNT, GameManager.MAX_STARS,
				GameManager.getInstance().getBotCount(), false, true);
		ai = new ArtificialIntelligence(lb.Return_Map(),GameManager.getInstance().getBotAI());
		// BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mPhysicsWorld = new FixedStepPhysicsWorld(UPDATE_RATE,
				new Vector2(0, 0), false);
		player = new Player();

		player.load(resourcesManager.activity);
		hud = new PacHud();
		hud.load(resourcesManager.activity);
	}

	@SuppressWarnings("deprecation")
	private void setUpCamera()
	{
		Display display = resourcesManager.activity.getWindowManager()
				.getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();
		CAMERA_HEIGHT = display.getHeight();
		int factor = CAMERA_HEIGHT * CAMERA_WIDTH;
		if (factor < 120000)
		{
			CAMERA_WIDTH *= 2;
			CAMERA_HEIGHT *= 2;
		} else if (factor < 400000)
		{
			CAMERA_WIDTH = (int) (CAMERA_WIDTH * 1.5) / BLOCK_WIDTH
					* BLOCK_WIDTH;
			CAMERA_HEIGHT = (int) (CAMERA_HEIGHT * 1.5) / BLOCK_WIDTH
					* BLOCK_WIDTH;
		}
		BLOCK_X_COUNT = AREA_WIDTH / BLOCK_WIDTH;
		BLOCK_Y_COUNT = AREA_HEIGHT / BLOCK_HEIGHT;
		// TODO ///////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!here is the question how to
		// resetup the camera proprietly
	}

	private void setUpSensor()
	{
		AccelerometerSensor sns = new AccelerometerSensor(
				resourcesManager.activity);
		sns.setNoise(GRAVITY_NOISE);
		sns.setImpulseSkip(GRAVITY_IMPULSE_SKIP);
		sensor = sns;
		sensor.addOnGravityChangedListener(new OnGravityChangedListener()
		{
			@Override
			public void onGravityChanged(float[] vector, float[] delta)
			{
				Vector2 nv = new Vector2(vector[1], vector[0]);
				mPhysicsWorld.setGravity(nv.mul(GRAVITY_FACTOR));
				hud.getGravityHud().setGravity(nv);
			}
		});
	}

	private void setUpAudio()
	{
		if (resourcesManager.isAudioOn() == true)
		{
			resourcesManager.music.setVolume(0.5f);
			resourcesManager.music.setLooping(true);
			resourcesManager.music.play();
		}
	}

	private void setUpBackground()
	{
		LabyrinthBackground lbg = new LabyrinthBackground(resourcesManager,
				AREA_WIDTH, AREA_HEIGHT);
		lbg.load(resourcesManager.activity);
		attachChild(lbg.getSprite());
	}
	
	private void spawnPlayer()
	{
		int[] p = lb.Return_Player();
		player.createPhysicBody(mPhysicsWorld);
		mPhysicsWorld.registerPhysicsConnector(player.createPhysicsConnector());
		player.setPosition(p[1] * BLOCK_WIDTH, p[0] * BLOCK_HEIGHT);
		attachChild(player.getSprite());
	}

	private void createWalls(VertexBufferObjectManager vertexBufferObjectManager)
	{
		final Rectangle ground = new Rectangle(0, AREA_HEIGHT - 2, AREA_WIDTH,
				2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, AREA_WIDTH, 2,
				vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, AREA_HEIGHT,
				vertexBufferObjectManager);
		final Rectangle right = new Rectangle(AREA_WIDTH - 2, 0, 2,
				AREA_HEIGHT, vertexBufferObjectManager);
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(1,
				0.0f, 0.0f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right,
				BodyType.StaticBody, wallFixtureDef);

		List<int[]> tab = lb.Return_Blocks();
		final Wall[] walls = new Wall[tab.size()];
		for (int i = 0; i < tab.size(); i++)
		{
			walls[i] = new Wall(resourcesManager, tab.get(i)[1] * BLOCK_WIDTH,
					tab.get(i)[0] * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
			walls[i].load(resourcesManager.activity);
			walls[i].createPhysicBody(mPhysicsWorld);
			attachChild(walls[i].getSprite());
		}
		
		attachChild(right);
		attachChild(left);
		attachChild(roof);
		attachChild(ground);
	}

	private void createStars(VertexBufferObjectManager vertexBufferObjectManager)
	{
		List<int[]> tab = lb.Return_Stars();
		stars = new Star[tab.size()];
		for (int i = 0; i < tab.size(); i++)
		{
			stars[i] = new Star(resourcesManager, tab.get(i)[1] * BLOCK_WIDTH,
					tab.get(i)[0] * BLOCK_HEIGHT);
			stars[i].load(resourcesManager.activity);
			attachChild(stars[i].getSprite());
			registerUpdateHandler(new PlayerCollisionHandler<Star>(player,
					stars[i], new PlayerWithStarCollisionEffect()));
		}
	}

	private void createBots(VertexBufferObjectManager vertexBufferObjectManager)
	{
		List<int[]> tab = lb.Return_Bots();
		ghostBots = new GhostBot[tab.size()];
		for (int i = 0; i < tab.size(); i++)
		{
			ghostBots[i] = new GhostBot(resourcesManager, tab.get(i)[1]
					* BLOCK_WIDTH, tab.get(i)[0] * BLOCK_HEIGHT);
			ghostBots[i].load(resourcesManager.activity);
			registerUpdateHandler(new PlayerCollisionHandler<GhostBot>(player,
					ghostBots[i], new PlayerWithEnemyCollisionEffect()));
			attachChild(ghostBots[i].getSprite());
		}
		manager = new GhostBotMoveManager(ghostBots, GameManager.getInstance().getBotSpeed());
		manager.setListener(new MovesReadyListener()
		{
			@Override
			public void onReady(GhostBotMoveManager manager)
			{
				GhostBot[] bots = manager.getBots();
				List<int[]> positions = ai
						.Return_New_Positions(new int[] {
								(int)player.getX()/ BLOCK_WIDTH,
								(int)player.getY() / BLOCK_HEIGHT, });
				//Log.d("pacSON", String.format("%f %f", player.getX(), player.getY()));
				for (int i = 0; i < bots.length; i++)
				{
					manager.setNewMoveModifier(bots[i], positions.get(i)[1]
							* BLOCK_WIDTH, positions.get(i)[0] * BLOCK_HEIGHT);
				}
			}
		});
	}
}