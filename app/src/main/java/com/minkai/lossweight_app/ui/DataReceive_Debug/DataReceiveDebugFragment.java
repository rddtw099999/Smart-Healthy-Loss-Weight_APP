package com.minkai.lossweight_app.ui.DataReceive_Debug;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.minkai.lossweight_app.MainActivity;
import com.minkai.lossweight_app.R;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class DataReceiveDebugFragment extends Fragment {

    private DataReceiveDebugViewModel dataReceiveDebugViewModel;
    public TextView DataCount ;
    public TextView DataTime ;
    public TextView DataSPS ;
    public int startCount;
    public int currentCount;
    public int currentSPS;
    private int timecount;
    private int realtime;
    Handler handler;
    Timer timer = new Timer(true);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dataReceiveDebugViewModel =
                ViewModelProviders.of(this).get(DataReceiveDebugViewModel.class);
        View root = inflater.inflate(R.layout.fragment_data_debug, container, false);

        timer.schedule(new MyTimerTask(), 250, 250);
        DataCount = root.findViewById(R.id.text_receivedCount);
        DataTime = root.findViewById(R.id.text_received_time);
        DataSPS = root.findViewById(R.id.text_average_sps);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                DataTime.setText("時間："+ realtime);
                currentCount=((MainActivity)getActivity()).ReceiveData.size();
                DataCount.setText("接收數量："+currentCount);
                if(realtime!=0)
                currentSPS=(currentCount-startCount)/((realtime));
                DataSPS.setText("平均取樣率："+currentSPS+" /sps");
            }
        };
        startCount=((MainActivity)getActivity()).receivedSize;
        return root;
    }




    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            timecount=timecount+1;
            if(timecount>3){
                timecount=0;
                realtime=realtime+1;

            }
            handler.sendEmptyMessage(0);
        }
    };

    @Override
    public void onDestroyView() {
        timer.cancel();
        super.onDestroyView();
    }
}