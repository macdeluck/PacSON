package com.pacSON.entity;

import java.util.HashSet;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pacSON.GameActivity;
import com.pacSON.common.Vector2RotationCalculator;
import com.pacSON.common.Vector2RotationResult;
import com.pacSON.entity.modifiers.ModifiersFactory;
import com.pacSON.manager.ResourcesManager;

public class Player implements com.pacSON.entity.IEntity
{
	private Sprite mSprite;
	private Body mBody;
	private PhysicsConnector mConn;
	private ResourcesManager resourceManager;
	private PlayerStats mStats; 
	
	public static final int IMAGE_WIDTH = 48;
	public static final int IMAGE_HEIGHT = 48;
	public static final int SPRITE_WIDTH = 48;
	public static final int SPRITE_HEIGHT = 48;
	public static final int SPRITE_X = 0;
	public static final int SPRITE_Y = 0;
	public static final int RADIUS = 24;
	public static final float FIXTURE_DENSITY = 1f;
	public static final float FIXTURE_ELASTICITY = 0.0f;
	public static final float FIXTURE_FRICTION = 0.0f;
	public static final String FILE_NAME = "circle.png";
	public static final float ROTATION_DURATION = 0.2f;
	
	private float mImmortalityDuration = 3f;
	private int mImmortalityBlinks = 3;

	public Player(ResourcesManager resourcesManager)
	{
		this();
		this.resourceManager = resourcesManager;
	}
	public Player()
	{
		mStats = new PlayerStats();
	}
	
	public float getImmortalityDuration()
	{
		return mImmortalityDuration;
	}

	public void setImmortalityDuration(float mImmortalityDuration)
	{
		this.mImmortalityDuration = mImmortalityDuration;
	}

	public int getImmortalityBlinks()
	{
		return mImmortalityBlinks;
	}

	public void setImmortalityBlinks(int mImmortalityBlinks)
	{
		this.mImmortalityBlinks = mImmortalityBlinks;
	}
	
	public PlayerStats getStats()
	{
		return mStats;
	}
	
	@Override
	public Sprite getSprite()
	{
		return mSprite;
	}

	@Override
	public void setSprite(Sprite mSprite)
	{
		this.mSprite = mSprite;
	}
	

	public void load(GameActivity activity)
	{
		/*new Sprite((p % part_w) * CAMERA_WIDTH / part_w, p / part_w
				* CAMERA_HEIGHT / part_h, loadGraphics("circle.png"),
				getEngine().getVertexBufferObjectManager());*/
		mSprite = new Sprite(SPRITE_X, SPRITE_Y, resourceManager.player_reg, 
				activity.getVertexBufferObjectManager());
	}
	
	/*private TextureRegion loadGraphics(GameActivity activity, String name)
	{
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game");
		BitmapTextureAtlas bta = new BitmapTextureAtlas(activity.getTextureManager(),
				ELLIPSE_SPRITE_WIDTH, ELLIPSE_SPRITE_HEIGHT, TextureOptions.DEFAULT);

		TextureRegion reg = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bta, activity, name, ELLIPSE_SPRITE_X, ELLIPSE_SPRITE_Y);

		activity.getTextureManager().loadTexture(bta);

		return reg;
	}*/

	public Body createPhysicBody(PhysicsWorld pw)
	{
		FixtureDef def = PhysicsFactory.createFixtureDef(
				FIXTURE_DENSITY, 
				FIXTURE_ELASTICITY,
				FIXTURE_FRICTION);
		
		mBody = PhysicsFactory.createCircleBody(pw, 
				SPRITE_WIDTH/2, 
				SPRITE_HEIGHT/2,
				RADIUS, 
				BodyType.DynamicBody, def);
		// body.setAngularDamping(100f);
		return mBody;
	}

	public PhysicsConnector createPhysicsConnector()
	{
		mConn = new PhysicsConnector(mSprite, mBody, true, true)
		{
			private RotationModifier mod = null;
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				final IShape shape = this.mShape;
				final Body body = this.mBody;
	
				if(this.mUpdatePosition) 
				{
					final Vector2 position = body.getPosition();
					final float pixelToMeterRatio = this.mPixelToMeterRatio;
					shape.setPosition(position.x * pixelToMeterRatio - this.mShapeHalfBaseWidth, position.y * pixelToMeterRatio - this.mShapeHalfBaseHeight);
				}
	
				if(this.mUpdateRotation) 
				{
					Vector2 vec = mBody.getLinearVelocity();
					float l = vec.len();
					if (l!=0f)
					{
						Vector2RotationResult rot = Vector2RotationCalculator.getAngles(vec, mShape.getRotation());
						
						if (mod==null)
						{
							mod = new RotationModifier(ROTATION_DURATION, rot.fromAngle, rot.toAngle);
							mod.setAutoUnregisterWhenFinished(false);
							mShape.registerEntityModifier(mod);
						}
						else mod.reset(ROTATION_DURATION, rot.fromAngle, rot.toAngle);
					}
				}
			}
		};
		return mConn;
	}
	
	public Body getBody()
	{
		return mBody;
	}

	public void setBody(Body mBody)
	{
		this.mBody = mBody;
	}
	
	public void setPosition(float pX, float pY)
	{
	    final float widthD2 = mSprite.getWidth() / 2;
	    final float heightD2 = mSprite.getHeight() / 2;
	    final float angle = mBody.getAngle(); // keeps the body angle
	    final Vector2 v2 = Vector2Pool.obtain((pX + widthD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
	    		(pY+ heightD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	    mBody.setTransform(v2, angle);
	    Vector2Pool.recycle(v2);
	}
	
	public class PlayerStats
	{
		private HashSet<IPlayerStatsChangedListener> lcListeners;
		private HashSet<IPlayerStatsChangedListener> lsListeners;
		
		private int defaultLives = 3;
		
		private int lives;
		
		private int stars;
		
		protected boolean immortality;

		public int getStars()
		{
			return stars;
		}

		public void setStars(int stars)
		{
			this.stars = stars;
			starsChanged();
		}
		
		public PlayerStats()
		{
			super();
			lcListeners = new HashSet<IPlayerStatsChangedListener>();
			lsListeners = new HashSet<IPlayerStatsChangedListener>();
			reset();
		}

		public PlayerStats(int defaultLives)
		{
			super();
			this.defaultLives = defaultLives;
			reset();
		}

		public int getDefaultLives()
		{
			return defaultLives;
		}

		public void setDefaultLives(int defaultLives)
		{
			this.defaultLives = defaultLives;
		}
		
		public int getLives()
		{
			return lives;
		}

		public void setLives(int lives)
		{
			this.lives = lives;
			livesChanged();
		}

		public boolean registerLivesChangedListener(IPlayerStatsChangedListener listener)
		{
			return lcListeners.add(listener);
		}
		
		public boolean unregisterLivesChangedListener(IPlayerStatsChangedListener listener)
		{
			return lcListeners.remove(listener);
		}
		
		public boolean registerStarsChangedListener(IPlayerStatsChangedListener listener)
		{
			return lsListeners.add(listener);
		}
		
		public boolean unregisterStarsChangedListener(IPlayerStatsChangedListener listener)
		{
			return lsListeners.remove(listener);
		}
		
		public void reset()
		{
			lives = defaultLives;
			immortality = false;
			reseted();
		}
		
		private void reseted()
		{
			for(IPlayerStatsChangedListener l : lcListeners)
				l.statsReseted(this);
			for(IPlayerStatsChangedListener l : lsListeners)
				l.statsReseted(this);
		}
		
		private void livesChanged()
		{
			for(IPlayerStatsChangedListener l : lcListeners)
				l.statsChanged(this);
		}
		
		private void starsChanged()
		{
			for(IPlayerStatsChangedListener l : lsListeners)
				l.statsChanged(this);
		}
		
		public void setImmortality(float pDuration, int blinks)
		{
			immortality = true;
			Player.this.getSprite().registerEntityModifier(ModifiersFactory.getBlinkModifier(pDuration, blinks));
			Player.this.getSprite().registerEntityModifier(new DelayModifier(pDuration, new IEntityModifierListener() {
			    @Override
			    public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) 
			    {
			    }

			    @Override
			    public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) 
			    {
			    	immortality = false;
			    }
			}));
		}
		
		public boolean isImmortal()
		{
			return immortality;
		}
	}
}
