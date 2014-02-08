package com.pacSON.manager;

import com.pacSON.common.Configuration;
import com.pacSON.gameStats.PlayerStats;


public class GameManager
{
	private static GameManager instance = null;
	private int botAI;
	private float botSpeed;
	private int botCount;
	private int currentLevel;
	private PlayerStats playerStats;
	
	public final static int BOT_AI_STEP = 2;
	public final static int BOT_AI_MAX = 100;
	public final static float BOT_SPEED_STEP = -0.010f;
	public final static float BOT_SPEED_MIN = 0.65f;
	public final static int BOT_COUNT = 4;
	public final static int BOT_COUNT_INCREMENT = 6;
	public final static int BOT_COUNT_MAX = 8;
	
	public final static int MAX_LIVES = 3;
	public final static int MAX_STARS = (Configuration.DEBUG_ONE_STAR) ? 1 : 15;
	public final static int DEF_BOT_AI = 70;
	public final static float DEF_BOT_SPEED = 0.8f;
	
	public PlayerStats getPlayerStats()
	{
		return playerStats;
	}

	public void setPlayerStats(PlayerStats playerStats)
	{
		this.playerStats = playerStats;
	}
	
	private GameManager()
	{
		super();
		if (instance!=null)
			throw new RuntimeException();
		playerStats = new PlayerStats(MAX_LIVES);
		reset();
	}
	
	public static GameManager getInstance()
	{
		if (instance==null)
			instance = new GameManager();
		return instance;
	}
	
	public void reset()
	{
		botAI = DEF_BOT_AI;
		botSpeed = DEF_BOT_SPEED;
		botCount = BOT_COUNT;
		currentLevel = 1;
		playerStats.setTakenStars(0);
		playerStats.setStars(0);
		playerStats.setDefaultLives(MAX_LIVES);
		playerStats.setLives(MAX_LIVES);
	}

	public int getBotAI()
	{
		return botAI;
	}

	public float getBotSpeed()
	{
		return botSpeed;
	}

	public int getBotCount()
	{
		return botCount;
	}

	public int getCurrentLevel()
	{
		return currentLevel;
	}

	public void levelUp()
	{
		botAI += BOT_AI_STEP;
		if (botAI>BOT_AI_MAX)
			botAI=BOT_AI_MAX;
		
		botSpeed += BOT_SPEED_STEP;
		if (botSpeed<BOT_SPEED_MIN)
			botSpeed=BOT_SPEED_MIN;
		
		currentLevel++;
		playerStats.setTakenStars(0);
		if (playerStats.getLives()<MAX_LIVES)
			playerStats.setLives(playerStats.getLives()+1);
		if(currentLevel % BOT_COUNT_INCREMENT == 0)
		{
			botCount++;
			if (botCount>BOT_COUNT_MAX)
				botCount=BOT_COUNT_MAX;
		}
	}
}
