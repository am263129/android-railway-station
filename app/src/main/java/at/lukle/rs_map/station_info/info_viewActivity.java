package at.lukle.rs_map.station_info;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import at.lukle.rs_map.R;

public class info_viewActivity extends AppCompatActivity implements View.OnClickListener {

    boolean food = true;
    boolean tourist = false;
    ImageView btn_food;
    ImageView btn_tourist;
    ArrayList<info> info_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);
        btn_food = (ImageView)findViewById(R.id.infobtn_food);
        btn_tourist = (ImageView)findViewById(R.id.infobtn_tourist);
        btn_food.setOnClickListener(this);
        btn_tourist.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent.getStringExtra("TYPE").equals("Food"))
        {
            changeIcon(food);
        }
        else
        {
            changeIcon(tourist);
        }
    }
    private void changeIcon(boolean flag){
        if (flag){
            btn_tourist.setImageResource(R.drawable.ic_tourist1_unselected);
            btn_food.setImageResource(R.drawable.ic_food);
        }
        else
        {
            btn_tourist.setImageResource(R.drawable.ic_tourist1);
            btn_food.setImageResource(R.drawable.ic_food_unselected);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.infobtn_food:
                changeIcon(food);
                break;
            case R.id.infobtn_tourist:
                changeIcon(tourist);
                break;
        }
    }
}