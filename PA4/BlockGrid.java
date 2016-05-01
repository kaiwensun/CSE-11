import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.IOException;
import java.lang.Runnable;
import java.awt.event.*;

public class BlockGrid {

	public static void main(String[] a) {
		MyWindow window = new MyWindow();
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

}

class MyWindow extends JFrame implements Runnable
{
	private	Grid grid;
	public MyWindow() {
		super();
		Grid grid = new Grid();
		setSize(810, 540);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(grid);
		setVisible(true);
		grid.fillCell(0, 0);
		grid.fillCell(79, 0);
		grid.fillCell(0, 49);
		grid.fillCell(79, 49);
		grid.fillCell(39, 24);
	}

	public void run() {
	}
}

class Grid extends JPanel {
	private ArrayList<Point> fillCells;

	public Grid() {
		fillCells = new ArrayList<Point>();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Point fillCell : fillCells) {
			int cellX = (fillCell.x * 10);
			int cellY = (fillCell.y * 10);
			g.setColor(Color.RED);
			g.fillRect(cellX, cellY, 10, 10);
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 800, 500);

		for (int i = 0; i < 800; i += 10) {
			g.drawLine(i, 0, i, 500);
		}

		for (int i = 0; i < 500; i += 10) {
			g.drawLine(0, i, 800, i);
		}
	}

	public void fillCell(int x, int y) {
		fillCells.add(new Point(x, y));
		repaint();
	}

}
// vim: ts=4:sw=4:tw=78
