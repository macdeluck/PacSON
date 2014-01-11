package com.pacSON.AI;

import java.util.ArrayList;
import java.util.List;
import com.pacSON.labyrinth.*;

public class ArtificialIntelligence
{
	private LabyrinthMap map;
	private List<int[]> bots_positions;
	private List<int[]> new_bots_positions;
	private int[] player_position;

	public ArtificialIntelligence(LabyrinthMap _map)
	{
		map = _map;
		List<int[]> tmp = map.Return_Bots();
		bots_positions = new ArrayList<int[]>(tmp.size());
		new_bots_positions = new ArrayList<int[]>(tmp.size());
		for (int i = 0; i < tmp.size(); ++i)
		{
			bots_positions.add(new int[]{tmp.get(i)[0],tmp.get(i)[1]});
			new_bots_positions.add(new int[]{tmp.get(i)[0],tmp.get(i)[1]});
		}
	}

	public List<int[]> Return_New_Positions_Greedy(int[] _player_position)
	{
		player_position = _player_position;
		int x, y, dx, dy, new_x, new_y;
		boolean[] empty_fields = new boolean[4];
		for (int i = 0; i < bots_positions.size(); ++i)
		{
			new_x = x = new_bots_positions.get(i)[0];
			new_y = y = new_bots_positions.get(i)[1];
			dx = player_position[0] - x;
			dy = player_position[1] - y;
			if (map.Is_Empty_Generally(x, y - 1))
				empty_fields[0] = true;
			else
				empty_fields[0] = false;
			if (map.Is_Empty_Generally(x - 1, y))
				empty_fields[1] = true;
			else
				empty_fields[1] = false;
			if (map.Is_Empty_Generally(x, y + 1))
				empty_fields[2] = true;
			else
				empty_fields[2] = false;
			if (map.Is_Empty_Generally(x + 1, y))
				empty_fields[3] = true;
			else
				empty_fields[3] = false;
			if (dx < 0 && dy < 0)
			{
				if (empty_fields[2])
					new_y = y + 1;
				else if (empty_fields[3])
					new_x = x + 1;
				else if (empty_fields[0])
					new_y = y - 1;
				else if (empty_fields[1])
					new_x = x - 1;
			} else if (dx < 0 && dy >= 0)
			{
				if (empty_fields[0])
					new_y = y - 1;
				else if (empty_fields[3])
					new_x = x + 1;
				else if (empty_fields[2])
					new_y = y - 1;
				else if (empty_fields[1])
					new_x = x - 1;
			} else if (dx >= 0 && dy < 0)
			{
				if (empty_fields[2])
					new_y = y + 1;
				else if (empty_fields[1])
					new_x = x - 1;
				else if (empty_fields[0])
					new_y = y - 1;
				else if (empty_fields[3])
					new_x = x + 1;
			} else
			{
				if (empty_fields[0])
					new_y = y - 1;
				else if (empty_fields[1])
					new_x = x - 1;
				else if (empty_fields[2])
					new_y = y + 1;
				else if (empty_fields[3])
					new_x = x + 1;
			}
			map.Set_Empty(bots_positions.get(i)[0], bots_positions.get(i)[1]);
			if (map.Set_Bot_Position(new_x, new_y))
			{
				new_bots_positions.get(i)[0] = new_x;
				new_bots_positions.get(i)[1] = new_y;
			}
			bots_positions.get(i)[0] = x;
			bots_positions.get(i)[1] = y;
		}
		return new_bots_positions;
	}
}
