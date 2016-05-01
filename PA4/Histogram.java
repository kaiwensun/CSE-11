/** Histogram
 * Usage: Histogram [ width height] filename
 * @author Kaiwen Sun (Email: kas003@ucsd.edu)
 * @version CSE11 Winter 2016
 */

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.IOException;
import java.lang.Runnable;
import java.awt.event.*;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class Histogram {

	private static int grid_w_pix = 0;	//grid width in pixels
	private static int grid_h_pix = 0;	//grid height in pixels
	private static int cell_size = 1;	//size in pixels of each grid square
	private static ArrayList<Integer> data = new ArrayList<Integer>();

	/** main method, read from file and draw histogram
	 * @param argv Usage: Histogram [ width height] filename
	 */
	public static void main(String[] argv) {
		Assert.assertion(argv.length==1 || argv.length==3, "Improper number of arguments");

		String filename;
		if(argv.length==1)
		{
			grid_w_pix = 600;
			grid_h_pix = 400;
			filename = argv[0];
		}
		else
		{
			try{
				grid_w_pix = Integer.parseInt(argv[0]);
				grid_h_pix = Integer.parseInt(argv[1]);
			}
			catch(NumberFormatException e){
				Assert.assertion(false,"Invalid Integer");
			}
			filename = argv[2];
		}
		
		Assert.assertion(grid_w_pix>0 && grid_h_pix>0,"Invalid Dimensions");

		readFiles(filename);
		cell_size = grid_w_pix/data.size();

		Assert.assertion(cell_size>=3, "Too many bins for pixel width");

		MyWindow window = new MyWindow(grid_w_pix,grid_h_pix,cell_size,data);
		window.pack();
		SwingUtilities.invokeLater(window);
		try 
		{
			System.out.format("Hit Return to exit program");
			System.in.read();
		}
		catch (IOException e){}
		window.dispatchEvent(new WindowEvent(window, 
			WindowEvent.WINDOW_CLOSING));
        window.dispose();	
	}

	/** read histogram data from file
	 * @param filename specifies the name of data file
	 */
	private static void readFiles(String filename){
		try{
			Scanner sc = new Scanner(new FileInputStream(filename));
			Assert.assertion(sc.hasNext(),"No values to display");
			while(sc.hasNext()){
				int value = sc.nextInt();
				Assert.assertion(value>=0,"Value is negative");
				data.add(value);
			}
		}
		catch(java.io.IOException | java.security.AccessControlException e){//| java.io.FileNotFoundException e){
				Assert.assertion(false,"File system error");
		}
/*		catch(IOException e){
			System.err.println("IOException caught!!!!!");
		}*/
		catch(java.util.InputMismatchException e){
			Assert.assertion(false,"Non-integer in file");
		}
	}

}

class MyWindow extends JFrame implements Runnable
{
	private	Grid grid;
	private ArrayList<Integer> data = null;

	/** Create a JFrame window
	 * @param win_w window width
	 * @param win_h window height
	 * @param cell_size bar width
	 * @param data data read from file to build histogram
	 */
	public MyWindow(int win_w, int win_h, int cell_size, ArrayList<Integer> data) {
		super();
		Grid grid = new Grid(data.size()*cell_size,win_h,cell_size);
		this.data = data;

		//setSize(win_w+100, win_h+100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(grid);
		setVisible(true);


		Integer maxbar = Collections.max(this.data);
		for(int i=0;i<grid.logic_w;i++)
		{
			grid.fillCell(i, (maxbar-data.get(i))*grid.pix_h/maxbar);
		}
	}



	public void run() {
	}
}

class Grid extends JPanel {
	private ArrayList<Point> fillCells;
	protected int logic_w = 0;	//grid logic width
	protected int logic_h = 0;	//grid logic height
	protected int pix_w = 0;		//grid pixel width
	protected int pix_h = 0;		//grid pixel width
	protected int cell_w = 1;			//width in pixels of each grid square
	protected int cell_h = 1;			//height in pixels of each grid square

	/** print Grid's field values, for debugging use only.
	 */
	private void debugger_printfields(){
		System.out.println("logic_w="+logic_w);
		System.out.println("logic_h="+logic_h);
		System.out.println("pix_w="+pix_w);
		System.out.println("pix_h="+pix_h);
		System.out.println("cell_w="+cell_w);
		System.out.println("cell_h="+cell_h);
	}

	/** create a grid
	 * @param pix_w width of grid in pixels
	 * @param pix_h height of grid in pixels
	 * @param cell_size width of bar width, in order to calculate logical size
	 */
	public Grid(int pix_w, int pix_h,int cell_size) {
		this.pix_w = pix_w;
		this.pix_h = pix_h;
		this.cell_w = cell_size;
		this.cell_h = 1;
		this.logic_w = this.pix_w/this.cell_w;
		this.logic_h = this.pix_h/this.cell_h;

		//debugger_printfields();

		fillCells = new ArrayList<Point>();
	}


	/** paint component
	 * @param g Graphics object used for drawing
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Point fillCell : fillCells) {
			int cellX = (fillCell.x * cell_w);
			int cellY = (fillCell.y * cell_h);
			g.setColor(Color.YELLOW);
			g.fillRect(cellX, cellY, cell_w, this.pix_h-cellY);	//let the bottom of vertical bar lands on the ground
			g.setColor(Color.BLACK);
			g.drawLine(cellX, cellY, cellX, pix_h);
			g.drawLine(cellX+cell_w, cellY, cellX+cell_w, pix_h);
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, pix_w, pix_h);

	}

	/** fill a point to grid's data
	 * @param x index of data
	 * @param y height of bar
	 */
	public void fillCell(int x, int y) {
		fillCells.add(new Point(x, y));
		repaint();
	}

	/** get preferred size of JPannel
	 * @return  Dimension(pix_w+2,pix_h+2)
	 */
	public Dimension getPreferredSize(){
		return new Dimension(pix_w+2,pix_h+2);
	}

}

class Assert{
	/** judge condition. if condition is true, return true. otherwise, print
	 * out error message and may exit
	 * @param cond condition to be judged
	 * @param errmsg error message to be printed out after "Reason:"
	 * @param exiting if cond==false, whether exit the program or not.
	 * @return cond
	 */
	protected static boolean assertion(boolean cond, String errmsg, boolean exiting)
	{
		if(cond)
			return true;
		System.err.println("Usage: Histogram [ width height] filename");
		System.err.println("Reason: "+errmsg);
		if(exiting)
			System.exit(-1);
		return false;
	}

	/** judge condition. a default exit version of assertion
	 * @param cond condition to be judged
	 * @param errmsg error message to be printed out after "Reason:"
	 * @return cond
	 */
	protected static boolean assertion(boolean cond, String errmsg)
	{
		//by default exit if error
		assertion(cond, errmsg, true);
		return cond;
	}
}
// vim: ts=4:sw=4:tw=78
