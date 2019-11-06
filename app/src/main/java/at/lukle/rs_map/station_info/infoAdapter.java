package at.lukle.rs_map.station_info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import at.lukle.rs_map.R;

public class infoAdapter extends ArrayAdapter<info> {
    ArrayList<info> array_info = new ArrayList<>();
    char member;
    CheckBox hire;
    public infoAdapter(Context context, int textViewResourceId, ArrayList<info> objects) {
        super(context, textViewResourceId, objects);
        array_info = objects;
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
        ImageView info_pic = (ImageView)v.findViewById(R.id.info_pic);
        TextView info_name = (TextView)v.findViewById(R.id.info_name);
        TextView info_distance = (TextView)v.findViewById(R.id.info_distance);
        TextView info_description = (TextView)v.findViewById(R.id.link_description);

        info_name.setText(array_info.get(position).getInfo_name());
        info_distance.setText(array_info.get(position).getInfo_distance());
        info_pic.setImageBitmap(setPhoto(array_info.get(position).getInfo_pic()));

        return v;
    }

    private Bitmap setPhoto(String base64photo) {
        String imageDataBytes = base64photo.substring(base64photo.indexOf(",")+1);

        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }
}
