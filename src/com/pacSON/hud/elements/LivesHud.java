package com.pacSON.hud.elements;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.pacSON.GameActivity;
import com.pacSON.entity.IPlayerStatsChangedListener;
import com.pacSON.gameStats.PlayerStats;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.ResourcesManager;

public class LivesHud
{
	Sprite[] lives;
	private IPlayerStatsChangedListener listener = null;
	private ITextureRegion textureRegion;
	private VertexBufferObjectManager buffer;
	
	public static final String HUD_LIVES_NAME = "heart_sm.png";
	public static final int HUD_LIVES_WIDTH = 40;
	public static final int HUD_LIVES_HEIGHT= 40;
	public static final int HUD_LIVES_MARGIN = 10;

	public LivesHud(int livesCount)
	{
		super();
		lives = new Sprite[livesCount];
		listener = new IPlayerStatsChangedListener()
		{
			@Override
			public void statsChanged(PlayerStats stats)
			{
				if (stats.getLives()>-1 && stats.getLives()<lives.length)
					lives[stats.getLives()].setAlpha(0f);
			}

			@Override
			public void statsReseted(PlayerStats stats)
			{
				for(Sprite l : lives)
				{
					l.detachSelf();
					l.dispose();
				}
				lives = new Sprite[stats.getLives()];
				for (int i=0; i<lives.length; i++)
				{
					lives[i] = new Sprite(2, 2, textureRegion, buffer);
				}
			}
		};
	}

	public void setCamera(Camera camera)
	{
		for (int i=0; i<lives.length; i++)
			lives[i].setPosition(HUD_LIVES_MARGIN + (HUD_LIVES_WIDTH+HUD_LIVES_MARGIN)*i,
					camera.getHeight() - HUD_LIVES_MARGIN - HUD_LIVES_HEIGHT);
	}

	public void add()
	{
		GameActivity activity = ResourcesManager.getInstance().activity;
		loadTextureRegion(activity, HUD_LIVES_NAME, HUD_LIVES_WIDTH, HUD_LIVES_HEIGHT);
		buffer = activity.getEngine().getVertexBufferObjectManager();
		for (int i=0; i<lives.length; i++)
		{
			lives[i] = new Sprite(0, 0, textureRegion, buffer);
			Log.d("LivesHud", String.format("Actual live: %d", i));
			if (i>=GameManager.getInstance().getPlayerStats().getLives())
			{
				Log.d("LivesHud", String.format("Hidden"));
				lives[i].setAlpha(0f);
			}
		}
	}

	public void attach(IEntity parent)
	{
		for (int i=0; i<lives.length; i++)
			parent.attachChild(lives[i]);
	}
	
	private void loadTextureRegion(GameActivity activity, String name, int width, int height)
	{
		BitmapTextureAtlas bta = new BitmapTextureAtlas(activity.getTextureManager(), width, height, TextureOptions.DEFAULT);
		
		TextureRegion reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, activity, name, 0, 0);
		
		activity.getTextureManager().loadTexture(bta);
		
		textureRegion = reg;
	}
	
	public IPlayerStatsChangedListener getStatsChangedListener()
	{
		return listener;
	}
}
