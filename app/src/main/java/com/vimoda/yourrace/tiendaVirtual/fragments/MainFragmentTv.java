package com.vimoda.yourrace.tiendaVirtual.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.vimoda.yourrace.R;
import com.vimoda.yourrace.modelos.Zapatos;
import com.vimoda.yourrace.tiendaVirtual.ZapatillaAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainFragmentTv extends Fragment{

    private RecyclerView rvZapatillas;
    private ZapatillaAdapter adapter;
    ViewFlipper carrousel;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tv, container, false);

        rvZapatillas = view.findViewById(R.id.rvZapatillas);
        db = FirebaseFirestore.getInstance();
        rvZapatillas.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = db.collection("inventarios");

        FirestoreRecyclerOptions<Zapatos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Zapatos>()
                        .setQuery(query, Zapatos.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter = new ZapatillaAdapter(firestoreRecyclerOptions, getContext(),this);
        adapter.notifyDataSetChanged();
        rvZapatillas.setAdapter(adapter);

        int img[] ={R.drawable.zapatilla1, R.drawable.zapatilla2,R.drawable.zapatilla3};
        carrousel = view.findViewById(R.id.vf_carrousel);

        for (int image: img) {
            pasarImg(image);
        }

        return view;
    }

    public void pasarImg(int  image){
        Context context = getContext();
        ImageView imgv = new ImageView(context);
        imgv.setBackgroundResource(image);

        carrousel.addView(imgv);
        carrousel.setFlipInterval(3000);
        carrousel.setAutoStart(true);

        carrousel.setInAnimation(context, android.R.anim.slide_in_left);
        carrousel.setOutAnimation(context, android.R.anim.slide_out_right);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    public void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container1, fragment) // 'fragment_container' es el ID del contenedor de fragmentos en tu layout
                .addToBackStack(null)
                .commit();
    }
}