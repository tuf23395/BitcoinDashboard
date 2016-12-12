package edu.temple.bitcoindashboard;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class PriceFragment extends Fragment {

    Price currentPrice = new Price("", 0);
    TextView currencyText;
    TextView priceText;


    public PriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_price, container, false);

        currencyText = (TextView) v.findViewById(R.id.currencyText);
        priceText = (TextView) v.findViewById(R.id.priceText);

        fetchPrice();

        return v;
    }

    private void fetchPrice(){
        Thread t = new Thread(){
            @Override
            public void run(){

                URL bitcoinPriceURL;

                try {

                    bitcoinPriceURL = new URL("https://blockchain.info/ticker");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    bitcoinPriceURL.openStream()));

                    String response = "", tmpResponse;

                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONObject stockObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = stockObject;
                    bitcoinPriceHandler.sendMessage(msg);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler bitcoinPriceHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            JSONObject responseObject = (JSONObject) msg.obj;

            try {
                currentPrice = new Price(getResources(), responseObject.getJSONObject(getString(R.string.currencyName)+""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateViews();

            return false;
        }
    });

    private void updateViews() {
        currencyText.setText(currentPrice.getName());
        priceText.setText(String.valueOf(getString(R.string.currencySymbol) + currentPrice.getPrice()));
    }
}
