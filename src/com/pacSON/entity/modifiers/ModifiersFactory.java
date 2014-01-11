package com.pacSON.entity.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;

public class ModifiersFactory
{
	private ModifiersFactory()
	{
	}
	
	public static IEntityModifier getBlinkModifier(float pDuration, int blinks)
	{
		return new MyAlphaModifier(pDuration/(2*blinks), 2*blinks);
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
}
