import React from 'react';
import { useState } from 'react';
import MapTest from "./MapTest";
import '../output.css';
import '../index.css';
import MapClickable from "./MapClickable";


export default function Home() {
    const [departureDate, setDepartureDate] = useState("");
    const [departureTime, setDepartureTime] = useState("");
    const [terminal, setTerminal] = useState("");
	const [createLat, setCreateLat] = useState("");
	const [createLon, setCreateLon] = useState("");
	const [searchLat, setSearchLat] = useState("");
	const [searchLon, setSearchLon] = useState("");
	const [isCreateMapVisible, setIsCreateMapVisible] = useState(false);
	const [isSearchMapVisible, setIsSearchMapVisible] = useState(false);
	const [isResultMapVisible, setIsResultMapVisible] = useState(false);
	const [matches, setMatches] = useState([]);
	const [close_matches, setCloseMatches] = useState([]);
	
	function handleMapClick(e){
		e.preventDefault();
		if (e.target.id === "createFormMapButton"){
			setIsCreateMapVisible(true);
		}
		else if (e.target.id === "searchFormMapButton"){
			setIsSearchMapVisible(true);
		}
	}
	
    async function searchRide(e) {
        e.preventDefault();
		const url = "http://localhost:8050/RideshareApp/match-handler?";
		const data = new URLSearchParams();
		data.append("pickup_lat", searchLat);
		data.append("pickup_long", searchLon);
		data.append("terminal", terminal);
		data.append("start_time", departureDate + " " + departureTime + ":00");
		const response = await fetch(url + data);
		const result = await response.json();
		console.log(result);
		
		setMatches(result.matches);
		setCloseMatches(result.close_matches);
		setIsResultMapVisible(true);
    }

	async function createRide(e) {
        e.preventDefault();
		const url = "http://localhost:8050/RideshareApp/ride-req-handler?";
		const data = new URLSearchParams();
		data.append("pickup_lat", createLat);
		data.append("pickup_long", createLon);
		data.append("terminal", terminal);
		data.append("start_time", departureDate + " " + departureTime + ":00");
		data.append("username", localStorage.getItem("USER"));

		const response = await fetch(url + data);
		console.log(response);
		//setIsResultMapVisible(true);
    }

    return (
        <div className='min-h-screen bg-white- flex flex-col items-center'>
            <div className='bg-red shadow-md rounded-lg p-8 mt-10 w-full max-w-3xl'>
                <h1 className='text-5xl font-bold text-center text-red-800 mb-4'>WELCOME TO RideWithSC!</h1>
                <p className='text-center text-gray-600 mb-20'>Search for a ride that works with you or create a ride</p>
                {/* {departureDate} <br/>
                {departureTime} <br/>
                {terminal} <br/>
                {departureLocation} <br/> */}
                <form data-name="searchForm" className='space-y-4'>
                    <button className='w-full bg-yellow-600 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2' onClick={(e) => searchRide(e)}>Search for a ride</button>
                    <br/>
                    <label className='mr-5'>Please enter your departure date:</label>
                    <input type="date" id="departureDate" name="departureDate" onChange={(e) => setDepartureDate((e.target.value))} className="w-50 px-4 py-2 border border-gray-300 rounded-md shadow-sm"></input>
                    <br/>
                    <label className='mr-5'>Please enter your departure time:</label>
                    <input type="time" id="departureTime" name="departureTime" onChange={(e) => setDepartureTime(e.target.value)} className="w-50 px-4 py-2 border border-gray-300 rounded-md shadow-sm"></input>
                    <br/>
                    <label className='mr-5'>Please enter your terminal:</label>
                    <input type="text" id="terminal" name="terminal" onChange={(e) => setTerminal(e.target.value)} className="w-50 px-4 py-2 border border-gray-300 rounded-md shadow-sm"></input>
                    <br/>
                    <label className='mr-5'>Pickup Location:</label>
					<button id="searchFormMapButton" onClick={(e) => handleMapClick(e)} className='bg-transparent hover:bg-red-700 text-red-700 font-semibold hover:text-white py-2 px-4 border border-red-700 hover:border-transparent rounded'>
                        <img src="/marker.png" alt='Button Pic' className='w-6 h-6 pointer-events-none'/>   
                        {/* Button */}
                    </button>
					{searchLat} {searchLon}
					{isSearchMapVisible && <MapClickable setLat={setSearchLat} setLon={setSearchLon} setIsCreateMapVisible={setIsSearchMapVisible}></MapClickable>}
                </form>
                <br/>
                {localStorage.getItem("USER") && <form data-name="createForm" className='space-y-4'>
                    <button className='w-full bg-yellow-600 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2' onClick={createRide}>Create a ride</button>
                    <br/>
                    <label className='mr-5'>Please enter your departure date:</label>
                    <input type="date" id="departureDate" name="departureDate"
                           onChange={(e) => setDepartureDate((e.target.value))} className="w-50 px-4 py-2 border border-gray-300 rounded-md shadow-sm"></input>
                    <br/>
                    <label className='mr-5'>Please enter your departure time:</label>
                    <input type="time" id="departureTime" name="departureTime"
                           onChange={(e) => setDepartureTime(e.target.value)} className="w-50 px-4 py-2 border border-gray-300 rounded-md shadow-sm"></input>
                    <br/>
                    <label className='mr-5'>Please enter your terminal:</label>
                    <input type="text" id="terminal" name="terminal"
                           onChange={(e) => setTerminal(e.target.value)} className="w-50 px-4 py-2 border border-gray-300 rounded-md shadow-sm"></input>
                    <br/>
                    <label className='mr-5'>Pickup Location:</label>
					<button id="createFormMapButton" onClick={(e) => handleMapClick(e)} className="bg-transparent hover:bg-red-700 text-red-700 font-semibold hover:text-white py-2 px-4 border border-red-700 hover:border-transparent rounded">
	                    <img src="/marker.png" alt='Button Pic' className='w-6 h-6 pointer-events-none'/>   
	                    {/* Button */}
	                </button>
					{createLat} {createLon}
					{isCreateMapVisible && <MapClickable setLat={setCreateLat} setLon={setCreateLon} setIsCreateMapVisible={setIsCreateMapVisible}></MapClickable>}
                </form>}
                <br/>
                {isResultMapVisible && <MapTest matches={matches} close_matches={close_matches}/>}
            </div>
        </div>

    )
}

