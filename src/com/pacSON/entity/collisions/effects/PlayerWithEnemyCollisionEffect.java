package com.pacSON.entity.collisions.effects;

import com.pacSON.entity.GhostBot;
import com.pacSON.entity.Player;

public class PlayerWithEnemyCollisionEffect implements IPlayerCollisionEffect<GhostBot>
{
	@Override
	public void onCollision(Player player, GhostBot enemy)
	{
		if (!player.getStats().isImmortal())
		{
			player.getStats().setLives(player.getStats().getLives()-1);
			player.getStats().setImmortality(player.getImmortalityDuration(), 
					player.getImmortalityBlinks());
		}
	}

	@Override
	public void onReset(Player s1, GhostBot enemy)
	{
	}
	
}
