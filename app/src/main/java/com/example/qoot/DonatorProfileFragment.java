package com.example.qoot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;


public class DonatorProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // fire base variables
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userid;

    TextView Username, email, Ratings, Donations;
    ImageView Photo;

    // eventually we will add comments and ratings as well
    private OnFragmentInteractionListener mListener;

    public DonatorProfileFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_donator_profile, container, false);
        Photo = view.findViewById(R.id.imageView2);
        Username =  view.findViewById(R.id.textView4);
        email =  view.findViewById(R.id.emailDonator);
        Ratings =  view.findViewById(R.id.RateD);
        Donations =  view.findViewById(R.id.Donations);


        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference =db.collection("Donators").document(userid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Username.setText(documentSnapshot.getString("UserName"));
                email.setText(documentSnapshot.getString("Email"));
                // Ratings.setText(documentSnapshot.getString("Ratings"));
                // Donations.setText(documentSnapshot.getString("Donations"));
            }
        });

        // Inflate the layout for this fragment
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    public void OpenEditProfilePage(View view){

        Intent i = new Intent(getActivity().getApplication(), EditDonatorProfile.class);
        startActivity(i);
        //((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    public void OpenLogOut(View view) {
        FirebaseAuth.getInstance().signOut();
      //  Toast.makeText(DonatorProfileFragment.this, "log out Was Successful!!", Toast.LENGTH_SHORT).show();
     //   startActivity(new Intent(DonatorProfileFragment.this,LogIn.class));
        Intent i = new Intent(getActivity(), LogIn.class);
        startActivity(i);
      //  ((Activity) getActivity()).overridePendingTransition(0, 0);
    }


}
