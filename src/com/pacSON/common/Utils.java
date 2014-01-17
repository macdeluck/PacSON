package com.pacSON.common;

import org.andengine.entity.text.Text;

public class Utils
{
	private Utils()
	{
		super();
	}

	public static void CenterAlign(Text t)
	{
		t.setPosition(t.getX()-t.getWidth()/2, 
				t.getY());
	}
}
