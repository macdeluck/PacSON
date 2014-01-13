package com.pacSON.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.pacSON.base.BaseScene;
import com.pacSON.manager.SceneManager.SceneType;

public class SplashScene extends BaseScene
{
	private Sprite splash;
	
	@Override
	public void createScene(Object... onCreateParams)
	{
		splash = new Sprite(0,0, resourcesManager.splash_region, vbom)
    	{
    		@Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
    		{
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
    	};
    	
    	//splash.setScale(1.5f);
		int cameraWidth = (int) resourcesManager.camera.getWidth();
		int cameraHeight = (int) resourcesManager.camera.getHeight();
		int splashWidth = (int) resourcesManager.splash_region.getWidth();
		int splashHeight= (int) resourcesManager.splash_region.getHeight();
		int offsetX = ( cameraWidth - splashWidth ) / 2;
		int offsetY = ( cameraHeight - splashHeight ) / 2;
    	splash.setPosition(resourcesManager.camera.getXMin() + offsetX, resourcesManager.camera.getYMin() + offsetY);
    	attachChild(splash);
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene()
	{
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
	}
}