package com.pacSON.hud.elements;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.pacSON.GameActivity;
import com.pacSON.entity.IPlayerStatsChangedListener;
import com.pacSON.gameStats.PlayerStats;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.ResourcesManager;

public class StarHud
{
	Sprite star;
	Text text;
	
	private IPlayerStatsChangedListener listener = null;
	
	public static final String HUD_STAR_NAME = "star.png";
	public static final int HUD_STAR_WIDTH = 50;
	public static final int HUD_STAR_HEIGHT= 50;
	public static final int HUD_STAR_MARGIN = 10;

	public static final int HUD_TEXT_MAXCHARS = 5;

	public StarHud()
	{
		super();
		listener = new IPlayerStatsChangedListener()
		{
			@Override
			public void statsChanged(PlayerStats stats)
			{
				text.setText(Integer.toString(stats.getStars()));
			}

			@Override
			public void statsReseted(PlayerStats stats)
			{
				text.setText("0");
			}
		};
	}

	public void setCamera(Camera camera)
	{
		star.setPosition(HUD_STAR_MARGIN,
				camera.getHeight() - HUD_STAR_MARGIN - LivesHud.HUD_LIVES_HEIGHT - HUD_STAR_HEIGHT);
		text.setPosition(HUD_STAR_MARGIN + HUD_STAR_WIDTH + HUD_STAR_MARGIN,
				camera.getHeight() - HUD_STAR_MARGIN - LivesHud.HUD_LIVES_HEIGHT - HUD_STAR_HEIGHT);
	}

	public void add()
	{
		GameActivity activity = ResourcesManager.getInstance().activity;
		star = new Sprite(0,0, ResourcesManager.getInstance().star_reg, 
				activity.getEngine().getVertexBufferObjectManager());
		text = new Text(2, 2, ResourcesManager.getInstance().font , 
				Integer.toString(GameManager.getInstance().getPlayerStats().getStars()), 
				HUD_TEXT_MAXCHARS, activity.getEngine().getVertexBufferObjectManager());
	}

	public void attach(IEntity parent)
	{
		parent.attachChild(star);
		parent.attachChild(text);
	}
	
	public IPlayerStatsChangedListener getStatsChangedListener()
	{
		return listener;
	}
}
