package edu.temple.bitcoindashboard;


import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;


public class Address {

    private String address;
    private double balance;
    private boolean is_valid;

    public Address(String address, double balance, boolean is_valid) {
        this.address = address;
        this.balance = balance;
        this.is_valid = is_valid;
    }

    public Address (JSONObject addressObject) throws JSONException{
        this(addressObject.getString("address"), addressObject.getDouble("balance"), addressObject.getBoolean("is_valid"));
    }

    public String getAddress(){
        return address;
    }
    public double getBalance(){
        return balance;
    }
    public boolean isValid(){
        //true means is valid, false means is not valid
        return is_valid;
    }


    public JSONObject getAddressAsJSON(){
        JSONObject addressObject = new JSONObject();
        try {
            addressObject.put("address", address);
            addressObject.put("balance", balance);
            addressObject.put("is_valid", is_valid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addressObject;
    }

}

