package com.pacSON.physic.gravity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Gravity sensor class which uses Android's Accelerometer as sensor.
 * @author trolley
 *
 */
public class AccelerometerSensor extends GravitySensor
{
	private Context mContext;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private float mNoise, mFilterFactor;
	private float mX, mY, mZ;
	private int mImpulseSkip;
	private int impulse;
	
	/**
	 * Gets impulse skip.
	 * <br>Impulse skip determines how many of impulses users get. For example 1 means that user gets each
	 * impulse and 3 means that user gets one impulse and 2 are skipped.
	 * @return
	 */
	public int getImpulseSkip()
	{
		return mImpulseSkip;
	}

	/**
	 * Sets impulse skip. Impulse skip must positive Integer. Look at getImpulseSkip() for details.
	 * @param pImpulseSkip
	 * @throws IllegalArgumentException
	 */
	public void setImpulseSkip(int pImpulseSkip) throws IllegalArgumentException
	{
		if (pImpulseSkip<1) throw new IllegalArgumentException();
		this.mImpulseSkip = pImpulseSkip;
	}

	private SensorEventListener mSensorListener;
	
	/**
	 * Creates gravity sensor in given context.
	 * @param context
	 */
	public AccelerometerSensor(Context context)
	{
		super();
		this.mContext = context;
		this.mNoise = 0.2f;
		this.mFilterFactor = 0.1f;
		this.mImpulseSkip = 1;
		this.impulse = 0;
		this.mSensorListener = new SensorEventListener()
		{
			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1)
			{
			}

			@Override
			public void onSensorChanged(SensorEvent event)
			{
				if (++impulse<mImpulseSkip) return;
				impulse = 0;
				
				boolean modified = false;
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				float[] delta = new float[] {mX, mY, mZ};
				
				if (Math.abs(x - mNoise)>mNoise)
				{
					modified = true;
					mX = mFilterFactor*mX + (1f-mFilterFactor)*x;
				}

				if (Math.abs(y - mNoise)>mNoise)
				{
					modified = true;
					mY = mFilterFactor*mY + (1f-mFilterFactor)*y;
				}

				if (Math.abs(z - mNoise)>mNoise)
				{
					modified = true;
					mZ = mFilterFactor*mZ + (1f-mFilterFactor)*z;
				}
				delta[0]-=mX;
				delta[1]-=mY;
				delta[2]-=mZ;
				
				if (modified)
					gravityChanged(new float[] {mX, mY, mZ}, delta);
			}
		};
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mX = mY = mZ = 0f;
	}

	/**
	 * Gets filter factor used to smooth sensor.
	 * @return Filter factor value.
	 */
	public float getFilterFactor()
	{
		return mFilterFactor;
	}
	
	/**
	 * Sets filter factor used to smooth sensor. The value of fiter factor should be between 1 and 0.
	 * @param filterFactor
	 * 		New value of filter factor.
	 * @throws IllegalArgumentException
	 * 		Thrown when new value is not between 0 and 1.
	 */
	public void setFilterFactor(float filterFactor) throws IllegalArgumentException
	{
		if (filterFactor>1f || filterFactor<0f) throw new IllegalArgumentException("Filter factor must be value between 1 and 0");
		this.mFilterFactor = filterFactor;
	}

	/**
	 * Gets noise value used to determine if user should be informed about gravity change.
	 * @return value of sensor accuracy
	 */
	public float getNoise()
	{
		return mNoise;
	}
	
	/**
	 * Sets noise value used to determine if user should be informed about gravity change.
	 * If value is lower than zero, each change will be saved.
	 * @param noise
	 */
	public void setNoise(float noise)
	{
		this.mNoise = noise;
	}

	@Override
	public void start()
	{
		mSensorManager.registerListener(mSensorListener, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	@Override
	public void stop()
	{
		mSensorManager.unregisterListener(mSensorListener);
	}
	
	@Override
	public float getX()
	{
		return mX;
	}
	@Override
	public float getY()
	{
		return mY;
	}
	@Override
	public float getZ()
	{
		return mZ;
	}

	@Override
	public float[] getVector()
	{
		return new float[] {getX(), getY(), getZ()};
	}
}
