package com.pacSON.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.pacSON.GameActivity;
import com.pacSON.base.BaseScene;
import com.pacSON.scene.GameScene;
import com.pacSON.scene.HiScoresScene;
import com.pacSON.scene.LoadingScene;
import com.pacSON.scene.GameOverScene;
import com.pacSON.scene.MainMenuScene;
import com.pacSON.scene.OptionsScene;
import com.pacSON.scene.SplashScene;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
public class SceneManager
{
	// ---------------------------------------------
	// SCENES
	// ---------------------------------------------

	private BaseScene<?> splashScene;
	private BaseScene<?> menuScene;
	private BaseScene<?> hiScoresScene;

	public BaseScene<?> getSplashScene()
	{
		return splashScene;
	}

	public void setSplashScene(BaseScene<?> splashScene)
	{
		this.splashScene = splashScene;
	}

	public BaseScene<?> getMenuScene()
	{
		return menuScene;
	}

	public void setMenuScene(BaseScene<?> menuScene)
	{
		this.menuScene = menuScene;
	}

	public BaseScene<?> getGameScene()
	{
		return gameScene;
	}

	public void setGameScene(BaseScene<?> gameScene)
	{
		this.gameScene = gameScene;
	}

	public BaseScene<?> getLoadingScene()
	{
		return loadingScene;
	}

	public void setLoadingScene(BaseScene<?> loadingScene)
	{
		this.loadingScene = loadingScene;
	}

	public BaseScene<?> getGameOverScene()
	{
		return gameOverScene;
	}

	public void setGameOverScene(BaseScene<?> gameOverScene)
	{
		this.gameOverScene = gameOverScene;
	}

	public BaseScene<?> getOptionsScene()
	{
		return optionsScene;
	}
	
	public void setHiScoresScene(BaseScene<?> hiScoresScene)
	{
		this.hiScoresScene = hiScoresScene;
	}

	public BaseScene<?> getHiScoresScene()
	{
		return hiScoresScene;
	}

	public void setOptionsScene(BaseScene<?> optionsScene)
	{
		this.optionsScene = optionsScene;
	}

	public void setCurrentSceneType(SceneType currentSceneType)
	{
		this.currentSceneType = currentSceneType;
	}

	public void setCurrentScene(BaseScene<?> currentScene)
	{
		this.currentScene = currentScene;
	}

	private BaseScene<?> gameScene;
	private BaseScene<?> loadingScene;
	private BaseScene<?> gameOverScene;
	private BaseScene<?> optionsScene;

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private BaseScene<?> currentScene;

	private Engine engine = ResourcesManager.getInstance().engine;

	public enum SceneType
	{
		SCENE_SPLASH, SCENE_MENU, SCENE_GAME, SCENE_LOADING, SCENE_GAME_OVER, SCENE_OPTIONS, SCENE_HISCORES,
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void setScene(BaseScene<?> scene)
	{
		currentScene.onSceneUnset();
		ResourcesManager.getInstance().camera.setCenterDirect(GameActivity.BACKGROUND_HEIGHT / 2, GameActivity.BACKGROUND_WIDTH / 2);
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
		scene.onSceneSet();
	}

	public void setScene(SceneType sceneType)
	{
		switch (sceneType)
		{
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		case SCENE_HISCORES:
			setScene(hiScoresScene);
			break;
		case SCENE_GAME_OVER:
			setScene(gameOverScene);
			break;
		case SCENE_OPTIONS:
			setScene(optionsScene);
		default:
			break;
		}
	}

	public void createMenuScene()
	{
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene(null);
		loadingScene = new LoadingScene(null);
		gameOverScene = new GameOverScene(null);
		// optionsScene = new OptionsScene();
		SceneManager.getInstance().setScene(menuScene);
		disposeSplashScene();
	}

	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene(null);
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	private void disposeSplashScene()
	{
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	public void loadOptionsScene(final Engine mEngine)
	{
		loadOptionsScene(mEngine, null);
	}

	public void loadOptionsScene(final Engine mEngine, final BaseScene<?> param)
	{
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.5f,
				new ITimerCallback()
				{
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadOptionsResources();
						optionsScene = new OptionsScene(param);
						setScene(optionsScene);
					}
				}));
	}

	public void loadGameScene(final Engine mEngine)
	{
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.5f,
				new ITimerCallback()
				{
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadGameResources();
						gameScene = new GameScene(true);
						gameScene.setOnSceneTouchListener(ResourcesManager.getInstance().activity);
					    ResourcesManager.getInstance().activity.mPinchZoomDetector = new PinchZoomDetector(ResourcesManager.getInstance().activity);

					     // Enable the zoom detector
					    ResourcesManager.getInstance().activity.mPinchZoomDetector.setEnabled(true);
						setScene(gameScene);
					}
				}));
	}

	public void loadGameSceneFromOptions(final Engine mEngine)
	{
		setScene(loadingScene);
		mEngine.registerUpdateHandler(new TimerHandler(0.5f,
				new ITimerCallback()
				{
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						mEngine.unregisterUpdateHandler(pTimerHandler);
						gameScene = getGameScene();
						setScene(gameScene);
					}
				}));
	}

	public void loadGameNextLevel(final Engine mEngine)
	{
		setScene(loadingScene);
		mEngine.registerUpdateHandler(new TimerHandler(0.5f,
				new ITimerCallback()
				{
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						mEngine.unregisterUpdateHandler(pTimerHandler);
						gameScene = new GameScene(false);
						setScene(gameScene);
					}
				}));
	}

	public void loadMenuScene(final Engine mEngine)
	{
		setScene(loadingScene);
		if (gameScene != null)
		{
			gameScene.disposeScene();
			ResourcesManager.getInstance().unloadGameTextures();
			ResourcesManager.getInstance().activity.mPinchZoomDetector.setEnabled(false);
		}
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback()
				{
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadMenuTextures();
						setScene(menuScene);
					}
				}));
	}
	
	public void loadHiScoresScene(final Engine mEngine)
	{
		setScene(loadingScene);
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback()
				{
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadHighScoresResources();
						hiScoresScene = new HiScoresScene(null);
						setScene(hiScoresScene);
					}
				}));
	}

	public void loadMenuSceneFromOver(final Engine mEngine)
	{
		setScene(gameOverScene);
		if (gameScene != null)
		{
			gameScene.disposeScene();
			ResourcesManager.getInstance().unloadGameTextures();
		}
		mEngine.registerUpdateHandler(new TimerHandler(3f,
				new ITimerCallback()
				{
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						mEngine.unregisterUpdateHandler(pTimerHandler);
						hiScoresScene = new HiScoresScene(null);
						setScene(hiScoresScene);
					}
				}));
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static SceneManager getInstance()
	{
		return INSTANCE;
	}

	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}

	public BaseScene<?> getCurrentScene()
	{
		return currentScene;
	}
}