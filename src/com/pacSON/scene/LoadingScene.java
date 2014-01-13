package com.pacSON.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
//import org.andengine.util.adt.color.Color;



import com.pacSON.base.BaseScene;
import com.pacSON.manager.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{
	@Override
	public void createScene(Object... onCreateParams)
	{
		setBackground(new Background(1f, 1f, 1f));
		int cameraWidth = (int) resourcesManager.camera.getWidth();
		int cameraHeight = (int) resourcesManager.camera.getHeight();
		int offsetX = ( cameraWidth - 150) / 2;
		int offsetY = (int) (( cameraHeight - resourcesManager.font.getLineHeight()) / 2);
		attachChild(new Text(resourcesManager.camera.getXMin() + offsetX, resourcesManager.camera.getYMin() + offsetY, resourcesManager.font, "Loading...", vbom));
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene()
	{

	}
}