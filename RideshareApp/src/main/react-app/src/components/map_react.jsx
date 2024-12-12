import React, { useEffect, useRef } from 'react';

const RideshareMap = () => {
  const mapRef = useRef(null); // To store the Google Map instance
  const map = useRef(null); // To store the map itself

  // Function to fetch data from the servlet
  const getData = () => {
    const servletURL = new URL('http://localhost:8080/Rideshare-App-Test/MatchServlet');
    const params = {
      pickup_lat: '37.7749', // Replace with actual user input
      pickup_long: '-118.3847', // Replace with actual user input
      terminal: '4', // Replace with actual user input
      start_time: '2024-12-03 08:30:00', // Replace with actual user input
    };

    // Add query parameters
    Object.keys(params).forEach((key) => servletURL.searchParams.append(key, params[key]));

    fetch(servletURL)
      .then((response) => response.json())
      .then((data) => fillMap(data))
      .catch((error) => console.error('Error:', error));
  };

  // Function to populate the map with markers
  const fillMap = (data) => {
    const { matches, close_matches: closeMatches } = data;

    // Add markers for matches
    matches.forEach((match) => {
      const pin = new window.google.maps.Marker({
        position: { lat: match.pickup_latitude, lng: match.pickup_longitude },
        map: map.current,
        icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png',
      });

      pin.addListener('click', () => displayRideInfo(match, pin));
    });

    // Add markers for close matches
    closeMatches.forEach((closeMatch) => {
      const pin = new window.google.maps.Marker({
        position: { lat: closeMatch.pickup_latitude, lng: closeMatch.pickup_longitude },
        map: map.current,
        icon: 'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
      });

      pin.addListener('click', () => displayRideInfo(closeMatch, pin));
    });
  };

  // Function to display an info window for a marker
  const displayRideInfo = (match, pin) => {
    const infoWindow = new window.google.maps.InfoWindow({
      content: `
        <div>
          <p><strong>User ID:</strong> ${match.user_id}</p>
          <p><strong>Lat:</strong> ${match.pickup_latitude}</p>
          <p><strong>Long:</strong> ${match.pickup_longitude}</p>
          <p><strong>Terminal:</strong> ${match.terminal}</p>
          <p><strong>Time:</strong> ${match.start_time}</p>
        </div>
      `,
    });

    infoWindow.open(map.current, pin);
  };

  // Initialize the Google Map
  const initMap = () => {
    map.current = new window.google.maps.Map(mapRef.current, {
      center: { lat: 34.0522, lng: -118.2437 },
      zoom: 10,
    });
    getData(); // Fetch data and populate the map
  };

  // Load the Google Maps script
  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCyMrB-eJZUgg78y8SpzjvM9eBmnEg8Sg0&callback=initMap';
    script.async = true;
    script.defer = true;
    script.onload = initMap;
    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script); // Clean up the script on component unmount
    };
  }, []);

  return <div ref={mapRef} style={{ height: '500px', width: '100%' }} />;
};

export default RideshareMap;
