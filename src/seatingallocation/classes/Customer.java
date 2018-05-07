package seatingallocation.classes;


public class Customer implements Comparable<Customer> {
	//class variables
	private String Name;
	private boolean Adult;
	private char BookingDisplay = '*';
	private String ClassType;
	private String SeatType;
	private int[] Seat;

	//default constructor
	public Customer() {}
	
	//creates an instance of customer
	public Customer(String name, boolean adult, String classType, String seatType) {
		Name = name;
		Adult = adult;
		ClassType = classType;
		SeatType = seatType;
		
		setBookingDisplay();
	}
	
	//getters and setters
	public String getName() {
			return Name;
	}
	
	public boolean isAdult() {
		return Adult;
	}
	
	public char getBookingDisplay() {
		return BookingDisplay;
	}
	
	public String getClassType() {
		return ClassType;
	}
	
	public String getSeatType() {
		return SeatType;
	}
	
	public int[] getSeat() {
		return Seat;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public void setAdult(boolean adult) {
		Adult = adult;
		setBookingDisplay();
	}
	
	public void setClassType(String classType) {
		ClassType = classType;
	}
	
	public void setSeatType(String seatType) {
		SeatType = seatType;
	}

	public void setSeat(int[] seat) {
		Seat = seat;
	}
	
	public void setBookingDisplay() {
		if(Adult) {
			BookingDisplay = 'A';
		}
		else {
			BookingDisplay = 'C';
		}
	}

	//formats customer details and prints them to the console
	public void print() {
		System.out.println(String.format("%1$s",
				"Name: " + Name + 
				"\nAdult: " + (Adult ? "Yes" : "No") + 
				"\nClass: " + ClassType + 
				"\nSeat Type: " + SeatType + 
				"\nSeat: " + (Seat[1] + 1) + Columns.byIndex(Seat[0]) + ".\n"));
		
	}
	
	//returns adulthood and seat of the customer for saving in file
	@Override
	public String toString() {
		if (this.Name != null) {
			return String.format("%1$-30s", 
					"\nThere is " + 
							(Adult ? "an Adult " : "a Child ") + 
							"in seat " + 
							(Seat[1] + 1) + 
							Columns.byIndex(Seat[0]) + ".");	
		}
		else {
			return String.format("%1$-30s","Seat " + (Seat[1] + 1) + Columns.byIndex(Seat[0]) + " is empty.");
		}
	}
	
	//compare method for sorting customers
	@Override
	public int compareTo(Customer c) {
		return Name.toLowerCase().compareTo(c.Name.toLowerCase());
	}

	
}
