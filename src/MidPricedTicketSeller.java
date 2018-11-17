public class MidPricedTicketSeller extends TicketSeller {

	public MidPricedTicketSeller(Theater t, String name) {
		super(t, name);
	}

	@Override
	int[] checkAvailableSeats() {
		boolean[][] seat = getTheater().getSeats();
		int[] rowsToCheck = new int[] {4, 5, 3, 6, 2, 7, 1, 8, 0, 9};
		int rowCheckIndex = 0, column = 0;
		while(seat[rowsToCheck[rowCheckIndex]][column]) {
			if(column == 9) {
				if(rowCheckIndex == 9) {
					return null;
				}else{
					rowCheckIndex++;
					column = 0;
				}
			}else {
				column++;
			}
		}
		return new int[] {rowsToCheck[rowCheckIndex], column};
	}

	@Override
	public int getProcessingTime() {
		return (int)(Math.random() * 3) + 2;
	}

}
