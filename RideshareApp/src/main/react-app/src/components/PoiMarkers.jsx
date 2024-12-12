import React, { useState } from 'react';
import { AdvancedMarker, Pin, InfoWindow } from "@vis.gl/react-google-maps";

export default function PoiMarkers(props) {
    let [selectedPin, setSelectedPin] = useState(null); //store info about selected pin on map
	let [email, setEmail] = useState("");
	
    const handleClick = async (ev, poi) => {
        console.log('marker clicked:', ev);
        console.log('poi:', poi);
        
        if (selectedPin && selectedPin.ride_id === poi.ride_id) {
            setSelectedPin(null);
			setEmail("");
        } else {
			const url = "http://localhost:8050/RideshareApp/EmailServlet?";
			const data = new URLSearchParams();
			data.append("user_id", poi.user_id);
	
			const response = await fetch(url + data);
			const result = await response.json();
			console.log(result);
			setEmail(result.email);
            setSelectedPin(poi);
        }
    };

    //use this function to link user to the chat frontend page (Rishi is making)
    const displayChat = (poi) => {
        window.location.href = `/chat/${poi.user_id}`;
    };

    return (
        <>
            {props.matches.map((poi) => (
                <AdvancedMarker
                    key={poi.ride_id}
                    position={{lat: poi.pickup_latitude, lng: poi.pickup_longitude}}
                    clickable={true}
                    onClick={(ev) => handleClick(ev, poi)}
                >
                    <Pin background={'blue'} glyphColor={'#000'} borderColor={'#000'} />
                    {selectedPin && selectedPin.ride_id === poi.ride_id && (
                        <InfoWindow position={{lat: poi.pickup_latitude, lng: poi.pickup_longitude}}>
                            <div>
                                <button onClick={() => displayChat(poi)}>
                                    Contact info: {email}
                                </button>
                            </div>
                        </InfoWindow>
                    )}
                </AdvancedMarker>
            ))}

            {props.close_matches.map((poi) => (
                <AdvancedMarker
                    key={poi.ride_id}
                    position={{lat: poi.pickup_latitude, lng: poi.pickup_longitude}}
                    clickable={true}
                    onClick={(ev) => handleClick(ev, poi)}
                >
                    <Pin background={'red'} glyphColor={'#000'} borderColor={'#000'} />
                    {selectedPin && selectedPin.ride_id === poi.ride_id && (
                        <InfoWindow position={{lat: poi.pickup_latitude, lng: poi.pickup_longitude}}>
                            <div>
                                <button onClick={() => displayChat(poi)}>
                                    Contact info: {email}
                                </button>
                            </div>
                        </InfoWindow>
                    )}
                </AdvancedMarker>
            ))}
        </>
    );
}