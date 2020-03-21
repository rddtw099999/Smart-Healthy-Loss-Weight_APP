package com.minkai.lossweight_app.ui.RealTimePlot;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.minkai.lossweight_app.MainActivity;
import com.minkai.lossweight_app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;



public class RealtimePlotFragment extends Fragment {

    Timer timer = new Timer(true);

    private float latestData;
    private int latestCount;
    private float timeCounter;

    private LineChart mChart;

    private boolean start_record;
    private float record_time;
    private int record_from;
    private int record_to;

    private boolean plotData = true;

    Handler handler;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         View root = inflater.inflate(R.layout.fragment_realtime_plot, container, false);


        timer.schedule(new MyTimerTask(), 100, 100);

        mChart = root.findViewById((R.id.chart1));

        // enable touch gestures
        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.getDescription().setEnabled(false);
        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        mChart.setData(data);


        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(1f);
        leftAxis.setAxisMinimum(-1f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

        //feedMultiple();
        Button btnRcd=root.findViewById(R.id.btn_startrecord);
        Button btnStopRcd=root.findViewById(R.id.btn_stoprecord);
        Button btnplay=root.findViewById(R.id.btn_play);
        Button btnexpRAW=root.findViewById(R.id.btn_export_raw);
        Button btnexpWAV=root.findViewById(R.id.btn_export_wav);

        TextView text_recordtime=root.findViewById(R.id.text_record_time);
        btnStopRcd.setEnabled(false);
        btnplay.setEnabled(false);
        btnRcd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "錄音已經開始，從陣列位置:" + latestCount, Toast.LENGTH_LONG).show();
                record_time=0;
                record_from=latestCount;
                start_record=true;
                btnRcd.setEnabled(false);
                btnStopRcd.setEnabled(true);

            }
        });
        btnStopRcd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getActivity(), "錄音已經結束，結束陣列位置:"+ latestCount, Toast.LENGTH_LONG).show();
                record_to=latestCount;
                start_record=false;
                btnRcd.setEnabled(true);
                btnStopRcd.setEnabled(false);
                btnplay.setEnabled(true);
            }
        });
        btnplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "開發中...", Toast.LENGTH_LONG).show();



//                int length = record_to-record_from; //10 seconds long
//                byte[] data = new byte[length];
//
//                final int TEST_SR = 8000; //This is from an example I found online.
//                final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
//                final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//                final int TEST_MODE = AudioTrack.MODE_STATIC; //I need static mode.
//                final int TEST_STREAM_TYPE = AudioManager.STREAM_ALARM;
//                AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT, length, TEST_MODE);
//
//
//
//                short[] array=new short[length];
//                int arraycount=0;
//                for(int i=record_from;i<=record_to;i++){
//                    float tempdata=Float.parseFloat(((MainActivity) getActivity()).ReceiveData.get(i))*Short.MAX_VALUE;
//                    array[arraycount]=(short)tempdata;
//                            arraycount+=1;
//                }
//
//
//                track.write(array, 0, length);
//                track.play();

            }
        });


        btnexpRAW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Please Wait :(" + latestCount, Toast.LENGTH_LONG).show();




                File file = new File(Environment.getExternalStorageDirectory(), "SmartHealthyLossWeight");
                if (!file.exists()) {
                    file.mkdir();
                }


                    try {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd_hh_mm_ss");

                        String fileName = sDateFormat.format(new java.util.Date());
                        File gpxfile= new File(file, fileName);
                        FileWriter writer = new FileWriter(gpxfile);
                        for(int i=record_from;i<=record_to;i++)
                        {
                            writer.append(((MainActivity) getActivity()).ReceiveData.get(i)+"\n");

                        }

                        writer.flush();
                        writer.close();
                        Toast.makeText(getActivity(), "檔案已經儲存至內部儲存空間的 SmartHealthyLossWeight資料夾內，檔案名稱為:" + fileName, Toast.LENGTH_LONG).show();
                    } catch (Exception e) { }



            }
        });
        btnexpWAV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "開發中 :(" + latestCount, Toast.LENGTH_LONG).show();


            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                text_recordtime.setText("長度:" + String.format("%02d:%02d" , (int)record_time/60,(int)record_time%60));
            }
        };



        return root;
    }
    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            if(plotData){
                addEntry();
            }
        }
    };
    private void addEntry() {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);


            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            if(start_record){
                handler.sendEmptyMessage(0);
                record_time+=0.1;
            }

            timeCounter+=0.1;

            //latestCount=((MainActivity)getActivity()).ReceiveData.size();
            latestCount=((MainActivity)getActivity()).receivedSize;
            if(latestCount!=0) {
                latestData = Integer.valueOf(((MainActivity) getActivity()).ReceiveData.get(latestCount - 1));
                latestData = (latestData - 500) / 500;
            }
            data.addEntry(new Entry(timeCounter,  latestData) , 0);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(10);
            mChart.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "麥克風即時時域圖");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(1f);
        set.setColor(Color.RED);
        set.setHighlightEnabled(false);
        set.setDrawValues(true);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }


    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File file = new File(mcoContext.getFilesDir(),"SmartLossWeight");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }


    @Override
    public void onDestroyView() {
        timer.cancel();
        super.onDestroyView();
    }

}