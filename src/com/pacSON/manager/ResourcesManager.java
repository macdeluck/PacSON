package com.pacSON.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

import com.pacSON.GameActivity;
import com.pacSON.entity.Ellipse;

public class ResourcesManager
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Music music;
	public Font font;
	
	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------
	
	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion about_region;
	public ITextureRegion exit_region;
	
	// Game Texture Regions
	public ITextureRegion reg;
	
	
	private BitmapTextureAtlas splashTextureAtlas;
	private BitmapTextureAtlas gameTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
//	// Level Complete Window
//	public ITextureRegion complete_window_region;
//	public ITiledTextureRegion complete_stars_region;
	
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}
	
	public void loadGameResources()
	{
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}
	
	private void loadMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "itemPlay.png");
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "itemOptions.png");
        about_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "itemAbout.png");
        exit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "itemExit.png");
       
    	try 
    	{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
	}
	
	private void loadMenuAudio()
	{
		
	}
	
	private void loadMenuFonts()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	private void loadGameGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),
				Ellipse.ELLIPSE_SPRITE_WIDTH, Ellipse.ELLIPSE_SPRITE_HEIGHT, TextureOptions.DEFAULT);

		reg = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, Ellipse.ELLIPSE_FILE_NAME, Ellipse.ELLIPSE_SPRITE_X, Ellipse.ELLIPSE_SPRITE_Y);

		activity.getTextureManager().loadTexture(gameTextureAtlas);
	}
	
	private void loadGameFonts()
	{
		
	}
	
	private void loadGameAudio()
	{
		try
		{
		    music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"media/music.mpga");
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	}
	
	public void unloadGameTextures()
	{
		gameTextureAtlas.unload();
		reg = null;
	}
	
	public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "PacSON.png", 0, 0);
        splashTextureAtlas.load();	
	}
	
	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}
	
	public void unloadMenuTextures()
	{
		menuTextureAtlas.unload();
	}
	
	public void loadMenuTextures()
	{
		menuTextureAtlas.load();
	}
	
	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br><br>
	 * We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------
	
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
}