package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class Ellipse
{
	private Sprite mSprite;
	private Body mBody;
	private PhysicsConnector mConn;
	private ResourcesManager resourceManager; 
	
	public static final int ELLIPSE_SPRITE_WIDTH = 50;
	public static final int ELLIPSE_SPRITE_HEIGHT = 50;
	public static final int ELLIPSE_SPRITE_X = 0;
	public static final int ELLIPSE_SPRITE_Y = 0;
	public static final int ELLIPSE_RADIUS = 24;
	public static final float ELLIPSE_FIXTURE_DENSITY = 1f;
	public static final float ELLIPSE_FIXTURE_ELASTICITY = 0.0f;
	public static final float ELLIPSE_FIXTURE_FRICTION = 0.0f;
	public static final String ELLIPSE_FILE_NAME = "circle.png";

	public Ellipse(ResourcesManager resourcesManager)
	{
		this.resourceManager = resourcesManager;
	}
	public Ellipse(){}
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
		mSprite = new Sprite(0, 0, resourceManager.reg, 
				activity.getVertexBufferObjectManager());
	}
	
	private TextureRegion loadGraphics(GameActivity activity, String name)
	{
		BitmapTextureAtlas bta = new BitmapTextureAtlas(activity.getTextureManager(),
				ELLIPSE_SPRITE_WIDTH, ELLIPSE_SPRITE_HEIGHT, TextureOptions.DEFAULT);

		TextureRegion reg = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bta, activity, name, ELLIPSE_SPRITE_X, ELLIPSE_SPRITE_Y);

		activity.getTextureManager().loadTexture(bta);

		return reg;
	}

	public Body createPhysicBody(PhysicsWorld pw)
	{
		FixtureDef def = PhysicsFactory.createFixtureDef(
				ELLIPSE_FIXTURE_DENSITY, 
				ELLIPSE_FIXTURE_ELASTICITY,
				ELLIPSE_FIXTURE_FRICTION);
		
		mBody = PhysicsFactory.createCircleBody(pw, 
				ELLIPSE_SPRITE_WIDTH/2, 
				ELLIPSE_SPRITE_HEIGHT/2,
				ELLIPSE_RADIUS, 
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
