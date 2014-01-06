package com.pacSON.entity.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;

public class ModifiersFactory
{
	private ModifiersFactory()
	{
	}
	
	public static IEntityModifier getBlinkModifier(float pDuration, int blinks)
	{
		return new MyAlphaModifier(pDuration/(2*blinks), 2*blinks);
	}
	
	public static IEntityModifier getMoveAndGoBackModifier(float pDuration, float x1, float x2, float y1, float y2)
	{
		return new MyMoveModifier(pDuration, x1, x2, y1, y2);
	}
	
	private static class MyAlphaModifier extends AlphaModifier
	{
		private int mBlinksLeft;
		
		public MyAlphaModifier(float pDuration, int blinksLeft)
		{
			this(pDuration, 1f, 0f, blinksLeft);
			this.setAutoUnregisterWhenFinished(true);
		}
		
		private MyAlphaModifier(float pDuration, float fromAlpha, float toAlpha, int blinksLeft)
		{
			super(pDuration, fromAlpha, toAlpha);
			mBlinksLeft = blinksLeft;
		}
		
		@Override
		protected void onModifierFinished(IEntity pItem)
		{
			super.onModifierFinished(pItem);
			if (mBlinksLeft-1>0)
				pItem.registerEntityModifier(new MyAlphaModifier(getDuration(),getToValue(), getFromValue(),mBlinksLeft-1));
		}
	}

	public static class MyMoveModifier extends MoveModifier
	{

		public MyMoveModifier(float pDuration, float x1, float x2, float y1, float y2)
		{
			super(pDuration, x1, x2, y1, y2);
		}
		
		@Override
		protected void onModifierFinished(IEntity pItem)
		{
			pItem.registerEntityModifier(new MyMoveModifier(getDuration(), 
					getToValueA(), getFromValueA(), getToValueB(), getFromValueB()));
		}
	}
}
