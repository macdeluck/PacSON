package com.pacSON.manager;

import java.io.IOException;
import java.util.HashSet;

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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.pacSON.GameActivity;
import com.pacSON.entity.Bot;
import com.pacSON.entity.GhostBot;
import com.pacSON.entity.LabyrinthBackground;
import com.pacSON.entity.Player;
import com.pacSON.entity.Star;
import com.pacSON.entity.Wall;
import com.pacSON.hud.elements.GravityHud;
import com.pacSON.tools.ToggleButtonSprite;

public class ResourcesManager
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Music music;
	public Font font;
	public Font levelFont;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion about_region;
	public ITextureRegion exit_region;

	// Game Texture Regions
	public ITextureRegion player_reg;
	public ITextureRegion wall_reg;
	public ITextureRegion bot_reg;
	public ITextureRegion ghostBotGreen_reg;
	public ITextureRegion star_reg;
	public ITextureRegion background_reg;
	public ITextureRegion settings_reg;
	public ITextureRegion gameOver_reg;
	public ITextureRegion hud_reg;
	public ITextureRegion gravArrow_reg;

	private BitmapTextureAtlas gameOverTextureAtlas;
	private BitmapTextureAtlas splashTextureAtlas;
	private BitmapTextureAtlas playerTextureAtlas;
	private BitmapTextureAtlas wallTextureAtlas;
	private BitmapTextureAtlas starTextureAtlas;
	private BitmapTextureAtlas botTextureAtlas;
	private BitmapTextureAtlas ghostBotGreenTextureAtlas;
	private BitmapTextureAtlas backgroundTextureAtlas;
	private BitmapTextureAtlas settingsTextureAtlas;
	private BitmapTextureAtlas hudTextureAtlas;
	private BitmapTextureAtlas gravArrowTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;

	private final String TOGGLE_BUTTON_AUDOIO = "speakers.png";
	private final String TOGGLE_TICK_AND_CROSS = "tickAndCross.png";
	private boolean isGamePaused = false;
	private HashSet<IPauseChanged> pauseChangedListeners = new HashSet<IPauseChanged>();
	private HashSet<IFPSCounterEnableChanged> fpsEnabledListeners = new HashSet<IFPSCounterEnableChanged>();
	
	public boolean isGamePaused()
	{
		return isGamePaused;
	}

	public void setGamePaused(boolean isGamePaused)
	{
		this.isGamePaused = isGamePaused;
		for(IPauseChanged l : pauseChangedListeners)
			l.onPauseChanged(isGamePaused);
	}
	
	public void togglePause()
	{
		setGamePaused(!isGamePaused);
	}
	
	public boolean addOnPauseChangedListener(IPauseChanged pc)
	{
		return pauseChangedListeners.add(pc);
	}
	
	public boolean removeOnPauseChangedListener(IPauseChanged pc)
	{
		return pauseChangedListeners.remove(pc);
	}

	/**
	 * Texture Region for the game graphics
	 */
	private BitmapTextureAtlas mSoundButtonBitmapTextureAtlas;
	private BitmapTextureAtlas mPauseButtonBitmapTextureAtlas;
	public TiledTextureRegion soundButtonTextureRegion;
	public TiledTextureRegion PauseButtonTextureRegion;
	public ToggleButtonSprite soundToggleButtonSprite;
	public ToggleButtonSprite pauseToggleButtonSprite;
	public TiledTextureRegion tickAndCrossButtonTextureRegion;
	public ToggleButtonSprite tickAndCrossToggleButtonSprite;

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

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

	public void loadOptionsResources()
	{
		loadOptionsGraphics();
	}

	private void loadOptionsGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/options/");
		this.mSoundButtonBitmapTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 200, 200, TextureOptions.BILINEAR);

		this.soundButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mSoundButtonBitmapTextureAtlas,
						activity.getAssets(), TOGGLE_BUTTON_AUDOIO, 0, 0, 2, 1);

		this.tickAndCrossButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mSoundButtonBitmapTextureAtlas,
						activity.getAssets(), TOGGLE_TICK_AND_CROSS, 0, 100, 2,
						1);

		this.mSoundButtonBitmapTextureAtlas.load();
	}

	private void loadMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity, "menu.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "itemPlay.png");
		options_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity, "itemOptions.png");
		about_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "itemAbout.png");
		exit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "itemExit.png");

		try
		{
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e)
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
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 50, true,
				Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	private void loadGameGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		playerTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), Player.IMAGE_WIDTH,
				Player.IMAGE_HEIGHT, TextureOptions.DEFAULT);
		wallTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), Wall.IMAGE_WIDTH,
				Wall.IMAGE_HEIGHT, TextureOptions.REPEATING_BILINEAR);
		starTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), Star.IMAGE_WIDTH,
				Star.IMAGE_HEIGHT, TextureOptions.DEFAULT);
		botTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), Bot.IMAGE_WIDTH,
				Bot.IMAGE_HEIGHT, TextureOptions.DEFAULT);
		ghostBotGreenTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), GhostBot.IMAGE_WIDTH,
				GhostBot.IMAGE_HEIGHT, TextureOptions.DEFAULT);
		backgroundTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), LabyrinthBackground.IMAGE_WIDTH,
				LabyrinthBackground.IMAGE_HEIGHT, TextureOptions.REPEATING_NEAREST);
		settingsTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 117, 121, TextureOptions.DEFAULT);
		hudTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				GravityHud.HUD_BASE_WIDTH, GravityHud.HUD_BASE_HEIGHT, TextureOptions.DEFAULT);
		gravArrowTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				GravityHud.HUD_GRAV_ARROW_WIDTH, GravityHud.HUD_GRAV_ARROW_HEIGHT, TextureOptions.DEFAULT);
		
		hud_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity,
				GravityHud.HUD_BASE_NAME, 0, 0);
		gravArrow_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gravArrowTextureAtlas, activity,
				GravityHud.HUD_GRAV_ARROW_NAME, 0, 0);
		player_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				playerTextureAtlas, activity, Player.FILE_NAME, 0, 0);
		wall_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				wallTextureAtlas, activity, Wall.FILE_NAME, 0, 0);
		star_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				starTextureAtlas, activity, Star.FILE_NAME, 0, 0);
		bot_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				botTextureAtlas, activity, Bot.FILE_NAME, 0, 0);
		ghostBotGreen_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				ghostBotGreenTextureAtlas, activity, GhostBot.GREEN_FILE_NAME, 0, 0);
		background_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				backgroundTextureAtlas, activity, LabyrinthBackground.FILE_NAME, 0, 0);
		settings_reg = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(settingsTextureAtlas, activity,
						"settings.png", 0, 0);

		activity.getTextureManager().loadTexture(playerTextureAtlas);
		activity.getTextureManager().loadTexture(wallTextureAtlas);
		activity.getTextureManager().loadTexture(starTextureAtlas);
		activity.getTextureManager().loadTexture(botTextureAtlas);
		activity.getTextureManager().loadTexture(ghostBotGreenTextureAtlas);
		activity.getTextureManager().loadTexture(backgroundTextureAtlas);
		activity.getTextureManager().loadTexture(settingsTextureAtlas);
		activity.getTextureManager().loadTexture(hudTextureAtlas);
		activity.getTextureManager().loadTexture(gravArrowTextureAtlas);
		
		this.mPauseButtonBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				200, 200, TextureOptions.BILINEAR);
		this.PauseButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mPauseButtonBitmapTextureAtlas, activity.getAssets(), 
						"playPause.png", 0, 0, 2, 1);	
		this.mPauseButtonBitmapTextureAtlas.load();
	}

	private void loadGameFonts()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		levelFont = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 30, true,
				Color.WHITE, 2, Color.BLACK);
		levelFont.load();
	}

	private void loadGameAudio()
	{
		try
		{
			music = MusicFactory.createMusicFromAsset(engine.getMusicManager(),
					activity, "media/music.mpga");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void unloadGameTextures()
	{
		// TODO
		playerTextureAtlas.unload();
		player_reg = null;
		wallTextureAtlas.unload();
		wall_reg = null;
		starTextureAtlas.unload();
		star_reg = null;
		botTextureAtlas.unload();
		bot_reg = null;
		backgroundTextureAtlas.unload();
		background_reg = null;
		settingsTextureAtlas.unload();
		settings_reg = null;
	}

	public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "PacSON.png", 0, 0);
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
	
	public void loadGameOverTextures()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameOverTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 350,
				280, TextureOptions.DEFAULT);
		gameOver_reg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameOverTextureAtlas, activity, "GameOver.jpeg", 0, 0);
		gameOverTextureAtlas.load();
	}
	public void unloadGameOverTextures()
	{
		gameOverTextureAtlas.unload();
	}
	

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare
	 *            Resources Manager properly, setting all needed parameters, so
	 *            we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, GameActivity activity,
			BoundCamera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
	
	public boolean isAudioOn()
	{
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(com.pacSON.R.string.preference_key),
				Context.MODE_PRIVATE);
		final String key = activity.getString(com.pacSON.R.string.isAudioOn);
		if (!pref.contains(key))
			pref.edit().putBoolean(key, false).commit();
		return pref.getBoolean(key, false);
	}
	
	public boolean isFpsCounterEnabled()
	{
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(com.pacSON.R.string.preference_key),
				Context.MODE_PRIVATE);
		final String key = activity.getString(com.pacSON.R.string.isFpsCounterEnabled);
		if (!pref.contains(key))
			pref.edit().putBoolean(key, true).commit();
		return pref.getBoolean(key, true);
	}
	
	public void setFpsCounterEnable(boolean value)
	{
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(com.pacSON.R.string.preference_key),
				Context.MODE_PRIVATE);
		final String key = activity.getString(com.pacSON.R.string.isFpsCounterEnabled);
		pref.edit().putBoolean(key, value).commit();
		Log.d("Resource Manager", "FPS enable changed: "+value);
		for(IFPSCounterEnableChanged l : fpsEnabledListeners)
			l.onEnableChanged(isFpsCounterEnabled());
	}
	
	public boolean addOnFpsCounterEnableChangedListener(IFPSCounterEnableChanged listener)
	{
		return fpsEnabledListeners.add(listener);
	}
	
	public boolean removeOnFpsCounterEnableChangedListener(IFPSCounterEnableChanged listener)
	{
		return fpsEnabledListeners.remove(listener);
	}
}