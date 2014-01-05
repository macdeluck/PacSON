package com.pacSON.physic.gravity;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for all classes which are gravity sensors.
 * 
 * @author trolley
 *
 */
public abstract class GravitySensor
{
	/**
	 * Set of gravity value change listeners
	 */
	Set<OnGravityChangedListener> mListeners = new HashSet<OnGravityChangedListener>();
	
	/**
	 * Method which should be called if value of gravity has been changed.
	 * 
	 * @param vector Actual vector of gravity.
	 * @param delta Change of vector.
	 */
	protected final void gravityChanged(float[] vector, float[] delta)
	{
		for(OnGravityChangedListener l : mListeners)
			l.onGravityChanged(vector, delta);
	}
	
	/**
	 * Adds a gravity change listener.
	 * 
	 * @param listener 
	 * <br>Listener to be added
	 * @return True if listener has been successfully added. Otherwise false.
	 */
	public boolean addOnGravityChangedListener(OnGravityChangedListener listener)
	{
		return mListeners.add(listener);
	}
	/**
	 * Removes listener from the set.
	 * @param listener 
	 * <br>Listener to be removed
	 * @return 
	 */
	public boolean removeOnGravityChangedListener(OnGravityChangedListener listener)
	{
		return mListeners.remove(listener);
	}
	
	/**
	 * Starts a sensor.
	 */
	public abstract void start();
	
	/**
	 * Stops a sensor.
	 */
	public abstract void stop();
	
	/**
	 * Gets X-value of gravity vector.
	 * @return X-value of gravity vector.
	 */
	public abstract float getX();
	/**
	 * Gets Y-value of gravity vector.
	 * @return Y-value of gravity vector.
	 */
	public abstract float getY();

	/**
	 * Gets Z-value of gravity vector.
	 * @return Z-value of gravity vector.
	 */
	public abstract float getZ();

	/**
	 * Gets gravity vector. The vector length should be 3.
	 * @return Gravity vector.
	 */
	public abstract float[] getVector();
}
