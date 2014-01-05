package com.pacSON.physic.gravity;

/**
 * Listener interface called when gravity in gravity sensor has been changed.
 * @author trolley
 *
 */
public interface OnGravityChangedListener
{
	/**
	 * Called when gravity in gravity sensor has been changed.
	 * 
	 * @param vector Actual vector of gravity.
	 * @param delta Change of vector.
	 */
	void onGravityChanged(float[] vector, float delta[]);
}
