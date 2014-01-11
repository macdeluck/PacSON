package com.pacSON;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;

public class GameActivity extends BaseGameActivity
{
	private BoundCamera camera;
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final int BLOCK_WIDTH = 60;
	//private static final int BLOCK_HEIGHT = 60;
	//private static final int AREA_WIDTH = 960;
	//private static final int AREA_HEIGHT = 1560;
	//private int BLOCK_X_COUNT;
	//private int BLOCK_Y_COUNT;
	private static final int BACKGROUND_WIDTH = 480;
	private static final int BACKGROUND_HEIGHT = 800;	
	//private static final int ITEMS_COUNT = 5;
	private final int FPS = 60;
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		return new LimitedFPSEngine(pEngineOptions, FPS);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
//		camera = new BoundCamera(0, 0, 800, 480);
//		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
//		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
////		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
//		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
//		return engineOptions;
		//Display display = getWindowManager().getDefaultDisplay();
		//CAMERA_WIDTH = display.getWidth();
		//CAMERA_HEIGHT = display.getHeight();
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
		//BLOCK_X_COUNT = AREA_WIDTH / BLOCK_WIDTH;
		//BLOCK_Y_COUNT = AREA_HEIGHT / BLOCK_HEIGHT;
//		camera = new BoundCamera((AREA_WIDTH - CAMERA_WIDTH) / 2,
//				(AREA_HEIGHT - CAMERA_HEIGHT) / 2, CAMERA_WIDTH, CAMERA_HEIGHT);// cam;
		camera = new BoundCamera(0,0, BACKGROUND_HEIGHT, BACKGROUND_WIDTH);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		{
            @Override
			public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		System.exit(0);	
	}
}