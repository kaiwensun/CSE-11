/** AnimatedSort
 * Usage: AnimatedSort [ width height] filename
 * @author Kaiwen Sun (Email: kas003@ucsd.edu)
 * @version CSE11 Winter 2016
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.ArrayList;
import java.lang.Runnable;

import java.util.Scanner;
import java.io.FileInputStream;
import java.util.Collections;

public class AnimatedSort{

	private static int grid_w_pix = 0;	//grid width in pixels
	private static int grid_h_pix = 0;	//grid height in pixels
	private static int cell_size = 1;	//size in pixels of each grid square
	private static ArrayList<Double> data = new ArrayList<Double>();		//array to be sorted

	/** main method, read from file and draw histogram
	 * @param argv Usage: AnimatedSort [ width height] filename
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
	}

	/** read histogram data from file
	 * @param filename specifies the name of data file
	 */
	private static void readFiles(String filename){
		try{
			Scanner sc = new Scanner(new FileInputStream(filename));
			Assert.assertion(sc.hasNext(),"No values to display");
			while(sc.hasNext()){
				double value = sc.nextDouble();
				Assert.assertion(value>=0,"Value is negative");
				data.add(value);
			}
			sc.close();
		}
		catch(java.io.IOException | java.security.AccessControlException e){//| java.io.FileNotFoundException e){
				Assert.assertion(false,"File system error");
		}
		catch(java.util.InputMismatchException e){
			Assert.assertion(false,"Non-double in file");
		}
	}
}

class MyWindow extends JFrame implements Runnable
{
	private	Grid grid;								//grid showing bars
	private ArrayList<Double> data = null;			//keep a constant copy of file data
	private ArrayList<Double> const_data = null;	//keep a constant copy of file data
	private double maxbar = 1;						//max value of data array. needed because max values might be overwritten during mergesort 
	private JPanel topbtn = initTopButtons();		//JPanel containing start and pause buttons
	private JComboBox sortcombo = initComboBox();	//JCombox choosing sort type
	private JSlider slider = initSlider();			//JSlider adjusting sorting speed

	private enum SortState{PAUSE,RUNNING};			//Sort State
	private enum SortMode{MERGE,BUBBLE};			//Sort Type
	private final int INIT_SLIDER_POSI = 1;			//sort fastest by default
	private final int INIT_SORT_WAITTIME = INIT_SLIDER_POSI*10;	//initial sortwaittime 
	private int sortwaittime = INIT_SORT_WAITTIME;	//how long should sorter sleep during when compare 
	private SortState sortstate = SortState.RUNNING;//Sort State
	private SortMode sortmode = SortMode.MERGE;		//SortMode

	private Sorter sorter;							//reference to currently active sorter

	/** Create a JFrame window
	 * @param win_w window width
	 * @param win_h window height
	 * @param cell_size bar width
	 * @param data data read from file to build histogram
	 */
	public MyWindow(int win_w, int win_h, int cell_size, ArrayList<Double> data) {
		super();
		const_data = new ArrayList<Double>(data);
		this.data = new ArrayList<Double>(const_data);
		maxbar = Collections.max(data);

		grid = new Grid(this.data.size()*cell_size,win_h,cell_size);

		//setSize(win_w+100, win_h+100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(BorderLayout.NORTH,topbtn);
		add(BorderLayout.EAST,sortcombo);
		add(BorderLayout.CENTER,grid);
		add(BorderLayout.SOUTH,slider);
		setVisible(true);

		drawGrid();
		pack();
	}

	/**
	 * create new sorter instance
	 * initiate data array and active latest sortmode 
	 * @return newly created sorter instance
	 */
	private Sorter createNewSorter(){
		ArrayCopier.copy(data, const_data);
		if(this.sortmode==SortMode.MERGE)
			return new MergeSort(this.data,this,sortwaittime);
		else if(this.sortmode==SortMode.BUBBLE)
			return new BubbleSort(this.data,this,sortwaittime);
		else
			return null;	//should never be here
	}
	
	/**
	 * fill bar cells and draw
	 */
	private void drawGrid(){
		for(int i=0;i<grid.logic_w;i++)
		{
			grid.fillCell(i, (int)(((double)maxbar-data.get(i))*grid.pix_h/maxbar));
		}
	}

	/**
	 * update bar cells and draw
	 */
	protected void reDrawGrid(){
		for(int i=0;i<grid.logic_w;i++)
		{
			grid.refillCell(i, (int)(((double)maxbar-data.get(i))*grid.pix_h/maxbar));
		}
	}

	/**
	 * initiate JPannel containing start and pause buttons
	 * @return JPannel reference
	 */
	private JPanel initTopButtons(){
		final JPanel topbtn = new JPanel();
		final JButton bstart = new JButton("start");
		final JButton bpause = new JButton("pause");
		bstart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DB.stdout("start button is pressed");
				if(sorter!=null){
					Sorter.allowedrunning_sortid+=1;	//stop the previous sort, and allow the newsort to run.
					sorter.resume();					//resume in case the sorter is paused
				}
				while(Sorter.currentlyrunning_sortid!=-1){
					try{
						Thread.sleep(1);
					}catch(InterruptedException e2){
					}
					//DB.stdout("New Sorter is waiting to be invoked.(Sorter.allowed="+Sorter.allowedrunning_sortid+",currentlyrun="+Sorter.currentlyrunning_sortid+")");
				}
				sorter = createNewSorter();
				bpause.setText("pause");
				sortstate=SortState.RUNNING;
				sorter.resume();
				if(sorter.sorterid<Sorter.allowedrunning_sortid)
					return;
				DB.stdout("New Sorter "+sorter.sorterid+" is going to be invoked.(Sorter.allowed="+Sorter.allowedrunning_sortid+",currentlyrun="+Sorter.currentlyrunning_sortid+")");
				new Thread(sorter).start();
			}
		});
		bpause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(sortstate==SortState.PAUSE){
					DB.stdout("resume button is pressed");
					bpause.setText("pause");
					sortstate=SortState.RUNNING;
					sorter.resume();
				}
				else if(sortstate==SortState.RUNNING){
					DB.stdout("pause button is pressed");
					bpause.setText("resume");
					sortstate=SortState.PAUSE;
					sorter.pause();
				}
			}
		});
		topbtn.add(bstart);
		topbtn.add(bpause);
		return topbtn;
	}
	
	/**
	 * initiate JComboBox instance
	 * @return JComboBox instance
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox initComboBox(){
		final DefaultComboBoxModel sortname = new DefaultComboBoxModel();
		sortname.addElement("Merge");
		sortname.addElement("Bubble");
		final JComboBox sortcombo = new JComboBox(sortname);
		sortcombo.setSelectedIndex(0);	//Selects the item at index anIndex.
		sortcombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if (sortcombo.getSelectedIndex() != -1) {
					DB.stdout("Selected: "+ sortcombo.getItemAt(sortcombo.getSelectedIndex()));
				}
				if("Merge".equals(sortcombo.getItemAt(sortcombo.getSelectedIndex()))){
					sortmode = SortMode.MERGE;
				}
				else if ("Bubble".equals(sortcombo.getItemAt(sortcombo.getSelectedIndex()))){
					sortmode = SortMode.BUBBLE;
				}
			}
		}); 
		return sortcombo;
	}
	
	/**
	 * initiate JSlider instance
	 * @return JSlider instance
	 */
	private JSlider initSlider(){
		final int FPS_MIN = 1;
		final int FPS_MAX = 100;
		final int FPS_INIT = INIT_SLIDER_POSI;    //initial frames per second
		JSlider slider = new JSlider(JSlider.HORIZONTAL,FPS_MIN, FPS_MAX, FPS_INIT);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				DB.stdout("slider is changed: "+((JSlider)e.getSource()).getValue());
				sortwaittime = ((JSlider)e.getSource()).getValue()*10;
				Sorter.waittime = sortwaittime;
			}
		});
		return slider;
	}
	
	/**
	 * grid getter
	 * @return grid
	 */
	protected Grid getGrid(){
		return grid;
	}

	/**
	 * Make MyWindow runnable 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
	}
}

class Grid extends JPanel {
	private ArrayList<Point> fillCells;
	protected int logic_w = 0;	//grid logic width
	protected int logic_h = 0;	//grid logic height
	protected int pix_w = 0;	//grid pixel width
	protected int pix_h = 0;	//grid pixel width
	protected int cell_w = 1;	//width in pixels of each grid square
	protected int cell_h = 1;	//height in pixels of each grid square

	protected int compared1 = -1;	//index of blue bar1
	protected int compared2 = -1;	//index of blue bar2

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
		for (int i=0;i<fillCells.size();i++) {
			Point fillCell = fillCells.get(i);
			int cellX = (fillCell.x * cell_w);
			int cellY = (fillCell.y * cell_h);
			if(i==compared1 || i==compared2)
				g.setColor(Color.BLUE);
			else
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
		//paintImmediately(0,0,this.pix_w,this.pix_h);
		repaint();
	}

	/** refill a point to grid's data
	 * @param x index of data
	 * @param y height of bar
	 */
	protected void refillCell(int x,int y){
		fillCells.set(x,new Point(x, y));
		//paintImmediately(0,0,this.pix_w,this.pix_h);
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
		System.err.println("Usage: AnimatedSort [ width height] filename");
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
class DB{
	//Debugger
	static boolean isDebuging = false;	//switch of debug mode
	
	/**
	 * System.out.println
	 * @param msg
	 */
	protected static void stdout(String msg){
		if(isDebuging){
			System.out.println(msg);
		}
	}
	
	/**
	 * pause until keyboard interrupt throws exception
	 */
	protected static void pause(){
		if(isDebuging){
			try{System.in.read();}
			catch(Exception e){}
		}
	}
	
	/**
	 * Convert ArrayList to string
	 * @param array
	 * @return string of array
	 */
	protected static String ArrayListToString(ArrayList<Double> array){
		if(isDebuging)
		{
			StringBuilder sb = new StringBuilder();
			for(Double d : array){
				sb.append(d);
				sb.append(" ");
			}
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * sleep for a while
	 * @param second duration of sleep
	 */
	protected static void sleep(int second){
		if(isDebuging){
			try{
				Thread.sleep(second*1000);
			}catch(InterruptedException e){
			}
		}
	}
}
class ArrayCopier{
	/**
	 * Copy content of ArrayList to another
	 * @param dst destination
	 * @param src source
	 * @return successfully copied or not
	 */
	static protected <T> boolean copy(ArrayList<T> dst,ArrayList<T> src){
		if(dst.size()!=src.size())
			return false;
		for(int i=0;i<dst.size();i++)
			dst.set(i, src.get(i));
		return true;
	}
}
// vim: ts=4:sw=4:tw=78
