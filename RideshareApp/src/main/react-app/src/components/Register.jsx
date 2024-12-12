import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../output.css";
import "../index.css";

// placeholder registration page, feel free to add on or delete :}

export default function Register() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  // REGISTRATION FUNCTION: TODO
  function handleRegister(e) {
    e.preventDefault();
    if (password !== confirmPassword) {
      setMessage("Passwords do not match.");
      return;
    }
    // FILL IN FUNCTIONALITY HERE!! Temporary
    fetch("http://localhost:8050/RideshareApp/RegisterServlet?", {
      method: "POST",
      body: new URLSearchParams({ username: email, password: password }),
    })
      .then((response) => {
        // Check if the response was redirected (success scenario)
        //if (response.redirected && response.url.endsWith('maps.html')) {
        //  window.location.href = response.url;
        //  return null; // Stop further processing since we're redirecting
        //}
        return response.text();
      })
      .then((data) => {
        if (!data) return; // Already handled a redirect
        if (data.includes("Username already exists")) {
          setMessage("Email already exists. Please choose another.");
        } else if (data.includes("Username or password not provided.")) {
          setMessage("Email or password not provided.");
        } else if (data.includes("Failed to register user.")) {
          setMessage("Failed to register user.");
        } else if (data.includes("Registered successfully.")) {
          setMessage("Registered successfully!");
          navigate("/login");
        } else if (
          data.includes("Database error") ||
          data.includes("JDBC Driver not found")
        ) {
          setMessage(data);
        } else {
          setMessage("Unknown response from server. Check server logic.");
        }
      })
      .catch((error) => {
        setMessage("Error: " + error.message);
      });
  }

  return (
    <div className="min-h-screen bg-white- flex flex-col items-center">
      <div className="bg-white-500 shadow-md rounded-lg p-8 mt-10 w-full max-w-3xl">
        <h1 className="text-5xl font-bold text-center text-red-800 mb-4">
          Registration
        </h1>
        <p className="text-center text-black-200 mb-10">
          Create your account to start using RideWithSC!
        </p>

        <form className="space-y-4">
          <label className="block text-black">
            Username:
            <input
              type="text"
              id="username"
              name="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
              required
            />
          </label>

          <label className="block text-black">
            Email:
            <input
              type="email"
              id="email"
              name="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
              required
            />
          </label>

          <label className="block text-black">
            Password:
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
              required
            />
          </label>

          <label className="block text-black">
            Confirm Password:
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
              required
            />
          </label>
          {message}
          {/* registration */}
          <button
            type="submit"
            onClick={handleRegister}
            className="w-full bg-yellow-600 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2 hover:bg-yellow-700"
          >
            Register
          </button>
        </form>
      </div>
    </div>
  );
}
