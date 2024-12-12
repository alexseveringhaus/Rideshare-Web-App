package rideshare_servlets;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import rideshare_ridehandling.DBRideHandler;
import rideshare_ridehandling.RideRequest;

//from https://github.com/google/gson/blob/main/UserGuide.md#built-in-serializers-and-deserializers
class LDTSerializer implements JsonSerializer<LocalDateTime> {
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public LDTSerializer() {}
	@Override
	public JsonElement serialize(LocalDateTime src, Type typeOfSRC, JsonSerializationContext context) {
		return new JsonPrimitive(src.format(dateFormat));
	}
}

@WebServlet("/match-handler")
public class MatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public MatchServlet() {
		super();
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter pw = null;
		try {
			//prepare for JSON response
			resp.setContentType("application/json");	
			pw = resp.getWriter();
	        resp.addHeader("Access-Control-Allow-Origin", "*");

			//get request parameters
			double pickup_latitude = Double.parseDouble(req.getParameter("pickup_lat"));
			double pickup_longitude = Double.parseDouble(req.getParameter("pickup_long"));
			String terminal = req.getParameter("terminal");
			String start_time = req.getParameter("start_time");
			RideRequest rReq = new RideRequest(pickup_latitude, pickup_longitude, terminal, start_time);
			System.out.println(pickup_latitude + pickup_longitude + terminal + start_time);
			//make database connection and get response
			DBRideHandler dbh = new DBRideHandler();
			Map<String, List<RideRequest>> result = dbh.findMatches(rReq);
			
			//turn that into JSON to respond
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LDTSerializer()).create();
			String responseStr = gson.toJson(result);
			pw.write(responseStr);
		}
		catch(Exception e) {
			System.out.println("Error occurred in match servlet: " + e.getMessage());
		}
		finally {
			if(pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}
}
