package com.pacSON.entity.collisions.effects;

import com.pacSON.entity.GhostBot;
import com.pacSON.entity.Player;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.SceneManager;

public class PlayerWithEnemyCollisionEffect implements IPlayerCollisionEffect<GhostBot>
{
	@Override
	public void onCollision(Player player, GhostBot enemy)
	{
		if (!player.isImmortal())
		{
			if(GameManager.getInstance().getPlayerStats().getLives() == 1)
				SceneManager.getInstance().getCurrentScene().onGameOverHappened();
			GameManager.getInstance().getPlayerStats().setLives(
					GameManager.getInstance().getPlayerStats().getLives()-1);
			player.setImmortality(player.getImmortalityDuration(), 
					player.getImmortalityBlinks());
		}
	}

	@Override
	public void onReset(Player s1, GhostBot enemy)
	{
	}
	
}
