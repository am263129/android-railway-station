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

public class info_viewActivity extends AppCompatActivity implements View.OnClickListener {

    boolean food = true;
    boolean tourist = false;
    ArrayList<info> array_lifo_restaurant = new ArrayList<info>();
    ArrayList<info> array_lifo_tourist = new ArrayList<info>();
    ImageView btn_food;
    ImageView btn_tourist;
    ListView list_info;
    infoAdapter  adapter_restaurant,adapter_tourist;
    ArrayList<info> info_list;
    String[] restaurant_names = {"Rios Restaurant","White Hill Restaurant","Freeze wine","Tony restaurant","Stop and go!"};
    String[] restaurant_description = {"This restaurant is the famous of France Wine. There, you can find Freezed pock and delicious food","This restaurant is one of the most famous Pizza restaurant in the City."
            ,"More than 5000 people have lunch here. Delicious meal and cheap price makes them have lunch here","Only a few people can have a eating here. There are only 3 members in this restaurant and the price of the meal is very high"
            ,"Spargetti and Italian Pizza is this restaurant's main food. to eat it, more and more people come here"};
    String[] tourist_name = {"Hall of Glory","Palace of Ivory","Home of Holy Knight","Blood of Fight Warrior","Winner and Loser"};
    String[] tourist_description = {"The building was built in more than 3 thousands years ago. You can see the sign of the patient knight"
            ,"There was more than 600 kilos of ivory there. it was keeping them for more than 2 thousands years, no one know that who did keep them and how did bright that",
            "Here, there was the biggest battle in the Europe. more than 10 million knight fight here and more than 8000 knights died",
            "No one know why so many tourists visit here!",
            "7 hundreds years ago, one knight fight here to keep piece against Dark magic party. after war of 40 years, he finally defeat the dark party and keep piece"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);
        btn_food = (ImageView)findViewById(R.id.infobtn_food);
        btn_tourist = (ImageView)findViewById(R.id.infobtn_tourist);
        TextView station_name = (TextView)findViewById(R.id.station_name);
        list_info = (ListView)findViewById(R.id.list_info);
        btn_food.setOnClickListener(this);
        btn_tourist.setOnClickListener(this);
        Intent intent = getIntent();
        make_randomdata_restaurant();
        make_randomdata_tourist();
        if (intent.getStringExtra("TYPE").equals("Food"))
        {
            changeIcon(food);
        }
        else
        {
            changeIcon(tourist);
        }
        String selected = intent.getStringExtra("NAME");
        station_name.setText(selected);


    }



    private void changeIcon(boolean flag){
        if (flag){
            btn_tourist.setImageResource(R.drawable.ic_tourist1_unselected);
            btn_food.setImageResource(R.drawable.ic_food);
            list_info.setAdapter(adapter_restaurant);

        }
        else
        {
            btn_tourist.setImageResource(R.drawable.ic_tourist1);
            btn_food.setImageResource(R.drawable.ic_food_unselected);
            list_info.setAdapter(adapter_tourist);
        }
    }



    private void make_randomdata_restaurant() {
        final int min = 0;
        final int max = 4;
        final int random_count = new Random().nextInt((max - min) + 1) + min+1;
        final int init_index =  R.drawable.ico_go;
        ArrayList<Integer> list_random_image = new ArrayList<Integer>();
        ArrayList<Integer> list_random_name = new ArrayList<Integer>();
        ArrayList<Integer> list_random_description = new ArrayList<Integer>();
        for (int i = 0; i< random_count; i ++) {
            int random_image =  get_random_item(list_random_image);
            list_random_image.add(random_image);
            int random_name =  get_random_item(list_random_name);
            list_random_name.add(random_name);
            int random_description =  get_random_item(list_random_description);
            list_random_description.add(random_description);
            final int min_distance = 100;
            final int max_distance = 2000;
            final int random_distance = new Random().nextInt((max_distance - min_distance) + 1) + min;
            info info = new info(restaurant_names[random_name],init_index + random_image+1,String.valueOf(random_distance),restaurant_description[random_description]);
            array_lifo_restaurant.add(info);
        }
        adapter_restaurant = new infoAdapter(info_viewActivity.this,R.layout.item_info,array_lifo_restaurant);

    }
    private void make_randomdata_tourist() {
        final int min = 0;
        final int max = 4;
        final int random_count = new Random().nextInt((max - min) + 1) + min+1;
        final int init_index = R.drawable.item_food_5;
        ArrayList<Integer> list_random_image = new ArrayList<Integer>();
        ArrayList<Integer> list_random_name = new ArrayList<Integer>();
        ArrayList<Integer> list_random_description = new ArrayList<Integer>();
        for (int i = 0; i< random_count; i ++) {
            int random_image =  get_random_item(list_random_image);
            list_random_image.add(random_image);
            int random_name =  get_random_item(list_random_name);
            list_random_name.add(random_name);
            int random_description =  get_random_item(list_random_description);
            list_random_description.add(random_description);
            final int min_distance = 100;
            final int max_distance = 2000;
            final int random_distance = new Random().nextInt((max_distance - min_distance) + 1) + min;
            info info = new info(tourist_name[random_name], init_index + random_image + 1, String.valueOf(random_distance), tourist_description[random_description]);
            array_lifo_tourist.add(info);
        }

        adapter_tourist = new infoAdapter(info_viewActivity.this,R.layout.item_info,array_lifo_tourist);
    }
    private int get_random_item(ArrayList<Integer> list_items){
        final int min = 0;
        final int max = 4;
        int random_number = 0;
        do {
            random_number = new Random().nextInt((max - min) + 1) + min;
        }while (!get_new(list_items,random_number));
        return random_number;
    }

    private boolean get_new(ArrayList<Integer> list_items, int random_number){
        boolean new_only = true;
        for(int i = 0; i < list_items.size();i++){
            if(list_items.get(i) == random_number) {
                new_only = false;
                break;
            }
        }
        return new_only;
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
