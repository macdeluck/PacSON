package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class Bot implements IPacSONEntity
{
	private Sprite mSprite;
	private Body mBody;
	private ResourcesManager resourceManager;
	
	public static final int IMAGE_WIDTH = 50;
	public  static final int IMAGE_HEIGHT = 50;
	private int SPRITE_X = 0;
	private int SPRITE_Y = 0;
	public static final float FIXTURE_DENSITY = 1f;
	public static final float FIXTURE_ELASTICITY = 0.0f;
	public static final float FIXTURE_FRICTION = 0.0f;
	public static final String FILE_NAME = "devil.png";
	
	public Bot() {}
	
	public Bot(ResourcesManager resourceManager, int x, int y)
	{
		this.resourceManager = resourceManager;
		SPRITE_X = x+5;
		SPRITE_Y = y+5;
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
		mSprite = new Sprite(SPRITE_X, SPRITE_Y, resourceManager.bot_reg,
				activity.getVertexBufferObjectManager());
	}

	public Body createPhysicBody(PhysicsWorld pw)
	{
		FixtureDef def = PhysicsFactory.createFixtureDef(
				FIXTURE_DENSITY, 
				FIXTURE_ELASTICITY,
				FIXTURE_FRICTION);
		mBody = PhysicsFactory.createBoxBody(pw, mSprite,
				BodyType.KinematicBody, def);
		return mBody;
	}

	@Override
	public void dispose()
	{
		mSprite.detachSelf();
		mSprite.dispose();
	}
}
