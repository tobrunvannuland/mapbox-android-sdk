package com.mapbox.mapboxsdk.android.testapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.ITileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;

public class MainTestFragment extends Fragment {
    private LatLng startingPoint = new LatLng(51f, 0f);
    private MapView mv;
    private String satellite = "mapbox.satellite";
    private String street = "mapbox.streets";
    private String terrain = "mapbox.outdoors";
    private String currentLayer = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_maintest, container, false);

        mv = (MapView) view.findViewById(R.id.mapview);
        // Set Default Map Type
        replaceMapView(terrain);
        currentLayer = "terrain";
        mv.setUserLocationEnabled(true)
            .setUserLocationTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW);
        // Set a reasonable user location zoom level
        mv.setUserLocationRequiredZoom(16);

        /*
        // Original GeoJSON Test that caus es crash when Hardware Acceleration when enabled in TestApp
        mv.loadFromGeoJSONURL("https://gist.githubusercontent.com/tmcw/4a6f5fa40ab9a6b2f163/raw/b1ee1e445225fc0a397e2605feda7da74c36161b/map.geojson");
        */
        // Smaller GeoJSON Test
        mv.loadFromGeoJSONURL("https://gist.githubusercontent.com/bleege/133920f60eb7a334430f/raw/5392bad4e09015d3995d6153db21869b02f34d27/map.geojson");
        Marker m = new Marker(mv, "Edinburgh", "Scotland", new LatLng(55.94629, -3.20777));
        m.setIcon(new Icon(getActivity(), Icon.Size.SMALL, "marker-stroked", "FF0000"));
        mv.addMarker(m);

        m = new Marker(mv, "Stockholm", "Sweden", new LatLng(59.32995, 18.06461));
        m.setIcon(new Icon(getActivity(), Icon.Size.MEDIUM, "city", "FFFF00"));
        mv.addMarker(m);

        m = new Marker(mv, "Prague", "Czech Republic", new LatLng(50.08734, 14.42112));
        m.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "land-use", "00FFFF"));
        mv.addMarker(m);

        m = new Marker(mv, "Athens", "Greece", new LatLng(37.97885, 23.71399));
        mv.addMarker(m);

        m = new Marker(mv, "Milwaukee", "Wisconsin", new LatLng(43.04506, -87.92217));
        m.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "city", "0000FF"));
        mv.addMarker(m);

        /*
           m = new Marker(mv, "Prague2", "Czech Republic", new LatLng(50.0875, 14.42112));
           m.setIcon(new Icon(getBaseContext(), Icon.Size.LARGE, "land-use", "00FF00"));
           mv.addMarker(m);
           */
        // Set Button Listeners
        Button satBut = changeButtonTypeface((Button) view.findViewById(R.id.satbut));
        satBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentLayer.equals("satellite")) {
                    replaceMapView(satellite);
                    currentLayer = "satellite";
                }
            }
        });
        Button terBut = changeButtonTypeface((Button) view.findViewById(R.id.terbut));
        terBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentLayer.equals("terrain")) {
                    replaceMapView(terrain);
                    currentLayer = "terrain";
                }
            }
        });
        Button strBut = changeButtonTypeface((Button) view.findViewById(R.id.strbut));
        strBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentLayer.equals("street")) {
                    replaceMapView(street);
                    currentLayer = "street";
                }
            }
        });

        /*
           Button altBut = changeButtonTypeface((Button) view.findViewById(R.id.strAltMap));
           altBut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           Intent altMapActivity =
           new Intent(getActivity(), AlternateMapTestActivity.class);
           startActivity(altMapActivity);
           }
           });

           Button pinsButton = changeButtonTypeface((Button) view.findViewById(R.id.markersButton));
           pinsButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           Intent altMapActivity =
           new Intent(getActivity(), MarkersTestActivity.class);
           startActivity(altMapActivity);
           }
           });
           */

        Button spinButton = changeButtonTypeface((Button) view.findViewById(R.id.spinButton));
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.setMapOrientation(mv.getMapOrientation() + 45f);
            }
        });

        /*
           Button selectBut = changeButtonTypeface((Button) view.findViewById(R.id.layerselect));
           selectBut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
           ab.setTitle("Select Layer");
           ab.setItems(availableLayers, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface d, int choice) {
           replaceMapView(availableLayers[choice]);
           }
           });
           ab.show();
           }
           });
           */

        mv.setOnTilesLoadedListener(new TilesLoadedListener() {
            @Override
            public boolean onTilesLoaded() {
                return false;
            }

        @Override
        public boolean onTilesLoadStarted() {
            // TODO Auto-generated method stub
            return false;
        }
        });
        mv.setVisibility(View.VISIBLE);

        PathOverlay equator = new PathOverlay();
        equator.addPoint(0, -89);
        equator.addPoint(0, 89);
        mv.getOverlays().add(equator);

        return view;
    }

    final String[] availableLayers = {
        "OpenStreetMap", "OpenSeaMap", "mapquest", "open-streets-dc.mbtiles", "test.MBTiles"
    };

    protected void replaceMapView(String layer) {
        ITileLayer source;
        BoundingBox box;
        if (layer.equalsIgnoreCase("OpenStreetMap")) {
            source = new WebSourceTileLayer("openstreetmap",
                    "http://tile.openstreetmap.org/{z}/{x}/{y}.png").setName("OpenStreetMap")
                    .setAttribution("© OpenStreetMap Contributors")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else if (layer.equalsIgnoreCase("OpenSeaMap")) {
            source = new WebSourceTileLayer("openstreetmap",
                    "http://tile.openstreetmap.org/seamark/{z}/{x}/{y}.png").setName(
                    "OpenStreetMap")
                    .setAttribution("© OpenStreetMap Contributors")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else if (layer.equalsIgnoreCase("mapquest")) {
            source = new WebSourceTileLayer("mapquest",
                    "http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png").setName(
                    "MapQuest Open Aerial")
                    .setAttribution(
                            "Tiles courtesy of MapQuest and OpenStreetMap contributors.")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else {
            source = new MapboxTileLayer(layer);
        }
        mv.setTileSource(source);
        box = source.getBoundingBox();
        mv.setScrollableAreaLimit(box);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        mv.setCenter(mv.getTileProvider().getCenterCoordinate());
        mv.setZoom(0);
        Log.d("MainActivity", "zoomToBoundingBox " + box.toString());
        //        mv.zoomToBoundingBox(box);
    }

    private void addLine() {
        // Configures a line
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(5);

        PathOverlay po = new PathOverlay().setPaint(linePaint);

        po.addPoint(startingPoint);
        po.addPoint(new LatLng(51.7, 0.3));
        po.addPoint(new LatLng(51.2, 0));

        // Adds line and marker to the overlay
        mv.getOverlays().add(po);
    }

    private Button changeButtonTypeface(Button button) {
        //Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/semibold.ttf");
        //button.setTypeface(tf);
        return button;
    }

    public LatLng getMapCenter() {
        return mv.getCenter();
    }

    public void setMapCenter(ILatLng center) {
        mv.setCenter(center);
    }

    /**
     * Method to show settings  in alert dialog
     * On pressing Settings button will lauch Settings Options - GPS
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
