/** define a 2D array of chars as a way to make ascii art.
 * can place and clear an arbitrary 2D array of chars in the grid
 * if asked-for array fits.
 * @author Kaiwen Sun
 * @version CSE11 Spring 2015
 */
import java.util.Arrays;

public class AsciiGrid
{
	private final static char EMPTY = ' ';
	private final static int DEFAULT_ROW_SIZE = 25;	//default num of row
	private final static int DEFAULT_COL_SIZE = 40;	//default num of col
	private static final char NEWLINE = '\n';

	private char grid[][];
	private final int height;
	private final int width;

	/** Constructor 
	 */
	public AsciiGrid()
	{
		this(DEFAULT_ROW_SIZE,DEFAULT_COL_SIZE);
	}
	/** Constructor 
	 * @param row number of rows in the ascii grid 
	 * @param col number of columns in the ascii grid 
	 */
	public AsciiGrid(int row, int col)
	{
		height = Math.max(row,0);
		width = Math.max(col,0);
		grid = new char[height][width];
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
				grid[i][j]=EMPTY;
	}

	/** return a row x col array of the current char array  
	 * This should be a full/deep copy, not a reference to internal
	 * storage
	 * @return array of chars 
	 */
	public char [][] getChars()
	{
		char[][] rtn = new char[height][width];
		for(int i=0;i<height;i++)
			System.arraycopy(grid[i],0,rtn[i],0,width);		//don't forget deep copy!!!
		return rtn;
	}

	private boolean placeShape(AsciiShape shape,int r, int c,boolean doingClear)
	{
		boolean is_updated = false;
		if(shape==null)
			return false;
		int shape_height = shape.getHeight();
		int shape_width = shape.getWidth();
		int grid_i, grid_j;
		int shape_i, shape_j;

		Character[][] shape_table = shape.getShape();
		if (shape_table==null)
			return false;

		try{
			for(grid_i=r,shape_i=0;grid_i<height && shape_i<shape_height;grid_i++,shape_i++)
			{
				for(grid_j=c,shape_j=0;grid_j<width && shape_j<shape_width;grid_j++,shape_j++)
				{
					if(isInGrid(grid_i,grid_j) && shape_table[shape_i][shape_j] != (Character)null)
					{
						grid[grid_i][grid_j] = doingClear?EMPTY:shape_table[shape_i][shape_j];
						is_updated = true;
					}
				}
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			//this line is not expected to be executed
			return is_updated;
		}
		return is_updated;
	}


	/** 
	 * place the 2D shape in the grid at location (r,c) 
	 *
	 * @param shape AsciiShape object 
	 * @param r row in the grid where to place the first row of the shape
	 * @param c column in the grid where to place the first column of the shape
	 * @return true, if grid was updated, false otherwise 
	 *
	 * Implementation note: don't attempt to set anything outside of the grid.
	 * Start at coordinates (r,c). The loop through the rows and columns of
	 * the Shape (see getShape()) and adding r to the row coord, c to the
	 * column coord  of each element to 'translate the shape' to have it's
	 * upper left corner at (r,c).  Only 
	 * set of the grid element if the getShape()[i][j] element is non-null and
	 * is translated to be within the boundaries of the grid.  
	 */
	public boolean placeShape(AsciiShape shape,int r, int c)
	{
		return placeShape(shape,r,c,false);
	}
	
	/** 
	 * clear the elements in the grid  defined by the 2D shape 
	 * starting at grid at location (r,c). The contents of the 
	 * shape are irrelevant only the dimensions of each row are used.
	 * Clear only if every row of the shape fits in the grid. 
	 * Cleared elements in the grid are set to the EMPTY char.
	 *
	 * @param shape AsciiShape object  
	 * @param r row in the grid where to start the clearing 
	 * @param c column in the grid where to start the clearing 
	 * @return true, if grid was updated, false otherwise 
	 *
	 * Implementation note: placeShape and clearShape are nearly identical.
	 * The ONLY difference is what you see the grid element to be (the symbol
	 * of the shape OR a ' '.  Define a helper.
	 */
	public boolean clearShape(AsciiShape shape,int r, int c)
	{
		return placeShape(shape,r,c,true);
	}
	
	/** Return the width and height of the grid 
	 *  @return array where index=0 is nrows, index=1 ncolumns 
	 */
	public int [] getSize()
	{
		int[] size = {height,width};
		return size;
	}

	/** create a nice, printable representation of the grid and
	 * filled coordinates
	 *
	 * the grid should be framed. A row of "=' (length = width of grid + 2)
	 * should be used to frame the top and bottom of the grid. The '|' should
	 * be used to frame the left and right side of each row of the grid. e.g 
	 * 1x1  empty grid      2 x 2 empty grid
	 * ===                  ====
	 * | |                  |  |
	 * ===                  |  |
	 *                      ====
	 */
	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder((height+2)*(width+3));
		char[] topbottom_frame = new char[width+2];
		Arrays.fill(topbottom_frame, '=');
		output.append(topbottom_frame);
		output.append(NEWLINE);
		for(char[] row : grid)
		{
			output.append('|');
			output.append(row);
			output.append('|');
			output.append(NEWLINE);
		}
		output.append(topbottom_frame);
		return output.toString();
	}
	
	private boolean isInGrid(int row,int col)
	{
		return 0<=row && row<height && 0<=col && col<width;
	}
}
// vim: ts=4:sw=4:tw=78
