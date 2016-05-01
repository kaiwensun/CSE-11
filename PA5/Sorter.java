/** Sorter
 * @author Kaiwen Sun (Email: kas003@ucsd.edu)
 * @version CSE11 Winter 2016
 */
import java.util.ArrayList;
public abstract class Sorter implements Runnable {

	protected ArrayList<Double> array;	//reference to the sorted array
	protected MyWindow win;				//reference to the JFrame
	protected static int waittime;		//control sorting speed, in ms

	protected int sorterid = 0;			//id of the current sorter instance 
	protected static int allowedrunning_sortid = 0;		//shared flag indicating which sorter is allowed to run
	protected static int currentlyrunning_sortid = -1;	//shared flag indicating which sorter is actually running
	private volatile boolean isPaused = false;					//shared flag indicating whether sorting is paused (busy waiting)
	private static int num_of_instance_created = 0;		//shared id recorder counting num of instances created

	/**
	 * Constructor of MyWindow
	 * @param array to be sorted. changes made to the array will affect bars appearance 
	 * @param win reference to MyWindow instance extended from JFrame
	 * @param waittime sleep duration in unit of ms. used to control speed.
	 */
	Sorter(ArrayList<Double> array,MyWindow win,int waittime){
		this.array = array;
		this.win = win;
		Sorter.waittime = waittime;
		this.sorterid = Sorter.num_of_instance_created++;
	}
	
	/**
	 * abstract sort() method to do sorting
	 */
	public abstract void sort();
	
	/** set sorting to pause.
	 */
	protected void pause(){
		isPaused = true;
	}
	
	/** resume sorting.
	 */
	protected void resume(){
		isPaused = false;
	}
	
	/** swap the array element at index i and j.
	 * @param i one element index
	 * @param j another element index
	 */
	protected void swap(int i, int j){
		Double tmp = array.get(i);
		array.set(i,array.get(j));
		array.set(j,tmp);
		stopCompare();
	}
	
	/** compare, color and check Sorter instance validation, and pause if needed.
	 * Pause by busy-wait if needed.
	 * Check instance validation before and after pause, so you don't have to wait for resumption when the Sorter should stop.
	 *  
	 * @param i value to be compared at index1 
	 * @param j value to be compared at index2
	 * @param dont_color_before don't color bars with index before dont_color_before (inclusive)
	 * @return array.get(i).compareTo(array.get(j));
	 */
	protected int compare(int i,int j,int dont_color_before)
	{
		Grid grid = win.getGrid();
		grid.compared1 = i<=dont_color_before?-1:i;
		grid.compared2 = j<=dont_color_before?-1:j;
		win.reDrawGrid();
		DB.stdout("Checking amIAllowedToRun()="+amIAllowedToRun()+" allowedid="+allowedrunning_sortid+" myid="+this.sorterid);
		if(this.amIAllowedToRun()==false)
			this.quitFromSort();
		DB.stdout("checking isPaused="+isPaused);
		while(isPaused){
			/*try{
				Thread.sleep(1);
			}
			catch(InterruptedException e){
			}*/
		}
		this.sleep();
		DB.stdout("checking isPaused="+isPaused);
		while(isPaused){
			/*try{
				Thread.sleep(1);
			}
			catch(InterruptedException e){
			}*/
		}	//if is paused, then busy wait
		DB.stdout("survive while(isPaused)");
		return array.get(i).compareTo(array.get(j));
	}
	
	/**
	 * compare(i,j,-1)
	 * @param i value to be compared at index1 
	 * @param j value to be compared at index2
	 * @return array.get(i).compareTo(array.get(j));
	 */
	protected int compare(int i,int j){
		return compare(i, j,-1);
	}
	
	/**
	 * set all blue bars back to yellow, and redraw grid.
	 */
	protected void stopCompare(){
		Grid grid = win.getGrid();
		grid.compared1 = -1;
		grid.compared2 = -1;
		win.reDrawGrid();
	}
	
	/**
	 * sleep for this.waittime ms
	 */
	protected void sleep(){
		try{
			Thread.sleep(waittime);
		}catch(InterruptedException e){
		}
	}
	
	/**
	 * start sorting.
	 * 1.register sorter itself at Sorter.currentlyrunning_sortid
	 * 2.sort()
	 * 3.deregister from Sorter.currentlyrunning_sortid 
	 */
	public void run(){
		DB.stdout("Sorterid="+this.sorterid+" starts to run.(allowed="+Sorter.allowedrunning_sortid+")");
		Sorter.currentlyrunning_sortid = this.sorterid;
		stopCompare();
		sort();
		if(Sorter.currentlyrunning_sortid==this.sorterid)	//in case currently-running id was changed by newly started sorter 
			Sorter.currentlyrunning_sortid = -1;
	}
	
	/**
	 * check whether the Sorter instance it self is allowed to continue running.
	 * @return true/false: whether allowed to run 
	 */
	protected boolean amIAllowedToRun(){
		DB.stdout(this.sorterid+" is "+(sorterid==allowedrunning_sortid)+" to run");
		return sorterid==allowedrunning_sortid;
	}

	/**
	 * deregister sorter itself, then throw StopSorterException
	 */
	protected void quitFromSort(){
		if(Sorter.currentlyrunning_sortid==this.sorterid)	//in case currently-running id was changed by newly started sorter
			Sorter.currentlyrunning_sortid = -1;
		DB.stdout("Sorterid="+this.sorterid+" quitFromSort. allowewd="+Sorter.allowedrunning_sortid);
		throw new StopSorterException("sorterid="+this.sorterid+" is stoped by quitFromSort()");
	}
}

class StopSorterException extends RuntimeException{
	/**
	 * Constructor of StopSorterException
	 * @param str exception message
	 */
	public StopSorterException(String str){
		super(str);
	}
}

