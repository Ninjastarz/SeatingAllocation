package seatingallocation.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;


public class SeatManager {
	private Customer[][] Seats;
	
	private Customer[] Customers;

	public SeatManager() {}
	
	//getters and setters
	public Customer[][] getSeats() {
		return Seats;
	}

	public void setSeats(Customer[][] seats) {
		Seats = seats;
	}
	
	public Customer[] getCustomers() {
		return Customers;
	}

	public void setCustomers(Customer[] customers) {
		Customers = customers;
	}

	//searches the seat bookings for a suitable seat
	public void AddCustomer(Customer c) {

		int start = 0;
		int end = 0;
		String seatType = c.getSeatType();
		boolean seatAllocated = false;
		
		//separates the seating array for searching by class type
		switch(c.getClassType()) {
		case "first": 
			start = 0;
			end = 1;
			break;
		case "business":
			start = 2;
			end = 5;
			break;
		case "economy":
			start = 6;
			end = 11;
			break;
		}
		
		for (int i = start; i <= end; i++) {
			if(seatType == "window" && Seats[0][i].getBookingDisplay() == '*') {
				seatAllocated = AllocateSeat(c, 0, i);
			}
			
			else if(seatType == "window" && Seats[5][i].getBookingDisplay() == '*') {
				seatAllocated = AllocateSeat(c, 5, i);
			}
			
			else if(seatType == "aisle" && Seats[2][i].getBookingDisplay() == '*') {
				seatAllocated = AllocateSeat(c, 2, i);
			}
			
			else if(seatType == "aisle" && Seats[3][i].getBookingDisplay() == '*') {
				seatAllocated = AllocateSeat(c, 3, i);
			}
			
			else if(seatType == "middle" && Seats[1][i].getBookingDisplay() == '*') {
				seatAllocated = AllocateSeat(c, 1, i);
			}
			
			else if(seatType == "middle" && Seats[4][i].getBookingDisplay() == '*') {
				seatAllocated = AllocateSeat(c, 4, i);
			}
			
			if(seatAllocated) {
				break;
			}
		}
		if(!seatAllocated) {
			System.out.println("Error: No available " + seatType + " seats in " + c.getClassType() + ".");
		}
		else {
			//replaces customer array with one that is 1 bigger then sorts the array by name
			int oldLength = Customers.length;
			Customer[] newCustArr = new Customer[oldLength + 1];
			System.arraycopy(Customers, 0, newCustArr, 0, oldLength);
			newCustArr[oldLength] = c;
			Customers = newCustArr;
			Arrays.sort(Customers);
			
			System.out.println("Success! Seat " + (c.getSeat()[1] + 1) + 
					Columns.byIndex(c.getSeat()[0]) + " has been booked for " + c.getName() + ".\n");
		}
	}

	//uses a binary search method to find a customer by name
	public Customer BinarySearch(String name) {
	    Customer target = new Customer();
	    target.setName(name);
		int low = 0;
		int high = Customers.length - 1;
		int mid;

		while (low <= high) {
			mid = (low + high) / 2;

			if (Customers[mid].compareTo(target) < 0) {
				low = mid + 1;
			} else if (Customers[mid].compareTo(target) > 0) {
				high = mid - 1;
			} else {
				return Customers[mid];
			} 
		}
		return null;
	}
	
	//replaces a customer with a default customer in seating array and updates customer array
	public void CancelSeatAllocation(Customer c) {
		Customer newCust = new Customer();
		newCust.setSeat(c.getSeat());
		Seats[c.getSeat()[0]][c.getSeat()[1]] = newCust;
		
		int index = 0;
		int length = Customers.length;
		
		for (int i = 0; i < length; i++) {
            if (c.equals(Customers[i])) {
                index = i;
                break;
            }
        }
		
		Customer[] newCustArr = new Customer[Customers.length - 1];
		System.arraycopy(Customers, 0, newCustArr, 0, index);
		if (index < length - 1) {
            System.arraycopy(Customers, index + 1, newCustArr, index, length - index - 1);
        }
		
		Customers = newCustArr;
		
		UpdateFile(c.getSeat()[0], c.getSeat()[1]);
	}
	
	//replaces default customer with user entered one in seating array
	private boolean AllocateSeat(Customer c, int column, int row) {
		Seats[column][row] = c;
		c.setSeat(new int[] {column, row});
		UpdateFile(column, row);
		return true;
	}
	
	//creates arrays and populates seats with default data
	public void NewFlight() {
		Customers = new Customer[0];
		Seats = new Customer[6][12];
		
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 12; j++) {
				Seats[i][j] = new Customer();
			}
		}
		PopulateFile();
	}
	
	//returns the seat allocations as a list to be displayed
	private List<String> getSeatsString(){
		List<String> lines = new ArrayList<String>();
		lines.add("        A B C   D E F");
		String row = "";
		for(int i = 0; i < 12; i++) {
			
			if(i < 9) {
				row += ("Row " + (i + 1) + "   ");
			}
			else {
				row += ("Row " + (i + 1) + "  ");
			}
			
			for(int j = 0; j < 6; j++) {
				row += (Seats[j][i].getBookingDisplay() + " ");
				
				if(j == 2) {
					row += ("  ");
				}
				
				if(j == 5) {
					lines.add(row);
					row = "";
				}
			}
		}
		return lines;
	}
	
	//prints the seat allocations to the console
	public void PrintSeats() {
		List<String> lines = getSeatsString();
		for (String line : lines) {
			System.out.println(line);
		}
		System.out.println("\n");
	}

	//populates fixed length file with empty seats
	private void PopulateFile() {

		String data = "Seating allocations for this flight:";

		for(int i = 0; i < 12; i++) {
			for(int j = 0; j < 6; j++) {
				data += String.format("%1$-30s" ,"\nSeat " + (i + 1) + Columns.byIndex(j) + " is empty.");
			}
		}
		
		Path file = Paths.get("SeatingData.txt");
		
		try {
			Files.write(file, data.getBytes(), StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//updates the SeatingData file at a specific point using a random access method
	public void UpdateFile(int column, int row) {
		int pos = 36 + (row * 180) + (column * 30);
		try {
			RandomAccessFile writeFile = new RandomAccessFile("SeatingData.txt", "rw");
			writeFile.seek(pos);
			writeFile.write(Seats[column][row].toString().getBytes());
			writeFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<String> lines = getSeatsString();
		Path file = Paths.get("SeatingPlan.txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
