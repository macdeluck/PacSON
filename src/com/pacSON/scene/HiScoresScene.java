package com.pacSON.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;

import com.pacSON.base.BaseScene;
import com.pacSON.common.Utils;
import com.pacSON.manager.HiScoresManager;
import com.pacSON.manager.ResourcesManager;
import com.pacSON.manager.SceneManager;
import com.pacSON.manager.SceneManager.SceneType;

public class HiScoresScene extends BaseScene<Void>
{
	private Text hiScoresLabel;
	private Text[] scoresLabels;
	
	public static final String HISCORES_LABEL = "Hi-Scores";
	public static final float HISCORES_LABEL_MARGIN = 20;
	public static final float SCORES_MARGIN = 5;
	public static final float SCORES_TOP_MARGIN = 40;
	
	public HiScoresScene(Void onCreateParam)
	{
		super(onCreateParam);
	}

	@Override
	public void createScene(Void onCreateParams)
	{
		ResourcesManager rm = ResourcesManager.getInstance();
		Camera camera = rm.camera;
		hiScoresLabel = new Text(camera.getWidth()/2, HISCORES_LABEL_MARGIN
				,rm.font , HISCORES_LABEL, rm.activity.getVertexBufferObjectManager());
		Utils.CenterAlign(hiScoresLabel);
		attachChild(hiScoresLabel);
		
		int[] hsc = HiScoresManager.getHiScores();
		scoresLabels = new Text[hsc.length];
		for(int i=0; i<hsc.length; i++)
		{
			float yPos = HISCORES_LABEL_MARGIN + hiScoresLabel.getHeight()+ SCORES_TOP_MARGIN + 
					i*(SCORES_MARGIN+hiScoresLabel.getHeight());
			scoresLabels[i] = new Text(camera.getWidth()/2, 
					yPos, rm.font, Integer.toString(i+1) + ". " +Integer.toString(hsc[hsc.length-1-i]), 
					rm.activity.getVertexBufferObjectManager());
			Utils.CenterAlign(scoresLabels[i]);
			attachChild(scoresLabels[i]);
		}
		
	}
	
	@Override
	public void onSceneUnset()
	{
		disposeScene();
		super.onSceneUnset();
	}

	@Override
	public void onBackKeyPressed()
	{
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_HISCORES;
	}

	@Override
	public void disposeScene()
	{
		hiScoresLabel.dispose();
		/* TODO
		for(Text t : scoresLabels)
			t.dispose();*/
		dispose();
	}

}
