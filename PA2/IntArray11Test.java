/**
 * Test  
 * @author Kaiwen Sun
 * @version CSE11-Winter2016-PR2
 */
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
public class IntArray11Test
{
	private static final int NELEM=100;

	
	/** Main method **/ 
	public static void main(String [] args)
	{

		System.out.println("Starting Tests");

		testGetArray();
		testGetNArrays();
		testConstructors();
		testToString();
		testGetNelem();
		testGetElement();
		testSetElement();
		testAppend();
		testDelete();
		testInsert();
		testReverse();
		testSwap();

		// All Tests Completed
		System.out.println("SUCCESS! All Tests completed without error");
	}

	/* some general methods building test framework */
	
	/** print out which testfunction is starting to be called, by reading the function
	 * stack.
	 *
	 */
	private static void startTest()
	{
		int depth = 1;
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		System.out.println(ste[ste.length - 1 - depth].getMethodName()+" starts");
	}

	/** print out which testfucntion is endding from being called, by reading
	 * the function stack. also provide a spliter line after printing
	 * testcases.
	 *
	 */
	private static void endTest()
	{
		int depth = 1;
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		System.out.println(ste[ste.length - 1 - depth].getMethodName()+" ends");
		System.out.println("================================================");
	}

	/** print out error message, stop testing, and exit program
	 *	@param messsage is the message to be printed out
	 */
	private static void abort(String message)
	{
		System.out.println("Error in test: " + message);
		System.exit(-1);
	}


	/** Various Testers **/
	private static void testGetArray()
	{
		startTest();

		IntArray11 tArray;
		// Create a test array 1..NELEM
		tArray=new IntArray11(NELEM);
		int [] cArray;
		
		// Check that array created was returned properly
		System.out.println("Testing getArray");
		cArray = tArray.getArray();
		if (cArray.length != NELEM)
			abort("Array length should be " + NELEM + " returned " + cArray.length);
		for (int i = 0; i < NELEM; i++)
			if (cArray[i] != i+1)
				abort("Element at " + i + " was " + cArray[i] + " expected " + (i+1));

		// Check the getArray() returned a COPY of the internal array.
		cArray[0]=999999;
		cArray=tArray.getArray();
		if(cArray[0]!=1)
			abort("getArray() doesn't return a COPY of internal array");

		endTest();
	}

	private static void testGetNArrays()
	{
		startTest();

		final int NBUILD = 10;
		IntArray11 tArray = new IntArray11();
		int nbuilt = IntArray11.getNArrays(); 

		for (int i = 0; i < NBUILD; i++)
		{
			tArray = new IntArray11();
			tArray = new IntArray11(NELEM);
			tArray = new IntArray11(tArray.getArray());
		} 
		// Check getNArrays 
		System.out.println("Testing getNArrays");
		if (tArray.getNArrays() != (3 * NBUILD) + nbuilt)
			abort("Not counting number of array builds correctly: " + tArray.getNArrays());

		endTest();
	}

	private static void testConstructors()
	{
		startTest();

		IntArray11 t = new IntArray11();
		if(t.getNelem()!=0)
			abort("t.getNelem()!=0");
		t = new IntArray11(-3);
		if(t.getNelem()!=0)
			abort("t.getNelem()!=0");
		t = new IntArray11(0);
		if(t.getNelem()!=0)
			abort("t.getNelem()!=0");
		t = new IntArray11(3);
		if(t.getNelem()!=3)
			abort("t.getNelem()!=3");
		t = new IntArray11(100);
		if(t.getNelem()!=100)
			abort("t.getNelem()!=100");
		int []target = {1,2,3};
		t = new IntArray11(3);
		if(!Arrays.equals(target,t.getArray()))
			abort("IntArray11(i) fials. t != {1,2,3}");
		t = new IntArray11(target);
		if(!Arrays.equals(target,t.getArray()))
			abort("IntArray11(arr) fails. t != {1,2,3}");
		int []target2 = {342,54,76,987,765,654,233,546,76,564543,54,3,6,843,21,23,6,86534,54,54};
		t = new IntArray11(target2);
		if(!Arrays.equals(target2,t.getArray()))
			abort("IntArray11(arr) fails. t != {1,2,3}");

		endTest();
	}

	private static void testToString()
	{
		startTest();

		ArrayList<Integer> target = new ArrayList<Integer>();
		IntArray11 test = new IntArray11();
		for(int i=-100;i<100;i++)
		{
			target.add(i);
			test.append(i);

			if (!target.toString().equals(test.toString()))
				abort(target.toString()+" toString fails");
		}

		endTest();

	}

	private static void testGetNelem()
	{
		startTest();

		ArrayList<Integer> target = new ArrayList<Integer>();
		IntArray11 test = new IntArray11();
		for(int i=-100;i<100;i++)
		{
			int applen = (int)(Math.random()*10);
			for(int j=0;j<applen;j++)
			{
				target.add(i);
				test.append(i);
			}

			if (target.size()!=test.getNelem())
				abort("There should be "+target.size()+" elements in "+target+", but test.getNelem() returns "+test.getNelem()+" for "+test);
		}

		endTest();
	}

	private static void testGetElement()
	{
		startTest();
		
		final int NELE = 1000;
		IntArray11 test = new IntArray11(NELE);
		for(int i=-NELE;i<2*NELE;i++)
		{
			if(i>=0 && i<NELE)
			{
				if(i+1!=test.getElement(i))
					abort("the no."+i+" element in test is not "+(i+1));
			}
			else
			{
				if(test.getElement(i)!=Integer.MIN_VALUE)
					abort("the no."+i+" element in test is not Integer.MIN_VALUE");
			}
		}

		endTest();
	}

	private static void testSetElement()
	{
		startTest();

		final int NELE = 100;
		Random rg = new Random();
		int[] target = new int[100];
		for (int i=0;i<NELE;i++)
			target[i]=rg.nextInt();
		IntArray11 test = new IntArray11(target);
		for(int i=0;i<1000;i++)
		{
			int index = (Math.abs(rg.nextInt())%(3*NELE))-(int)(NELE);
			int value = rg.nextInt();
			boolean success = true;
			try{
				target[index]=value;
			}
			catch(IndexOutOfBoundsException e)
			{
				success = false;
				if(test.setElement(index,value)==true)
					abort("test.setElement("+index+","+value+"should not succeed");
			}
			if(success)
			{
				if(test.setElement(index,value)==false)
					abort("test.setElement("+index+","+value+"should not fail");
			}
			if(!Arrays.equals(target,test.getArray()))
				abort("The no."+index+"element doesn't match!");
		}

		endTest();
	}

	private static void testAppend()
	{
		startTest();

		final int NELE = 100;
		Random rg = new Random();
		ArrayList<Integer> target = new ArrayList<Integer>(NELE);
		IntArray11 test = new IntArray11();
		for(int i=0;i<NELE;i++)
		{
			int value = rg.nextInt();
			target.add(value);
			test.append(value);
			if(!target.toString().equals(test.toString()))
				abort("Append no."+i+"element fails");
		}

		endTest();
	}

	private static void testDelete()
	{
		startTest();

		final int NELE = 100;
		Random rg = new Random();
		ArrayList<Integer> target = new ArrayList<Integer>(NELE);
		IntArray11 test = new IntArray11();
		for(int i=0;i<NELE;i++)
		{
			int value = rg.nextInt();
			target.add(value);
			test.append(value);
		}
		for(int i=NELE;i>0;i--)
		{
			boolean success = true;
			int index = (Math.abs(rg.nextInt())%(3*NELE))-(int)(NELE);
			try{
				target.remove(index);
			}
			catch(IndexOutOfBoundsException e){
				success = false;
				if(test.delete(index)==true)
					abort("test.delete("+index+") on array len="+test.getNArrays()+" should not succeed");
				i++;
			}
			if(success)
			{
				if(test.delete(index)==false)
					abort("test.delete("+index+") on array len="+test.getNArrays()+" should not fail");
			}
			if(!target.toString().equals(test.toString()))
				abort("Delete no."+index+" element fails");
		}

		endTest();
	}

	private static void testInsert()
	{
		startTest();

		/* Test IntArray11.insert(index, value) */
		final int NELE = 100;
		Random rg = new Random();
		ArrayList<Integer> target = new ArrayList<Integer>(NELE);
		IntArray11 test = new IntArray11();
		for(int i=1;i<NELE+1;i++)
		{
			boolean success = true;
			int index = (Math.abs(rg.nextInt())%(3*NELE))-(int)(NELE);
			int value = rg.nextInt();
			try{
				target.add(index,value);
			}
			catch(IndexOutOfBoundsException e)
			{
				success = false;
				i--;
				if(test.insert(index,value)==true)
					abort("Insert "+value+" to no."+index+"should fail, but test.insert() succeeded. (target.size()="+target.size()+", test.getNelem()="+test.getNelem()+")");
			}
			if (success)
			{
				if(test.insert(index,value)==false)
						abort("Insert "+value+" to no."+index+" slot should succeed, but test.insert() failed. (target.size()="+target.size()+", test.getNelem()="+test.getNelem()+")");
			}
			if(!target.toString().equals(test.toString()))
			{
				abort("Insert "+value+" to no."+index+" slot fails(target.size()="+target.size()+", test.getNelem()="+test.getNelem()+", i="+i+")(target=="+target+", test=="+test+")");
			}

		}

		/* Test IntArray11.insert(value) */
		for(int i=0;i<10;i++)
		{
			target.add(0,i);
			test.insert(i);
			if(!target.toString().equals(test.toString()))
				abort("insert("+i+") fails");
		}

		endTest();
	}

	private static void testReverse()
	{
		startTest();

		final int NELE = 1000;
		IntArray11 test = new IntArray11(NELE);

		
		ArrayList<Integer> target = new ArrayList<Integer>(NELE);
		for(int i=1;i<=NELE;i++)
			target.add(i);

		Random rg = new Random();
		//test reverse(l,r)
		for(int i=0;i<NELE*4;i++)
		{
			/* WARNING: left>right case may be tested
			 * This is not clarified by the assignment
			 * The assumption is that IntArray11 will do nothing in this case.
			 * That is how I implemented my IntArray11
			 * If your IntArray11 doesn't do this, the test will fail (If your
			 * IntArray11 doesn't raise an exception, the test will not abort).
			 */
			int left = Math.abs(rg.nextInt())%NELE;
			int right = Math.abs(rg.nextInt())%NELE;
			test.reverse(left,right);
			if(left<=right)
			{
				for(int l=left,r=right;l<r;l++,r--)
				{
					Integer tmpInteger = target.get(l);
					target.set(l,target.get(r));
					target.set(r,tmpInteger);
				}
			}
			if(!target.toString().equals(test.toString()))
			{
				if(left>right)
					System.out.println("inverse "+left+" and "+right+" fails (IntArray11Test assumes IntArray11 will do nothing when left>left, and this case is tested.)");
				else
					abort("inverse "+left+" and "+right+" fails (target="+target+"; test="+test+")");
			}
		}

		//test reverse()
		test.reverse();
		Collections.reverse(target);
		if(!target.toString().equals(test.toString()))
			abort("inverse() fails");

		test.reverse();
		Collections.reverse(target);
		if(!target.toString().equals(test.toString()))
			abort("inverse() fails");

		endTest();
	}

	private static void testSwap()
	{
		startTest();
		final int NELE = 100;
		Random rg = new Random();
		ArrayList<Integer> target = new ArrayList<Integer>(NELE);
		IntArray11 test = new IntArray11();

		for(int i=0;i<NELE;i++)
		{
			int value = rg.nextInt();
			target.add(value);
			test.append(value);
		}
		for(int i=0;i<3*NELE;i++)
		{
			int index1 = (Math.abs(rg.nextInt())%(3*NELE))-(int)(NELE);
			int index2 = (Math.abs(rg.nextInt())%(3*NELE))-(int)(NELE);
			boolean success = true;
			try{
				Integer tmpInteger = target.get(index1);
				target.set(index1,target.get(index2));
				target.set(index2,tmpInteger);
			}
			catch(IndexOutOfBoundsException e)
			{
				if(test.swap(index1,index2)==true)
					abort("swaping no."+index1+" and no."+index2+" element should not succeed (len="+NELE);
				success = false;
			}
			if(success)
			{
				if(test.swap(index1,index2)==false)
					abort("swaping no."+index1+" and no."+index2+" element should not fail (len="+NELE);
			}
			if (!target.toString().equals(test.toString()))
				abort("after swaping no."+index1+" and no."+index2+" element, the array is not as expected");
		}

		endTest();
	}

}
// vim: ts=4:sw=4:tw=78:
