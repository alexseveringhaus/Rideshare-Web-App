package rideshare_ridehandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RideRequest {
	//date format variable
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	//private member variables corresponding to table columns
	private int ride_id;
	private int user_id;
	private double pickup_latitude;
	private double pickup_longitude;
	private String terminal;
	private LocalDateTime start_time;
	private LocalDateTime end_time;
	
	//constructors
	public RideRequest(double pickup_latitude, double pickup_longitude, String terminal, String start_time) {
		this.pickup_latitude = pickup_latitude;
		this.pickup_longitude = pickup_longitude;
		this.terminal = terminal;
		this.start_time = LocalDateTime.parse(start_time, dateFormat);
		this.end_time = null;
		this.ride_id = -1;
		this.user_id = -1;
	}
	public RideRequest(int user_id, double pickup_latitude, double pickup_longitude, String terminal, String start_time) {
		this.user_id = user_id;
		this.pickup_latitude = pickup_latitude;
		this.pickup_longitude = pickup_longitude;
		this.terminal = terminal;
		this.start_time = LocalDateTime.parse(start_time, dateFormat);
		this.end_time = null;
		this.ride_id = -1;
	}
	public RideRequest(int user_id, double pickup_latitude, double pickup_longitude, String terminal, String start_time, String end_time) {
		this.user_id = user_id;
		this.pickup_latitude = pickup_latitude;
		this.pickup_longitude = pickup_longitude;
		this.terminal = terminal;
		this.start_time = LocalDateTime.parse(start_time, dateFormat);
		this.end_time = LocalDateTime.parse(end_time, dateFormat);
		this.ride_id = -1;
	}
	public RideRequest(int ride_id, int user_id, double pickup_latitude, double pickup_longitude, String terminal, String start_time) {
		this.ride_id = ride_id;
		this.user_id = user_id;
		this.pickup_latitude = pickup_latitude;
		this.pickup_longitude = pickup_longitude;
		this.terminal = terminal;
		this.start_time = LocalDateTime.parse(start_time, dateFormat);
		this.end_time = null;
	}
	
	//getter methods
	public int getUserId() {
		return this.user_id;
	}
	public double getPickupLat() {
		return this.pickup_latitude;
	}
	public double getPickupLong() {
		return this.pickup_longitude;
	}
	public String getTerminal() {
		return this.terminal;
	}
	public LocalDateTime getStartTime() {
		return this.start_time;
	}
	public LocalDateTime getEndTime() {
		return this.end_time;
	}
	
	//for debugging purposes
	@Override
	public String toString() {
		return "\n{\n\t'ride_id': " + (ride_id == -1 ? "null":ride_id) + ",\n" + 
				"\t'user_id': " + (user_id == -1 ? "null":user_id) + ",\n" + 
				"\t'pickup_lat': " + pickup_latitude + ",\n" +
				"\t'pickup_long': " + pickup_longitude + ",\n" + 
				"\t'terminal': " + terminal + ",\n" +
				"\t'start_time': " + start_time.format(dateFormat) + "\n}\n";
	}
}
