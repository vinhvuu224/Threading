import java.util.Arrays;
import java.util.Scanner;

public class Theater implements Runnable{
	
	private int theaterSize;
	private int numOfCustomers;
	private boolean[][] seats;
	private int currentTime = 0;
	private TicketSeller[] sellers;
	private String[][] printedSeats;
	private Object lock = new Object();
	
	public Theater(int theaterSize, int numOfCustomers) {
		this.theaterSize = theaterSize;
		this.numOfCustomers = numOfCustomers;
		seats = new boolean[theaterSize][theaterSize];
		printedSeats = new String[theaterSize][theaterSize];
		
		//initialize empty seats with dashes
		for (int i = 0; i<theaterSize;i++) {
			for(int j = 0; j<theaterSize;j++) {
				printedSeats[i][j]="____";
			}
		}
		
		sellers = new TicketSeller[] {
				new HighPricedTicketSeller(this, "H0"),
				
				new MidPricedTicketSeller(this, "M1"),
				new MidPricedTicketSeller(this, "M2"),
				new MidPricedTicketSeller(this, "M3"),
				
				new LowPricedTicketSeller(this, "L1"),
				new LowPricedTicketSeller(this, "L2"),
				new LowPricedTicketSeller(this, "L3"),
				new LowPricedTicketSeller(this, "L4"),
				new LowPricedTicketSeller(this, "L5"),
				new LowPricedTicketSeller(this, "L6")
		};
	}
	
	public Object getLock() {
		return lock;
	}
	
	public int getTheaterSize() {
		return theaterSize;
	}
	
	public int getNumOfCustomers() {
		return numOfCustomers;
	}
	
	public int getCurrentTime() {
		return currentTime;
	}
	
	public boolean[][] getSeats(){
		return seats;
	}
	
	public void printSeats() {
		for(String[] row : printedSeats) {
			System.out.println(Arrays.toString(row));
		}
	}
	
	public void assignSeats(int row, int column, String customerName) {
		seats[row][column] = true;
		printedSeats[row][column] = customerName;
	}
	
	public void run() {

		boolean end = false;
		
		for(TicketSeller ts : sellers) {
			new Thread(ts).start();
		}
		
		while(!end&&currentTime<60) {
			end = true;
			for(TicketSeller ts : sellers) {
				if(ts.hasCustomersRemaining()) {
					end = false;
					ts.resetProcessedStatus();
				}
			}
			
			boolean processed = false;
			while(!processed) {
				processed = true;
				for(TicketSeller ts : sellers) {
					processed = processed && ts.hasBeenProcessed();
				}
			}
			currentTime++;
			
			System.out.println("0:" + String.format("%02d", currentTime));
			
		}
		System.out.println("Finished");
	}
	
	public static void main (String[] args){
		
		//Use command line for argument
		if (args.length>0) {
			Theater t = new Theater(10,new Integer(args[0]));
			new Thread(t).start();
		}
		//Use console for argument
		else {
			System.out.println("Enter how many customers for each seller:");
			Scanner scan = new Scanner(System.in);
			Theater t = new Theater(10,scan.nextInt());
			new Thread(t).start();
		}
		
		
	}
	
}
