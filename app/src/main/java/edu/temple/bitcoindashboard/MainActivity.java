package edu.temple.bitcoindashboard;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MenuFragment.menuInterface {
    boolean twoPanes;
    PriceFragment pf;
    ChartFragment cf;
    BlockFragment bf;
    AddressFragment af;
    MenuFragment mf;
    QRFragment qf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.menuFragment) != null) {
            twoPanes = true;
        }

        pf = new PriceFragment();
        cf = new ChartFragment();
        bf = new BlockFragment();
        af = new AddressFragment();
        qf = new QRFragment();

        if (twoPanes) {
            mf = new MenuFragment();
            loadFragment(R.id.menuFragment, mf, false);
            loadFragment(R.id.detailsFragment, pf, false);

        } else {
            final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.my_priceAction:
                                    loadFragment(R.id.detailsFragment, pf, false);
                                    break;
                                case R.id.my_chartAction:
                                    loadFragment(R.id.detailsFragment, cf, false);
                                    break;
                                case R.id.my_blockAction:
                                    loadFragment(R.id.detailsFragment, bf, false);
                                    break;
                                case R.id.my_addressAction:
                                    loadFragment(R.id.detailsFragment, af, false);
                                    break;
                                case R.id.my_qrAction:
                                    loadFragment(R.id.detailsFragment, qf, false);
                                    break;

                            }
                            return true;
                        }
                    });
            loadFragment(R.id.detailsFragment, pf, false);
        }

    }

    private void loadFragment(int ID, Fragment fragment, boolean backStack) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction().replace(ID, fragment);
        if (backStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
        fm.executePendingTransactions();
    }

    @Override
    public void menuHandler(View v, int position) {
        if(position==0){
            loadFragment(R.id.detailsFragment, pf, false);
        }
        else if(position==1){
            loadFragment(R.id.detailsFragment, cf, false);
        }
        else if(position==2){
            loadFragment(R.id.detailsFragment, bf, false);
        }
        else if(position==3){
            loadFragment(R.id.detailsFragment, af, false);
        }
        else if(position==4){
            loadFragment(R.id.detailsFragment, qf, false);
        }
    }
}
