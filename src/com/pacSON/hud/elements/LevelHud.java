package com.pacSON.hud.elements;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.text.Text;

import com.pacSON.GameActivity;
import com.pacSON.entity.IPlayerStatsChangedListener;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.ResourcesManager;

public class LevelHud
{
	Text text;
	
	private IPlayerStatsChangedListener listener = null;
	
	public static final int HUD_LEVELHUD_MARGIN = 10;

	public static final String HUD_LEVEL_TXT = "Level ";
	
	public static final int HUD_LEVELHUD_MAXCHARS = HUD_LEVEL_TXT.length() + 3;

	public LevelHud()
	{
		super();
	}

	public void setCamera(Camera camera)
	{
		text.setPosition(camera.getWidth()/2, HUD_LEVELHUD_MARGIN);
		update();
	}

	public void add()
	{
		GameActivity activity = ResourcesManager.getInstance().activity;
		text = new Text(2, 2, ResourcesManager.getInstance().levelFont , 
				Integer.toString(GameManager.getInstance().getPlayerStats().getStars()), 
				HUD_LEVELHUD_MAXCHARS, activity.getEngine().getVertexBufferObjectManager());
	}

	public void attach(IEntity parent)
	{
		parent.attachChild(text);
		update();
	}
	
	public IPlayerStatsChangedListener getStatsChangedListener()
	{
		return listener;
	}
	
	public void update()
	{
		final String txt = String.format("%s %d", HUD_LEVEL_TXT, GameManager.getInstance().getCurrentLevel());
		text.setText(txt);
		text.setPosition(text.getX()-text.getWidth()/2 , text.getY());
	}
}
