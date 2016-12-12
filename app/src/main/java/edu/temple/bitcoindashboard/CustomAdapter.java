package edu.temple.bitcoindashboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Tom on 9/27/2016.
 */
public class CustomAdapter extends ArrayAdapter {

    public CustomAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = super.getView(position, convertView, parent);

        return v;
    }

}
