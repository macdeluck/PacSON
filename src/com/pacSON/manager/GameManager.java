package com.pacSON.manager;

import com.pacSON.common.PlayerStats;


public class GameManager
{
	private static GameManager instance = null;
	private int botAI;
	private float botSpeed;
	private int botCount;
	private int currentLevel;
	private PlayerStats playerStats;

	public PlayerStats getPlayerStats()
	{
		return playerStats;
	}

	public void setPlayerStats(PlayerStats playerStats)
	{
		this.playerStats = playerStats;
	}

	public final static int BOT_AI_STEP = 2;
	public final static float BOT_SPEED_STEP = -0.025f;
	
	public final static int MAX_LIVES = 3;
	public final static int MAX_STARS = 15;
	public final static int DEF_BOT_AI = 80;
	public final static float DEF_BOT_SPEED = 0.75f;
	public final static int DEF_BOT_COUNT = 4;			
	
	private GameManager()
	{
		super();
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
		botCount = DEF_BOT_COUNT;
		currentLevel = 1;
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
		botAI += DEF_BOT_AI;
		botSpeed += DEF_BOT_SPEED;
		currentLevel++;
		if (playerStats.getLives()<MAX_LIVES)
			playerStats.setLives(playerStats.getLives()+1);
	}
}
