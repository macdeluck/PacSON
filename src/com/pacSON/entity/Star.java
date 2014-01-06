package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class Star
{

	private Sprite mSprite;
	private ResourcesManager resourceManager;

	public static final int IMAGE_WIDTH = 50;
	public static final int IMAGE_HEIGHT = 50;
	private int SPRITE_X = 0;
	private int SPRITE_Y = 0;
	public static final String FILE_NAME = "star.png";
	
	public Star() {}
	
	public Star(ResourcesManager resourcesManager,int x, int y)
	{
		this.resourceManager = resourcesManager;
		SPRITE_X = x+5;
		SPRITE_Y = y+5;
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
		mSprite = new Sprite(SPRITE_X, SPRITE_Y, resourceManager.star_reg, 
				activity.getVertexBufferObjectManager());
	}

}
