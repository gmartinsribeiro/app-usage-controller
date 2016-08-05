package com.aptoide.pt.appusagedetector;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aptoide.appinformer.AppUsageInfo;
import com.aptoide.appinformer.AppUsageManager;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LastUsedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LastUsedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LastUsedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public LastUsedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LastUsedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LastUsedFragment newInstance(String param1, String param2) {
        LastUsedFragment fragment = new LastUsedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_last_used, container, false);
        listView = (ListView) view.findViewById(R.id.listView2);
        populateAppList();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(listView != null && listView.getAdapter() != null){
            if(listView.getAdapter().getCount() == 0){
                populateAppList();
            }
        }
    }

    private void populateAppList() {
        AppUsageManager uD = new AppUsageManager(getActivity());
        try {
            List<AppUsageInfo> apps = uD.getRecentlyUsedApps();
            // Create the adapter to convert the array to views
            AppUsageInfoArrayAdapter adapter = new AppUsageInfoArrayAdapter(getActivity().getApplicationContext(), R.layout.listitem, apps);
            adapter.setFragmentUser(AppUsageInfoArrayAdapter.FragmentType.LASTUSED);
            // Attach the adapter to a ListView
            listView.setAdapter(adapter);
        } catch (SecurityException e){
            ((MainActivity) getActivity()).showSettingsDialog();
        }

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
        void onFragmentInteraction(Uri uri);
    }
}
