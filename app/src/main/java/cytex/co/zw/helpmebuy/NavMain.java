package cytex.co.zw.helpmebuy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import cytex.co.zw.helpmebuy.fragment.AddProduct;
import cytex.co.zw.helpmebuy.fragment.AddUser;
import cytex.co.zw.helpmebuy.fragment.ReportsFragment;
import cytex.co.zw.helpmebuy.fragment.ViewProducts;
import cytex.co.zw.helpmebuy.fragment.ViewUsers;

public class NavMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        AddUser addUser=new AddUser();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,addUser,"Adding user");
        transaction.commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();  gara
        }
    }

    public void showMassage(String Title,String Message)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showMassage("About this App","\n\n 1.\t This app help the blinds by searching for product details " +
                    "on the app\n" +
                    "2.\t The response is in form of voice\n" +
                    "3.\t Admin add products details \n" +
                    "4.\t Admin can also add user that login in to the system\n" +
                    "5.\t All rights reserved\n"+
                    "-------------------------\n\n"+
                    "5.\t Developed by Patience Njiri 2019\n\n\n");
            return true;
        }
        else if(id==R.id.action_logout){
            android.app.AlertDialog.Builder alertDialogBuilder =  new android.app.AlertDialog.Builder(this)
                    .setTitle("Logout?")
                    .setMessage("Are you sure you want to Logout from " + getString(R.string.app_name) + "?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            signOut();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            AddUser addUser=new AddUser();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,addUser,"Adding user");
            transaction.commit();
        } else if (id == R.id.nav_gallery) {
            ViewUsers viewUsers=new ViewUsers();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,viewUsers,"View users");
            transaction.commit();
        } else if (id == R.id.nav_slideshow) {
            AddProduct addProduct=new AddProduct();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,addProduct,"Add Product");
            transaction.commit();
        } else if (id == R.id.nav_manage) {
            ViewProducts viewProducts=new ViewProducts();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,viewProducts,"View products");
            transaction.commit();
        }
        else if (id == R.id.report) {
            ReportsFragment reportsFragment=new ReportsFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,reportsFragment,"View reports");
            transaction.commit();
        }
        else if (id == R.id.nav_send) {
            showMassage("About this App","\n\n 1.\t This app help the blinds by searching for product details " +
                    "on the app\n" +
                    "2.\t The response is in form of voice\n" +
                    "3.\t Admin add products details \n" +
                    "4.\t Admin can also add user that login in to the system\n" +
                    "5.\t All rights reserved\n"+
                    "-------------------------\n\n"+
                    "Developed by Patience Njiri 2019\n\n");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(NavMain.this,LoginActivity.class));
    }
}
