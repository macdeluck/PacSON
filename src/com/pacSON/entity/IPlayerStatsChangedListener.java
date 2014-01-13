package com.pacSON.entity;

import com.pacSON.common.PlayerStats;

public interface IPlayerStatsChangedListener
{
	public void statsChanged(PlayerStats stats);
	public void statsReseted(PlayerStats stats);
}
