package com.pacSON.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

import com.pacSON.base.BaseScene;
import com.pacSON.manager.SceneManager;
import com.pacSON.manager.SceneManager.SceneType;
import com.pacSON.tools.ToggleButtonSprite;
import com.pacSON.tools.ToggleButtonSprite.ToggleState;
import com.pacSON.tools.ToggleButtonSpriteInterface;

public class OptionsScene extends BaseScene{
	@Override
	public void createScene()
	{
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		int cameraWidth = (int) resourcesManager.camera.getWidth();
		int cameraHeight = (int) resourcesManager.camera.getHeight();
		int offsetX = ( cameraWidth - 150) / 2;
		int offsetY = (int) (( cameraHeight - resourcesManager.font.getLineHeight()) / 2);
		attachChild(new Text(resourcesManager.camera.getXMin() + offsetX, 50, resourcesManager.font, "Options", vbom));
		createAudioToogleButton((int)resourcesManager.camera.getXMin()+ offsetX, 150);
		createFPSToogleButton((int)resourcesManager.camera.getXMin()+ offsetX, 300);
	}
	private void createAudioToogleButton(int pX, int pY)
	{
		attachChild(new Text(pX - 100, pY + 25, resourcesManager.font, "Audio", vbom));
		resourcesManager.soundToggleButtonSprite = new ToggleButtonSprite(pX + 200, pY, resourcesManager.soundButtonTextureRegion, resourcesManager.vbom);//vbo
		if (resourcesManager.isAudioOn == true){
			resourcesManager.soundToggleButtonSprite.setState(ToggleState.ON);
		}
		else{
			resourcesManager.soundToggleButtonSprite.setState(ToggleState.OFF);
		}
		attachChild(resourcesManager.soundToggleButtonSprite);
		
		/**
		 * Set the Toggle Click Listener for the button
		 */
		resourcesManager.soundToggleButtonSprite.setOnToggleClickListener(new ToggleButtonSpriteInterface() {
			
			@Override
			public void onOnClick(ToggleButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				resourcesManager.isAudioOn = false;
				if (resourcesManager.music != null)
				{
					if (resourcesManager.isAudioOn == true)
						resourcesManager.music.play();
					else
						resourcesManager.music.stop();
				}
			}
			
			@Override
			public void onOffClick(ToggleButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				resourcesManager.isAudioOn = true;
				if (resourcesManager.music != null)
				{
					if (resourcesManager.isAudioOn == true)
						resourcesManager.music.play();
					else
						resourcesManager.music.stop();
				}
			}
		});
		registerTouchArea(resourcesManager.soundToggleButtonSprite);
	}
	private void createFPSToogleButton(int pX, int pY)
	{
		attachChild(new Text(pX - 150, pY + 25, resourcesManager.font, "Fps counter", vbom));
		resourcesManager.tickAndCrossToggleButtonSprite = new ToggleButtonSprite(pX + 200, pY, resourcesManager.tickAndCrossButtonTextureRegion, resourcesManager.vbom);//vbo
		if (resourcesManager.FPS_COUNTER_ENABLE == true){
			resourcesManager.tickAndCrossToggleButtonSprite.setState(ToggleState.ON);
		}
		else{
			resourcesManager.tickAndCrossToggleButtonSprite.setState(ToggleState.OFF);
		}
		attachChild(resourcesManager.tickAndCrossToggleButtonSprite);
		
		/**
		 * Set the Toggle Click Listener for the button
		 */
		resourcesManager.tickAndCrossToggleButtonSprite.setOnToggleClickListener(new ToggleButtonSpriteInterface() {
			
			@Override
			public void onOnClick(ToggleButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				resourcesManager.FPS_COUNTER_ENABLE = false;
			}
			
			@Override
			public void onOffClick(ToggleButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				resourcesManager.FPS_COUNTER_ENABLE = true;
			}
		});
		registerTouchArea(resourcesManager.tickAndCrossToggleButtonSprite);
	}
	@Override
	public void onBackKeyPressed()
	{
		SceneManager.getInstance().loadMenuScene(engine);
		return;
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_OPTIONS;
	}

	@Override
	public void disposeScene()
	{

	}
}
