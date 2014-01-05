package com.pacSON.labyrinth;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class LabyrinthMap
{
	private int width;
	private int height;
	private int[][] map;
	private Random rnd;

	public LabyrinthMap(int _width, int _height)
	{
		width = _width;
		height = _height;
		map = new int[height][width];
		rnd = new Random(System.currentTimeMillis());
	}

	public boolean Is_Empty(int x, int y)
	{
		if (x >= 0 && x < height && y >= 0 && y < width && map[x][y] == 0)
			return true;
		else
			return false;
	}

	public boolean Is_Empty_Generally(int x, int y)
	{
		if (x < 0 || x >= height || y < 0 || y >= width || map[x][y] == 0)
			return true;
		else
			return false;
	}

	public boolean Set_Object(int x, int y)
	{
		if (x < 0 || x >= height || y < 0 || y >= width)
			return false;
		map[x][y] = 2;
		return true;
	}

	public boolean Set_Block(int field)
	{
		if (field < 0 || field >= width * height)
			return false;
		int y = field % width;
		int x = field / width;
		map[x][y] = 1;
		return true;
	}

	public boolean Set_Block(int x, int y)
	{
		if (x < 0 || x >= height || y < 0 || y >= width)
			return false;
		map[x][y] = 1;
		return true;
	}

	public List<int[]> Return_Blocks()
	{
		List<int[]> res = new ArrayList<int[]>(0);
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (map[i][j] == 1)
					res.add(new int[]{i,j});
		return res;
	}

	public List<int[]> Return_Objects()
	{
		List<int[]> res = new ArrayList<int[]>(0);
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (map[i][j] == 2)
					res.add(new int[]{i,j});
		return res;
	}

	public List<int[]> Check_And_Repair(int x, int y)
	{
		List<int[]> res = new ArrayList<int[]>(0);
		int[] fill_res;
		for (int i = 0; i < 4; i++)
		{
			fill_res = Fill_Square((x - 2 + i) * width + y - 2);
			if (fill_res != null)
			{
				res.add(fill_res);
				Set_Block(fill_res[0], fill_res[1]);
			}
			fill_res = Fill_Square((x - 2 + i) * width + y + 1);
			if (fill_res != null)
			{
				res.add(fill_res);
				Set_Block(fill_res[0], fill_res[1]);
			}
			fill_res = Fill_Square((x - 2) * width + y - 2 + i);
			if (fill_res != null)
			{
				res.add(fill_res);
				Set_Block(fill_res[0], fill_res[1]);
			}
			fill_res = Fill_Square((x + 1) * width + y - 2 + i);
			if (fill_res != null)
			{
				res.add(fill_res);
				Set_Block(fill_res[0], fill_res[1]);
			}
		}
		return res;
	}

	private int[] Fill_Square(int p)
	{
		if (Check_Square(p))
		{
			List<Integer> good_points = Return_Good_Points(p);
			if (good_points == null || good_points.size() <= 0)
				return null;
			int[] tmp_tab = new int[2];
			int selected_point = good_points
					.get(rnd.nextInt(good_points.size()));
			tmp_tab[1] = selected_point % width;
			tmp_tab[0] = selected_point / width;
			return tmp_tab;
		}
		return null;
	}

	private List<Integer> Return_Good_Points(int p)
	{
		List<Integer> res = new ArrayList<Integer>(0);
		if (Check_Corners(p))
			res.add(p);
		if (Check_Corners(p + 1))
			res.add(p + 1);
		if (Check_Corners(p + width))
			res.add(p + width);
		if (Check_Corners(p + width + 1))
			res.add(p + width + 1);
		return res;
	}

	private boolean Check_Corners(int p)
	{
		int y = p % width;
		int x = p / width;
		if (Is_Empty_Generally(x - 1, y - 1)
				&& Is_Empty_Generally(x - 1, y + 1)
				&& Is_Empty_Generally(x + 1, y - 1)
				&& Is_Empty_Generally(x + 1, y + 1))
			return true;
		return false;
	}

	private boolean Check_Square(int p)
	{
		int y = p % width;
		int x = p / width;
		if (Is_Empty(x, y) && Is_Empty(x, y + 1) && Is_Empty(x + 1, y)
				&& Is_Empty(x + 1, y + 1))
			return true;
		return false;
	}

	public void Remove_Dead_Ends()
	{
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				if (Check_Cross(i, j) < 2)
					Set_Block(i, j);
			}
	}

	private int Check_Cross(int x, int y)
	{
		int count = 0;
		if (Is_Empty(x, y - 1))
			count++;
		if (Is_Empty(x, y + 1))
			count++;
		if (Is_Empty(x - 1, y))
			count++;
		if (Is_Empty(x + 1, y))
			count++;
		return count;
	}
	
	public int[] Return_First_Empty()
	{
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			if(map[i][j] == 0)
				return new int[]{i,j};
		return new int[]{0,0};
	}
}
