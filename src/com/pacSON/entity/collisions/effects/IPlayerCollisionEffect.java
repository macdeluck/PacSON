package com.pacSON.entity.collisions.effects;

import com.pacSON.entity.IPacSONEntity;
import com.pacSON.entity.Player;

public interface IPlayerCollisionEffect<T extends IPacSONEntity>
{
	public void onCollision(Player player, T target);
	public void onReset(Player player, T target);
}
