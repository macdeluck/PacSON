package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class Wall
{
	private Sprite mSprite;
	private Body mBody;
	private ResourcesManager resourceManager;
	
	private int SPRITE_WIDTH;
	private int SPRITE_HEIGHT;
	private int SPRITE_X;
	private int SPRITE_Y;
	public static final int IMAGE_WIDTH = 60;
	public static final int IMAGE_HEIGHT = 60;
	public static final float FIXTURE_DENSITY = 1f;
	public static final float FIXTURE_ELASTICITY = 0.0f;
	public static final float FIXTURE_FRICTION = 0.0f;
	public static final String FILE_NAME = "wall.png";
	
	public Wall() {}
	
	public Wall(ResourcesManager resourcesManager,int x, int y, int width, int height)
	{
		this.resourceManager = resourcesManager;
		SPRITE_X = x;
		SPRITE_Y = y;
		SPRITE_WIDTH = width;
		SPRITE_HEIGHT = height;
	}
	
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
		resourceManager.wall_reg.setTextureWidth(SPRITE_WIDTH);
		resourceManager.wall_reg.setTextureHeight(SPRITE_HEIGHT);
		mSprite = new Sprite(SPRITE_X, SPRITE_Y, resourceManager.wall_reg, 
				activity.getVertexBufferObjectManager());
		mSprite.setCullingEnabled(true);		
	}
	
	public Body createPhysicBody(PhysicsWorld pw)
	{
		FixtureDef def = PhysicsFactory.createFixtureDef(
				FIXTURE_DENSITY, 
				FIXTURE_ELASTICITY,
				FIXTURE_FRICTION);
		
		mBody = PhysicsFactory.createBoxBody(pw, mSprite,
				BodyType.StaticBody, def);
		return mBody;
	}
}
