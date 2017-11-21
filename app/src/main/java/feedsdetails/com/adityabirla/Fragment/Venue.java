package feedsdetails.com.adityabirla.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import feedsdetails.com.adityabirla.Activity.FileView;
import feedsdetails.com.adityabirla.Activity.HelpDesk;
import feedsdetails.com.adityabirla.Activity.MapActivity;
import feedsdetails.com.adityabirla.Activity.VideoPlayer;
import feedsdetails.com.adityabirla.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Venue.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Venue#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Venue extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout lin_schedule;
    LinearLayout lin_venue,lin_md,lin_galary,lin_helpdesk;

    private OnFragmentInteractionListener mListener;

    public Venue() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Venue.
     */
    // TODO: Rename and change types and number of parameters
    public static Venue newInstance(String param1, String param2) {
        Venue fragment = new Venue();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_venue, container, false);
        lin_schedule=(LinearLayout)v.findViewById(R.id.lin_schedule);
        lin_venue=(LinearLayout)v.findViewById(R.id.lin_venue);
        lin_galary=(LinearLayout)v.findViewById(R.id.lin_galary);
        lin_md=(LinearLayout)v.findViewById(R.id.lin_md);
        lin_helpdesk=(LinearLayout)v.findViewById(R.id.lin_helpdesk);
        lin_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FileView.class);
                intent.putExtra("PATH","http://applicationworld.net/forum/files/schedule.pdf");
                startActivity(intent);
            }
        });
        lin_venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }
        });
        lin_md.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VideoPlayer.class);
                intent.putExtra("PATH","http://applicationworld.net/forum/files/mdSpeech.mp4");
                startActivity(intent);
            }
        });
        lin_galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FileView.class);
                intent.putExtra("PATH","http://applicationworld.net/forum/files/MPC_Photo.pptx");
                startActivity(intent);
            }
        });
        lin_helpdesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HelpDesk.class);
                //intent.putExtra("PATH","http://applicationworld.net/forum/files/MPC_Photo.pptx");
                startActivity(intent);
            }
        });
        return v;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
