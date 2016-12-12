package edu.temple.bitcoindashboard;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    ListView lv;
    menuInterface activity;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_menu, container, false);
        lv = (ListView) v.findViewById(R.id.fragmentMenuList);

        Resources r = getResources();
        CustomAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1, r.getStringArray(R.array.menuArray));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                activity.menuHandler(null, position);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity c) {
        super.onAttach(c);
        activity = (menuInterface) c;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public interface menuInterface{
        void menuHandler(View v, int position);
    }

}
