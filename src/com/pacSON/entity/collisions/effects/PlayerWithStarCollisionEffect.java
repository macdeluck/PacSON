package com.pacSON.entity.collisions.effects;

import com.pacSON.entity.Player;
import com.pacSON.entity.Player.PlayerStats;
import com.pacSON.entity.Star;

public class PlayerWithStarCollisionEffect implements IPlayerCollisionEffect<Star>
{

	@Override
	public void onCollision(Player player, Star target)
	{
		if (!target.isTaken())
		{
			PlayerStats stats = player.getStats();
			stats.setStars(stats.getStars()+1);
			target.getSprite().setAlpha(0f);
			target.setTaken(true);
		}
	}

	@Override
	public void onReset(Player player, Star target)
	{
		player.getStats().setStars(0);
		target.setTaken(false);
		target.getSprite().setAlpha(1f);
	}

}
