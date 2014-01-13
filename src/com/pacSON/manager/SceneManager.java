package com.pacSON.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.pacSON.base.BaseScene;
import com.pacSON.scene.GameScene;
import com.pacSON.scene.LoadingScene;
import com.pacSON.scene.MainMenuScene;
import com.pacSON.scene.OptionsScene;
import com.pacSON.scene.SplashScene;

public class SceneManager
{
	//---------------------------------------------
	// SCENES
	//---------------------------------------------
	
	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	private BaseScene optionsScene;
	
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	private static final SceneManager INSTANCE = new SceneManager();
	
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	
	private BaseScene currentScene;
	
	private Engine engine = ResourcesManager.getInstance().engine;
	
	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_LOADING,
		SCENE_OPTIONS,
	}
	
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------
	
	public void setScene(BaseScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
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
			case SCENE_OPTIONS:
				setScene(optionsScene);
			default:
				break;
		}
	}
	
	public void createMenuScene()
	{
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
//		optionsScene = new OptionsScene();
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
	}
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
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
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() 
		{
            @Override
			public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadOptionsResources();
        		optionsScene = new OptionsScene();
        		setScene(optionsScene);
            }
		}));
	}
	public void loadGameScene(final Engine mEngine)
	{
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() 
		{
            @Override
			public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadGameResources();
        		gameScene = new GameScene(true);
        		setScene(gameScene);
            }
		}));
	}
	
	public void loadMenuScene(final Engine mEngine)
	{
		setScene(loadingScene);
		if (gameScene != null){
			gameScene.disposeScene();
			ResourcesManager.getInstance().unloadGameTextures();
		}
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
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
	
	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------
	
	public static SceneManager getInstance()
	{
		return INSTANCE;
	}
	
	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene()
	{
		return currentScene;
	}
}