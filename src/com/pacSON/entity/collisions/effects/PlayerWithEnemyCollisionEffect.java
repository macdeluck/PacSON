package com.pacSON.entity.collisions.effects;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.entity.Player;

public class PlayerWithEnemyCollisionEffect implements IPlayerCollisionEffect
{
	@Override
	public void onCollision(Player player, Sprite enemy)
	{
		if (!player.getStats().isImmortal())
		{
			player.getStats().setLives(player.getStats().getLives()-1);
			player.getStats().setImmortality(player.getImmortalityDuration(), 
					player.getImmortalityBlinks());
		}
	}

	@Override
	public void onReset(Player s1, Sprite enemy)
	{
	}
	
}
