package com.pacSON.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;

import android.graphics.Color;
import android.graphics.Typeface;

import com.pacSON.GameActivity;
import com.pacSON.hud.elements.GravityHud;
import com.pacSON.hud.elements.LevelHud;
import com.pacSON.hud.elements.LivesHud;
import com.pacSON.hud.elements.StarHud;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;
import com.pacSON.tools.ToggleButtonSprite;
import com.pacSON.tools.ToggleButtonSprite.ToggleState;
import com.pacSON.tools.ToggleButtonSpriteInterface;

public class PacHud extends HUD
{
	private GravityHud gravityHud;
	private LivesHud livesHud;
	private StarHud starsHud;
	private LevelHud levelHud;
	
	public PacHud()
	{ 
		gravityHud = new GravityHud();
		livesHud = new LivesHud(GameManager.MAX_LIVES);
		starsHud = new StarHud();
		levelHud = new LevelHud();
	}

	public LevelHud getLevelHud()
	{
		return levelHud;
	}
	public void setLevelHud(LevelHud levelHud)
	{
		this.levelHud = levelHud;
	}
	public StarHud getStarsHud()
	{
		return starsHud;
	}
	public void setStarsHud(StarHud starHud)
	{
		this.starsHud = starHud;
	}
	
	public LivesHud getLivesHud()
	{
		return livesHud;
	}

	public void setLivesHud(LivesHud livesHud)
	{
		this.livesHud = livesHud;
	}

	public GravityHud getGravityHud()
	{
		return gravityHud;
	}

	public void setGravityHud(GravityHud gravityHud)
	{
		this.gravityHud = gravityHud;
	}
	
	public void load()
	{
		this.attachChild(addOptionsButton());
		addPauseToogleButton();
		if(ResourcesManager.getInstance().isFpsCounterEnabled())
		{
			this.attachChild(addFpsCounter());
		}
		this.gravityHud.add();
		this.gravityHud.attach(this);
		
		this.livesHud.add();
		this.livesHud.attach(this);
		
		this.starsHud.add();
		this.starsHud.attach(this);
		
		this.levelHud.add();
		this.levelHud.attach(this);
	}
	private void addPauseToogleButton()
	{
		ResourcesManager resourcesManager = ResourcesManager.getInstance();
		resourcesManager.pauseToggleButtonSprite = new ToggleButtonSprite(5, 5, resourcesManager.PauseButtonTextureRegion, resourcesManager.vbom);//vbo
		if (ResourcesManager.gamePaused == false){
			resourcesManager.pauseToggleButtonSprite.setState(ToggleState.ON);
		}
		else{
			resourcesManager.pauseToggleButtonSprite.setState(ToggleState.OFF);
		}
		attachChild(resourcesManager.pauseToggleButtonSprite);
		
		/**
		 * Set the Toggle Click Listener for the button
		 */
		resourcesManager.pauseToggleButtonSprite.setOnToggleClickListener(new ToggleButtonSpriteInterface() {
			
			@Override
			public void onOnClick(ToggleButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				ResourcesManager.gamePaused = true;
			}
			
			@Override
			public void onOffClick(ToggleButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				ResourcesManager.gamePaused = false;
			}
		});
		registerTouchArea(resourcesManager.pauseToggleButtonSprite);
	}
	private ButtonSprite addOptionsButton()
	{
		ResourcesManager resourcesManager = ResourcesManager.getInstance();
		ButtonSprite button = new ButtonSprite(resourcesManager.camera.getWidth() - resourcesManager.settings_reg.getWidth(), 5, resourcesManager.settings_reg, resourcesManager.vbom) 
		{
		       @Override
		       public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		           if(pTouchEvent.isActionDown()) 
		           {
		        	   //SceneManager.getInstance().getCurrentScene().disposeScene();//this is to change experience
		               SceneManager.getInstance().loadOptionsScene(ResourcesManager.getInstance().engine,
		            		   SceneManager.getInstance().getGameScene());
		           }
		           return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		       }
		};
		registerTouchArea(button);
		return button;
	}
	private Text addFpsCounter()
	{
		ResourcesManager resourcesManager = ResourcesManager.getInstance();
		final FPSCounter fpsCounter = new FPSCounter();
		resourcesManager.activity.getEngine().registerUpdateHandler(fpsCounter);
		final Text fpsText = new Text(resourcesManager.camera.getWidth() - resourcesManager.settings_reg.getWidth() - 100, 
				5, loadFont(resourcesManager.activity), "FPS:",
				"FPS:".length() + 4, resourcesManager.activity.getEngine().getVertexBufferObjectManager());

		final String form = "%.0f";
		this.registerUpdateHandler(new TimerHandler(1 / 20.0f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						fpsText.setText("FPS: " + String.format(form, fpsCounter.getFPS()));
					}
				}));
		return fpsText;
	}
	
	@Override
	public void setCamera(Camera camera)
	{
		super.setCamera(camera);
		this.gravityHud.setCamera(camera);
		this.livesHud.setCamera(camera);
		this.starsHud.setCamera(camera);
		this.levelHud.setCamera(camera);
	}
	
	private Font loadFont(GameActivity activity)
	{
		Font mFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(),
				256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, Color.WHITE);
		mFont.load();
		return mFont;
	}
}
