package com.pacSON.entity;

import com.pacSON.entity.Player.PlayerStats;

public interface IPlayerStatsChangedListener
{
	public void statsChanged(PlayerStats stats);
	public void statsReseted(PlayerStats stats);
}
