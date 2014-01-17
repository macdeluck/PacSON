package com.pacSON.hud.elements;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;

import android.graphics.Typeface;

import com.badlogic.gdx.math.Vector2;
import com.pacSON.GameActivity;
import com.pacSON.common.Vector2RotationCalculator;
import com.pacSON.common.Vector2RotationResult;
import com.pacSON.manager.ResourcesManager;

public class GravityHud
{
	private Sprite hudBase;
	private Sprite gravityArrow;
	private Text gravityValue;
	private RotationModifier arrowRotationModifier;
	
	public static final String HUD_BASE_NAME = "hud.png";
	public static final int HUD_BASE_WIDTH = 100;
	public static final int HUD_BASE_HEIGHT = 100;
	public static final String HUD_GRAV_ARROW_NAME = "arrow_sm.png";
	
	public static final int HUD_GRAV_ARROW_WIDTH = 60;
	public static final int HUD_GRAV_ARROW_HEIGHT = 46;
	public static final int HUD_GRAV_ARROW_MARGINX = 20;
	public static final int HUD_GRAV_ARROW_MARGINY = 27;
	public static final float HUD_GRAV_ARROW_ROTATION_DURATION = 0.5f;
	public static final int HUD_GRAV_VAL_MARGINX = 55;
	public static final int HUD_GRAV_VAL_MARGINY = 63;
	public static final int HUD_GRAV_VAL_MAXCHARS = 5;
	
	public void attach(IEntity parent)
	{
		parent.attachChild(hudBase);
		parent.attachChild(gravityArrow);
		parent.attachChild(gravityValue);
	}
	
	public Sprite getHudBase()
	{
		return hudBase;
	}

	public Sprite getGravityArrow()
	{
		return gravityArrow;
	}

	public Text getGravityValue()
	{
		return gravityValue;
	}
	
	public void add()
	{
		GameActivity activity = ResourcesManager.getInstance().activity;
		hudBase = new Sprite(2, 2, ResourcesManager.getInstance().hud_reg,
					activity.getEngine().getVertexBufferObjectManager());
		gravityArrow = new Sprite(2, 2, ResourcesManager.getInstance().gravArrow_reg,
				activity.getEngine().getVertexBufferObjectManager());
		gravityValue = new Text(2, 2, loadFont(activity),"0.0", HUD_GRAV_VAL_MAXCHARS, activity.getEngine().getVertexBufferObjectManager());
				
	}
	
	public void setCamera(Camera camera)
	{
		hudBase.setPosition(camera.getWidth()-hudBase.getWidth(),
				camera.getHeight()-hudBase.getHeight());
		gravityArrow.setPosition(camera.getWidth()-hudBase.getWidth()+HUD_GRAV_ARROW_MARGINX,
				camera.getHeight()-hudBase.getHeight()+HUD_GRAV_ARROW_MARGINY);
		gravityValue.setPosition(camera.getWidth()-hudBase.getWidth()+HUD_GRAV_VAL_MARGINX,
				camera.getHeight()-hudBase.getHeight()+HUD_GRAV_VAL_MARGINY);
	}
	
	public void setGravity(Vector2 vec)
	{
		float l = vec.len();
		if (l!=0f)
		{
			Vector2RotationResult rot = Vector2RotationCalculator.getAngles(vec, gravityArrow.getRotation());
			
			if (arrowRotationModifier==null)
			{
				arrowRotationModifier = new RotationModifier(HUD_GRAV_ARROW_ROTATION_DURATION, rot.fromAngle, rot.toAngle);
				arrowRotationModifier.setAutoUnregisterWhenFinished(false);
				gravityArrow.registerEntityModifier(arrowRotationModifier);
			}
			else arrowRotationModifier.reset(HUD_GRAV_ARROW_ROTATION_DURATION, rot.fromAngle, rot.toAngle);
			//gravityArrow.setRotation(angle);
		}
		String form = "%1.1f";
		if (l>=10)
			form = "%1.0f";
		gravityValue.setText(String.format(form, l));
	}
	
	private Font loadFont(GameActivity activity)
	{
		Font mFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(),
				256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		mFont.load();
		return mFont;
	}
}