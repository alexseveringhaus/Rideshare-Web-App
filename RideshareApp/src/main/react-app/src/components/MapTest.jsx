import React, {useCallback} from 'react'
import {
    APIProvider,
    Map,
    AdvancedMarker,
    MapCameraChangedEvent,
    Pin, useMap
} from '@vis.gl/react-google-maps';
import PoiMarkers from "./PoiMarkers";
import './map.css';

//main map component. replace the apiKey with your api key for it to work!!
//make sure to pass in matches and close_matches as props to PoiMarkers
export default function MapTest(props) {
    return (
        <APIProvider apiKey="">
            <Map style={{width: '100vw', height: '100vh'}}
                 className="map-container"
                 defaultCenter={{ lat: 34.0522, lng: -118.2437 }}
                 defaultZoom={10}
                 mapId='DEMO_MAP_ID'
                 gestureHandling={'greedy'}
                 disableDefaultUI={true}>
                <PoiMarkers matches={props.matches} close_matches={props.close_matches}/>
            </Map>
        </APIProvider>
    )
}