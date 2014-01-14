package com.pacSON.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
//import org.andengine.util.adt.color.Color;



import com.pacSON.base.BaseScene;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;
import com.pacSON.manager.SceneManager.SceneType;

public class GameOverScene extends BaseScene<Void>
{
	public GameOverScene(Void onCreateParam)
	{
		super(onCreateParam);
	}

	@Override
	public void createScene(Void onCreateParam)
	{
		setBackground(new Background(1f, 1f, 1f));
		int cameraWidth = (int) resourcesManager.camera.getWidth();
		int cameraHeight = (int) resourcesManager.camera.getHeight();
		int offsetX = ( cameraWidth - 350) / 2;
		int offsetY = (int) ( cameraHeight - 280)/2;
		ResourcesManager.getInstance().loadGameOverTextures();
		attachChild(new Sprite(offsetX, offsetY, resourcesManager.gameOver_region, new VertexBufferObjectManager()));
		//attachChild(new Text(resourcesManager.camera.getXMin() + offsetX, resourcesManager.camera.getYMin() + offsetY, resourcesManager.font, "YOU FOOL", vbom));
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME_OVER;
	}

	@Override
	public void disposeScene()
	{

	}

	@Override
	public void onGameOverHappened()
	{
		SceneManager.getInstance().loadMenuSceneFromOver(engine);
		
	}
}
