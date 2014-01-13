package com.pacSON.gameStats;

import java.util.HashSet;

import com.pacSON.entity.IPlayerStatsChangedListener;

public class PlayerStats
{
	private HashSet<IPlayerStatsChangedListener> lcListeners;
	private HashSet<IPlayerStatsChangedListener> lsListeners;
	
	private int defaultLives = 3;
	
	private int lives;
	
	private int stars;

	public int getStars()
	{
		return stars;
	}

	public void setStars(int stars)
	{
		this.stars = stars;
		starsChanged();
	}
	
	protected PlayerStats()
	{
		super();
		lcListeners = new HashSet<IPlayerStatsChangedListener>();
		lsListeners = new HashSet<IPlayerStatsChangedListener>();
		reset();
	}

	public PlayerStats(int defaultLives)
	{
		this();
		this.defaultLives = defaultLives;
		reset();
	}

	public int getDefaultLives()
	{
		return defaultLives;
	}

	public void setDefaultLives(int defaultLives)
	{
		this.defaultLives = defaultLives;
	}
	
	public int getLives()
	{
		return lives;
	}

	public void setLives(int lives)
	{
		if (lives<0)
			lives = 0;
		else if (lives>defaultLives)
			lives = defaultLives;
		this.lives = lives;
		livesChanged();
	}

	public boolean registerLivesChangedListener(IPlayerStatsChangedListener listener)
	{
		return lcListeners.add(listener);
	}
	
	public boolean unregisterLivesChangedListener(IPlayerStatsChangedListener listener)
	{
		return lcListeners.remove(listener);
	}
	
	public boolean registerStarsChangedListener(IPlayerStatsChangedListener listener)
	{
		return lsListeners.add(listener);
	}
	
	public boolean unregisterStarsChangedListener(IPlayerStatsChangedListener listener)
	{
		return lsListeners.remove(listener);
	}
	
	public void reset()
	{
		lives = defaultLives;
		reseted();
	}
	
	private void reseted()
	{
		for(IPlayerStatsChangedListener l : lcListeners)
			l.statsReseted(this);
		for(IPlayerStatsChangedListener l : lsListeners)
			l.statsReseted(this);
	}
	
	private void livesChanged()
	{
		for(IPlayerStatsChangedListener l : lcListeners)
			l.statsChanged(this);
	}
	
	private void starsChanged()
	{
		for(IPlayerStatsChangedListener l : lsListeners)
			l.statsChanged(this);
	}
}
