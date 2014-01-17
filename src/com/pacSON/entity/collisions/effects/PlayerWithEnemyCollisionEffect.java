package com.pacSON.entity.collisions.effects;

import com.pacSON.entity.GhostBot;
import com.pacSON.entity.Player;
import com.pacSON.manager.GameManager;
import com.pacSON.manager.HiScoresManager;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;

public class PlayerWithEnemyCollisionEffect implements IPlayerCollisionEffect<GhostBot>
{
	@Override
	public void onCollision(Player player, GhostBot enemy)
	{
		if (!player.isImmortal())
		{
			if(GameManager.getInstance().getPlayerStats().getLives() == 1)
			{
				HiScoresManager.addScore(GameManager.getInstance().getPlayerStats().getStars());
				SceneManager.getInstance().loadMenuSceneFromOver(ResourcesManager.getInstance().engine);
			}
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
