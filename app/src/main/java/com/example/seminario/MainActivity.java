package com.example.seminario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Switch swMode;
    private Switch swLighter;
    private Switch swBluetooth;

    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashlightOn;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout home = findViewById(R.id.rl_home);
        RelativeLayout profile = findViewById(R.id.rl_profile);
        RelativeLayout settings = findViewById(R.id.rl_settings);
        RelativeLayout about = findViewById(R.id.rl_about);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        ImageButton btnOpenDrawer = findViewById(R.id.btn_open_drawer);

        // Set click listener for the button to open the drawer
        btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // Set up ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set up NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Handle Navigation Drawer item clicks
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        home.setVisibility(View.VISIBLE);
                        profile.setVisibility(View.INVISIBLE);
                        settings.setVisibility(View.INVISIBLE);
                        about.setVisibility(View.INVISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_profile:
                        home.setVisibility(View.INVISIBLE);
                        profile.setVisibility(View.VISIBLE);
                        settings.setVisibility(View.INVISIBLE);
                        about.setVisibility(View.INVISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_settings:
                        home.setVisibility(View.INVISIBLE);
                        profile.setVisibility(View.INVISIBLE);
                        settings.setVisibility(View.VISIBLE);
                        about.setVisibility(View.INVISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_about:
                        home.setVisibility(View.INVISIBLE);
                        profile.setVisibility(View.INVISIBLE);
                        settings.setVisibility(View.INVISIBLE);
                        about.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_exit:
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

        swMode = findViewById(R.id.sw_mode);

        swMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle switch state changes
                switch (buttonView.getId()) {
                    case R.id.sw_mode:
                        if (isChecked) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        swLighter = findViewById(R.id.sw_lighter);

        swLighter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle switch state changes
                switch (buttonView.getId()) {
                    case R.id.sw_lighter:
                        if (isChecked) {
                            turnOnFlashlight();
                        } else {
                            turnOffFlashlight();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Obtém o ID da câmera traseira
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        swBluetooth = findViewById(R.id.sw_bluetooth);

        swBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle switch state changes
                switch (buttonView.getId()) {
                    case R.id.sw_bluetooth:
                        if (isChecked) {
                            enableBluetooth();
                        } else {
                            disableBluetooth();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    private void turnOnFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, true); // Liga a lanterna
            isFlashlightOn = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, false); // Desliga a lanterna
            isFlashlightOn = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void enableBluetooth() {
        if (bluetoothAdapter == null) {
            // O dispositivo não suporta Bluetooth
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            // O Bluetooth não está ativado, solicita a ativação
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableBluetoothIntent, 0);
        } else {
            // O Bluetooth já está ativado
            Toast.makeText(this, "Bluetooth já ativo!", Toast.LENGTH_SHORT).show();
        }
    }

    public void finish(){
        this.finishAffinity();
    }
    private void disableBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothAdapter.disable();
            Toast.makeText(this, "Bluetooth disabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bluetooth is already disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // O Bluetooth foi ativado com sucesso
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            } else {
                // O Bluetooth não foi ativado
                Toast.makeText(this, "Bluetooth couldn't be enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isFlashlightOn) {
            turnOffFlashlight(); // Certifique-se de desligar a lanterna quando a atividade é pausada
        }
    }

}