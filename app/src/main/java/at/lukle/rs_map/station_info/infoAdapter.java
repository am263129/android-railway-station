package at.lukle.rs_map.station_info;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import at.lukle.rs_map.MainActivity;
import at.lukle.rs_map.R;

public class infoAdapter extends ArrayAdapter<info> {
    ArrayList<info> array_info = new ArrayList<>();
    char member;
    CheckBox hire;
    URL url;
    Bitmap default_image;
    Context context;
    ImageView info_pic;
    public infoAdapter(Context context, int textViewResourceId, ArrayList<info> objects) {
        super(context, textViewResourceId, objects);
        array_info = objects;
        this.context =context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_info, null);
        info_pic = (ImageView)v.findViewById(R.id.info_pic);
        TextView info_name = (TextView)v.findViewById(R.id.info_name);
        TextView info_distance = (TextView)v.findViewById(R.id.info_distance);
        TextView info_description = (TextView)v.findViewById(R.id.link_description);
        LinearLayout distance = (LinearLayout)v.findViewById(R.id.area_distance);

        final TextView desctiption = (TextView)v.findViewById(R.id.info_description);
        default_image = BitmapFactory.decodeResource(MainActivity.getInstance().getResources(),
                R.drawable.item_food);
        info_name.setText(array_info.get(position).getInfo_name());
        info_distance.setText(array_info.get(position).getInfo_distance());
        String index = array_info.get(position).getInfo_pic();



//        new DownloadImageTask(info_pic,default_image, position).execute(array_info.get(position).getInfo_pic());
//        Picasso.get().load("https://www.sendspace.com/file/ds8m19").into(info_pic);
        Glide.with(context).load("https://www.sendspace.com/file/ds8m19").into(info_pic);
        desctiption.setText(array_info.get(position).getInfo_description());
        info_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desctiption.setVisibility(View.VISIBLE);
            }
        });
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:" + String.valueOf(array_info.get(position).getLat()) + "," + String.valueOf(array_info.get(position).getLng()) + "?q="
                        + String.valueOf(array_info.get(position).getLat())
                        + "," + String.valueOf(array_info.get(position).getLng()) + "(" + array_info.get(position).getInfo_name()+")";
                Uri.parse(uri);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri));
                intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                context.startActivity(intent);
            }
        });

        return v;
    }
}
