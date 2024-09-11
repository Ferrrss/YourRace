package com.vimoda.yourrace.tiendaVirtual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.vimoda.yourrace.Login;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.tiendaVirtual.fragments.pedidos_compras.FragmentOrdersTv;
import com.vimoda.yourrace.tiendaVirtual.fragments.perfil.FragmentProfileTv;
import com.vimoda.yourrace.tiendaVirtual.fragments.carrito.FragmentShoppingCart;
import com.vimoda.yourrace.tiendaVirtual.fragments.MainFragmentTv;
import com.google.android.material.navigation.NavigationView;

public class MainTienda extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView rvZapatillas;
    DrawerLayout dl;
    ActionBarDrawerToggle abt;
    Toolbar t;
    NavigationView nv;

    //Variables para cargar el Fragment
    FragmentManager fm;
    FragmentTransaction ft;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tienda);
        t = findViewById(R.id.mt_toolbar1);
        setSupportActionBar(t);
        dl = findViewById(R.id.dl_drawer1);
        nv = findViewById(R.id.nv_menu_lateral1);
        rvZapatillas = findViewById(R.id.rvZapatillas);
        //Establecer evento onclick al navigationView
        nv.setNavigationItemSelectedListener(this);

        abt = new ActionBarDrawerToggle(this,dl,t,R.string.open_drawer,R.string.close_drawer);
        dl.addDrawerListener(abt);
        abt.setDrawerIndicatorEnabled(true);
        abt.syncState();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container1, new MainFragmentTv());
        ft.commit();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        dl.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId()==R.id.menu_home){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container1, new MainFragmentTv());
            ft.commit();
        }
        if(menuItem.getItemId()==R.id.menu_shopping_cart){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container1, new FragmentShoppingCart());
            ft.commit();
        }
        if(menuItem.getItemId()==R.id.menu_tvorders){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container1, new FragmentOrdersTv());
            ft.commit();
        }
        if(menuItem.getItemId()==R.id.menu_profile){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container1, new FragmentProfileTv());
            ft.commit();
        }
        if(menuItem.getItemId()==R.id.menu_exit){
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }
        return false;
    }
}