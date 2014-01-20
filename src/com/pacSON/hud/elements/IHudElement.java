package com.pacSON.hud.elements;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;

public interface IHudElement
{
	void setCamera(Camera camera);
	void add();
	void attach(IEntity parent);
	void dispose();
}
