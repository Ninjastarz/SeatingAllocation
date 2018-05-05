package seatingallocation.main;
import java.util.ArrayList;
import java.util.List;

import seatingallocation.classes.*;
public class Main {

	public static void main(String[] args) {
		SeatManager sm = new SeatManager();
		List<Customer> customers = new ArrayList<Customer>();
		sm.NewFlight();
		Customer noob = new Customer("Levi", true, "economy", "middle");
		sm.AddCustomer(noob);
		System.out.println(sm.getSeats()[1][6].toString());
		sm.PrintSeats();
		System.out.println(noob.toString());
	}

}
