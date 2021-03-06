package com.lochbridge.peike.demo.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.lochbridge.peike.demo.R;
import com.lochbridge.peike.demo.model.Movie;
import com.lochbridge.peike.demo.util.StorageUtil;
import com.lochbridge.peike.demo.views.LocalMovieAdapter;

import java.util.List;

public class LocalMovieFragment extends BaseMovieListFragment {
    private static final String LOG_TAG = "LocalMovieFragment";
    private LocalMovieAdapter localMovieAdapter;
    private GridView localGridView;
    private TextView emptyLocalTextView;

    public static LocalMovieFragment newInstance(String param1, String param2) {
        LocalMovieFragment fragment = new LocalMovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshLocalView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        localGridView = (GridView) view.findViewById(R.id.local_gridview);
        emptyLocalTextView = (TextView) view.findViewById(R.id.empty_local_textview);
        localMovieAdapter = new LocalMovieAdapter(getActivity());
        localGridView.setAdapter(localMovieAdapter);
        super.onViewCreated(localGridView,savedInstanceState);
    }

    private void refreshLocalView() {
        List<Movie> movies = StorageUtil.readAllLocalMovies(getActivity());
        localMovieAdapter.udpateGrid(movies);
        emptyLocalTextView.setVisibility(movies.isEmpty() ? View.VISIBLE: View.GONE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
