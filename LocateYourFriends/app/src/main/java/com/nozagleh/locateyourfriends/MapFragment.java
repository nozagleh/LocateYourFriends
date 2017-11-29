package com.nozagleh.locateyourfriends;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment
    implements OnMapReadyCallback {
    private View view;

    private GoogleMap googleMap;
    private SupportMapFragment map;

    private FragmentListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setScrollGesturesEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (PermissionManager.checkPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            this.googleMap.setMyLocationEnabled(true);
        }
    }
}
