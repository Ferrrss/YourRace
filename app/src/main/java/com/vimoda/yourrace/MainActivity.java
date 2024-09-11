package com.vimoda.yourrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vimoda.yourrace.Fragments.Inventario.FragmentInventory;
import com.vimoda.yourrace.Fragments.MainFragment;
import com.google.android.material.navigation.NavigationView;
import com.vimoda.yourrace.crud.PerfilFragmentG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout dl;
    ActionBarDrawerToggle abt;
    Toolbar t;
    NavigationView nv;

    //Variables para cargar el Fragment
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = findViewById(R.id.mt_toolbar);
        setSupportActionBar(t);
        dl = findViewById(R.id.dl_drawer);
        nv = findViewById(R.id.nv_menu_lateral);
        //Establecer evento onclick al navigationView
        nv.setNavigationItemSelectedListener(this);

        abt = new ActionBarDrawerToggle(this,dl,t,R.string.open_drawer,R.string.close_drawer);
        dl.addDrawerListener(abt);
        abt.setDrawerIndicatorEnabled(true);
        abt.syncState();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new MainFragment());
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        dl.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId()==R.id.menu_home){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new MainFragment());
            ft.commit();
        }
        if(menuItem.getItemId()==R.id.menu_inventory){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new FragmentInventory());
            ft.commit();
        }
        if(menuItem.getItemId()==R.id.menu_profileT){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new PerfilFragmentG());
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


/*if(menuItem.getItemId()==R.id.menu_sales){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new FragmentSales());
            ft.commit();
        }*/
        /*if(menuItem.getItemId()==R.id.menu_orders){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new FragmentOrders());
            ft.commit();
        }*/
        /*if(menuItem.getItemId()==R.id.menu_analysis_reports){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new FragmentAnalysisReports());
            ft.commit();
        }*/