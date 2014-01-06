package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class Player
{
	private Sprite mSprite;
	private Body mBody;
	private PhysicsConnector mConn;
	private ResourcesManager resourceManager; 
	
	public static final int IMAGE_WIDTH = 50;
	public static final int IMAGE_HEIGHT = 50;
	public static final int SPRITE_WIDTH = 50;
	public static final int SPRITE_HEIGHT = 50;
	public static final int SPRITE_X = 0;
	public static final int SPRITE_Y = 0;
	public static final int RADIUS = 24;
	public static final float FIXTURE_DENSITY = 1f;
	public static final float FIXTURE_ELASTICITY = 0.0f;
	public static final float FIXTURE_FRICTION = 0.0f;
	public static final String FILE_NAME = "circle.png";

	public Player(ResourcesManager resourcesManager)
	{
		this.resourceManager = resourcesManager;
	}
	public Player(){}
	public Sprite getSprite()
	{
		return mSprite;
	}

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
		mConn = new PhysicsConnector(mSprite, mBody, true, false);
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
}
