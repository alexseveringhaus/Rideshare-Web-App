package rideshare_servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rideshare_userhandling.DBUserHandler;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
		response.addHeader("Access-Control-Allow-Origin", "*");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            response.getWriter().println("Username or password not provided.");
            return;
        }
        try {
        	DBUserHandler dbUH = new DBUserHandler();
        	if (dbUH.getSingleUserByUsername(username) == null) {
        		dbUH.addUser(username, "first", "last", password, 0, "0", 0.0,0.0, "0");
        		response.getWriter().println("Registered successfully.");
        	}
        	else {
        		response.getWriter().println("Username already exists");
        	}
        } catch (SQLException sqle) {
            response.getWriter().println("Database error: " + sqle.getMessage());
        }
    }
}
