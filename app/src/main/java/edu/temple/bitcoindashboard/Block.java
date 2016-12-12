package edu.temple.bitcoindashboard;

import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tom on 11/28/2016.
 */
public class Block {

    private int nb;
    private double difficulty, daysDestroyed;
    private String prevNb, nextNb, fee, size;

    public Block(int nb, String prevNb, String nextNb, double difficulty, double daysDestroyed, String fee, String size) {
        this.nb = nb;
        this.prevNb = prevNb;
        this.nextNb = nextNb;
        this.difficulty = difficulty;
        this.daysDestroyed = daysDestroyed;
        this.fee = fee;
        this.size = size;
    }

    public Block (JSONObject blockObject) throws JSONException{
        this(blockObject.getInt("nb"),blockObject.getString("prev_block_hash"), blockObject.getString("next_block_hash"),
        blockObject.getDouble("difficulty"),blockObject.getDouble("days_destroyed"),blockObject.getString("fee"),
                blockObject.getString("size"));
    }

    public String getBlock(){
        return nb+"";
    }
    public int getBlockAsInt(){return nb;}
    public String getDifficulty(){
        return difficulty+"";
    }
    public String getDaysDestroyed(){
        return daysDestroyed+"";
    }
    public String getFee(){
        return fee;
    }
    public String getSize(){return size;}
    public String getNextBlock(){return nextNb;}
    public String getPreviousBlock(){return prevNb;}


    @Override
    public boolean equals(Object object){

        return false;
    }

    public JSONObject getBlockAsJSON(){
        JSONObject blockObject = new JSONObject();
        try {
            blockObject.put("nb", nb);
            blockObject.put("prevNb",prevNb);
            blockObject.put("nextNb",nextNb);
            blockObject.put("difficulty", difficulty);
            blockObject.put("daysDestroyed", daysDestroyed);
            blockObject.put("fee",fee);
            blockObject.put("size", size);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blockObject;
    }
}
