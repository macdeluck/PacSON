package com.pacSON.entity;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.GameActivity;
import com.pacSON.manager.ResourcesManager;

public class GhostBot implements IPacSONEntity
{
	private Sprite mSprite;
	private ResourcesManager resourceManager;
	private static int colorIndex = 0;
	
	public final static String GREEN_FILE_NAME = "ghostgreen.png";
	public final static String BLUE_FILE_NAME = "ghostblue.png";
	public final static String RED_FILE_NAME = "ghostred.png";
	public final static String YELLOW_FILE_NAME = "ghostyellow.png";
	public final static String CYAN_FILE_NAME = "ghostcyan.png";
	public final static String MAGENTA_FILE_NAME = "ghostmagenta.png";
	public final static String[] FILE_NAMES = 
		{
			RED_FILE_NAME,
			GREEN_FILE_NAME,
			BLUE_FILE_NAME,
			YELLOW_FILE_NAME,
			MAGENTA_FILE_NAME,
			CYAN_FILE_NAME
		};
	public final static int COLORS_COUNT = 6;
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
		mSprite = new Sprite(GHOST_SPRITE_X, GHOST_SPRITE_Y, resourceManager.ghostBotReg[nextColorIndex()], 
				resourceManager.vbom);
	}
	
	public static int nextColorIndex()
	{
		if (colorIndex>COLORS_COUNT)
			colorIndex=0;
		return colorIndex++;
	}
	
	public static void resetColorIndex()
	{
		colorIndex = 0;
	}
}
