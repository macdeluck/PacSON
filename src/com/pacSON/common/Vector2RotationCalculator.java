package com.pacSON.common;

import com.badlogic.gdx.math.Vector2;

public class Vector2RotationCalculator
{
	public static Vector2RotationResult getAngles(Vector2 vec, float baseAngle)
	{
		Vector2RotationResult result = new Vector2RotationResult();
		float l = vec.len();
		float cos = vec.x/l;
		float fromAngle = baseAngle;
		float toAngle = -(float)(Math.acos(cos)*180/Math.PI);
		if (vec.y>0)
			toAngle = -toAngle;
		
		// make sure angles are between 0 and 360
		if (fromAngle<0)
			fromAngle+=360;
		else if (fromAngle>360)
			fromAngle-=360;
		
		if (toAngle<0)
			toAngle+=360;
		else if (toAngle>360)
			toAngle-=360;
		
		// if rotation change is greater than 180 change rotation direction
		if (toAngle-fromAngle>180)
			toAngle-=360;
		else if (toAngle-fromAngle>180)
			toAngle+=360;
		result.fromAngle = fromAngle;
		result.toAngle = toAngle;
		return result;
	}
	
	public static float getAngle(Vector2 vec)
	{
		float l = vec.len();
		float cos = vec.x/l;
		return (float)(Math.acos(cos)*180/Math.PI);
	}
}
