package edu.temple.bitcoindashboard;


import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlockFragment extends Fragment {

    String block = "last";
    String URL = "http://btc.blockr.io/api/v1/block/info/";
    Block currentBlock = new Block(0,"","",0.0,0.0,"","");
    EditText blockEdit;
    TextView blockText;
    TextView difficulty;
    TextView fee;
    TextView daysDestroyed;
    TextView size;
    protected MotionEvent mLastOnDownEvent = null;


    public BlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_block, container, false);

        //initialize the text that is grabbed from the internet

        blockEdit = (EditText) v.findViewById(R.id.blockBlockEdit);
        blockText = (TextView) v.findViewById(R.id.blockBlockVal);
        difficulty = (TextView) v.findViewById(R.id.blockDifficultyVal);
        fee = (TextView) v.findViewById(R.id.blockFeeVal);
        daysDestroyed = (TextView) v.findViewById(R.id.blockDaysDestroyedVal);
        size = (TextView) v.findViewById(R.id.blockSizeVal);

        fetchBlock();

        //initialize swipe navigation for the blocks

        RelativeLayout blockLayout = (RelativeLayout) v.findViewById(R.id.blockRelLayout);

        final GestureDetector detector = new GestureDetector(v.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(velocityX > 0) {
                    nextBlock(currentBlock.getNextBlock());
                }
                else {
                    previousBlock(currentBlock.getPreviousBlock());
                }
                return true;
            }
        });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });

        //initialize the search button

        ImageButton searchBtn = (ImageButton) v.findViewById(R.id.buttonBlockchain);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                block = blockEdit.getText().toString();
                fetchBlock();
            }
        });

        //initialize the left and right navigation buttons for the blocks

        ImageButton leftNavBtn = (ImageButton) v.findViewById(R.id.blockLeftBtn);
        leftNavBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                previousBlock(currentBlock.getPreviousBlock());
            }
        });

        ImageButton rightNavBtn = (ImageButton) v.findViewById(R.id.blockRightBtn);
        rightNavBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                nextBlock(currentBlock.getNextBlock());
            }
        });

        return v;
    }
    private void nextBlock(String next){
        if(!next.equals("")) {
            block = next;
            fetchBlock();
        }
        else {
            Toast.makeText(getActivity(),getString(R.string.lastBlock),Toast.LENGTH_SHORT).show();
        }
    }
    private void previousBlock(String previous){
        if(currentBlock.getBlockAsInt() > 1){
            block = previous;
            fetchBlock();
        }
        else{
            Toast.makeText(getActivity(),getString(R.string.firstBlock),Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchBlock(){
        Thread t = new Thread(){
            @Override
            public void run(){

                URL bitcoinPriceURL;

                try {
                    String apiURL = URL + block;
                    bitcoinPriceURL = new URL(apiURL);

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
                    bitcoinBlockHandler.sendMessage(msg);
                } catch (Exception e){
                    //toast?
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler bitcoinBlockHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            JSONObject responseObject = (JSONObject) msg.obj;

            try {
                currentBlock = new Block(responseObject.getJSONObject("data"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateViews();

            return false;
        }
    });

    private void updateViews() {

        blockEdit.setText(currentBlock.getBlock());
        blockText.setText(currentBlock.getBlock());
        difficulty.setText(currentBlock.getDifficulty());
        fee.setText(currentBlock.getFee());
        daysDestroyed.setText(currentBlock.getDaysDestroyed());
        size.setText(currentBlock.getSize());
        block = currentBlock.getBlock();

    }
    public String getBlock(){
        return block;
    }

}
