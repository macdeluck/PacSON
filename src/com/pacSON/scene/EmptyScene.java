package com.pacSON.scene;

import com.pacSON.base.BaseScene;
import com.pacSON.manager.SceneManager.SceneType;

public class EmptyScene extends BaseScene<Void>
{

	public EmptyScene(Void onCreateParam)
	{
		super(onCreateParam);
	}

	@Override
	public void createScene(Void onCreateParams)
	{
	}

	@Override
	public void onBackKeyPressed()
	{
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_EMPTY;
	}

	@Override
	public void disposeScene()
	{
	}
}
