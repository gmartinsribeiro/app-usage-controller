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
 * {@link MostUsedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MostUsedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostUsedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView listView;


    private OnFragmentInteractionListener mListener;

    public MostUsedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MostUsedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MostUsedFragment newInstance(String param1, String param2) {
        MostUsedFragment fragment = new MostUsedFragment();
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

    private void populateAppList() {
        AppUsageManager uD = new AppUsageManager(getActivity());

        try {
            List<AppUsageInfo> apps = uD.getMostUsedApps();
            // Create the adapter to convert the array to views
            AppUsageInfoArrayAdapter adapter = new AppUsageInfoArrayAdapter(getActivity().getApplicationContext(), R.layout.listitem, apps);
            adapter.setFragmentUser(AppUsageInfoArrayAdapter.FragmentType.LASTUSED);
            // Attach the adapter to a ListView
            listView.setAdapter(adapter);
        } catch (SecurityException e){
            ((MainActivity) getActivity()).showSettingsDialog();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_most_used, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
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
