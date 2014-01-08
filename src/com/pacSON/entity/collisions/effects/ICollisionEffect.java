package com.pacSON.entity.collisions.effects;

import org.andengine.entity.sprite.Sprite;

public interface ICollisionEffect
{
	public void onCollision(Sprite s1, Sprite s2);
	public void onReset(Sprite s1, Sprite s2);
}
