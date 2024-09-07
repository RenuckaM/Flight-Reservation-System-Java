package javapro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Flight {

		    private int flightId;
		    private String airline;
		    private String source;
		    private String destination;
		    private int seatsAvailable;

		    public Flight(int flightId, String airline, String source, String destination, int seatsAvailable) {
		        this.flightId = flightId;
		        this.airline = airline;
		        this.source = source;
		        this.destination = destination;
		        this.seatsAvailable = seatsAvailable;
		    }

		    public int getFlightId() {
		        return flightId;
		    }

		    public String getAirline() {
		        return airline;
		    }

		    public String getSource() {
		        return source;
		    }

		    public String getDestination() {
		        return destination;
		    }

		    public int getSeatsAvailable() {
		        return seatsAvailable;
		    }
		}

		class FlightManager {
		    private Connection connection;

		    public FlightManager() {
		        // Initialize the database connection
		        try {
		            Class.forName("com.mysql.jdbc.Driver");
		            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/flight_booking", "username", "password");
		        } catch (ClassNotFoundException | SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    public void displayFlights() {
		        try {
		            Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM flights");

		            System.out.println("Flight ID\tAirline\t\tSource\t\tDestination\tSeats Available");
		            System.out.println("-------------------------------------------------------------------");

		            while (resultSet.next()) {
		                int flightId = resultSet.getInt("flightId");
		                String airline = resultSet.getString("airline");
		                String source = resultSet.getString("source");
		                String destination = resultSet.getString("destination");
		                int seatsAvailable = resultSet.getInt("seatsAvailable");

		                System.out.println(flightId + "\t\t" + airline + "\t\t" + source + "\t\t" + destination + "\t\t" + seatsAvailable);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    public void bookFlight(int flightId, int seats) {
		        try {
		            Flight flight = getFlight(flightId);
		            if (flight != null) {
		                int availableSeats = flight.getSeatsAvailable();

		                if (seats <= availableSeats) {
		                    PreparedStatement statement = connection.prepareStatement("UPDATE flights SET seatsAvailable = ? WHERE flightId = ?");
		                    statement.setInt(1, availableSeats - seats);
		                    statement.setInt(2, flightId);
		                    int rowsUpdated = statement.executeUpdate();

		                    if (rowsUpdated > 0) {
		                        System.out.println("Flight booked successfully!");
		                    } else {
		                        System.out.println("Failed to book the flight. Please try again.");
		                    }
		                } else {
		                    System.out.println("Insufficient seats available for booking.");
		                }
		            } else {
		                System.out.println("Invalid flight ID.");
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    public void modifyFlight(int flightId, int seats) {
		        try {
		            Flight flight = getFlight(flightId);
		            if (flight != null) {
		                int availableSeats = flight.getSeatsAvailable();

		                PreparedStatement statement = connection.prepareStatement("UPDATE flights SET seatsAvailable = ? WHERE flightId = ?");
		                statement.setInt(1, availableSeats + seats);
		                statement.setInt(2, flightId);
		                int rowsUpdated = statement.executeUpdate();

		                if (rowsUpdated > 0) {
		                    System.out.println("Flight modified successfully!");
		                } else {
		                    System.out.println("Failed to modify the flight. Please try again.");
		                }
		            } else {
		                System.out.println("Invalid flight ID.");
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    public void cancelFlight(int flightId) {
		        try {
		            Flight flight = getFlight(flightId);
		            if (flight != null) {
		                int availableSeats = flight.getSeatsAvailable();

		                PreparedStatement statement = connection.prepareStatement("UPDATE flights SET seatsAvailable = ? WHERE flightId = ?");
		                statement.setInt(1, availableSeats + 1);
		                statement.setInt(2, flightId);
		                int rowsUpdated = statement.executeUpdate();

		                if (rowsUpdated > 0) {
		                    System.out.println("Flight canceled successfully!");
		                } else {
		                    System.out.println("Failed to cancel the flight. Please try again.");
		                }
		            } else {
		                System.out.println("Invalid flight ID.");
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    private Flight getFlight(int flightId) throws SQLException {
		        PreparedStatement statement = connection.prepareStatement("SELECT * FROM flights WHERE flightId = ?");
		        statement.setInt(1, flightId);
		        ResultSet resultSet = statement.executeQuery();

		        if (resultSet.next()) {
		            String airline = resultSet.getString("airline");
		            String source = resultSet.getString("source");
		            String destination = resultSet.getString("destination");
		            int seatsAvailable = resultSet.getInt("seatsAvailable");

		            return new Flight(flightId, airline, source, destination, seatsAvailable);
		        }

		        return null;
		    }
		}
		    public static void main(String[] args) {
		        FlightManager flightManager = new FlightManager();
		        Scanner scanner = new Scanner(System.in);

		        int choice = 0;

		        do {
		            System.out.println("\nFlight Booking System");
		            System.out.println("1. Display Flights");
		            System.out.println("2. Book Flight");
		            System.out.println("3. Modify Flight");
		            System.out.println("4. Cancel Flight");
		            System.out.println("5. Exit");
		            System.out.print("Enter your choice: ");
		            choice = scanner.nextInt();

		            switch (choice) {
		                case 1:
		                    flightManager.displayFlights();
		                    break;
		                case 2:
		                    System.out.print("Enter the Flight ID: ");
		                    int bookFlightId = scanner.nextInt();
		                    System.out.print("Enter the number of seats to book: ");
		                    int seatsToBook = scanner.nextInt();
		                    flightManager.bookFlight(bookFlightId, seatsToBook);
		                    break;
		                case 3:
		                    System.out.print("Enter the Flight ID: ");
		                    int modifyFlightId = scanner.nextInt();
		                    System.out.print("Enter the number of seats to modify: ");
		                    int seatsToModify = scanner.nextInt();
		                    flightManager.modifyFlight(modifyFlightId, seatsToModify);
		                    break;
		                case 4:
		                    System.out.print("Enter the Flight ID: ");
		                    int cancelFlightId = scanner.nextInt();
		                    flightManager.cancelFlight(cancelFlightId);
		                    break;
		                case 5:
		                    System.out.println("Thank you for using the Flight Booking System!");
		                    break;
		                default:
		                    System.out.println("Invalid choice. Please try again.");
		                    break;
		            }
		        } while (choice != 5);

		        scanner.close();
		   
	}

}
