package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class LabyrinthBackground implements IPacSONEntity
{
	private Sprite mSprite;
	private ResourcesManager resourceManager;

	public static final int IMAGE_WIDTH = 320;
	public static final int IMAGE_HEIGHT = 320;
	private int SPRITE_WIDTH;
	private int SPRITE_HEIGHT;
	public static final int SPRITE_X = 0;
	public static final int SPRITE_Y = 0;
	public static final String FILE_NAME = "background.jpg";
	
	public LabyrinthBackground() {}
	
	public LabyrinthBackground(ResourcesManager resourceManager, int width, int height)
	{
		this.resourceManager = resourceManager;
		SPRITE_WIDTH = width;
		SPRITE_HEIGHT = height;
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
		resourceManager.background_reg.setTextureWidth(SPRITE_WIDTH);
		resourceManager.background_reg.setTextureHeight(SPRITE_HEIGHT);
		mSprite = new Sprite(SPRITE_X, SPRITE_Y, resourceManager.background_reg,
				activity.getVertexBufferObjectManager());
	}

	@Override
	public void dispose()
	{
		mSprite.detachSelf();
		mSprite.dispose();
	}
}
