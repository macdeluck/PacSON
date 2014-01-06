	package com.pacSON.tools;

	public interface ToggleButtonSpriteInterface {

		/**
		 * Method is invoked when Off state is clicked
		 * @param pButtonSprite
		 * @param pTouchAreaLocalX
		 * @param pTouchAreaLocalY
		 */
		void onOffClick(ToggleButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY);

		/**
		 * Method is invoked when On state is clicked
		 * @param pButtonSprite
		 * @param pTouchAreaLocalX
		 * @param pTouchAreaLocalY
		 */
		void onOnClick(ToggleButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY);

	}

