package edu.temple.bitcoindashboard;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {
    ArrayList addressList;
    LinearLayout addressLayout;
    ScrollView sv;
    String url = "http://btc.blockr.io/api/v1/address/info/";
    Address currentAddress;
    TextView addressBalance;
    boolean addToArrayList = true;


    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        File path = getActivity().getApplicationContext().getFilesDir();
        File file = new File(path, "savedAddressesBitcoinDash.txt");

        FileInputStream in = null;
        ObjectInputStream obj = null;
        try {
            in = new FileInputStream(file);
            obj = new ObjectInputStream(in);
            addressList = (ArrayList) obj.readObject();
            in.close();
            obj.close();
            System.out.println("Read success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Read fail");
            addressList = new ArrayList();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();


        File path = getActivity().getBaseContext().getFilesDir();
        File file = new File(path, "savedAddressesBitcoinDash.txt");

        FileOutputStream fos= null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(addressList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_address, container, false);
        addressLayout = (LinearLayout) v.findViewById(R.id.addressLinearLayout);
        sv = (ScrollView) v.findViewById(R.id.addressScrollView);
        addressBalance = (TextView) v.findViewById(R.id.addressCurrentBalance);
        if(addressList!=null){
        loadArray(addressList);
        }
        else{
            addressList = new ArrayList();
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addToArrayList = true;
                fetchAddress(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                // do s.th.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void addAddress(String address)
    {
      addressList.add(address);
      final TextView tv = new TextView(this.getActivity().getApplicationContext());
        tv.setText(address);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToArrayList = false;
                fetchAddress(tv.getText().toString());
            }
        });
        addressLayout.addView(tv);
    }
    public void loadAddress(String address)
    {
        final TextView tv = new TextView(this.getActivity().getApplicationContext());
        tv.setText(address);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToArrayList = false;
                fetchAddress(tv.getText().toString());
            }
        });
        addressLayout.addView(tv);
    }

    private void fetchAddress(final String query){
        Thread t = new Thread(){
            @Override
            public void run(){

                try {

                    URL addressURL = new URL(url+query);

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    addressURL.openStream()));

                    String response = "", tmpResponse;

                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONObject stockObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = stockObject;
                    addressHandler.sendMessage(msg);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler addressHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            JSONObject responseObject = (JSONObject) msg.obj;

            try {
                currentAddress = new Address(responseObject.getJSONObject("data"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(currentAddress.isValid()){
            updateViews();
                if(addToArrayList) {
                    addAddress(currentAddress.getAddress());
                }
            }
            else{
                Toast.makeText(getActivity(),getString(R.string.invalidAddress),Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    });

    private void updateViews() {
        addressBalance.setText(currentAddress.getBalance()+"");
    }
    private void loadArray(ArrayList addresses){
        addToArrayList = false;
        for(int i = 0; i<addresses.size(); i++)
        loadAddress(addresses.get(i).toString());
    }
}
