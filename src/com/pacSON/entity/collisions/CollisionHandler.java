package com.pacSON.entity.collisions;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;

import com.pacSON.entity.collisions.effects.ICollisionEffect;

public class CollisionHandler implements IUpdateHandler
{
	protected Sprite target1;
	protected Sprite target2;
	protected ICollisionEffect effect;

	public CollisionHandler(Sprite target1, Sprite target2,
			ICollisionEffect effect)
	{
		super();
		this.target1 = target1;
		this.target2 = target2;
		this.effect = effect;
	}

	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		if(target1.collidesWith(target2))
		{
			effect.onCollision(target1, target2);
		}
	}

	@Override
	public void reset()
	{
		effect.onReset(target1, target2);
	}
}
