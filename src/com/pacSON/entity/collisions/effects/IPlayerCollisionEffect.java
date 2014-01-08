package com.pacSON.entity.collisions.effects;

import org.andengine.entity.sprite.Sprite;

import com.pacSON.entity.Player;

public interface IPlayerCollisionEffect
{
	public void onCollision(Player player, Sprite target);
	public void onReset(Player player, Sprite target);
}
