package seatingallocation.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

public class SeatManager {
	private Customer[][] Seats;
	
	public SeatManager() {}
	
	public Customer[][] getSeats() {
		return Seats;
	}

	public void setSeats(Customer[][] seats) {
		Seats = seats;
	}

	//adds a new customer to the seat bookings
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
			System.out.println("Success! " + seatType + " seat booked in aisle " + c.getSeat()[1] + ".");
		}
	}

	public void CancelSeatAllocation(Customer c) {
		Customer newCust = new Customer();
		newCust.setSeat(c.getSeat());
		Seats[c.getSeat()[0]][c.getSeat()[1]] = newCust;
		
		UpdateFile(c.getSeat()[0], c.getSeat()[1]);
	}
	
	private boolean AllocateSeat(Customer c, int column, int row) {
		Seats[column][row] = c;
		c.setSeat(new int[] {column, row});
		UpdateFile(column, row);
		return true;
	}
	
	public void NewFlight() {
		Seats = new Customer[6][12];
		
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 12; j++) {
				Seats[i][j] = new Customer();
			}
		}
		PopulateFile();
	}
	
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
	
	public void PrintSeats() {
		List<String> lines = getSeatsString();
		for (String line : lines) {
			System.out.println(line);
		}
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
