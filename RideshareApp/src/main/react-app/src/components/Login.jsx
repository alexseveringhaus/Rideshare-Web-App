import React from 'react'
import { useState } from 'react';
import {useNavigate} from 'react-router-dom';

// placeholder login page, feel free to add on to or delete :}

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
  	const [message, setMessage] = useState("");
	const navigate = useNavigate();
	
    // complete functionality!
	function handleLogin(e) {
	    e.preventDefault();
	    fetch('http://localhost:8050/RideshareApp/LoginServlet?', {
	      method: 'POST',
	      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	      body: new URLSearchParams({ username: email, password: password })
	    })
	    .then(response => {
	      // Check if the response is a redirected response to maps.html
	      //if (response.redirected && response.url.endsWith('maps.html')) {
	      //  window.location.href = response.url;
	      //  return null; // Stop further processing since we're redirecting
	      //}
	      // If not redirected, read the response text
	      return response.text();
	    })
	    .then(data => {
			console.log(data);
	      // If data is null, we already handled a redirect
	      if (!data) return;
	      // Check response data for known messages
	      if (data.includes("Username does not exist.")) {
	        setMessage("Email does not exist.");
	      } else if (data.includes("Incorrect password.")) {
	        setMessage("Incorrect password.");
	      } else if (data.includes("Login successful.")){
			setMessage("Login success!");
			localStorage.setItem("USER", email);
			navigate("/home");
		  }
		  else {
	        setMessage("Unknown response from server. Check servlet logic.");
	      }
	    })
	    .catch(error => {
	      setMessage("Error: " + error.message);
	    });
	  };
  
    return (
      <div className='min-h-screen bg-white flex flex-col items-center'>
        <div className='bg-white shadow-md rounded-lg p-8 mt-10 w-full max-w-3xl'>

          <h1 className='text-5xl font-bold text-center text-red-800 mb-4'>Login</h1>
          

          <p className='text-center text-black mb-10'>
            Sign in to your RideWithSC account!
          </p>
  

          <form className='space-y-4'>

            <label className='block text-black'>
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
  

            <label className='block text-black'>
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
  			{message}

            <button
              type="submit"
              onClick={handleLogin}
              className='w-full bg-yellow-600 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2 hover:bg-yellow-700'
            >
              Login
            </button>
          </form>
        </div>
      </div>
    );
}
