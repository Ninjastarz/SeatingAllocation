package seatingallocation.main;

import java.util.Scanner;

import seatingallocation.classes.*;

public class Main {

	public static void main(String[] args) {
		SeatManager sm = new SeatManager();
		sm.NewFlight();
		Customer defaultCustomer = new Customer("Levi", true, "business", "window");
		Customer defaultConfused = new Customer("George", true, "economy", "middle");
		Customer defaultPeasant = new Customer("Peter", true, "first", "window");
		Customer deefultCuster = new Customer("Tom", true, "business", "middle");
		sm.AddCustomer(defaultCustomer);
		sm.AddCustomer(defaultConfused);
		sm.AddCustomer(defaultPeasant);
		sm.AddCustomer(deefultCuster);
		
		boolean exit = false;
		boolean validInput;

		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to the seating manager, please enter a number to select your desired option:");

		while(!exit) {
			
			do {
				System.out.println("1 = Show seating plan\n2 = Create new customer\n3 = Cancel a customer\n4 = Search for a customer\n5 = Exit");
				validInput = true;
				switch (sc.nextLine()) {
				case "1":
					System.out.println("Seating Plan:");
					sm.PrintSeats();
					break;		
				case "2":
					Customer created = CreateCust(sc);
					sm.AddCustomer(created);
					System.out.println("This is your new customer.");
					created.print();
					
					break;
				case "3":
					System.out.println("Enter the name of the customer you wish to cancel.");
					Customer toDel = sm.BinarySearch(sc.nextLine());
					if (toDel != null) {
						sm.CancelSeatAllocation(toDel);
						System.out.println("Successfully cancelled customer booking");
					} else {
						System.out.println("Could not find a customer with that name.");
					}
					
					break;
				case "4":
					System.out.println("Enter the name of the customer you want to find");
					Customer searched = sm.BinarySearch(sc.nextLine());
					if (searched != null) {
						searched.print();
					} else {
						System.out.println("Could not find a customer with that name.");
					}
					break;
				case "5":
					System.out.println("Exiting program.");
					exit = true;
					break;
				default:
					System.out.println("Invalid input. please enter a number from 1 to 5");
					validInput = false;
					break;
				}
			} while(!validInput);
			
		}
		sc.close();
	}
	
	private static Customer CreateCust(Scanner sc) {
		Customer c = new Customer();

		boolean validInput;

		System.out.println("Enter name");
		String tryName = sc.nextLine();
		
		if (tryName.isEmpty()) {
			c.setName("Blank");
		}
		else {
			c.setName(tryName);
		}
		

		do {
			validInput = true;
			System.out.println("Is the customer an Adult?\n1 = Yes\n2 = No");
			switch(sc.nextLine()) {
			case "1":
				c.setAdult(true);
				break;
			case "2":
				c.setAdult(false);
				break;
			default:
				System.out.println("Invalid input. please enter number 1 for Adult or 2 for Child");
				validInput = false;
				break;
			}
		} while(!validInput);

		do {
			validInput = true;
			System.out.println("Preferred class\n1 = First class\n2 = Business class\n3 = Economy class");
			switch(sc.nextLine()){
			case "1":
				c.setClassType("first");
				break;
			case "2":
				c.setClassType("business");
				break;
			case "3":
				c.setClassType("economy");
				break;
			default:
				System.out.println("Invalid input. please enter a number 1 to 3");
				validInput = false;
				break;
			}
		} while(!validInput);

		validInput = true;

		do {
			validInput = true;
			System.out.println("Preferred seat\n1 = Window\n2 = Middle\n3 = Aisle");
			switch(sc.nextLine()){
			case "1":
				c.setSeatType("window");
				break;
			case "2":
				c.setSeatType("middle");
				break;
			case "3":
				c.setSeatType("aisle");
				break;
			default:
				System.out.println("Invalid input. please enter a number 1 to 3");
				validInput = false;
				break;
			}
		} while(!validInput);
		return c;
	}

	public static void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	}  
	
}