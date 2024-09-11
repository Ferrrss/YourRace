package com.vimoda.yourrace.Fragments.Inventario;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.vimoda.yourrace.R;
import com.vimoda.yourrace.adapter.ZapatoAdapter;
import com.vimoda.yourrace.modelos.Zapatos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentInventory extends Fragment implements ZapatoAdapter.OnZapatoDeletedListener {
    private ImageButton registrar;
    private RecyclerView zRecycler;
    private EditText edtBuscador;
    ZapatoAdapter zAdapter;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Ubica el fragment view que usaremos
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        //inicializando Vistas
        registrar = view.findViewById(R.id.btnAgregarStock);
        edtBuscador = view.findViewById(R.id.edtBuscador);
        zRecycler = view.findViewById(R.id.rcvStocks);
        //Inicializando el adaptador
        db = FirebaseFirestore.getInstance();
        zRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = db.collection("inventarios");
        FirestoreRecyclerOptions<Zapatos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Zapatos>()
                        .setQuery(query, Zapatos.class)
                        .setLifecycleOwner(this)
                        .build();

        zAdapter = new ZapatoAdapter(firestoreRecyclerOptions);
        zAdapter.setOnZapatoDeletedListener(this);
        zRecycler.setAdapter(zAdapter);

        // Configurar el listener para el EditText
        edtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                buscar(s.toString());
            }
        });

        registrar.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AgregarInventario.class);
            startActivity(i);
        });
        return view;
    }

    //Funcion para hacer la busqueda
    private void buscar(String busqueda) {
        Query query = db.collection("inventarios")
                .orderBy("marca")
                .orderBy("modelo")
                .startAt(busqueda)
                .endAt(busqueda + "\uf8ff");

        FirestoreRecyclerOptions<Zapatos> newfirestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Zapatos>()
                        .setQuery(query, Zapatos.class)
                        .setLifecycleOwner(this)
                        .build();

        zAdapter.updateOptions(newfirestoreRecyclerOptions);

    }

    @Override
    public void onStart() {
        super.onStart();
        zAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        zAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (zAdapter != null) {
            zAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onZapatoDeleted() {
        // Vuelve a realizar la búsqueda con el texto actual
        buscar(edtBuscador.getText().toString());
    }
}