package tw.alphacamp.tripphotoapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import tw.alphacamp.tripphotoapp.R;
import tw.alphacamp.tripphotoapp.object.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 15/1/24.
 */
public class MapFragment extends PlaceholderFragment implements OnMapReadyCallback {

    private static View rootView;

    private List<Photo> photoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoList = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ViewGroup parentViewGroup = (ViewGroup) rootView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView!=null) {
            ViewGroup containerParent = (ViewGroup)container.getParent();
            if (containerParent !=null) {
                containerParent.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException ex ){
            Log.e("TripPhoto", ex.getLocalizedMessage());
        }

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map_view);
        mapFragment.getMapAsync(this);

        return rootView;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        ParseQuery<Photo> photoParseQuery = ParseQuery.getQuery(Photo.class);
        photoParseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        photoParseQuery.findInBackground(new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e == null) {
                    photoList.clear();
                    photoList.addAll(photos);

                    List<MarkerOptions> markerOptionsList = new ArrayList<MarkerOptions>();
                    for (Photo eachPhoto : photos) {
                        if (eachPhoto.getLocation() != null) {

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .title(eachPhoto.getName())
                                    .position(new LatLng(eachPhoto.getLocation().getLatitude(),
                                            eachPhoto.getLocation().getLongitude()
                                            ));
                            markerOptionsList.add(markerOptions);
                            googleMap.addMarker(markerOptions);
                        }
                    }

                    //Calculate the markers to get their position
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (MarkerOptions marker : markerOptionsList) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    // Change the padding as per needed
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                    googleMap.moveCamera(cameraUpdate);
                }
            }
        });
    }
}
