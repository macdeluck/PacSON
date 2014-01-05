package com.pacSON.labyrinth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LabyrinthManager
{
	private int width;
	private int height;
	private int object_count;
	private boolean many_centroids; // one or four starting points
	private boolean dead_ends; // allow dead ends or not
	private LabyrinthMap map;
	private List<int[]> active_fields;
	private Random rnd;

	public LabyrinthManager()
	{
	}

	public void Generate_Labyrinth(int _width, int _height, int _object_count,
			boolean _many_centroids, boolean _dead_ends)
	{
		width = _width;
		height = _height;
		object_count = _object_count;
		many_centroids = _many_centroids;
		dead_ends = _dead_ends;
		rnd = new Random(System.currentTimeMillis());
		map = new LabyrinthMap(width, height);
		active_fields = new ArrayList<int[]>(0);
		generate_labyrinth();
	}

	private void generate_labyrinth()
	{
		Calculate_Centroids();
		int counter = 5;
		int idx = 0;
		int[] tmp_tab;
		while (counter > 0)
		{
			if(idx >= active_fields.size()) break;
			tmp_tab = active_fields.get(idx++);
			if (tmp_tab != null)
			{
				counter--;
				List<int[]> check_res = map.Check_And_Repair(tmp_tab[0],
						tmp_tab[1]);
				if (check_res != null)
				{
					counter += check_res.size();
					active_fields.addAll(check_res);
				}
			}
		}
		if(dead_ends)
			map.Remove_Dead_Ends();
		Add_Objects();
	}


	private void Calculate_Centroids()
	{
		if (many_centroids)
		{
			int y = width / 8 + rnd.nextInt(width / 4);
			int x = height / 8 + rnd.nextInt(height / 4);
			Generate_Centroid(x, y);
			y = width * 5 / 8 + rnd.nextInt(width / 4);
			x = height / 8 + rnd.nextInt(height / 4);
			Generate_Centroid(x, y);
			y = width / 8 + rnd.nextInt(width / 4);
			x = height * 5 / 8 + rnd.nextInt(height / 4);
			Generate_Centroid(x, y);
			y = width * 5 / 8 + rnd.nextInt(width / 4);
			x = height * 5 / 8 + rnd.nextInt(height / 4);
			Generate_Centroid(x, y);
		} else
		{
			int y = width / 4 + rnd.nextInt(width / 2);
			int x = height / 4 + rnd.nextInt(height / 2);
			Generate_Centroid(x, y);
		}		
	}

	private void Add_Objects()
	{
		int count = object_count;
		while(count > 0)
		{
			int p_x = rnd.nextInt(height);
			int p_y= rnd.nextInt(width);
			if(map.Is_Empty(p_x,p_y))
			{
				map.Set_Object(p_x,p_y);
				count--;
			}
		}
	}

	public List<int[]> Return_Blocks()
	{
		return map.Return_Blocks();
	}
	
	public List<int[]> Return_Objects()
	{
		return map.Return_Objects();
	}
	
	public int[] Return_First_Empty()
	{
		return map.Return_First_Empty();
	}
	
	private void Generate_Centroid(int x, int y)
	{
		int[] tmp_tab = new int[2];
		tmp_tab[0] = x;
		tmp_tab[1] = y;
		if (map.Set_Block(tmp_tab[0], tmp_tab[1]))
			active_fields.add(tmp_tab);
		Random rnd = new Random(System.currentTimeMillis());
		tmp_tab = new int[2];
		tmp_tab[0] = x - 2;
		tmp_tab[1] = y - 2 + rnd.nextInt(5);
		if (map.Set_Block(tmp_tab[0], tmp_tab[1]))
			active_fields.add(tmp_tab);
		tmp_tab = new int[2];
		tmp_tab[0] = x + 2;
		tmp_tab[1] = y - 2 + rnd.nextInt(5);
		if (map.Set_Block(tmp_tab[0], tmp_tab[1]))
			active_fields.add(tmp_tab);
		tmp_tab = new int[2];
		tmp_tab[0] = x - 1 + rnd.nextInt(3);
		tmp_tab[1] = y + 2;
		if (map.Set_Block(tmp_tab[0], tmp_tab[1]))
			active_fields.add(tmp_tab);
		tmp_tab = new int[2];
		tmp_tab[0] = x - 1 + rnd.nextInt(3);
		tmp_tab[1] = y - 2;
		if (map.Set_Block(tmp_tab[0], tmp_tab[1]))
			active_fields.add(tmp_tab);
	}
}
