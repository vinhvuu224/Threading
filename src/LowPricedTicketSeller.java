public class LowPricedTicketSeller extends TicketSeller {
	
	public LowPricedTicketSeller(Theater t, String name) {
		super(t, name);
	}

	@Override
	int[] checkAvailableSeats() {
		boolean[][] seat = getTheater().getSeats();
		int row = 9, column = 0;
		while(seat[row][column]) {
			if(column == 9) {
				if(row == 0) {
					return null; //No seats available
				}else {
					column = 0;
					row--;
				}
			}else {
				column++;
			}
		}
		return new int[] {row, column};
	}

	@Override
	public int getProcessingTime() {
		return (int)(Math.random()* 4) + 4;
	}
	
}

