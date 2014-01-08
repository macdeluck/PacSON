package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class GhostBot
{
	private Sprite mSprite;
	private ResourcesManager resourcesManager;
	
	public GhostBot(ResourcesManager resourcesManager)
	{
		super();
		this.resourcesManager = resourcesManager;
	}

	public final static String GREEN_FILE_NAME = "ghostgreen.png";
	public final static int GHOST_SPRITE_X = -100;
	public final static int GHOST_SPRITE_Y = -100;
	public final static int IMAGE_WIDTH = 42;
	public final static int IMAGE_HEIGHT = 50;
	
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
		mSprite = new Sprite(GHOST_SPRITE_X, GHOST_SPRITE_Y, resourcesManager.ghostBotGreen_reg, 
				resourcesManager.vbom);
	}
}
