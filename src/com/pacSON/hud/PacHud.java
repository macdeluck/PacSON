package com.pacSON.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import android.graphics.Typeface;

import com.badlogic.gdx.math.Vector2;
import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;
import com.pacSON.tools.ToggleButtonSprite;
import com.pacSON.tools.ToggleButtonSpriteInterface;
import com.pacSON.tools.ToggleButtonSprite.ToggleState;

public class PacHud extends HUD
{
	private Sprite hudBase;
	private Sprite gravityArrow;
	private Text gravityValue;
	private final String HUD_BASE_NAME = "hud.png";
	private final int HUD_BASE_WIDTH = 100;
	private final int HUD_BASE_HEIGHT = 100;
	private final String HUD_GRAV_ARROW_NAME = "arrow_sm.png";
	
	private final int HUD_GRAV_ARROW_WIDTH = 60;
	private final int HUD_GRAV_ARROW_HEIGHT = 46;
	private final int HUD_GRAV_ARROW_MARGINX = 20;
	private final int HUD_GRAV_ARROW_MARGINY = 27;
	private final float HUD_GRAV_ARROW_ROTATION_DURATION = 0.5f;
	
	private final int HUD_GRAV_VAL_MARGINX = 55;
	private final int HUD_GRAV_VAL_MARGINY = 63;
	private final int HUD_GRAV_VAL_MAXCHARS = 5;
	
	private RotationModifier arrowRotationModifier;
	private ResourcesManager resourcesManager;
	public PacHud(ResourcesManager resourcesManager){
		this.resourcesManager = resourcesManager;
	}
	public PacHud(){}
	public void load(GameActivity activity)
	{
		hudBase = new Sprite(2, 2, loadTextureRegion(activity, HUD_BASE_NAME, HUD_BASE_WIDTH, HUD_BASE_HEIGHT),
					activity.getEngine().getVertexBufferObjectManager());
		gravityArrow = new Sprite(2, 2, loadTextureRegion(activity, HUD_GRAV_ARROW_NAME, HUD_GRAV_ARROW_WIDTH, HUD_GRAV_ARROW_HEIGHT),
				activity.getEngine().getVertexBufferObjectManager());
		gravityValue = new Text(2, 2, loadFont(activity),"0.0", HUD_GRAV_VAL_MAXCHARS, activity.getEngine().getVertexBufferObjectManager());
		
		this.attachChild(addOptionsButton());
		addPauseToogleButton();
		if(resourcesManager.FPS_COUNTER_ENABLE){
		this.attachChild(addFpsCounter(activity));
		}
		this.attachChild(hudBase);
		this.attachChild(gravityArrow);
		this.attachChild(gravityValue);
	}
	private void addPauseToogleButton()
	{
		resourcesManager.pauseToggleButtonSprite = new ToggleButtonSprite(5, 5, resourcesManager.PauseButtonTextureRegion, resourcesManager.vbom);//vbo
		if (resourcesManager.gamePaused == false){
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
				resourcesManager.gamePaused = true;
			}
			
			@Override
			public void onOffClick(ToggleButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				resourcesManager.gamePaused = false;
			}
		});
		registerTouchArea(resourcesManager.pauseToggleButtonSprite);
	}
	private ButtonSprite addOptionsButton()
	{
		ButtonSprite button = new ButtonSprite(resourcesManager.camera.getWidth() - resourcesManager.settings_region.getWidth(), 5, resourcesManager.settings_region, resourcesManager.vbom) {
		       @Override
		       public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		           if(pTouchEvent.isActionDown()) {
		        	   SceneManager.getInstance().getCurrentScene().disposeScene();//this is to change experience
		               SceneManager.getInstance().loadOptionsScene(resourcesManager.engine);
		           }
		           return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		       }
		};
		registerTouchArea(button);
		return button;
	}
	private Text addFpsCounter(GameActivity activity){
		final FPSCounter fpsCounter = new FPSCounter();
		activity.getEngine().registerUpdateHandler(fpsCounter);
		final Text fpsText = new Text(resourcesManager.camera.getWidth() - resourcesManager.settings_region.getWidth() - 100, 5, loadFont(activity), "FPS:",
				"FPS:".length() + 4, activity.getEngine().getVertexBufferObjectManager());

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
		hudBase.setPosition(this.getCamera().getWidth()-hudBase.getWidth(),
							this.getCamera().getHeight()-hudBase.getHeight());
		gravityArrow.setPosition(this.getCamera().getWidth()-hudBase.getWidth()+HUD_GRAV_ARROW_MARGINX,
				this.getCamera().getHeight()-hudBase.getHeight()+HUD_GRAV_ARROW_MARGINY);
		gravityValue.setPosition(this.getCamera().getWidth()-hudBase.getWidth()+HUD_GRAV_VAL_MARGINX,
				this.getCamera().getHeight()-hudBase.getHeight()+HUD_GRAV_VAL_MARGINY);
	}
	
	public void setGravity(Vector2 vec)
	{
		float l = vec.len();
		if (l!=0f)
		{
			float cos = vec.x/l;
			float fromAngle = gravityArrow.getRotation();
			float toAngle = -(float)(Math.acos(cos)*180/Math.PI);
			if (vec.y>0)
				toAngle = -toAngle;
			
			// make sure angles are between 0 and 360
			if (fromAngle<0)
				fromAngle+=360;
			else if (fromAngle>360)
				fromAngle-=360;
			
			if (toAngle<0)
				toAngle+=360;
			else if (toAngle>360)
				toAngle-=360;
			
			// if rotation change is greater than 180 change rotation direction
			if (toAngle-fromAngle>180)
				toAngle-=360;
			else if (toAngle-fromAngle>180)
				toAngle+=360;
			
			if (arrowRotationModifier==null)
			{
				arrowRotationModifier = new RotationModifier(HUD_GRAV_ARROW_ROTATION_DURATION, fromAngle, toAngle);
				arrowRotationModifier.setAutoUnregisterWhenFinished(false);
				gravityArrow.registerEntityModifier(arrowRotationModifier);
			}
			else arrowRotationModifier.reset(HUD_GRAV_ARROW_ROTATION_DURATION, fromAngle, toAngle);
			//gravityArrow.setRotation(angle);
		}
		String form = "%1.1f";
		if (l>=10)
			form = "%1.0f";
		gravityValue.setText(String.format(form, l));
	}
	
	private TextureRegion loadTextureRegion(GameActivity activity, String name, int width, int height)
	{
		BitmapTextureAtlas bta = new BitmapTextureAtlas(activity.getTextureManager(), width, height, TextureOptions.DEFAULT);
		
		TextureRegion reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, activity, name, 0, 0);
		
		activity.getTextureManager().loadTexture(bta);
		
		return reg;
	}
	
	private Font loadFont(GameActivity activity)
	{
		Font mFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(),
				256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		mFont.load();
		return mFont;
	}
}
