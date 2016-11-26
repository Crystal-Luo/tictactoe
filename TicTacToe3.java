import java.util.Scanner;
public class TicTacToe3 {
	private boolean win;
	private boolean draw;
	private Board b;
	public static void main (String [] args)
	{
		TicTacToe3 game = new TicTacToe3();
		game.run();
	}
	
	public TicTacToe3()
	{
		win = false;
		draw = false;
		b = new Board();
	}
	public void printLabeledBoard()
	{
		System.out.println("-------------------");
		for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y<3; y++)
            {
        		System.out.print("| " + x + ", " + y);
            }
            System.out.println("|\n-------------------");
        }
	}

	public void run()
	{
		Scanner reader = new Scanner(System.in);
		System.out.println("Welcome to Tic Tac Toe!");
		System.out.println("Here is the board layout with each square labeled: row, column");
		printLabeledBoard();
		//Board b = new Board();
		int[][] board = b.board();
		while (!win && !draw)
		{
			System.out.println("Enter your move");
			System.out.print("Row: ");
			int r = reader.nextInt();
			System.out.print("Col: ");
			int c = reader.nextInt();
			while (board[r][c] != 0)
			{
				System.out.println("Invalid square. Please reenter.");
				System.out.print("Row: ");
				r = reader.nextInt();
				System.out.print("Col: ");
				c = reader.nextInt();
			}
			b.makeMove('X', r, c);
			
			win = b.checkWin('X');
			System.out.println(b);
			draw = b.checkDraw();
			if (win)
			{
				System.out.println("You win!");
				System.exit(0);
			}
			
			if (!win && !draw)
			{
				System.out.println("Computer's turn");
				Move compMove = getMove('O', b);
				//getMove returns an array consisting of a score, an x value, and a y value. Use these in making the move.
				b.makeMove('O', compMove.row(), compMove.col());
				System.out.println(b);
				win = b.checkWin('O');
				draw = b.checkDraw();
			}
		}
		
		reader.close();
		if (win)
		{
			System.out.println("Game over!");
			System.exit(0);
		}
		
		else if (draw)
		{
			System.out.println("Draw!");
			System.exit(0);
		}
	}
	
	private Move getMove(char turn, Board board) //recursive method. Uses minimax algorithm.
	//It looks through every end possibility and assigns it a score. It chooses based off of those scores.
	{
		int score = board.getScore();
		if (score != 0) //if someone won
		{
			return new Move(score,-1,-1);
		}
		
		if (board.checkDraw()) //if board is completely filled
		{
			return new Move(0,-1,-1);
		}
		Board bTemp = board.copy();
		Move moveNow;
		
		Move[][] movesList = new Move[3][3];
		if (turn == 'X')
		{
			Move maxMove = new Move(-1, -1, -1);
			for (int x = 0; x < 3; x++)
			{
				for (int y = 0; y < 3; y++) //go over every spot on the board
				{
					if (bTemp.board()[x][y] == 0)
					{
						bTemp.makeMove(turn, x, y);
						moveNow = getMove('O', bTemp);
						movesList[x][y] = new Move(moveNow.score(), x, y);
					}
					bTemp = board.copy();
				}
			}
			 
			for (int x = 0; x < 3; x++)
			{
				for (int y = 0; y < 3; y++)
				{
					Move m = movesList[x][y];
					if (m != null && m.score() >= maxMove.score())
					{
						maxMove = m;
					}
				}
			}
			return maxMove;
		}
		else if (turn == 'O')
		{
			Move maxMove = new Move(1, -1, -1);
			for (int x = 0; x < 3; x++)
			{
				for (int y = 0; y < 3; y++) //go over every spot on the board
				{
					if (bTemp.board()[x][y] == 0)
					{
						bTemp.makeMove(turn, x, y);
						moveNow = getMove('X', bTemp);
						movesList[x][y] = new Move(moveNow.score(), x, y);
					}
					bTemp = board.copy();
				}
			}
			for (int x = 0; x < 3; x++)
			{
				for (int y = 0; y < 3; y++)
				{
					Move m = movesList[x][y];
					if (m != null && m.score() <= maxMove.score())
					{
						maxMove = m;
					}
				}
			}
			return maxMove;
		}
		else
		{
			throw new IllegalStateException("Turn invalid");
		}
	}
}
	
	
	
class Board {
	private int[][] board;
	public Board()
	{
		board = new int[3][3];
		for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y<3; y++)
            {
                board[x][y] = 0;
            }
        }
	}
	public String toString()
	{
		String printBoard = "-------------\n";
		for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y<3; y++)
            {
            	if (board[x][y] == 1)
            		printBoard += "| X ";
            	else if (board[x][y] == -1)
            		printBoard += "| O ";
            	else
            		printBoard += "|   ";
            }
            printBoard += "|\n-------------\n";
        }
		return printBoard;
	}
	public boolean checkWin(char turn)
	{
		int a;
		if (turn == 'X')
		{
			a = 1;
		}
		else
		{
			a = -1;
		}
		
		if (board[0][0]==a)
        {
            if ( (board[0][1]==a&&board[0][2]==a) || (board[1][0]==a&&board[2][0]==a) || (board[1][1]==a&&board[2][2]==a) )
            {
            	return true;
            }
        }
		if (board[0][1]==a)
        {
            if ( (board[0][0]==a&&board[0][2]==a) || (board[1][1]==a&&board[2][1]==a) )
            {
            	return true;
            }
        }
		if (board[0][2]==a)
        {
            if ( (board[0][0]==a&&board[0][1]==a) || (board[1][2]==a&&board[2][2]==a) || (board[1][1]==a&&board[2][0]==a) )
            {
            	return true;
            }
        }

		if (board[1][0]==a)
        {
            if ( (board[1][1]==a&&board[1][2]==a) || (board[0][0]==a&&board[2][0]==a))
            {
            	return true;
            }
        }
		if (board[1][1]==a)
        {
            if ( (board[0][0]==a&&board[2][2]==a) || (board[0][1]==a&&board[2][1]==a) || (board[0][2]==a&&board[2][0]==a) || (board[1][0]==a&&board[1][2]==a) )
            {
            	return true;
            }
        }
		if (board[1][2]==a)
        {
            if ( (board[1][0]==a&&board[1][1]==a) || (board[0][2]==a&&board[2][2]==a) )
            {
            	return true;
            }
        }
        
		if (board[2][0]==a)
        {
            if ( (board[2][1]==a&&board[2][2]==a) || (board[0][0]==a&&board[1][0]==a) || (board[1][1]==a&&board[0][2]==a) )
            {
            	return true;
            }
        }
		if (board[2][1]==a)
        {
            if ( (board[2][0]==a&&board[2][2]==a) || (board[0][1]==a&&board[1][1]==a) )
            {
            	return true;
            }
        }
		if (board[2][2]==a)
        {
            if ( (board[2][0]==a&&board[2][1]==a) || (board[0][2]==a&&board[1][2]==a) || (board[0][0]==a&&board[1][1]==a) )
            {
            	return true;
            }
        }
        return false;
	}
	
	public boolean checkDraw()
	{
		for (int x=0; x<3; x++)
		{
			for (int y=0; y<3; y++)
			{
				if (board[x][y] == 0)
					return false;
			}
		}
		return true;
	}
	
	public Board copy() //we want to pass a copy of the board to getMove(), not the board itself
	//We don't actually want to modify the board until a move has been determined
	{
		Board b2 = new Board();
		for (int x = 0; x<3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				b2.board[x][y] = board[x][y];
			}
		}
		return b2;
	}
	
	public int[][] board()
	{
		return board;
	}
	
	public void makeMove(char turn, int row, int col)
	{
		if (board[row][col] == 0)
		{
			if (turn == 'X')
				board[row][col] = 1;
			else
				board[row][col] = -1;
		}
	}
	
	public int getScore()
	{
		if (checkWin('X'))
			return 1;
		if (checkWin('O'))
			return -1;
		else
			return 0;
	}
}
class Move {
	private int x;
	private int y;
	private int score;
	public Move(int score, int x, int y)
	{
		this.score = score;
		this.x = x;
		this.y = y;
	}
	public String toString()
	{
		return "Score: " + score + "\nRow: " + x + "\nCol: " + y;
	}
	public int score()
	{
		return score;
	}
	public int row()
	{
		return x;
	}
	public int col()
	{
		return y;
	}
}