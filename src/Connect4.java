import javax.swing.*; //panels and frames and buttons and menus
import java.awt.*;	//colors and shapes and fonts
import java.awt.event.*; //listen to devices and buttons and menus

public class Connect4 extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{
	int[][] grid;
	JFrame frame;
	Font font=new Font("SansSerif",Font.PLAIN,25);

	String player1Name;
	String player2Name;

	int currentPlayer; //switches between player 1 and 2
	String currentName;

	int winner;
	String winMsg;
	int[][] winningCells;

	boolean gameOn;

	Color[] colors;

	int cheatKey = KeyEvent.VK_E;
	boolean cheatOn = false;

	int hoverCol = -1; // -1 means not over any column

	int startX = 500; // change this to move table left/right
	int startY = 210;  // change this to move table up/down
	int cellSize = 100;



	public Connect4()
	{
		frame=new JFrame("Connect 4 Game! by Mahit");
		frame.add(this); // add the panel to the frame...add canvas to frame
		frame.setSize(1200,600);

		this.addMouseListener(this); // make sure game is listening for mouse input
		this.addMouseMotionListener(this); // this is for the hover preview

		this.setFocusable(true);
		this.addKeyListener(this);

		getPlayerNames();
		setup();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // memory cleanup when closed
		frame.setVisible(true);

	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// background
		g.setColor(new Color(239,226,216));
		g.fillRect(0, 0, getWidth(), getHeight());

		int rows = grid.length; // 6
		int cols = grid[0].length; // 7

		int holeSize = 70; // size of each circle
		int gap = cellSize; // distance between columns/rows
		int pad = (gap - holeSize) / 2; // center hole in each gap

		int boardW = cols * gap;
		int boardH = rows * gap;

		// blue board
		g.setColor(new Color(18,107,201));
		g.fillRoundRect(startX, startY, boardW, boardH, 20, 20);
		g.setColor(Color.BLACK);
		g.drawRoundRect(startX, startY, boardW, boardH, 20, 20);

		// draw circles
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				int x = startX + c * gap + pad;
				int y = startY + r * gap + pad;

				if(grid[r][c] == 1)
					g.setColor(colors[1]);
				else if(grid[r][c] == 2)
					g.setColor(colors[2]);
				else
					g.setColor(new Color(239,226,216)); // empty hole

				g.fillOval(x, y, holeSize, holeSize);

				if (!gameOn && winner != 0 && isWinningCell(r, c))
				{
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(5));
					g2.setColor(Color.GREEN);   // highlight color
					g2.drawOval(x, y, holeSize, holeSize);
					g2.setStroke(new BasicStroke(1)); // reset
				}
				else
				{
					g.setColor(Color.BLACK);
					g.drawOval(x, y, holeSize, holeSize);
				}

			}
		}

		// letters A-G on top
		g.setFont(new Font("Arial Black", Font.BOLD, 36));
		g.setColor(Color.BLACK);

		for (int c = 0; c < 7; c++)
		{
			String letter = "" + (char)('A' + c);
			int x = startX + c * cellSize + cellSize / 2 - 12; // center-ish
			int y = startY - 20;
			g.drawString(letter, x, y);
		}

		// numbers 6-1 on left
		for (int r = 0; r < 6; r++)
		{
			String num = "" + (6 - r); // top row shows 6, bottom shows 1
			int x = startX - 45;
			int y = startY + r * cellSize + cellSize / 2 + 12;
			g.drawString(num, x, y);
		}


		// player text
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("Currently Playing: Player " + currentPlayer, 50, 150);
		if(!currentName.equals("Player 1") && !currentName.equals("Player 2"))
		{
			g.drawString(currentName, 50, 200);
		}

		// hover preview
		if(hoverCol != -1 && canMove(hoverCol))
		{
			int hx = startX + hoverCol * gap + pad;
			int hy = startY - gap + pad; // one row above board

			Graphics2D g2 = (Graphics2D) g; //cast to 2D Graphics
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f)); //make it transparent
			g2.setColor(colors[currentPlayer]);
			g2.fillOval(hx, hy, holeSize, holeSize);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}

		if(!gameOn)
		{
			g.setColor(Color.BLACK);

			g.drawString("Game Over!", 50, 300);

			if(winner == 0)
				winMsg = "Game ended in a Draw!";
			else
				winMsg = "Player " + winner + " wins!";

			g.drawString(winMsg, 50, 350);

			g.drawString("Press N to start a new game", 50, 400);
		}
	}

	public void getPlayerNames()
	{
		player1Name = JOptionPane.showInputDialog(frame, "Enter Player 1 name:");

		if(player1Name == null || player1Name.trim().isEmpty())
			player1Name = "Player 1";

		player2Name = JOptionPane.showInputDialog(frame, "Enter Player 2 name:");
		if(player2Name == null || player2Name.trim().isEmpty())
			player2Name = "Player 2";


		currentName = player1Name;

	}

	public void setup()
	{

		grid=new int[6][7];
		gameOn = true;

		winner = 0;
		winMsg = "";
		winningCells = new int[4][2];

		colors=new Color[3];
		colors[0]=Color.LIGHT_GRAY;
		colors[1]=Color.YELLOW;
		colors[2]=Color.RED;

		currentPlayer = 1;
		currentName = player1Name;
	}

	public boolean isFull()
	{
		for(int r=0;r<grid.length;r++)
		{
			for(int c=0;c<grid[r].length;c++)
			{
				if(grid[r][c]==0)
					return false;
			}
		}
		return true;
	}

	public boolean canMove(int col)
	{
		//if the top is empty you can move

		if(col < 0 || col >= grid[0].length) //invalid
			return false;

        return grid[0][col] == 0;
    }

	public void dropPiece(int column)
	{
		for(int r = grid.length - 1; r >= 0; r--)
		{
			if(grid[r][column] == 0)
			{

				grid[r][column] = currentPlayer;

				if(checkWin(currentPlayer))
				{
					winner = currentPlayer;
					gameOn = false;
				}
				else if(isFull())
				{
					winner = 0; // 0 means draw
					gameOn = false;
				}


				// switch players
				if(gameOn && !cheatOn)
				{
					if (currentPlayer == 1)
					{
						currentPlayer = 2;
						currentName = player2Name;
					}
					else
					{
						currentPlayer = 1;
						currentName = player1Name;
					}
				}



				break;
			}
		}
	}

	public int getCol(int mouseX)
	{
		int col = (mouseX - startX) / cellSize;

		if (col < 0 || col >= grid[0].length)
			return -1;

		return col;
	}

	public int getRow(int mouseY)
	{
		int row = (mouseY - startY) / cellSize;

		if (row < 0 || row >= grid.length)
			return -1;

		return row;
	}

	public boolean checkWin(int player)
	{
		int rows = grid.length;
		int cols = grid[0].length;

		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				if(grid[r][c] == player)
				{
					// horizontal
					if(c + 3 < cols && grid[r][c+1] == player && grid[r][c+2] == player && grid[r][c+3] == player)
					{
						for (int i = 0; i < 4; i++)
						{
							winningCells[i][0] = r;
							winningCells[i][1] = c + i;
						}

						return true;
					}

					// vertical
					if(r + 3 < rows && grid[r+1][c] == player && grid[r+2][c] == player && grid[r+3][c] == player)
					{
						for (int i = 0; i < 4; i++)
						{
							winningCells[i][0] = r + i;
							winningCells[i][1] = c;
						}
						return true;
					}

					// diagonal up to down
					if(r + 3 < rows && c + 3 < cols && grid[r+1][c+1] == player && grid[r+2][c+2] == player && grid[r+3][c+3] == player)
					{
						for (int i = 0; i < 4; i++)
						{
							winningCells[i][0] = r + i;
							winningCells[i][1] = c + i;
						}
						return true;
					}

					// diagonal down to up
					if(r - 3 >= 0 && c + 3 < cols && grid[r-1][c+1] == player && grid[r-2][c+2] == player && grid[r-3][c+3] == player)
					{
						for (int i = 0; i < 4; i++)
						{
							winningCells[i][0] = r - i;
							winningCells[i][1] = c + i;
						}
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isWinningCell(int row, int col)
	{
		if(winningCells == null)
			return false;

		for (int i = 0; i < 4; i++)
		{
			if (winningCells[i][0] == row && winningCells[i][1] == col)
				return true;
		}

		return false;
	}


	@Override
	public void mouseClicked(MouseEvent e)
	{
		//System.out.println(e.getX());
		//System.out.println(e.getY());

		int col = getCol(e.getX());
		int row = getRow(e.getY());

		System.out.println();
		//System.out.println("Col: "+col+" Row: "+row);

		if(col!=-1 && row!=-1 && canMove(col) && gameOn)
			dropPiece(col);

		repaint();

		//left-37, up-38, right-39, down-40
		// N is 78
		// Enter is 10

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		hoverCol = getCol(e.getX());
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}
	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			startX -= 5; // left
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			startX += 5; // right
		if(e.getKeyCode() == KeyEvent.VK_UP)
			startY -= 5; // up
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			startY += 5; // down
		if(e.getKeyCode() == KeyEvent.VK_COMMA)
			cellSize -= 5;
		if(e.getKeyCode() == KeyEvent.VK_PERIOD)
			cellSize += 5;

		repaint();

		//reset key
		if(e.getKeyCode() == KeyEvent.VK_N)
			setup();

		//rename key
		if(e.getKeyCode() == KeyEvent.VK_R)
			getPlayerNames();

		//cheat key
		if(e.getKeyCode() == cheatKey)
		{
			cheatOn = !cheatOn;
			gameOn = true;
			System.out.println("Cheat mode: " + cheatOn);
		}

	}

	@Override
	public void keyReleased(KeyEvent e)
	{

	}



	public static void main(String[] args)
	{
		Connect4 app=new Connect4();
	}
}
