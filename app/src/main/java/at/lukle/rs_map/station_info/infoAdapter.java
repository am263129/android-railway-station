package at.lukle.rs_map.station_info;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

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


        String urlOfImage = array_info.get(position).getInfo_pic();

        String image_name = array_info.get(position).getInfo_pic().substring(0,array_info.get(position).getInfo_pic().length()-4);
        String package_name = context.getPackageName();
        int id = context.getResources().getIdentifier("drawable/"+image_name, null, context.getPackageName());
//        int resId = parent.getResources().getIdentifier(image_name, "drawable", package_name);
//        info_pic.setImageResource(R.drawable.);
//        new ImageDownloadTask(info_pic).execute(urlOfImage);
        try {
            info_pic.setImageResource(id);
        }catch (Exception e){
            Log.e("Wanning"," No image file");
        }
        if (array_info.get(position).getInfo_description().equals(""))
            desctiption.setVisibility(View.GONE);

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


    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap>{

        //Initialize an ImageView Class/widget
        ImageView imageView;

        public ImageDownloadTask(ImageView iv){
            //Specify the initialized ImageView is same as the method calling ImageView
            this.imageView=iv;
        }

        //Background task to download image as bitmap
        protected Bitmap doInBackground(String... urls){
            String urlToDisplay = urls[0];
            Bitmap bmp = null;
            try{
                //Try to download the image from web as stream
                InputStream inputStream = new java.net.URL(urlToDisplay).openStream();
                //decodeStream(InputStream is) method decode an input stream into a bitmap.
                bmp = BitmapFactory.decodeStream(inputStream);

            }catch(Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        //do something with the result
        protected void onPostExecute(Bitmap result)
        {
            //Specify ImageView image source from downloaded image
            imageView.setImageBitmap(result);
        }
    }
}
