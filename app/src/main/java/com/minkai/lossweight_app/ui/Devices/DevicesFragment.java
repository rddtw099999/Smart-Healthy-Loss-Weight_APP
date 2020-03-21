package com.minkai.lossweight_app.ui.Devices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.minkai.lossweight_app.MainActivity;
import com.minkai.lossweight_app.R;
import com.minkai.lossweight_app.ui.RealTimePlot.RealtimePlotFragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class DevicesFragment extends Fragment {

    private DevicesViewModel galleryViewModel;
    private ListView mDevicesListView;
    Timer connectStatusChecker = new Timer(true);;
    Handler handler;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(DevicesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        if (((MainActivity)getActivity()).bluetoothAdapter == null) {
            Toast.makeText((getActivity()).getApplicationContext(), "你的裝置不支援藍芽=  =", Toast.LENGTH_LONG).show();
        }
        else
        {
            //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            //registerReceiver(receiver, filter);
            if (!((MainActivity)getActivity()).bluetoothAdapter.isEnabled()) {
                Toast.makeText((getActivity()), "藍芽未開啟，請在此畫面允許藍芽開啟，才能與裝置連接~", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ((MainActivity)getActivity()).REQUEST_ENABLE_BT);
            }else
            {
                ((MainActivity)getActivity()).discover();
            }

        }

        mDevicesListView = root.findViewById(R.id.mDevicesListView);
        mDevicesListView.setAdapter(((MainActivity)getActivity()).mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(((MainActivity)getActivity()).mDeviceClickListener);

        Button btnrescan=root.findViewById(R.id.btn_rescan);
        Button btndisc=root.findViewById(R.id.btn_disconnect);

        btnrescan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnrescan.setEnabled(false);
                ((MainActivity)getActivity()).discover();
            }
        });
        btndisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).bluetoothManager.close();
            }
        });
        connectStatusChecker.schedule(new MyTimerTask(), 500, 500);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
              if(((MainActivity)getActivity()).bluetoothAdapter.isDiscovering()==false){
                    btnrescan.setEnabled(true);
              }else {
                  btnrescan.setEnabled(false);
              }

            }
        };
        return root;
    }

    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            handler.sendEmptyMessage(0);
        }
    };

    @Override
    public void onDestroyView() {
        connectStatusChecker.cancel();
        super.onDestroyView();
    }
}