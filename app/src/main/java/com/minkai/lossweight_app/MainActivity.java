package com.minkai.lossweight_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private SimpleBluetoothDeviceInterface deviceInterface;
    private AppBarConfiguration mAppBarConfiguration;
    // Our main handler that will receive callback notifications
    // bluetooth background worker thread to send and receive data
    private BluetoothAdapter mBTAdapter;
    // bi-directional client-to-client data path

    // #defines for identifying shared types between calling functions
    public final static int REQUEST_ENABLE_BT = 1;
    // used to identify adding bluetooth names
    // used in bluetooth handler to identify message update
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter bluetoothAdapter;
    public int receivedSize;
    public ArrayAdapter<String> mBTArrayAdapter;
    public ArrayList<String> ReceiveData = new ArrayList();
    BluetoothSPP bt = new BluetoothSPP(this);
    //private ConnectedThread mConnectedThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_status, R.id.nav_devices, R.id.nav_realtime_plot, R.id.nav_data_debug,
                 R.id.nav_share, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTArrayAdapter = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_list_item_1);

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "你的裝置不支援藍芽=  =", Toast.LENGTH_LONG).show();
        }
        else
        {
            //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            //registerReceiver(receiver, filter);
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "藍芽未開啟，請在此畫面允許藍芽開啟，才能與裝置連接~", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else
            {
                Toast.makeText(getApplicationContext(), "請從選單中「藍芽裝置管理」中連接裝置", Toast.LENGTH_LONG).show();
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                discover();
            }

        }

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,BLUETOOTH_ADMIN,BLUETOOTH_SERVICE,ACCESS_FINE_LOCATION}, 1);
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                // Do something when data incoming
                //Log.i("Rcv_Str", message);
                //Log.i("Rcv_Byte", String.valueOf(uint16(data[0],data[1])));
                //Toast.makeText(getApplicationContext(), message,
                        //Toast.LENGTH_SHORT).show();
                ReceiveData.add(message);

            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                // Do something when successfully connected
                Toast.makeText(getApplicationContext(), "Connected to " + name ,
                        Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                // Do something when connection was disconnected
            }

            public void onDeviceConnectionFailed() {
                // Do something when connection failed
            }
        });
        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            public void onServiceStateChanged(int state) {
                if(state == BluetoothState.STATE_CONNECTED) {
                    Log.i("Check", "State : Connected");

                }
                else if(state == BluetoothState.STATE_CONNECTING)
                    Log.i("Check", "State : Connecting");
                else if(state == BluetoothState.STATE_LISTEN)
                    Log.i("Check", "State : Listen");
                else if(state == BluetoothState.STATE_NONE)
                    Log.i("Check", "State : None");
            }
        });

    }

    public static int uint16(byte hi, byte lo) {
        return ((hi & 0xff) << 8) | (lo & 0xff);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_adddevices:
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.nav_devices);

                return true;
            case R.id.action_settings:
                Toast.makeText(this, "還在開發中唷", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregisterReceiver(receiver);
    }

    public AdapterView.OnItemClickListener mDeviceClickListener = new
            AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

                    if(!bluetoothAdapter.isEnabled()) {
                        Toast.makeText(getApplicationContext(), "Bluetooth not on",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bluetoothAdapter.cancelDiscovery();

                    // Get the device MAC address, which is the last 17 chars in the View
                    String info = ((TextView) v).getText().toString();
                    final String address = info.substring(info.length() - 17);
                    final String name = info.substring(0,info.length() - 17);
                    Toast.makeText(getApplicationContext(), "正在連線到裝置:"+name+"("+address+")",
                            Toast.LENGTH_SHORT).show();

                    // Spawn a new thread to avoid blocking the GUI one
                   connectDevice(address,name);
                }
            };



    public void discover(){
       if(bluetoothAdapter.isDiscovering()){ //如果已經找到裝置
           bluetoothAdapter.cancelDiscovery(); //取消尋找
           Toast.makeText(getApplicationContext(),"掃描已經停止，請至右上角「重新掃描」",Toast.LENGTH_SHORT).show();
       }
      else{
            if(bluetoothAdapter.isEnabled()) { //如果沒找到裝置且已按下尋找
                mBTArrayAdapter.clear(); // clear items
                bluetoothAdapter.startDiscovery(); //開始尋找
                Toast.makeText(getApplicationContext(), "請稍後，正在掃描裝置 :)",
                        Toast.LENGTH_SHORT).show();
                getApplicationContext().registerReceiver(blReceiver, new
                        IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "開藍芽好嗎:(",
                        Toast.LENGTH_SHORT).show();
            }
       }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();

            }
        }
    };
    private void connectDevice(String mac,String name) {
        bt.connect(mac);
    }



    }



