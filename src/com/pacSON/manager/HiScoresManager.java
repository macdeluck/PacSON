package com.pacSON.manager;

import java.util.Arrays;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pacSON.GameActivity;

public class HiScoresManager
{
	public final static String DEFAULT_HISCORES_STRING = "#0#0#0#0#0#0#0#0#0#0";
	public final static char DEFAULT_HISCORES_STRING_SEPARATOR = '#';
	public final static int HISCORES_COUNT = 10;
	
	private HiScoresManager()
	{
	}
	
	public static int[] getHiScores()
	{
		GameActivity activity = ResourcesManager.getInstance().activity;
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(com.pacSON.R.string.preference_key),
				Context.MODE_PRIVATE);
		final String key = activity.getString(com.pacSON.R.string.hiScores);
		String result = pref.getString(key, DEFAULT_HISCORES_STRING);
		Log.d("HiScores", "Getting scores: "+result);
		return parseHiScores(result);
	}
	
	public static int[] addScore(int score)
	{
		int[] scores = getHiScores();
		Arrays.sort(scores);
		for (int i=scores.length-1; i>=0; i--)
			if (scores[i]<score)
			{
				int c = scores[i];
				scores[i] = score;
				score = c;
			}
		saveHiScores(scores);
		return scores;
	}
	
	public static void saveHiScores(int[] scores)
	{
		GameActivity activity = ResourcesManager.getInstance().activity;
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(com.pacSON.R.string.preference_key),
				Context.MODE_PRIVATE);
		final String key = activity.getString(com.pacSON.R.string.hiScores);
		String parsed = parseHiScores(scores);
		Log.d("HiScores", "Saving scores: "+parsed);
		pref.edit().putString(key, parsed).commit();
	}
	
	public static int[] parseHiScores(String str)
	{
		int[] result = new int[HISCORES_COUNT];
		String[] res = str.split(String.format("%c%c",'\\',DEFAULT_HISCORES_STRING_SEPARATOR));
		for(int i=1; i<HISCORES_COUNT+1; i++)
			result[i-1] = Integer.parseInt(res[i]);
		Arrays.sort(result);
		return result;
	}
	
	public static String parseHiScores(int[] scores)
	{
		StringBuilder result = new StringBuilder();
		for(int i : scores)
		{
			result.append(DEFAULT_HISCORES_STRING_SEPARATOR);
			result.append(i);
		}
		return result.toString();
	}
}
