package com.epicodus.csiems.bibliography.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.epicodus.csiems.bibliography.BibliographyApplication;
import com.epicodus.csiems.bibliography.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 123; //dummy int to return during permissions check
    @Bind(R.id.scanButton) FloatingActionButton mScanButton;
    @Bind(R.id.searchButton) FloatingActionButton mSearchButton;
    @Bind(R.id.manualEntryButton) FloatingActionButton mManualEntryButton;
    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseRef = BibliographyApplication.getAppInstance().getFirebaseRef();
//        checkForAuthenticatedUser();

        mScanButton.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        mManualEntryButton.setOnClickListener(this);
    }

    private void checkForAuthenticatedUser() {
        AuthData authData = mFirebaseRef.getAuth();
        if (authData == null) {
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                this.logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mFirebaseRef.unauth();
        goToLoginActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Switch statement requires compile time constants, Butterknife bindings don't happen until onCreate (i.e. they don't cut it)
            case R.id.scanButton:
                //permission check on Camera, if permission granted, do the thing
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    launchBarcodeScannerFragment();
                } else {
                    // show notification as to why the scanner button won't work
                    showMessageOKCancel("You need to allow access to the camera to use that feature",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                                }
                            });
                    return;
                }
                break;
        }
    }

    //boiler plate function to handle request result, uses dummy int constant
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Thank you!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "CAMERA Use Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                // other 'case' lines to check for other permissions this app might request go below here
        }
    }
    //custom dialog message to notify user why button doesn't work
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void launchBarcodeScannerFragment() {
        FragmentManager fm = getSupportFragmentManager();
        BarcodeScannerFragment barcodeScanner = BarcodeScannerFragment.newInstance();
        barcodeScanner.show(fm, "fragment_barcode_scanner");
    }

}
