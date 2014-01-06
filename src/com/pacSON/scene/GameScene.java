package com.pacSON.scene;

import java.io.IOException;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pacSON.base.BaseScene;
import com.pacSON.entity.Ellipse;
import com.pacSON.hud.PacHud;
import com.pacSON.labyrinth.LabyrinthManager;
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
	private static final int ITEMS_COUNT = 5;
	private final int UPDATE_RATE = 60;
	private GravitySensor sensor;
	private LabyrinthManager lb;
	private Ellipse player;
	private PacHud hud;
	
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
	 if(resourcesManager.gamePaused) 
	 {
		 if(resourcesManager.isAudioOn)
			 resourcesManager.music.pause();
		 super.onManagedUpdate(0);
	 }
	 else  
	 {
		 if(resourcesManager.isAudioOn)
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
		if (resourcesManager.isAudioOn == true){
		resourcesManager.music.setVolume(0.5f);
		resourcesManager.music.setLooping(true);
		resourcesManager.music.play();
		}
	}
	protected void onCreateScene()
	{	
		setUpAudio();
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		registerUpdateHandler(mPhysicsWorld);

		final VertexBufferObjectManager vertexBufferObjectManager = resourcesManager.activity
				.getVertexBufferObjectManager();

		player.createPhysicBody(mPhysicsWorld);
		mPhysicsWorld.registerPhysicsConnector(player.createPhysicsConnector());

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
		
		List<int[]> tab = lb.Return_Blocks();

		final Rectangle[] walls = new Rectangle[tab.size()];
		for (int i = 0; i < tab.size(); i++)
		{
			walls[i] = new Rectangle(tab.get(i)[1] * BLOCK_WIDTH, tab.get(i)[0]
					* BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT,
					vertexBufferObjectManager);
			PhysicsFactory.createBoxBody(this.mPhysicsWorld, walls[i],
					BodyType.StaticBody, wallFixtureDef);
			walls[i].setCullingEnabled(true);
			attachChild(walls[i]);
		}
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right,
				BodyType.StaticBody, wallFixtureDef);

		int[] p = lb.Return_First_Empty();
		player.setPosition(p[1] * BLOCK_WIDTH, p[0] * BLOCK_HEIGHT);
		attachChild(player.getSprite());
		attachChild(right);
		attachChild(left);
		attachChild(roof);
		attachChild(ground);

		sensor.addOnGravityChangedListener(new OnGravityChangedListener()
		{
			@Override
			public void onGravityChanged(float[] vector, float[] delta)
			{
				Vector2 nv = new Vector2(vector[1], vector[0]);
				mPhysicsWorld.setGravity(nv);
				hud.setGravity(nv);
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
		lb.Generate_Labyrinth(BLOCK_X_COUNT, BLOCK_Y_COUNT, ITEMS_COUNT, false,
				true);

//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mPhysicsWorld = new FixedStepPhysicsWorld(UPDATE_RATE,
				new Vector2(0, 0), false);
		player = new Ellipse(resourcesManager);

		player.load(resourcesManager.activity);
		hud = new PacHud(resourcesManager);
		hud.load(resourcesManager.activity);
	}
}