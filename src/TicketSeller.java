import java.util.Arrays;

public abstract class TicketSeller implements Runnable {

	private Theater theater;
	private int[] customers; // Array of arrival times for customers
	private int customerIndex = 0;
	private int arrivalIndex = 0;
	private int nextAvailable = 0;
	private boolean processedTime = false;
	private String name;

	public TicketSeller(Theater t, String name) {
		theater = t;
		customers = generateCustomers(t.getNumOfCustomers());
		System.out.println(name + "Queue: " + Arrays.toString(customers));
		this.name = name;
	}

	abstract int[] checkAvailableSeats();

	public abstract int getProcessingTime();

	private int[] generateCustomers(int length) {
		int[] ret = new int[length];
		for (int i = 0; i < length; i++) {
			ret[i] = (int) (Math.random() * 60);
		}
		Arrays.sort(ret);
		return ret;
	}

	public Theater getTheater() {
		return theater;
	}

	public int[] getCustomerArray() {
		return customers;
	}

	public void makeSale() {
		if (customers[customerIndex] <= theater.getCurrentTime()) {
			synchronized (theater.getLock()) {
				int[] seatIndex = checkAvailableSeats();
				if (seatIndex != null) {
					//Check customerIndex to adjust formatting
					if(customerIndex+1<10){
						theater.assignSeats(seatIndex[0], seatIndex[1], name + "0" + (customerIndex+1));
					}else{
						theater.assignSeats(seatIndex[0], seatIndex[1], name + "" + (customerIndex+1));
					}
					
					nextAvailable = theater.getCurrentTime() + getProcessingTime();
					System.out.println(name + " Assigned Seat: " + "0:" + String.format("%02d", theater.getCurrentTime()));
					theater.printSeats();
				} else {
					System.out.println(name + " Theater full: " + "0:" + String.format("%02d", theater.getCurrentTime()));
					// theater.printSeats();
				}
				customerIndex++;
			}
		}
	}

	public boolean hasBeenProcessed() {
		return processedTime;
	}

	public void resetProcessedStatus() {
		processedTime = false;
	}

	@Override
	public void run() {
		System.out.println(name + " Start");
		//Var to print sold time once
		boolean once = false;
		while (hasCustomersRemaining()) {
			
			synchronized (this) {
				while (arrivalIndex < customers.length && customers[arrivalIndex] == theater.getCurrentTime()) {
					
					System.out.println(name + " Arrived: " + "0:" + String.format("%02d", theater.getCurrentTime()));
					arrivalIndex++;
					//Reactivate once for use in next customer
					if (once) {
						once = false;
					}
				}
				//Check if seller has completed sale
				if (nextAvailable!=0&&nextAvailable==theater.getCurrentTime()&&!once) {
					System.out.println(name + " Sold: " + "0:" + String.format("%02d", theater.getCurrentTime()));
					makeSale();
					once=true;
				}
				//else continue makeSale per usual
				else if (nextAvailable <= theater.getCurrentTime()) {
					makeSale();
				}
				processedTime = true;
			}
		}
		processedTime = true;
		System.out.println(name + " Done");
	}

	public boolean hasCustomersRemaining() {
		return customerIndex < customers.length;
	}
}
