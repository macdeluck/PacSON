package com.pacSON.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.pacSON.base.BaseScene;
import com.pacSON.manager.SceneManager;
import com.pacSON.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene<Void> implements IOnMenuItemClickListener
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	public MainMenuScene(Void onCreateParam)
	{
		super(onCreateParam);
	}

	private MenuScene menuChildScene;
	
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int HISCORES_OPTIONS = 2;
	private final int EXIT_OPTIONS = 3;
	//---------------------------------------------
	// METHODS FROM SUPERCLASS
	//---------------------------------------------

	@Override
	public void createScene(Void onCreateParams)
	{
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed()
	{
		System.exit(0);
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_MENU;
	}
	@Override
	public void disposeScene()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
		{
			case MENU_PLAY:
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case MENU_OPTIONS:
				SceneManager.getInstance().loadOptionsScene(engine);
				return true;
			case HISCORES_OPTIONS:
				SceneManager.getInstance().loadHiScoresScene(engine);
				return true;
			case EXIT_OPTIONS:
				resourcesManager.activity.finish();
				System.exit(0);
				return true;
			default:
				return false;
		}
	}
	
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------
	
	private void createBackground()
	{
		/*
		int cameraWidth = (int) resourcesManager.camera.getWidth();
		int cameraHeight = (int) resourcesManager.camera.getHeight();
		int splashWidth = (int) resourcesManager.splash_region.getWidth();
		int splashHeight= (int) resourcesManager.splash_region.getHeight();
		int offsetX = ( cameraWidth - splashWidth ) / 2;
		int offsetY = ( cameraHeight - splashHeight ) / 2;*/
		attachChild(new Sprite(0,0, resourcesManager.menu_background_region, vbom)
		{
    		@Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
    		{
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
		});
	}
	
	private void createMenuChildScene()
	{
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);
		int cameraWidth = (int) resourcesManager.camera.getWidth();
		// int cameraHeight = (int) resourcesManager.camera.getHeight();
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.2f, 1);
		final IMenuItem aboutMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(HISCORES_OPTIONS, resourcesManager.about_region, vbom), 1.2f, 1);
		final IMenuItem exitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(EXIT_OPTIONS, resourcesManager.exit_region, vbom), 1.2f, 1);
		
		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(optionsMenuItem);
		menuChildScene.addMenuItem(aboutMenuItem);
		menuChildScene.addMenuItem(exitMenuItem);
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
		playMenuItem.setPosition(cameraWidth - playMenuItem.getWidth() + 10, playMenuItem.getY() + 130);
		optionsMenuItem.setPosition(0, playMenuItem.getY());
		aboutMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + playMenuItem.getHeight() + 10);
		exitMenuItem.setPosition(0, playMenuItem.getY() + playMenuItem.getHeight() + 10);
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	}
}