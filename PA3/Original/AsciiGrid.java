/** define a 2D array of chars as a way to make ascii art.
 * can place and clear an arbitrary 2D array of chars in the grid
 * if asked-for array fits.
 * @author YOUR NAME HERE
 * @version CSE11 Spring 2015
 */
public class AsciiGrid
{
	/** Constructor 
	 */
	public AsciiGrid()
	{
	}
	/** Constructor 
	 * @param row number of rows in the ascii grid 
	 * @param col number of columns in the ascii grid 
	 */
	public AsciiGrid(int row, int col)
	{
	}

	/** return a row x col array of the current char array  
	 * This should be a full/deep copy, not a reference to internal
	 * storage
	 * @return array of chars 
	 */
	public char [][] getChars()
	{
		return (char [][]) null;
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
		return false;
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
		return false;
	}
	
	/** Return the width and height of the grid 
	 *  @return array where index=0 is nrows, index=1 ncolumns 
	 */
	public int [] getSize()
	{
		return (int []) null;
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
		String output = "";
		return output;
	}
	
}
// vim: ts=4:sw=4:tw=78
