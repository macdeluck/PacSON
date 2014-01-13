package com.pacSON.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
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
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Typeface;

import com.badlogic.gdx.math.Vector2;
import com.pacSON.GameActivity;
import com.pacSON.common.PlayerStats;
import com.pacSON.common.Vector2RotationCalculator;
import com.pacSON.common.Vector2RotationResult;
import com.pacSON.entity.IPlayerStatsChangedListener;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;
import com.pacSON.tools.ToggleButtonSprite;
import com.pacSON.tools.ToggleButtonSprite.ToggleState;
import com.pacSON.tools.ToggleButtonSpriteInterface;

public class PacHud extends HUD
{
	private ResourcesManager resourcesManager;
	private GravityHud gravityHud;
	private LivesHud livesHud;
	private StarHud starsHud;
	
	public StarHud getStarsHud()
	{
		return starsHud;
	}
	public void setStarsHud(StarHud starHud)
	{
		this.starsHud = starHud;
	}
	public PacHud(ResourcesManager resourcesManager)
	{
		this();
		this.resourcesManager = resourcesManager;
	}
	public PacHud()
	{ 
		gravityHud = new GravityHud();
		livesHud = new LivesHud(GameManager.MAX_LIVES);
		starsHud = new StarHud();
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
	
	public void load(GameActivity activity)
	{
		this.attachChild(addOptionsButton());
		addPauseToogleButton();
		if(resourcesManager.isFpsCounterEnabled())
		{
			this.attachChild(addFpsCounter(activity));
		}
		this.gravityHud.add(activity);
		this.gravityHud.attach(this);
		
		this.livesHud.add(activity);
		this.livesHud.attach(this);
		
		this.starsHud.add(activity);
		this.starsHud.attach(this);
	}
	private void addPauseToogleButton()
	{
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
		ButtonSprite button = new ButtonSprite(resourcesManager.camera.getWidth() - resourcesManager.settings_reg.getWidth(), 5, resourcesManager.settings_reg, resourcesManager.vbom) {
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
		final Text fpsText = new Text(resourcesManager.camera.getWidth() - resourcesManager.settings_reg.getWidth() - 100, 5, loadFont(activity), "FPS:",
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
		this.gravityHud.setCamera(camera);
		this.livesHud.setCamera(camera);
		this.starsHud.setCamera(camera);
	}
	
	private Font loadFont(GameActivity activity)
	{
		Font mFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(),
				256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		mFont.load();
		return mFont;
	}

	public class GravityHud
	{
		private Sprite hudBase;
		private Sprite gravityArrow;
		private Text gravityValue;
		private RotationModifier arrowRotationModifier;
		
		public static final String HUD_BASE_NAME = "hud.png";
		public static final int HUD_BASE_WIDTH = 100;
		public static final int HUD_BASE_HEIGHT = 100;
		public static final String HUD_GRAV_ARROW_NAME = "arrow_sm.png";
		
		public static final int HUD_GRAV_ARROW_WIDTH = 60;
		public static final int HUD_GRAV_ARROW_HEIGHT = 46;
		public static final int HUD_GRAV_ARROW_MARGINX = 20;
		public static final int HUD_GRAV_ARROW_MARGINY = 27;
		public static final float HUD_GRAV_ARROW_ROTATION_DURATION = 0.5f;
		public static final int HUD_GRAV_VAL_MARGINX = 55;
		public static final int HUD_GRAV_VAL_MARGINY = 63;
		public static final int HUD_GRAV_VAL_MAXCHARS = 5;
		
		public void attach(IEntity parent)
		{
			parent.attachChild(hudBase);
			parent.attachChild(gravityArrow);
			parent.attachChild(gravityValue);
		}
		
		public Sprite getHudBase()
		{
			return hudBase;
		}

		public Sprite getGravityArrow()
		{
			return gravityArrow;
		}

		public Text getGravityValue()
		{
			return gravityValue;
		}
		
		public void add(GameActivity activity)
		{
			hudBase = new Sprite(2, 2, loadTextureRegion(activity, HUD_BASE_NAME, HUD_BASE_WIDTH, HUD_BASE_HEIGHT),
						activity.getEngine().getVertexBufferObjectManager());
			gravityArrow = new Sprite(2, 2, loadTextureRegion(activity, HUD_GRAV_ARROW_NAME, HUD_GRAV_ARROW_WIDTH, HUD_GRAV_ARROW_HEIGHT),
					activity.getEngine().getVertexBufferObjectManager());
			gravityValue = new Text(2, 2, loadFont(activity),"0.0", HUD_GRAV_VAL_MAXCHARS, activity.getEngine().getVertexBufferObjectManager());
					
		}
		
		public void setCamera(Camera camera)
		{
			hudBase.setPosition(camera.getWidth()-hudBase.getWidth(),
					camera.getHeight()-hudBase.getHeight());
			gravityArrow.setPosition(camera.getWidth()-hudBase.getWidth()+HUD_GRAV_ARROW_MARGINX,
					camera.getHeight()-hudBase.getHeight()+HUD_GRAV_ARROW_MARGINY);
			gravityValue.setPosition(camera.getWidth()-hudBase.getWidth()+HUD_GRAV_VAL_MARGINX,
					camera.getHeight()-hudBase.getHeight()+HUD_GRAV_VAL_MARGINY);
		}
		
		public void setGravity(Vector2 vec)
		{
			float l = vec.len();
			if (l!=0f)
			{
				Vector2RotationResult rot = Vector2RotationCalculator.getAngles(vec, gravityArrow.getRotation());
				
				if (arrowRotationModifier==null)
				{
					arrowRotationModifier = new RotationModifier(HUD_GRAV_ARROW_ROTATION_DURATION, rot.fromAngle, rot.toAngle);
					arrowRotationModifier.setAutoUnregisterWhenFinished(false);
					gravityArrow.registerEntityModifier(arrowRotationModifier);
				}
				else arrowRotationModifier.reset(HUD_GRAV_ARROW_ROTATION_DURATION, rot.fromAngle, rot.toAngle);
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

	public class LivesHud
	{
		Sprite[] lives;
		private IPlayerStatsChangedListener listener = null;
		private ITextureRegion textureRegion;
		private VertexBufferObjectManager buffer;
		
		public static final String HUD_LIVES_NAME = "heart_sm.png";
		public static final int HUD_LIVES_WIDTH = 40;
		public static final int HUD_LIVES_HEIGHT= 40;
		public static final int HUD_LIVES_MARGIN = 10;

		public LivesHud(int livesCount)
		{
			super();
			lives = new Sprite[livesCount];
			listener = new IPlayerStatsChangedListener()
			{
				@Override
				public void statsChanged(PlayerStats stats)
				{
					if (stats.getLives()>-1 && stats.getLives()<lives.length)
						lives[stats.getLives()].setAlpha(0f);
				}

				@Override
				public void statsReseted(PlayerStats stats)
				{
					for(Sprite l : lives)
					{
						l.detachSelf();
						l.dispose();
					}
					lives = new Sprite[stats.getLives()];
					for (int i=0; i<lives.length; i++)
					{
						lives[i] = new Sprite(2, 2, textureRegion, buffer);
					}
				}
			};
		}

		public void setCamera(Camera camera)
		{
			for (int i=0; i<lives.length; i++)
				lives[i].setPosition(HUD_LIVES_MARGIN + (HUD_LIVES_WIDTH+HUD_LIVES_MARGIN)*i,
						camera.getHeight() - HUD_LIVES_MARGIN - HUD_LIVES_HEIGHT);
		}

		public void add(GameActivity activity)
		{
			loadTextureRegion(activity, HUD_LIVES_NAME, HUD_LIVES_WIDTH, HUD_LIVES_HEIGHT);
			buffer = activity.getEngine().getVertexBufferObjectManager();
			for (int i=0; i<lives.length; i++)
			{
				lives[i] = new Sprite(0, 0, textureRegion, buffer);
			}
		}

		public void attach(IEntity parent)
		{
			for (int i=0; i<lives.length; i++)
				parent.attachChild(lives[i]);
		}
		
		private void loadTextureRegion(GameActivity activity, String name, int width, int height)
		{
			BitmapTextureAtlas bta = new BitmapTextureAtlas(activity.getTextureManager(), width, height, TextureOptions.DEFAULT);
			
			TextureRegion reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, activity, name, 0, 0);
			
			activity.getTextureManager().loadTexture(bta);
			
			textureRegion = reg;
		}
		
		public IPlayerStatsChangedListener getStatsChangedListener()
		{
			return listener;
		}
	}

	public class StarHud
	{
		Sprite star;
		Text text;
		
		private IPlayerStatsChangedListener listener = null;
		
		public static final String HUD_STAR_NAME = "star.png";
		public static final int HUD_STAR_WIDTH = 50;
		public static final int HUD_STAR_HEIGHT= 50;
		public static final int HUD_STAR_MARGIN = 10;

		public static final int HUD_TEXT_MAXCHARS = 5;

		public StarHud()
		{
			super();
			listener = new IPlayerStatsChangedListener()
			{
				@Override
				public void statsChanged(PlayerStats stats)
				{
					text.setText(Integer.toString(stats.getStars()));
				}

				@Override
				public void statsReseted(PlayerStats stats)
				{
					text.setText("0");
				}
			};
		}

		public void setCamera(Camera camera)
		{
			star.setPosition(HUD_STAR_MARGIN,
					camera.getHeight() - HUD_STAR_MARGIN - LivesHud.HUD_LIVES_HEIGHT - HUD_STAR_HEIGHT);
			text.setPosition(HUD_STAR_MARGIN + HUD_STAR_WIDTH + HUD_STAR_MARGIN,
					camera.getHeight() - HUD_STAR_MARGIN - LivesHud.HUD_LIVES_HEIGHT - HUD_STAR_HEIGHT);
		}

		public void add(GameActivity activity)
		{
			star = new Sprite(0,0, resourcesManager.star_reg, 
					activity.getEngine().getVertexBufferObjectManager());
			text = new Text(2, 2, resourcesManager.font ,"0", 
					HUD_TEXT_MAXCHARS, activity.getEngine().getVertexBufferObjectManager());
		}

		public void attach(IEntity parent)
		{
			parent.attachChild(star);
			parent.attachChild(text);
		}
		
		public IPlayerStatsChangedListener getStatsChangedListener()
		{
			return listener;
		}
	}
}
