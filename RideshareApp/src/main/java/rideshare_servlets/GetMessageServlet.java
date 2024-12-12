
package rideshare_servlets;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import rideshare_messaging.DBMessenger;
import rideshare_messaging.Message;

@WebServlet("/get-messages")
public class GetMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public GetMessageServlet() {
		super();
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter pw = null;
		try {
			//prepare for JSON response
			resp.setContentType("application/json");	
			pw = resp.getWriter();
			resp.addHeader("Access-Control-Allow-Origin", "*");
			
			//get parameters
			int senderID = Integer.parseInt(req.getParameter("sender"));
			int recipientID = Integer.parseInt(req.getParameter("recipient"));
			
			//make database connection and get response
			DBMessenger dbh = new DBMessenger();
			List<Message> results = dbh.retrieveMessages(senderID, recipientID); 
			
			//turn that into JSON to respond
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LDTSerializer()).create();
			String responseStr = gson.toJson(results);
			pw.write(responseStr);
		}
		catch(Exception e) {
			System.out.println("Error occurred in message getting servlet: " + e.getMessage());
		}
		finally {
			if(pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}
}
