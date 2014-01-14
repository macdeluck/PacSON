package com.pacSON.entity.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;

import com.pacSON.entity.GhostBot;

public class GhostBotMoveManager
{
	private GhostBot[] bots;
	private int counter;
	private int listenersSet;
	private float moveSpeed;
	private MovesReadyListener listener;

	public GhostBotMoveManager(GhostBot[] bots, float moveSpeed)
	{
		super();
		this.bots = bots;
		this.moveSpeed = moveSpeed;
		listener = new MovesReadyListener()
		{
			@Override
			public void onReady(GhostBotMoveManager manager) {}
		};
		counter = 0;
		listenersSet = 0;
	}

	public MovesReadyListener getListener()
	{
		return listener;
	}

	public GhostBot[] getBots()
	{
		return bots;
	}

	public void setListener(MovesReadyListener listener)
	{
		if (listener!=null)
			this.listener = listener;
	}
	
	public void start()
	{
		listener.onReady(this);
	}
	
	public void setNewMoveModifier(GhostBot target, float deltaX, float deltaY)
	{
		listenersSet++;
		IEntityModifier mod = new GhostBotMoveModifier(moveSpeed, deltaX, deltaY);
		mod.setAutoUnregisterWhenFinished(true);
		target.getSprite().registerEntityModifier(mod);
	}
	
	private class GhostBotMoveModifier extends MoveByModifier
	{
		public GhostBotMoveModifier(float pDuration, float deltaX, float deltaY)
		{
			super(pDuration, deltaX, deltaY);
		}

		@Override
		protected void onModifierFinished(IEntity pItem)
		{
			if (pItem.equals(bots[0].getSprite()))
			{
				pItem = bots[0].getSprite();
			}
			super.onModifierFinished(pItem);
			if (++counter==listenersSet)
			{
				counter=0;
				listenersSet=0;
				listener.onReady(GhostBotMoveManager.this);
			}
		}
	}
}
