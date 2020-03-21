package com.minkai.lossweight_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private SimpleBluetoothDeviceInterface deviceInterface;
    private AppBarConfiguration mAppBarConfiguration;
    private BluetoothSocket mBTSocket = null;
    private Handler mHandler;
    // Our main handler that will receive callback notifications
    // bluetooth background worker thread to send and receive data
    private BluetoothAdapter mBTAdapter;
    // bi-directional client-to-client data path
    private final static int MESSAGE_READ = 2;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    // #defines for identifying shared types between calling functions
    public final static int REQUEST_ENABLE_BT = 1;
    // used to identify adding bluetooth names
    // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3;
    // used in bluetooth handler to identify message status
    private  String _recieveData = "";
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter bluetoothAdapter;
    public int receivedSize;
    public ArrayAdapter<String> mBTArrayAdapter;
    public ArrayList<String> ReceiveData = new ArrayList();
    //private ConnectedThread mConnectedThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_status, R.id.nav_devices, R.id.nav_realtime_plot, R.id.nav_data_debug,
                 R.id.nav_share, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothManager= BluetoothManager.getInstance();
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();



        mBTArrayAdapter = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_list_item_1);

//            mHandler = new Handler(){
//                public void handleMessage(android.os.Message msg){
//                    if(msg.what == MESSAGE_READ){ //收到MESSAGE_READ 開始接收資料
//                        String readMessage = null;
//
//                            byte[] received=(byte[])msg.obj;
//                            readMessage = new String(String.valueOf(uint16((byte) received[1],(byte) received[0])));
//
//                            //readMessage =  readMessage.substring(0,1);
//                            //取得傳過來字串的第一個字元，其餘為雜訊
//                            //_recieveData += readMessage; //拼湊每次收到的字元成字串
//
//                        Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_LONG).show();
//
//                    }
//
//                    if(msg.what == CONNECTING_STATUS){
//                        //收到CONNECTING_STATUS 顯示以下訊息
//                        if(msg.arg1 == 1)
//                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//
//                        else
//                            Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_LONG).show();
//                    }
//                }
//            };
//        mHandler = new Handler(){
//            public void handleMessage(android.os.Message msg){
//                if(msg.what == MESSAGE_READ){ //收到MESSAGE_READ 開始接收資料
//                    String readMessage = null;
//                    try {
//                        readMessage = new String((byte[]) msg.obj, "UTF-8");
//                        readMessage =  readMessage.substring(0,1);
//                        //取得傳過來字串的第一個字元，其餘為雜訊
//                        recieveData += readMessage; //拼湊每次收到的字元成字串
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    ReceiveData.add(recieveData);
//                    //Toast.makeText(getApplicationContext(), recieveData, Toast.LENGTH_LONG).show();
//
//                }
//
//                if(msg.what == CONNECTING_STATUS){
//                    //收到CONNECTING_STATUS 顯示以下訊息
//                    if(msg.arg1 == 1) {
//                        Toast.makeText(getApplicationContext(), "成功連線到裝置: " + (String) (msg.obj), Toast.LENGTH_LONG).show();
//                        connectName=(String) (msg.obj);
//                    }
//                    else
//                        Toast.makeText(getApplicationContext(),"連線失敗，原因: "
//                                + (String)(msg.obj), Toast.LENGTH_LONG).show();
//                }
//            }
//        };
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
               // discover();
            }

        }

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,BLUETOOTH_ADMIN,BLUETOOTH_SERVICE,ACCESS_FINE_LOCATION}, 1);
        //mDevicesListView= findViewById(R.id.mDevicesListView);
       //mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
       //mDevicesListView.setOnItemClickListener(mDeviceClickListener);S

       // listDevices.setAdapter(mBTArrayAdapter); // assign model to view
        //listDevices.setOnItemClickListener(mDeviceClickListener);

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
//        new Thread()
//        {
//            public void run() {
//                boolean fail = false;
//                //取得裝置MAC找到連接的藍芽裝置
//                BluetoothDevice device = mBTAdapter.getRemoteDevice(mac);
//
//                try {
//                    mBTSocket = createBluetoothSocket(device);
//                    //建立藍芽socket
//                } catch (IOException e) {
//                    fail = true;
//                    Toast.makeText(getBaseContext(), "Socket creation failed",
//                            Toast.LENGTH_SHORT).show();
//                }
//                // Establish the Bluetooth socket connection.
//                try {
//                    mBTSocket.connect(); //建立藍芽連線
//                } catch (IOException e) {
//                    try {
//                        fail = true;
//                        mBTSocket.close(); //關閉socket
//                        //開啟執行緒 顯示訊息
//                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
//                                .sendToTarget();
//                    } catch (IOException e2) {
//                        //insert code to deal with this
//                        Toast.makeText(getBaseContext(), "Socket creation failed",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//                if(fail == false) {
//                    //開啟執行緒用於傳輸及接收資料
//                    mConnectedThread = new ConnectedThread(mBTSocket);
//                    mConnectedThread.start();
//                    //開啟新執行緒顯示連接裝置名稱
//                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
//                            .sendToTarget();
//                }
//            }
//        }.start();
    }

//    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws
//            IOException {
//        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
//        //creates secure outgoing connection with BT device using UUID
//    }

//    private class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run() {
//            byte[] buffer = new byte[1024];  // buffer store for the stream
//            int bytes; // bytes returned from read()
//            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.available();
//                    if (bytes != 0) {
//                        bytes = mmInStream.available();
//                        // how many bytes are ready to be read?
//                        bytes = mmInStream.read(buffer, 0, bytes);
//                        // record how many bytes we actually read
//                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                                .sendToTarget(); // Send the obtained bytes to the UI activity
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                    break;
//                }
//            }
//        }


        //private void onMessageReceived(String message) {
            // We received a message! Handle it here.
            //Toast.makeText(getApplicationContext(), "Received a message! Message was: " + message, Toast.LENGTH_LONG).show(); // Replace context with your context instance.
           // ReceiveData.add(message);
           // receivedSize += 1;
      //  }


    }


