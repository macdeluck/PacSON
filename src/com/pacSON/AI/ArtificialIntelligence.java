package com.pacSON.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pacSON.labyrinth.LabyrinthMap;

public class ArtificialIntelligence
{
	private LabyrinthMap map;
	private List<int[]> bots_positions;
	private List<int[]> new_bots_positions;
	private List<int[]> res;
	private Random rnd;
	private int intelligence = 100;
	private final int max_inteligence = 100;

	public ArtificialIntelligence(LabyrinthMap _map, int _intelligence)
	{
		rnd = new Random(System.currentTimeMillis());
		intelligence = _intelligence;
		map = _map;
		List<int[]> tmp = map.Return_Bots();
		bots_positions = new ArrayList<int[]>(tmp.size());
		new_bots_positions = new ArrayList<int[]>(tmp.size());
		res = new ArrayList<int[]>(bots_positions.size());
		for (int i = 0; i < tmp.size(); ++i)
		{
			bots_positions.add(new int[] { tmp.get(i)[0], tmp.get(i)[1] });
			new_bots_positions.add(new int[] { tmp.get(i)[0], tmp.get(i)[1] });
			res.add(new int[] { 0, 0 });
		}
	}

	public List<int[]> Return_New_Positions(int[] player_position)
	{
		int x, y, dx, dy, new_x, new_y, old_x, old_y, actual_intelligence;
		boolean[] empty_fields;
		for (int i = 0; i < new_bots_positions.size(); ++i)
		{
			old_x = bots_positions.get(i)[0];
			old_y = bots_positions.get(i)[1];
			new_x = x = new_bots_positions.get(i)[0];
			new_y = y = new_bots_positions.get(i)[1];
			dx = x - player_position[0];
			dy = y - player_position[1];
			actual_intelligence = Scale_Intelligence(dx, dy);
			empty_fields = Find_Empty_Ways(x, y);

			if (dx < 0 && dy < 0)
			{
				if (rnd.nextInt(max_inteligence) <= actual_intelligence)
				{
					if (empty_fields[2] && empty_fields[3])
						if (rnd.nextInt(2) == 0)
							new_y = y + 1;
						else
							new_x = x + 1;
					else if (empty_fields[2])
						new_y = y + 1;
					else if (empty_fields[3])
						new_x = x + 1;
					else if (empty_fields[0])
						new_y = y - 1;
					else if (empty_fields[1])
						new_x = x - 1;
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

			} else if (dx < 0 && dy >= 0)
			{
				if (rnd.nextInt(max_inteligence) <= actual_intelligence)
				{
					if (empty_fields[0] && empty_fields[3])
						if (rnd.nextInt(2) == 0)
							new_y = y - 1;
						else
							new_x = x + 1;
					else if (empty_fields[0])
						new_y = y - 1;
					else if (empty_fields[3])
						new_x = x + 1;
					else if (empty_fields[2])
						new_y = y + 1;
					else if (empty_fields[1])
						new_x = x - 1;
				} else
				{
					if (empty_fields[2])
						new_y = y + 1;
					else if (empty_fields[1])
						new_x = x - 1;
					else if (empty_fields[0])
						new_y = y - 1;
					else if (empty_fields[3])
						new_x = x + 1;
				}
			} else if (dx >= 0 && dy < 0)
			{
				if (rnd.nextInt(max_inteligence) <= actual_intelligence)
				{
					if (empty_fields[2] && empty_fields[1])
						if (rnd.nextInt(2) == 0)
							new_y = y + 1;
						else
							new_x = x - 1;
					else if (empty_fields[2])
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
					else if (empty_fields[3])
						new_x = x + 1;
					else if (empty_fields[2])
						new_y = y + 1;
					else if (empty_fields[1])
						new_x = x - 1;
				}
			} else
			{
				if (rnd.nextInt(max_inteligence) <= actual_intelligence)
				{
					if (empty_fields[0] && empty_fields[1])
						if (rnd.nextInt(2) == 0)
							new_y = y - 1;
						else
							new_x = x - 1;
					else if (empty_fields[0])
						new_y = y - 1;
					else if (empty_fields[1])
						new_x = x - 1;
					else if (empty_fields[2])
						new_y = y + 1;
					else if (empty_fields[3])
						new_x = x + 1;
				} else
				{
					if (empty_fields[2])
						new_y = y + 1;
					else if (empty_fields[3])
						new_x = x + 1;
					else if (empty_fields[0])
						new_y = y - 1;
					else if (empty_fields[1])
						new_x = x - 1;
				}
			}

			if ((x != old_x || y != old_y) && map.Is_Bot(old_x, old_y))
				map.Set_Empty(old_x, old_y);
			if (map.Set_Bot_Position(new_x, new_y))
			{
				new_bots_positions.get(i)[0] = new_x;
				new_bots_positions.get(i)[1] = new_y;
				res.get(i)[0] = new_x - x;
				res.get(i)[1] = new_y - y;
			} else
			{
				res.get(i)[0] = 0;
				res.get(i)[1] = 0;
			}
			bots_positions.get(i)[0] = x;
			bots_positions.get(i)[1] = y;
		}
		return res;
	}

	private boolean[] Find_Empty_Ways(int x, int y)
	{
		boolean[] empty_fields = new boolean[4];
		if (map.Is_Enable_To_Move(x, y - 1))
			empty_fields[0] = true;
		else
			empty_fields[0] = false;
		if (map.Is_Enable_To_Move(x - 1, y))
			empty_fields[1] = true;
		else
			empty_fields[1] = false;
		if (map.Is_Enable_To_Move(x, y + 1))
			empty_fields[2] = true;
		else
			empty_fields[2] = false;
		if (map.Is_Enable_To_Move(x + 1, y))
			empty_fields[3] = true;
		else
			empty_fields[3] = false;
		return empty_fields;
	}

	private int Scale_Intelligence(int dx, int dy)
	{
		int actual_intelligence;
		double fact = dx * dx + dy * dy;
		if (fact <= 1)
			actual_intelligence = intelligence + 30;
		if (fact <= 9)
			actual_intelligence = intelligence + 20;
		else if (fact <= 25)
			actual_intelligence = intelligence + 10;
		else if (fact <= 49)
			actual_intelligence = intelligence;
		else if (fact <= 81)
			actual_intelligence = intelligence - 10;
		else
			actual_intelligence = intelligence - 20;
		return actual_intelligence;
	}
}
