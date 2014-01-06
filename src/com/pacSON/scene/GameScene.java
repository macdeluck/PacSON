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
import com.pacSON.base.BaseScene;
import com.pacSON.entity.Bot;
import com.pacSON.entity.GhostBot;
import com.pacSON.entity.LabyrinthBackground;
import com.pacSON.entity.Player;
import com.pacSON.entity.Star;
import com.pacSON.entity.Wall;
import com.pacSON.entity.modifiers.ModifiersFactory;
import com.pacSON.hud.PacHud;
import com.pacSON.labyrinth.LabyrinthManager;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;
import com.pacSON.manager.SceneManager.SceneType;
import com.pacSON.physic.gravity.AccelerometerSensor;
import com.pacSON.physic.gravity.GravitySensor;
import com.pacSON.physic.gravity.OnGravityChangedListener;

public class GameScene extends BaseScene //implements IOnSceneTouchListener
{
	private PhysicsWorld mPhysicsWorld;
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final int BLOCK_WIDTH = 60;
	private static final int BLOCK_HEIGHT = 60;
	private static final int AREA_WIDTH = 960;
	private static final int AREA_HEIGHT = 1560;
	private int BLOCK_X_COUNT;
	private int BLOCK_Y_COUNT;
	private static final int STARS_COUNT = 5;
	private final int BOTS_COUNT = 5;
	private final int UPDATE_RATE = 60;
	private GravitySensor sensor;
	private LabyrinthManager lb;
	private Player player;
	private Bot[] bots;
	private GhostBot ghostBot;
	private Star[] stars;
	private PacHud hud;
	
	@SuppressWarnings("deprecation")
	public void createEngineOptions()
	{
		Display display = resourcesManager.activity.getWindowManager().getDefaultDisplay();
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
		
		/////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!here is the question how to resetup the camera proprietly		
		
		
		AccelerometerSensor sns = new AccelerometerSensor(resourcesManager.activity);
		sns.setNoise(0.1f);
		sensor = sns;
	}
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
	 if(ResourcesManager.gamePaused) 
	 {
		 if(ResourcesManager.isAudioOn)
			 resourcesManager.music.pause();
		 super.onManagedUpdate(0);
	 }
	 else  
	 {
		 if(ResourcesManager.isAudioOn)
			 resourcesManager.music.resume();
		 super.onManagedUpdate(pSecondsElapsed);
	 }
	}
	@Override
	public void createScene()
	{
		createEngineOptions();
		loadResources();
		onCreateScene();
	}
	private void setUpAudio()
	{
		if (ResourcesManager.isAudioOn == true){
		resourcesManager.music.setVolume(0.5f);
		resourcesManager.music.setLooping(true);
		resourcesManager.music.play();
		}
	}
	protected void onCreateScene()
	{	
		setUpAudio();
		registerUpdateHandler(mPhysicsWorld);
		final VertexBufferObjectManager vertexBufferObjectManager = resourcesManager.activity
				.getVertexBufferObjectManager();

		LabyrinthBackground lbg = new LabyrinthBackground(resourcesManager,AREA_WIDTH,AREA_HEIGHT);
		lbg.load(resourcesManager.activity);
		attachChild(lbg.getSprite());
		//setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		player.createPhysicBody(mPhysicsWorld);
		mPhysicsWorld.registerPhysicsConnector(player.createPhysicsConnector());
		int[] p = lb.Return_Player();
		player.setPosition(p[1] * BLOCK_WIDTH, p[0] * BLOCK_HEIGHT);
		
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
			walls[i] = new Wall(resourcesManager,tab.get(i)[1] * BLOCK_WIDTH, tab.get(i)[0]
					* BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
			walls[i].load(resourcesManager.activity);
			walls[i].createPhysicBody(mPhysicsWorld);
			attachChild(walls[i].getSprite());
		}
		
		tab = lb.Return_Stars();
		stars = new Star[tab.size()];
		for (int i = 0; i < tab.size(); i++)
		{
			stars[i] = new Star(resourcesManager,tab.get(i)[1] * BLOCK_WIDTH, tab.get(i)[0]
					* BLOCK_HEIGHT);
			stars[i].load(resourcesManager.activity);
			attachChild(stars[i].getSprite());
		}
		tab = lb.Return_Bots();
		bots = new Bot[tab.size()];
		for (int i = 0; i < tab.size(); i++)
		{
			bots[i] = new Bot(resourcesManager,tab.get(i)[1] * BLOCK_WIDTH, tab.get(i)[0]
					* BLOCK_HEIGHT);
			bots[i].load(resourcesManager.activity);
			bots[i].createPhysicBody(mPhysicsWorld);
			attachChild(bots[i].getSprite());
		}
		//LIVES HUD SETUP
		player.getStats().registerLivesChangedListener(hud.getLivesHud().getStatsChangedListener());
		
		//CREATE GHOST
		ghostBot = new GhostBot(resourcesManager);
		ghostBot.load(resourcesManager.activity);
		ghostBot.getSprite().registerEntityModifier(
				ModifiersFactory.getMoveAndGoBackModifier(10f, 0, 800, 100, 100));
		
		attachChild(player.getSprite());
		attachChild(right);
		attachChild(left);
		attachChild(roof);
		attachChild(ground);
		attachChild(ghostBot.getSprite());
		registerUpdateHandler(player.new PlayerWithEnemyCollisionHandler(ghostBot.getSprite()));

		sensor.addOnGravityChangedListener(new OnGravityChangedListener()
		{
			@Override
			public void onGravityChanged(float[] vector, float[] delta)
			{
				Vector2 nv = new Vector2(vector[1], vector[0]);
				mPhysicsWorld.setGravity(nv);
				hud.getGravityHud().setGravity(nv);
			}
		});
		camera.setChaseEntity(player.getSprite());
		camera.setHUD(hud);
		sensor.start();
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
		camera.setCenter(resourcesManager.menu_background_region.getWidth() / 2, resourcesManager.menu_background_region.getHeight() / 2);
		resourcesManager.music.stop();
		// TODO code responsible for disposing scene
		// removing all game scene objects.
	}
	
	protected void loadResources()
	{

		lb = new LabyrinthManager();
		lb.Generate_Labyrinth(BLOCK_X_COUNT, BLOCK_Y_COUNT, STARS_COUNT, BOTS_COUNT, false,
				true);

//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mPhysicsWorld = new FixedStepPhysicsWorld(UPDATE_RATE,
				new Vector2(0, 0), false);
		player = new Player(resourcesManager);
		
		player.load(resourcesManager.activity);
		hud = new PacHud(resourcesManager);
		hud.load(resourcesManager.activity);
	}
}