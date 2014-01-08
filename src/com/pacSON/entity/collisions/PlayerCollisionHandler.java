package com.pacSON.entity.collisions;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.entity.Player;
import com.pacSON.entity.collisions.effects.ICollisionEffect;
import com.pacSON.entity.collisions.effects.IPlayerCollisionEffect;

public class PlayerCollisionHandler extends CollisionHandler
{
	public PlayerCollisionHandler(final Player player, final Sprite target, final IPlayerCollisionEffect effect)
	{
		super(player.getSprite(),target,new ICollisionEffect()
		{
			@Override
			public void onReset(Sprite s1, Sprite s2)
			{
				effect.onReset(player, target);
			}
			
			@Override
			public void onCollision(Sprite s1, Sprite s2)
			{
				effect.onCollision(player, target);
			}
		});
	}
}
