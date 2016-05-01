/** MergeSort extends Sorter
 * @author Kaiwen Sun (Email: kas003@ucsd.edu)
 * @version CSE11 Winter 2016
 */
import java.util.ArrayList;
public class MergeSort extends Sorter{

	/**
	 * Constructor of MergeSort
	 * @param array to be sorted. changes made to the array will affect bars appearance 
	 * @param win reference to MyWindow instance extended from JFrame
	 * @param waittime sleep duration in unit of ms. used to control speed.
	 */
	public MergeSort(ArrayList<Double> array,MyWindow win,int waittime){
		super(array,win,waittime);
	}
	
	/**
	 * Do merge sort
	 */
	public void sort(){
		try{
			mergeSort(0,array.size()-1);
			quitFromSort();
		}
		catch(StopSorterException e){
			DB.stdout("Mergesort sorterid="+this.sorterid+" returns (via StopSorterException)");
			return;
		}
	}
	
	/**
	 * recursively do merge sort
	 * @param left start index of left subarray
	 * @param right start index of right subarray
	 */
	private void mergeSort(final int left, final int right){
		if(left>=right)
			return;
		final int mid = (left+right)/2;
		mergeSort(left,mid);
		mergeSort(mid+1,right);
		int i=left,j=mid+1,target=left;
		ArrayList<Double> leftsubarray = new ArrayList<Double>(array.subList(left, mid+1));
		while(i<=mid || j<=right)
		{
			if(i==mid+1)
				break;	//set j=right+1 and set target if needed
				//array.set(target++, array.get(j++));
			else if(j==right+1)
				array.set(target++, leftsubarray.get((i++)-left));
			else{
				compare(i, j, target-1);//used for coloring and sleep, not for comparison
				if(this.amIAllowedToRun()==false)
					this.quitFromSort();
				if(leftsubarray.get(i-left)>=array.get(j))
					array.set(target++, array.get(j++));
				else
					array.set(target++, leftsubarray.get((i++)-left));				
			}	
		}
		stopCompare();
	}
}


