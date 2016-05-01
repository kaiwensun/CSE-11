/** Define user interaction with AsciiGrid
 * @author Kaiwen Sun
 * @version CSE11 Spring 2015
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;
public class AsciiArt
{
	private AsciiGrid grid = new AsciiGrid(20,40);
	private ArrayList<AsciiShape> shapes_in_grid = new ArrayList<AsciiShape>();

	public static void main(String[] args)
	{
		AsciiArt art = new AsciiArt();
		Scanner sc = new Scanner(System.in);
		while(true)
		{
			String[] cmd = getInput(sc);
			try
			{
				int result = art.parseAndExec(cmd);
				if(result==-2)
					printbad("");
				else if(result==-1)
					printbad(cmd[0]);
				else if(result==0)
					System.out.println("OK");
				else if(result==1)
				{
					System.out.println("OK");
					break;
				}
			}
			catch(NumberFormatException e)
			{
				art.printbad(cmd[0]);
			}
			catch(Exception e)
			{
				continue;
			}
		}
	}

	/** get structuralized user input. already to lower case.
	 * @param sc is a Scanner
	 * @return an array (String[]) of cmd and parameters. return null if no input in the line
	 */
	private static String[] getInput(Scanner sc)
	{
		try{
			System.out.print("> ");
			String cmd = sc.nextLine().toLowerCase();
			String[] splited = cmd. trim().split("(\\s)+");
			if(splited.length==0 || (splited.length==1 && splited[0].length()==0))
				return (String[])null;	//input nothing
			return splited;
		}
		catch(NoSuchElementException e)
		{
			//EOF (Ctrl+Z) is input
			System.exit(-1);
		}
		catch(Exception e)
		{
			return null;
		}
		return null;
	}

	/** parse structualized user input and try to execute
	 * @param cmd user's input
	 * @return -2 bad input command, -1 bad input parameters, 0 ok, 1 exit
	 * if -2 is returned, the caller should print BAD INPUT
	 * if -1 is returned, the caller should print BAD INPUT: Invalid ... parameters
	 * if 0 is returned, the caller should print OK
	 * if 1 is returned, the caller should print OK and exit
	 */
	private int parseAndExec(String[] cmd) throws NumberFormatException
	{
		if(cmd==null)
			return -2;
		switch(parseCmd(cmd))
		{
			case 0:	//grid
				return parseExecGrid(cmd);
			case 1:	//print
				return parseExecPrint(cmd);
			case 2:	//oval
				return parseExecOval(cmd);
			case 3:	//rectangle
				return parseExecRectangle(cmd);
			case 4:	//triangle
				return parseExecTriangle(cmd);
			case 5:	//list
				return parseExecList(cmd);
			case 6:	//place
				return parseExecPlace(cmd);
			case 7:	//clear
				return parseExecClear(cmd);
			case 8:	//symbol
				return parseExecSymbol(cmd);
			case 9:	//exit
				return 1;
			default:
				return -2;
		}
	}
	
	/** parse and execute symbol command
	 * @param cmd 
	 * @throws NumberFormatException
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecSymbol(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=3)
			return -1;
		
		int id = Integer.parseInt(cmd[1]);
		if(id<0 || id>=this.shapes_in_grid.size())	//no such shape
			return -1;

		if(cmd[2]==null || cmd[2].length()==0 || cmd[2].charAt(0)==' ' || cmd[2].charAt(0)=='\t')
			return -1;	//illegal symbol. Actually I don't think this line will execute
		char symbol = cmd[2].charAt(0);

		this.shapes_in_grid.get(id).setSymbol(symbol);	//return void
		return 0;
	}

	/** parse and execute clear command
	 * @param cmd 
	 * @throws NumberFormatException
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecClear(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=4)
			return -1;
		int id = Integer.parseInt(cmd[1]);
		int r = Integer.parseInt(cmd[2]);
		int c = Integer.parseInt(cmd[3]);
		if(id<0 || id>=this.shapes_in_grid.size())	//no such shape
			return -1;
		grid.clearShape(shapes_in_grid.get(id),r,c);
		return 0;
	}
	

	/** parse and execute place command
	 * @param cmd 
	 * @throws NumberFormatException
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecPlace(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=4)
			return -1;
		int id = Integer.parseInt(cmd[1]);
		int r = Integer.parseInt(cmd[2]);
		int c = Integer.parseInt(cmd[3]);
		if(id<0 || id>=this.shapes_in_grid.size())	//no such shape
			return -1;
		grid.placeShape(shapes_in_grid.get(id),r,c);
		return 0;
	}
	
	/** parse and execute list command
	 * @param cmd 
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecList(String[] cmd)
	{
		if (cmd.length!=1)
			return -1;
		for(int i=0;i<shapes_in_grid.size();i++)
		{
			System.out.println(i+":");
			System.out.println(shapes_in_grid.get(i));
		}
		return 0;
	}


	/** parse and execute triangle command
	 * @param cmd 
	 * @throws NumberFormatException
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecTriangle(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=3)
			return -1;
		int h = Integer.parseInt(cmd[1]);
		int w = Integer.parseInt(cmd[2]);
		if(h<0 || w<0)
			return -1;
		Triangle shape = new Triangle(h,w,'#');
		this.shapes_in_grid.add(shape);
		return 0;
	}

	/** parse and execute rectangle command
	 * @param cmd 
	 * @throws NumberFormatException
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecRectangle(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=3)
			return -1;
		int h = Integer.parseInt(cmd[1]);
		int w = Integer.parseInt(cmd[2]);
		if(h<0 || w<0)
			return -1;
		Rectangle shape = new Rectangle(h,w,'#');
		this.shapes_in_grid.add(shape);
		return 0;
	}

	/** parse and execute oval command
	 * @param cmd 
	 * @throws NumberFormatException
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecOval(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=3)
			return -1;
		int h = Integer.parseInt(cmd[1]);
		int w = Integer.parseInt(cmd[2]);
		if(h<0 || w<0)
			return -1;
		Oval shape = new Oval(h,w,'#');
		this.shapes_in_grid.add(shape);
		return 0;
	}

	/** parse and execute print command
	 * @param cmd
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecPrint(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=1)
			return -1;
		System.out.println(grid);
		return 0;
	}

	/** parse and execute grid command
	 * @param cmd 
	 * @throws NumberFormatException
	 * @return 0 ok, -1 number of parameters error
	 */
	private int parseExecGrid(String[] cmd) throws NumberFormatException
	{
		if (cmd.length!=3)
			return -1;
		int h = Integer.parseInt(cmd[1]);
		int w = Integer.parseInt(cmd[2]);
		/* handled by AsciiGrid, not AsciiArt
		 * if such cases occur, AsciiGrid will create a zero size grid
		if(h<0 || w<0)
			return -1;
		*/
		this.grid = new AsciiGrid(h,w);
		return 0;
	}

	/** parse the command type
	 * @param cmd structuralized user input
	 * @return 0-grid, 1-print, 2-oval, 3-rectangle, 4-triangle, 5-list, 6-place, 7-clear, 8-symbol, 9-exit, others(neg)-invalid
	 */
	private static int parseCmd(String[] cmd)
	{
		if(cmd==null || cmd.length==0 || "".equals(cmd[0]))	return -1;
		if("grid".equals(cmd[0]))	return 0;
		if("print".equals(cmd[0]))	return 1;
		if("oval".equals(cmd[0]))	return 2;
		if("rectangle".equals(cmd[0]))	return 3;
		if("triangle".equals(cmd[0]))	return 4;
		if("list".equals(cmd[0]))	return 5;
		if("place".equals(cmd[0]))	return 6;
		if("clear".equals(cmd[0]))	return 7;
		if("symbol".equals(cmd[0]))	return 8;
		if("exit".equals(cmd[0]))	return 9;
		return -2;
	}

	/** print BAD INPUT information
	 * @param cmdtype usually is cmd[0]
	 */
	private static void printbad(String cmdtype)
	{
		if(cmdtype==null || cmdtype.equals(""))
			System.out.println("BAD INPUT");
		else
		{
			if(cmdtype.equals("oval") || cmdtype.equals("rectangle") || cmdtype.equals("triangle"))
				cmdtype="shape";
			System.out.println("BAD INPUT: Invalid "+cmdtype+" parameters");
		}
	}
}

