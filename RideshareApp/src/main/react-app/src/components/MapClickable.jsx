import React, {useCallback} from 'react'
import {
    APIProvider,
    Map,
    AdvancedMarker,
    MapCameraChangedEvent,
    Pin, useMap
} from '@vis.gl/react-google-maps';

//map component to get user Lat/Long. replace the apiKey with your api key for it to work!!
//make sure to pass in matches and close_matches as props to PoiMarkers
export default function MapClickable(props) {
	function handleClick(e){
		props.setLat(e.detail.latLng.lat);
		props.setLon(e.detail.latLng.lng);
		props.setIsCreateMapVisible(false);
	}
    return (
        <APIProvider apiKey="">
            <Map style={{width: '100vw', height: '100vh'}}
                 className="map-container"
                 defaultCenter={{ lat: 34.0522, lng: -118.2437 }}
                 defaultZoom={10}
                 mapId='DEMO_MAP_ID'
                 gestureHandling={'greedy'}
                 disableDefaultUI={true}
				 clickable={true}
				 onClick={(ev) => handleClick(ev)}>
            </Map>
        </APIProvider>
    )
}