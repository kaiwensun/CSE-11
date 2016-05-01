/**
 * Provide a set of Array functions on an Array of ints
 * @author Kaiwen Sun
 * @version CSE11-WINTER16-PR2
 */
public class IntArray11
{
	private static int num_of_instance = 0;		//how many 
	private final static int MIN_LEN = 10;	//array size by default. Should not be 0 (see expandInternalArr())
	private int len;						//# of used elements, may or may not be equal to arr.length
	private int[] arr;						//internal array

	/** 
	 * 0-argument constructor. Valid instance of an Array of int,
	 * no ints are stored in the array.
	*/
	public IntArray11()
	{
		this(0);						//num_of_instance++ inside
	}

	/** 
	 * Store an array of size n. Initialize contents of the array to 
	 * be 1..n 
	 * @param size the number of elements to store in the array
	*/
	public IntArray11(int size)
	{
		if (size<0)
			size=0;								//avoid negative param error
		arr = new int[Math.max(size,MIN_LEN)];	//internal arr.length is not visible to users
		len = size;
		for(int i=0;i<len;i++)
			arr[i]=i+1;
		num_of_instance++;						//one more instance created
	}

	/** 
	 * Create an array of size n and store a copy of the contents of the
	 * input argument
	 * @param intArray array of elements to copy 
	*/
	public IntArray11(int[] intArray)
	{
		len = intArray.length;
		arr = new int[Math.max(intArray.length,MIN_LEN)];
		for (int i=0;i<len;i++)
			arr[i]=intArray[i];
		num_of_instance++;						//one more instance created
	}

	/* Make a string representation */
	/**
	 * Pretty Print  -- Empty String "[]"
	 *                  else "[e1, e2, ..., en]"
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for(int i=0;i<len;i++)
		{
			sb.append(arr[i]);
			if(i!=len-1)			//last elem is not followed by comma
				sb.append(", ");
		}
		sb.append(']');
		return sb.toString();		//convert StringBuffer to String
	}

	/* Getters and Setters */

	/** get the number of elements stored in the array  
	 * @return number of elements in the array
	*/
	public int getNelem()
	{
		return len; 
	}
	/** get the Element at index  
	 * @param index of data to retrieve 
	 * @return element if index is valid else  return 
	 * 		Integer.MIN_VALUE
	*/
	public int getElement(int index)
	{
		if(isValidIndex(index))
			return arr[index];
		return Integer.MIN_VALUE;
	}
	
	/** Determine how many IntArray11 Objects have been constructed 
	 * @return the number of times any of the constructors have been called 
	*/
	public static int getNArrays()
	{
		return num_of_instance;
	}
	/** retrieve a copy of the stored Array
	 * @return a copy of the Array. A new int array should be
	 * 		constructed of the correct size and values should
	 * 		copied into it.   
	*/
	public int[] getArray()
	{
		return this.getSlice(0,len);
	}

	/** set the value of an element in the stored arra
	 * @param index of element to store. Must be a valid index 
	 * @param element the data to insert in the array
	 * @return true if element set was successful
	*/
	public boolean setElement(int index, int element)
	{
		if (!isValidIndex(index))
			return false;
		arr[index]=element;
		return true;
	}

	/** Append element at the end of the array 
	 * @param element the data to append to the array
	 * @return true if element insertion was successful
	*/
	public boolean append(int element)
	{
		this.insert(len,element);
		return true;
	}

	/** Delete an element at index.
	 * @param index of element to delete 
	 * @return true if element deletion was successful, false otherwise
	*/
	public boolean delete(int index)
	{
		if(!isValidIndex(index))
			return false;
		for(int i=index+1;i<len;i++)
			arr[i-1]=arr[i];
		len--;
		return true;
	}

	/** Insert an element at the beginning of the array 
	 * @param element the data to insert in the array
	 * @return true if element insertion was successful
	*/
	public boolean insert(int element)
	{
		insert(0,element);
		return true;
	}

	/** Insert an element at index in the array
	 * @param index where to insert. Must be between 0 and number of
	 *              elements (inclusive) in the array. Inserting at 
	 *              index N of an N-element array is an append
	 * @param element the data to insert in the array
	 * @return true if element insertion was successful
	*/
	public boolean insert(int index, int element)
	{
		if(!isValidIndex(index) && index!=len)
			return false;

		//IntArray11Test helped me to fix it from if(index==arr.length)
		if (len==arr.length)
			expandInternalArr();
		for (int i=len;i>index;i--)
			arr[i]=arr[i-1];
		arr[index]=element;
		len++;
		return true;
	}


	/** reverse the order of the elements in the array 
	*/
	public void reverse()
	{
		reverse(0,len-1);
	}

	/** reverse the order of the elements in the array from start to
	*   to end index. Both indexes are included in the range. start &lt;= end. 
	*   @param start beginning index of to start the reverse
	*   @param end	ending index to end the reverse
	*   @return true if start and end index are valid, false otherwise
	*
	*/
	public boolean reverse(int start, int end)
	{
		if(isValidIndex(start) && isValidIndex(end) && start<=end)
		{
			while(start<end)
				swap(start++,end--);
			return true;
		}
		else
			return false;
	}

	/** swap two elements in the array 
	*   @param index1 index of first element 
	*   @param index2 index of second element
	*   @return true if index1 and index2 are valid, false otherwise
	*
	*/
	public boolean swap(int index1, int index2)
	{
		if (isValidIndex(index1) && isValidIndex(index2))
		{
			int tmp = arr[index1];
			arr[index1]=arr[index2];
			arr[index2]=tmp;
			return true;
		}
		else
			return false;
	}

	/** get a slice of the array
	 *	@param start index of start element (inclusive)
	 *	@param end index of end element (not inclusive)
	 *	@return an int array of length (end-start); {} if invalid parameters
	 *
	 */
	private int[] getSlice(int start, int end_exclusive)
	{
		if (end_exclusive<=start)
			end_exclusive=start;
		int[] rtn = new int[end_exclusive-start];
		for (int i=start;i<end_exclusive;i++)
			rtn[i-start] = arr[i];
		return rtn;
	}

	/** double the size of internal arr. User of this IntArray11 will not
	 * know this size change. Copy the content to new array. It is impossible
	 * for arr.length to be less than MIN_LEN.
	 * @param arr to be expanded
	 * @return expanded new arr
	 *
	 */
	private void expandInternalArr()
	{
		int[] newarr = new int[2*arr.length];
		for (int i=0;i<arr.length;i++)
			newarr[i]=arr[i];
		arr = newarr;
	}
	private boolean isValidIndex(int index)
	{
		return index>=0 && index<len;
	}
	
	/** for test only
	 *
	 */
	private String stringOfAllInfo()
	{
		String rtn = "num_of_instance="+num_of_instance+",MIN_LEN="+MIN_LEN+",len="+len+",arr=[";
		for(int i=0;i<len;i++)
			rtn+=arr[i]+",";
		rtn+="]";
		return rtn;
	}
}
// vim: ts=4:sw=4:tw=78:
