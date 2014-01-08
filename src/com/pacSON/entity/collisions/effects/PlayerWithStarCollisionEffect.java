package com.pacSON.entity.collisions.effects;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.entity.Player;
import com.pacSON.entity.Player.PlayerStats;

public class PlayerWithStarCollisionEffect implements IPlayerCollisionEffect
{

	@Override
	public void onCollision(Player player, Sprite target)
	{
		PlayerStats stats = player.getStats();
		stats.setStars(stats.getStars()+1);
		target.setAlpha(0f);
	}

	@Override
	public void onReset(Player player, Sprite target)
	{
		player.getStats().setStars(0);
		target.setAlpha(1f);
	}

}
