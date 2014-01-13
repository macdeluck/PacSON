package com.pacSON.manager;

import com.pacSON.gameStats.PlayerStats;


public class GameManager
{
	private static GameManager instance = null;
	private int botAI;
	private float botSpeed;
	private int botCount;
	private int currentLevel;
	private int takenStars;
	private PlayerStats playerStats;
	
	public final static int BOT_AI_STEP = 2;
	public final static float BOT_SPEED_STEP = -0.025f;
	
	public final static int MAX_LIVES = 3;
	public final static int MAX_STARS = 15;
	public final static int DEF_BOT_AI = 80;
	public final static float DEF_BOT_SPEED = 0.75f;
	public final static int DEF_BOT_COUNT = 4;		
	
	public PlayerStats getPlayerStats()
	{
		return playerStats;
	}

	public int getTakenStars()
	{
		return takenStars;
	}

	public void setTakenStars(int takenStars)
	{
		this.takenStars = takenStars;
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
		botCount = DEF_BOT_COUNT;
		currentLevel = 1;
		takenStars = 0;
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
		takenStars = 0;
		botAI += BOT_AI_STEP;
		botSpeed += BOT_SPEED_STEP;
		currentLevel++;
		if (playerStats.getLives()<MAX_LIVES)
			playerStats.setLives(playerStats.getLives()+1);
	}
}
