package com.pacSON.base;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager.SceneType;

public abstract class BaseScene<Parameter> extends Scene
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;
	
	//---------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------
	
	public BaseScene(Parameter onCreateParam)
	{
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		createScene(onCreateParam);
	}
	
	//---------------------------------------------
	// ABSTRACTION
	//---------------------------------------------
	
	public void onSceneSet() {}
	
	public void onSceneUnset() {}
	
	public abstract void createScene(Parameter onCreateParams);
	
	public abstract void onBackKeyPressed();
	
	public abstract SceneType getSceneType();
	
	public abstract void disposeScene();
	
}