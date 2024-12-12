package rideshare_ridehandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ride {
	//date format variable
		private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		//private member variables corresponding to table columns
		private int ride_id;
		private double pickup_latitude;
		private double pickup_longitude;
		private String terminal;
		private LocalDateTime start_time;
		
		//constructors
		public Ride(double pickup_latitude, double pickup_longitude, String terminal, String start_time) {
			this.pickup_latitude = pickup_latitude;
			this.pickup_longitude = pickup_longitude;
			this.terminal = terminal;
			this.start_time = LocalDateTime.parse(start_time, dateFormat);
			this.ride_id = -1;
		}
		public Ride(int ride_id, double pickup_latitude, double pickup_longitude, String terminal, String start_time) {
			this.ride_id = ride_id;
			this.pickup_latitude = pickup_latitude;
			this.pickup_longitude = pickup_longitude;
			this.terminal = terminal;
			this.start_time = LocalDateTime.parse(start_time, dateFormat);
		}
		
		//getter methods
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
		
		//for debugging purposes
		@Override
		public String toString() {
			return "\n{\n\t'ride_id': " + (ride_id == -1 ? "null":ride_id) + ",\n" + 
					"\t'pickup_lat': " + pickup_latitude + ",\n" +
					"\t'pickup_long': " + pickup_longitude + ",\n" + 
					"\t'terminal': " + terminal + ",\n" +
					"\t'start_time': " + start_time.format(dateFormat) + "\n}\n";
		}
}
