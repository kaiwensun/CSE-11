/** BubbleSort extends Sorter
 * @author Kaiwen Sun (Email: kas003@ucsd.edu)
 * @version CSE11 Winter 2016
 */
import java.util.ArrayList;
public class BubbleSort extends Sorter{

	/**
	 * Constructor of BubbleSort
	 * @param array to be sorted. changes made to the array will affect bars appearance 
	 * @param win reference to MyWindow instance extended from JFrame
	 * @param waittime sleep duration in unit of ms. used to control speed.
	 */
	public BubbleSort(ArrayList<Double> array,MyWindow win,int waittime){
		super(array,win,waittime);
	}
	
	/**
	 * Do bubble sort
	 */
	public void sort(){
		try{
			DB.stdout("BubbleSort sorterid="+this.sorterid+" starts to sort() the array:");
			DB.stdout(DB.ArrayListToString(this.array));
			boolean isChanged = true;
			int end_of_comp = array.size()-1;
			while(isChanged)
			{
				isChanged = false;
				for(int i=0;i<end_of_comp;i++)
				{
					if(compare(i,i+1)>0)
					{
						swap(i,i+1);
						isChanged = true;
					}
					if(this.amIAllowedToRun()==false)
					{
						this.quitFromSort();
					}
				}
				end_of_comp--;
			}
			stopCompare();
			this.quitFromSort();
		}
		catch(StopSorterException e){
			DB.stdout("Bubblesort sorterid="+this.sorterid+" returns (via StopSorterException)");
			return;
		}
	}
}


