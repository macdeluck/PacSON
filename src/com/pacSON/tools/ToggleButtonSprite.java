package com.pacSON.tools;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ToggleButtonSprite extends TiledSprite implements ToggleButtonSpriteInterface{
	
	/**
	 * Enum to store different ON and OFF state of the button
	 */
	public static enum ToggleState {
		OFF(0),ON(1);
		private final int mState;

		private ToggleState(final int pSate) {
			mState = pSate;
		}

		public int get() {
			return mState;
		}

	};


	private boolean mToogleState;

	private ToggleState mState;

	private ToggleButtonSpriteInterface mOnToggleClickListener;



	/**
	 * Constructor to create ToggleButtonSprite
	 * @param pX
	 * @param pY
	 * @param pNormalTextureRegion
	 * @param pVertexBufferObjectManager
	 */
	public ToggleButtonSprite(final float pX, final float pY, final TiledTextureRegion pNormalTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager);

		mState = ToggleState.OFF;
		setCurrentTileIndex(mState.get());
	}

	/**
	 * Method to setState of the ToggleButton
	 * @param pState
	 */
	public void setState(final ToggleState pState) {
		mState = pState;
		setCurrentTileIndex(mState.get());
		mToogleState = ToggleState.ON.equals(pState);
	}


	/**
	 * Method to change the state of the Toggle button
	 * @param pState
	 */
	private void changeState(final ToggleState pState) {
		mState = pState;
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
			final float pTouchAreaLocalY) {
		 if (pSceneTouchEvent.isActionUp()) {

			if (mOnToggleClickListener != null) {

				mToogleState = !mToogleState;
				if (!mToogleState) {
					setCurrentTileIndex(ToggleState.OFF.get());
					changeState(ToggleState.ON);
					mOnToggleClickListener.onOnClick(this, 
pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				} else {
					setCurrentTileIndex(ToggleState.ON.get());
					changeState(ToggleState.OFF);
					mOnToggleClickListener.onOffClick(this, 
pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				}
			}
		}

		return true;
	}

	/**
	 * Set the ToggleListenter for the ToggleButtonSprite
	 * @param pOnToggleClickListener
	 */
	public void setOnToggleClickListener(final ToggleButtonSpriteInterface pOnToggleClickListener) {
		mOnToggleClickListener = pOnToggleClickListener;
	}

	/**
	 * Set the state of the ToggleButton
	 * @param pIsON
	 */
	public void setEnabled(final boolean pIsON) {
		mToogleState = false;
		mState = pIsON ? ToggleState.ON : ToggleState.OFF;
		setCurrentTileIndex(mState.get());
	}

	@Override
	public void onOffClick(ToggleButtonSprite pButtonSprite,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
	}

	@Override
	public void onOnClick(ToggleButtonSprite pButtonSprite,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
	}
}

