package edu.temple.bitcoindashboard;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    String chartURL = "https://chart.yahoo.com/z?s=BTCUSD=X&t=1d" ;
    String dispalyedURL = "";
    ImageView i;
    Bitmap bitmap;
    View v;
    int timer = 0;

    public ChartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chart, container, false);

        i = (ImageView) v.findViewById(R.id.imageView);
        fetchChart();

        Button b1 = (Button) v.findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartURL = "https://chart.yahoo.com/z?s=BTCUSD=X&t=1d";
            }
        });

        Button b2 = (Button) v.findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartURL = "https://chart.yahoo.com/z?s=BTCUSD=X&t=7d";
            }
        });

        Button b3 = (Button) v.findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartURL = "https://chart.yahoo.com/z?s=BTCUSD=X&t=14d";
            }
        });

        return v;
    }

    private void fetchChart() {

        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(!dispalyedURL.equals(chartURL) || timer==15) {
                            i = (ImageView) v.findViewById(R.id.imageView);
                            bitmap = BitmapFactory.decodeStream((InputStream) new URL(chartURL).getContent());
                            if (getActivity() == null) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    i.setImageBitmap(bitmap);
                                    i.invalidate();
                                    System.out.println("Updated image");
                                    dispalyedURL = chartURL;
                                    timer = 0;
                                }
                            });
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        timer++;
                        sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }


        };
        t.start();

    }

}
