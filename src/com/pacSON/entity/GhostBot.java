package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class GhostBot implements IEntity
{
	private Sprite mSprite;
	private ResourcesManager resourceManager;
	
	public final static String GREEN_FILE_NAME = "ghostgreen.png";
	public int GHOST_SPRITE_X = 0;
	public int GHOST_SPRITE_Y = 0;
	public final static int IMAGE_WIDTH = 38;
	public final static int IMAGE_HEIGHT = 44;
	
	public GhostBot(ResourcesManager resourceManager, int x, int y)
	{
		super();
		GHOST_SPRITE_X = x + 9;
		GHOST_SPRITE_Y = y + 5;
		this.resourceManager = resourceManager;
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
		mSprite = new Sprite(GHOST_SPRITE_X, GHOST_SPRITE_Y, resourceManager.ghostBotGreen_reg, 
				resourceManager.vbom);
	}
}
