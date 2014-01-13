package com.pacSON.entity.collisions.effects;

import org.andengine.engine.Engine;

import com.pacSON.common.PlayerStats;
import com.pacSON.entity.Player;
import com.pacSON.entity.Star;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;

public class PlayerWithStarCollisionEffect implements IPlayerCollisionEffect<Star>
{

	@Override
	public void onCollision(Player player, Star target)
	{
		if (!target.isTaken())
		{
			PlayerStats stats = player.getStats();
			stats.setStars(stats.getStars()+1);
			GameManager.getInstance().setTakenStars(GameManager.getInstance().getTakenStars()+1);
			target.getSprite().setAlpha(0f);
			target.setTaken(true);
			if (GameManager.getInstance().getTakenStars()==GameManager.MAX_STARS)
				callNextLevel();
		}
	}

	@Override
	public void onReset(Player player, Star target)
	{
		player.getStats().setStars(0);
		target.setTaken(false);
		target.getSprite().setAlpha(1f);
	}
	
	public void callNextLevel()
	{
		Engine engine = ResourcesManager.getInstance().engine;
		SceneManager.getInstance().loadGameScene(engine, false);
	}
}
