package at.lukle.rs_map.station_info;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import at.lukle.rs_map.R;
import at.lukle.rs_map.util.Global;

public class info_viewActivity extends AppCompatActivity implements View.OnClickListener {

    boolean food = true;
    boolean tourist = false;
    ArrayList<info> array_lifo_restaurant = new ArrayList<info>();
    ArrayList<info> array_lifo_tourist = new ArrayList<info>();
    ImageView btn_food;
    ImageView btn_tourist;
    ListView list_info;
    infoAdapter  adapter_restaurant,adapter_tourist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);
        btn_food = (ImageView)findViewById(R.id.infobtn_food);
        btn_tourist = (ImageView)findViewById(R.id.infobtn_tourist);
        TextView station_name = (TextView)findViewById(R.id.station_name);
        list_info = (ListView)findViewById(R.id.list_info);
        btn_food.setOnClickListener(this);
//        btn_tourist.setOnClickListener(this);
        Intent intent = getIntent();
        String selected = intent.getStringExtra("NAME");
        station_name.setText(selected);

        for (int i = 0; i < Global.array_info.size(); i ++){
            if (Global.array_info.get(i).getStation_name().equals(selected)) {
                if (Global.array_info.get(i).getType().equals("tourist"))
                    array_lifo_tourist.add(Global.array_info.get(i));
                else
                    array_lifo_restaurant.add(Global.array_info.get(i));
            }
        }

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
            adapter_restaurant = new infoAdapter(info_viewActivity.this,R.layout.item_info,array_lifo_restaurant);
            list_info.setAdapter(adapter_restaurant);

        }
        else
        {
            btn_tourist.setImageResource(R.drawable.ic_tourist1);
            btn_food.setImageResource(R.drawable.ic_food_unselected);
            adapter_tourist = new infoAdapter(info_viewActivity.this,R.layout.item_info,array_lifo_tourist);
            list_info.setAdapter(adapter_tourist);
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
