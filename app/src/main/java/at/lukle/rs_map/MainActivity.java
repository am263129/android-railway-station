package at.lukle.rs_map;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import at.lukle.rs_map.searchspinner.SimpleArrayListAdapter;
import at.lukle.rs_map.station_info.info;
import at.lukle.rs_map.station_info.info_viewActivity;
import at.lukle.rs_map.util.Global;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements OnClickableAreaClickedListener,  View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    PhotoViewAttacher photoViewAttacher;
    Bitmap myBitmap;
    ImageView map;
    Bitmap tempBitmap;
    Canvas tempCanvas;
    Dialog dialog;
    State selected_station;
    boolean special = false;
    public static MainActivity mself;
    private SearchableSpinner mSearchableSpinner;
    private SimpleArrayListAdapter mSimpleArrayListAdapter;
    private ArrayList<String> mStrings =  new ArrayList<>();
    int addintional_y = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mself = this;
        init_data();
        init_arrayAdapter();
        map = (ImageView) findViewById(R.id.imageView);
        map.setImageResource(R.drawable.map);
        mSearchableSpinner = (SearchableSpinner) findViewById(R.id.SearchableSpinner);
        mSimpleArrayListAdapter = new SimpleArrayListAdapter(this, mStrings);
        mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
        mSearchableSpinner.setOnItemSelectedListener(mOnItemSelectedListener);

        photoViewAttacher = new PhotoViewAttacher(map);
        photoViewAttacher.setMinimumScale(1.3f);
        photoViewAttacher.setMaximumScale(3.0f);
        photoViewAttacher.setScale(2.0f, 480.9f, 405.9f, true);

        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(photoViewAttacher, this);
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);

        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);
        tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmap);





    }


    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof State) {
//            Log.e("Time","milestone1");
            selected_station = ((State) item);
//            Toast.makeText(this, selected_station, Toast.LENGTH_SHORT).show();

//            Log.e("Time","milestone2");
            for (int i = 0; i < mStrings.size(); i ++){
                if (mStrings.get(i).equals(((State) item).getName())) {
                    mSearchableSpinner.setSelectedItem(i+1);
                    break;
                }

            }

            showPOPupDialog((State) item);

//            Log.e("Time","milestone3");
//

        }
    }
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void showPOPupDialog(final State  item) {
        try{dialog.dismiss();}
        catch (Exception E){
            Log.e("Error","No Dialog");
        }

        ValueAnimator animator = ValueAnimator.ofInt(0, 240);
        animator.setDuration(2000);
        animator.setRepeatCount(10);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value > 120)
                    value = 240-value;
                Float radius = Float.parseFloat(String.valueOf(value));
                tempCanvas.drawBitmap(myBitmap, 0, 0, null);
                final Paint paint;
                paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);
                tempCanvas.drawCircle(convertDpToPixel(((State) item).getCenter_X(),MainActivity.this), convertDpToPixel(((State) item).getCenter_Y() +2 +addintional_y,MainActivity.this), radius, paint);
                map.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
            }
        });
        animator.start();
//
////Attach the canvas to the ImageView



        photoViewAttacher.setScale(photoViewAttacher.getScale(),((State) item).getCenter_X(),((State) item).getCenter_Y() + addintional_y,false);
        Log.e("Time","milestone4");
        dialog = new Dialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.popup_dialog);
        ImageView btn_food = (ImageView)dialog.findViewById(R.id.btn_food);
        ImageView btn_tourist = (ImageView)dialog.findViewById(R.id.btn_tourist);
        ImageView btn_route = (ImageView)dialog.findViewById(R.id.btn_route);
        btn_food.setOnClickListener(this);
        btn_tourist.setOnClickListener(this);
        btn_route.setOnClickListener(this);
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        LinearLayout train_2 = (LinearLayout)dialog.findViewById(R.id.train_2);
        ImageView train_name_1 = (ImageView)dialog.findViewById(R.id.tr_name_1);
        ImageView train_name_2 = (ImageView)dialog.findViewById(R.id.tr_name_2);
        RelativeLayout train_1_2 = (RelativeLayout)dialog.findViewById(R.id.train_1_2);
        ImageView direction = (ImageView)dialog.findViewById(R.id.direction);
        ImageView time_1_1 = (ImageView)dialog.findViewById(R.id.time_1_1);
        ImageView time_1_1_red = (ImageView)dialog.findViewById(R.id.time_1_1_red);
        ImageView time_1_1_blue = (ImageView)dialog.findViewById(R.id.time_1_1_blue);
        ImageView time_1_1_yellow = (ImageView)dialog.findViewById(R.id.time_1_1_yellow);
        ImageView time_1_2 = (ImageView)dialog.findViewById(R.id.time_1_2);
        ImageView time_2_2 = (ImageView)dialog.findViewById(R.id.time_2_2);
        ImageView time_1_2_blue = (ImageView)dialog.findViewById(R.id.time_1_2_blue);
        ImageView time_2_1 = (ImageView)dialog.findViewById(R.id.time_2_1);
        TextView time_1_1_time = (TextView)dialog.findViewById(R.id.time_1_1_text);
        TextView time_1_1_label = (TextView)dialog.findViewById(R.id.label_1_1);
        TextView time_1_2_time = (TextView)dialog.findViewById(R.id.time_1_2_text);
        TextView time_1_2_label = (TextView)dialog.findViewById(R.id.label_1_2);
        TextView time_2_1_time = (TextView)dialog.findViewById(R.id.time_2_1_text);
        TextView time_2_2_time = (TextView)dialog.findViewById(R.id.time_2_2_text);
        TextView time_2_2_label = (TextView)dialog.findViewById(R.id.label_2_2);

        Log.e("today",getToday());
        String day_of_week = getToday().split(" ")[0];
        String current_time = getToday().split(" ")[3];
        Log.e(day_of_week,current_time);
        switch (item.getTrainName()){
            case "M1":
                train_1_2.setVisibility(View.INVISIBLE);
                train_name_1.setImageResource(R.drawable.m1);
                if((day_of_week.equals("Fri")||day_of_week.equals("Sat") )) {
                    if (Integer.parseInt(current_time.split(":")[0]) >= 1
                            && Integer.parseInt(current_time.split(":")[0]) <= 7) {
                        time_1_1_time.setText("15");
                    }
                }
                else{
                    if (Integer.parseInt(current_time.split(":")[0]) >= 0
                            && Integer.parseInt(current_time.split(":")[0]) <= 5){
                        time_1_1_time.setText("20");
                    }
                    else {

                        if ((Integer.parseInt(current_time.split(":")[0]) >= 7
                                && Integer.parseInt(current_time.split(":")[0]) <= 9)
                                || (Integer.parseInt(current_time.split(":")[0]) >= 14
                                && Integer.parseInt(current_time.split(":")[0]) <= 18)
                                && (!day_of_week.equals("Sun"))) {
                            time_1_1_time.setText("4");
                        } else {
                            time_1_1_time.setText("6");
                        }
                    }


                }
                break;
            case "M2":
                train_name_1.setImageResource(R.drawable.m2);
                train_1_2.setVisibility(View.INVISIBLE);
                time_1_1_yellow.setVisibility(View.VISIBLE);
                if((day_of_week.equals("Fri")||day_of_week.equals("Sat") )) {
                    if (Integer.parseInt(current_time.split(":")[0]) >= 1
                            && Integer.parseInt(current_time.split(":")[0]) <= 7) {
                        time_1_1_time.setText("15");
                    }
                }
                else{
                    if (Integer.parseInt(current_time.split(":")[0]) >= 0
                            && Integer.parseInt(current_time.split(":")[0]) <= 5){
                        time_1_1_time.setText("20");
                    }
                    else {

                        if ((Integer.parseInt(current_time.split(":")[0]) >= 7
                                && Integer.parseInt(current_time.split(":")[0]) <= 9)
                                || (Integer.parseInt(current_time.split(":")[0]) >= 14
                                && Integer.parseInt(current_time.split(":")[0]) <= 18)
                                && (!day_of_week.equals("Sun"))) {
                            time_1_1_time.setText("4");
                        } else {
                            time_1_1_time.setText("6");
                        }

                    }
                }
                break;
            case "M3":
                train_1_2.setVisibility(View.INVISIBLE);
                train_name_1.setImageResource(R.drawable.m3);
                time_1_1_red.setVisibility(View.VISIBLE);
                if((day_of_week.equals("Fri")||day_of_week.equals("Sat") )) {
                    if (Integer.parseInt(current_time.split(":")[0]) >= 1
                            && Integer.parseInt(current_time.split(":")[0]) <= 7) {
                        time_1_1_time.setText("12");
                    }
                }
                else{
                    if (Integer.parseInt(current_time.split(":")[0]) >= 0
                            && Integer.parseInt(current_time.split(":")[0]) <= 5){
                        time_1_1_time.setText("6");
                        direction.setImageResource(R.drawable.ic_onearrow);
                    }
                    else {

                        if ((Integer.parseInt(current_time.split(":")[0]) >= 7
                                && Integer.parseInt(current_time.split(":")[0]) <= 9)
                                || (Integer.parseInt(current_time.split(":")[0]) >= 14
                                && Integer.parseInt(current_time.split(":")[0]) <= 18)
                                && (!day_of_week.equals("Sun"))) {
                            time_1_1_time.setText("3");
                        } else {
                            time_1_1_time.setText("4-5");
                        }
                    }
                }

                break;
            case "M4":
                train_1_2.setVisibility(View.INVISIBLE);
                train_name_1.setImageResource(R.drawable.m4);
                time_1_1.setVisibility(View.GONE);
                time_1_1_blue.setVisibility(View.VISIBLE);
                time_1_1_time.setText("Open");
                time_1_1_label.setText("2020");
                break;
            case "M1,M2":

                if((day_of_week.equals("Fri")||day_of_week.equals("Sat") )) {
                    if (Integer.parseInt(current_time.split(":")[0]) >= 1
                            && Integer.parseInt(current_time.split(":")[0]) <= 7) {
                        time_1_1_time.setText("7-8");
                        time_1_2_time.setText("7-8");
                    }
                }
                else{
                    if (Integer.parseInt(current_time.split(":")[0]) >= 0
                            && Integer.parseInt(current_time.split(":")[0]) <= 5){
                        time_1_1_time.setText("20");
                        time_1_2_time.setText("20");
                    }
                    else {
                        if ((Integer.parseInt(current_time.split(":")[0]) >= 7
                                && Integer.parseInt(current_time.split(":")[0]) <= 9)
                                || (Integer.parseInt(current_time.split(":")[0]) >= 14
                                && Integer.parseInt(current_time.split(":")[0]) <= 18)
                                && (!day_of_week.equals("Sun"))) {
                            time_1_1_time.setText("2");
                            time_1_2_time.setText("2");
                        } else {
                            time_1_1_time.setText("3");
                            time_1_2_time.setText("3");
                        }
                    }
                }
                break;
            case "M3,M4":
                train_name_1.setImageResource(R.drawable.m3_m4);
                time_1_2.setVisibility(View.GONE);
                time_1_2_blue.setVisibility(View.VISIBLE);
                time_1_1.setVisibility(View.GONE);
                time_1_1_red.setVisibility(View.VISIBLE);
                if((day_of_week.equals("Fri")||day_of_week.equals("Sat") )) {
                    if (Integer.parseInt(current_time.split(":")[0]) >= 1
                            && Integer.parseInt(current_time.split(":")[0]) <= 7) {
                        time_1_1_time.setText("12");
                        time_1_2_time.setText("Open");
                        time_1_2_label.setText("2020");
                    }
                }
                else{
                    if (Integer.parseInt(current_time.split(":")[0]) >= 0
                            && Integer.parseInt(current_time.split(":")[0]) <= 5){
                        time_1_1_time.setText("6");
                        time_1_2_time.setText("Open");
                        direction.setImageResource(R.drawable.ic_onearrow);
                        time_1_2_label.setText("2020");
                    }
                    else {

                        if ((Integer.parseInt(current_time.split(":")[0]) >= 7
                                && Integer.parseInt(current_time.split(":")[0]) <= 9)
                                || (Integer.parseInt(current_time.split(":")[0]) >= 14
                                && Integer.parseInt(current_time.split(":")[0]) <= 18)
                                && (!day_of_week.equals("Sun"))) {
                            time_1_1_time.setText("3");
                            time_1_2_time.setText("Open");
                            time_1_2_label.setText("2020");
                        } else {
                            time_1_1_time.setText("4-5");
                            time_1_2_time.setText("Open");
                            time_1_2_label.setText("2020");
                        }
                    }
                }
                break;
            case "M1,M2,M3,M4":
                time_2_2.setVisibility(View.VISIBLE);
                special = true;
            case "M1,M2,M3":
                train_2.setVisibility(View.VISIBLE);
                train_name_1.setImageResource(R.drawable.m1_m2);
                if (special)
                    train_name_2.setImageResource(R.drawable.m3_m4);
                else
                    train_name_2.setImageResource(R.drawable.m3);
                time_1_2.setVisibility(View.VISIBLE);

                if((day_of_week.equals("Fri")||day_of_week.equals("Sat") )) {
                    if (Integer.parseInt(current_time.split(":")[0]) >= 1
                            && Integer.parseInt(current_time.split(":")[0]) <= 7) {
                        time_1_1_time.setText("7-8");
                        time_1_2_time.setText("7-8");
                        time_2_1_time.setText("12");

                    }
                }
                else{
                    if (Integer.parseInt(current_time.split(":")[0]) >= 0
                            && Integer.parseInt(current_time.split(":")[0]) <= 5){
                        time_1_1_time.setText("20");
                        time_1_2_time.setText("20");
                        time_2_1_time.setText("6");
                        direction.setImageResource(R.drawable.ic_onearrow);
                    }
                    else {

                        if ((Integer.parseInt(current_time.split(":")[0]) >= 7
                                && Integer.parseInt(current_time.split(":")[0]) <= 9)
                                || (Integer.parseInt(current_time.split(":")[0]) >= 14
                                && Integer.parseInt(current_time.split(":")[0]) <= 18)
                                && (!day_of_week.equals("Sun"))) {
                            time_1_1_time.setText("2");
                            time_1_2_time.setText("2");
                            time_2_1_time.setText("3");
                        } else {
                            time_1_1_time.setText("3");
                            time_1_2_time.setText("3");
                            time_2_1_time.setText("4-5");
                        }
                    }
                }

                break;




        }
        Log.e("Time","milestone5");
        dialog.show();
    }


    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();


        for (int i = 0; i < Global.array_state.size(); i ++){
            if(i == 12 || i == 18)
                clickableAreas.add(new ClickableArea(Global.array_state.get(i).getArea_X()-15, Global.array_state.get(i).getArea_Y()-15 + addintional_y, 70, 70, Global.array_state.get(i)));
            else
                clickableAreas.add(new ClickableArea(Global.array_state.get(i).getArea_X()-15, Global.array_state.get(i).getArea_Y()-15 + addintional_y, 50, 50, Global.array_state.get(i)));
        }
        return clickableAreas;
    }

    private void init_data() {
        Global.array_state.clear();
        Global.array_state.add(new State( "Aksel Møllers Have"    , 299,268, 299,268,  55.686376, 12.533187,    "M3"));
        Global.array_state.add(new State( "Amagerbro"             , 700,427, 700,427,   55.663542, 12.602702,  "M2"));
        Global.array_state.add(new State( "Amager Strand"         , 795,522, 795,522,   55.65612,  12.631895,  "M2"));
        Global.array_state.add(new State( "Bella Center"          , 664,600, 664,600,   55.638067, 12.582899,  "M1"));
        Global.array_state.add(new State( "Christianshavn"        , 663,403, 663,403,   55.672098, 12.591663,  "M1,M2"));
        Global.array_state.add(new State( "DR Byen"               , 664,506, 664,506,   55.655848, 12.589044,  "M1"));
        Global.array_state.add(new State( "Enghave Plads"         , 398,443, 398,443,   55.667265, 12.545814,  "M3"));
        Global.array_state.add(new State( "Enghave Brygge (G)"    , 307,565, 307,565,    55.654212, 12.557069,  "M4"));
        Global.array_state.add(new State( "Fasanvej"              , 244,300, 244,300,    55.681675, 12.5231,    "M1,M2"));
        Global.array_state.add(new State( "Flintholm"             , 149,207, 149,207,    55.685642, 12.499116,  "M1,M2"));
        Global.array_state.add(new State( "Femøren"               , 825,552, 825,552,   55.645223, 12.638361,  "M2"));
        Global.array_state.add(new State( "Forum"                 , 381,348, 381,348,   55.681825, 12.552412,  "M1,M2"));
        Global.array_state.add(new State( "Frederiksberg"         , 307,348, 307,348,   55.681216, 12.531711,  "M1,M2,M3"));
        Global.array_state.add(new State( "Frederiksberg Allé"    , 349,412, 349,412,    55.673697, 12.540408,  "M3"));
        Global.array_state.add(new State( "Gammel Strand"         , 577,417, 577,417,   55.677765, 12.57959,   "M3,M4"));
        Global.array_state.add(new State( "Havneholmen (G)"       , 339,532, 339,532,   55.661299, 12.558911,  "M4"));
        Global.array_state.add(new State( "Islands Brygge"        , 665,458, 665,458,   55.663423, 12.585136,  "M1"));
        Global.array_state.add(new State( "Kastrup"               , 856,585, 856,585,   55.635673, 12.647003,  "M2"));
        Global.array_state.add(new State( "Kongens Nytorv"        , 615,355, 615,355,   55.679434, 12.585232,  "M1,M2,M3,M4"));
        Global.array_state.add(new State( "København H"           , 459,468, 459,468,   55.671929, 12.564114,  "M3,M4"));
        Global.array_state.add(new State( "Københavns Lufthavn"   , 888,616, 888,616,    55.62957,  12.649375,  "M2"));
        Global.array_state.add(new State( "Lergravsparken"        , 732,459, 732,459,   55.662233, 12.616295,  "M2"));
        Global.array_state.add(new State( "Lindevang"             , 196,252, 196,252,   55.683482, 12.51312,   "M1,M2"));
        Global.array_state.add(new State( "Marmorkirken"          , 630,292, 630,292,   55.685242, 12.588634,  "M3,M4"));
        Global.array_state.add(new State( "Mozarts Plads (G)"     , 242,628, 242,628,   55.648892, 12.534109,  "M4"));
        Global.array_state.add(new State( "Nordhavn"              , 728,79,  728,79,     55.705255, 12.590928,  "M4"));
        Global.array_state.add(new State( "Nuuks Plads"           , 315,216, 315,216,     55.688773, 12.542854,  "M3"));
        Global.array_state.add(new State( "Ny Ellebjerg (G)"      , 211,661, 211,661,     55.652933, 12.516034,  "M4"));
        Global.array_state.add(new State( "Nørreport"             , 540,350, 540,350,     55.683685, 12.571571,  "M1,M2"));
        Global.array_state.add(new State( "Nørrebros Rundddel"    , 347,175, 347,175,     55.694034, 12.548916,  "M3"));
        Global.array_state.add(new State( "Nørrebro"              , 392,143, 392,143,     55.70071,  12.537855,  "M3"));
        Global.array_state.add(new State( "Orientkaj"             , 760,48,  760,48,         55.711799, 12.595137,  "M4"));
        Global.array_state.add(new State( "Poul Henningsens Plads", 544,154, 544,154,    55.709263, 12.57665,   "M3"));
        Global.array_state.add(new State( "Rådhuspladsen"         , 524,450, 524,450,    55.676464, 12.568818,  "M3,M4"));
        Global.array_state.add(new State( "Skjold Plads"          , 442,131, 442,131,    55.703278, 55.703278,  "M3"));
        Global.array_state.add(new State( "Sluseholmen (G)"       , 276,594, 276,594,    55.645548, 12.544731 , "M4"));
        Global.array_state.add(new State( "Sundby"                , 664,552, 664,552,    55.645212, 12.585742,  "M1"));
        Global.array_state.add(new State( "Trianglen"             , 584,191, 584,191,    55.699252, 12.576081,  "M3"));
        Global.array_state.add(new State( "Vanløse"               , 102,158, 102,158,    55.687348, 12.491533,  "M1,M2"));
        Global.array_state.add(new State( "Vestamager"            , 665,694, 665,694,    55.619366, 12.575491,  "M1"));
        Global.array_state.add(new State( "Vibenshus Runddel"     , 497,134, 497,134,    55.70638,  12.563979,  "M3"));
        Global.array_state.add(new State( "Ørestad"               , 665,648, 665,648,    55.628978, 12.579393,  "M1"));
        Global.array_state.add(new State( "Øresund"               , 763,490, 763,490,    55.661347, 12.628824,  "M2"));
        Global.array_state.add(new State( "Østerport"             , 620,234, 620,234,    55.6932,   12.585403,  "M3,M4"));

        Global.array_info.add(new info("tourist","Bella Center Copenhagen",                                         "bella_center_copenhagen.jpg",                                   "0,5",            "Bella Center is a modern convention center with capacity for large meetings, conferences and congresses.",                   "Bella Center",                        55.637617,          12.578516));
        Global.array_info.add(new info("tourist", "Christiania Copenhagen",                                          "christiania_copenhagen.jpg",                                   "0,5",            "This is a really interesting place, both for its social and political history, and seeing it in person.",                    "Christianshavn",                      55.673559,          12.596817));
        Global.array_info.add(new info("tourist", "Vor Frelsers Kirke",                                              "vor_frelsers_kirke.jpg",                                       "0,2",            "The winding stairs on the outside of the tower is quite a unique architectural point and offer complete, uninterrupted 360 degree view.",                    "Christianshavn",                      55.672974,          12.594202));
        Global.array_info.add(new info("tourist", "Copenhagen Opera House",                                          "copenhagen_opera_house.jpg",                                   "0,2",            "",                   "Christianshavn",                      55.681957,          12.600757));
        Global.array_info.add(new info("tourist", "Danish Radio Concert Hall ",                                      "danish_radio_concert_hall.jpg",                               "0,2",            "The Danish Radio Concert Hall is a splendid venue for concerts due to its excellent acoustics.",                     "DR Byen",                         55.657975,          12.588876));
        Global.array_info.add(new info("tourist", "Christiansborg Slot",                                             "Christiansborg Slot.jpg",                                      "0,2",            "This palace is a must see in Copenhagen.",                   "Gammel Strand",                       55.676197,          12.580553));
        Global.array_info.add(new info("tourist", "Tivoli Gardens",                                                  "tivoli_gardens.jpg",                                           "0,3",            "Tivoli garden offers you a variety of different experiences.",                   "København H (Metro)",                         55.6736841,             12.5681471));
        Global.array_info.add(new info("tourist", "Copenhagen Main Station",                                         "copenhagen_main_station.jpg",                                  "0,0",            "The largest station in Copenhagen soon to have the new metro line as well.",                     "København H (Metro)",                         55.672731,          12.564825));
        Global.array_info.add(new info("tourist", "the_blue Planet - National Aquarium of Denmark",                  "the_blue_planet_national_aquarium_of_denmark.jpg",           "0,6",            "The Aquarium is 10 minutes away from Kastrup metro station.",                    "Kastrup",                         55.6381446,             12.6561446));
        Global.array_info.add(new info("tourist", "Copenhagen Airport",                                              "Copenhagen Airport.jpg",                                       "0,0",            "Copenhagen Airport",                     "Københavns Lufthavn",                         55.628818,          12.64417));
        Global.array_info.add(new info("tourist", "Nyhavn",                                                          "nyhavn.jpg",                                                   "0,5",            "Nyhavn was on our must-see list for our trip to Copenhagen",                     "Kongens Nytorv",                      55.679761,          12.591282));
        Global.array_info.add(new info("tourist", "Strøget",                                                         "Strøget.jpg",                                                  "0,5",            "Strøget is the place to shop. Since at least the early 1700s, many of the city’s most famous and expensive stores have been located along this 1.1 km strip",                    "Kongens Nytorv",                      55.679123,          12.574884));
        Global.array_info.add(new info("tourist", "Copenhagen Canal Tour",                                           "Copenhagen Canal Tour.jpg",                                    "0,5",            "A canal tour is a great way to get an overview of the city",                     "Kongens Nytorv",                      55.677568,          12.580477));
        Global.array_info.add(new info("tourist", "Amalienborg",                                                     "amalienborg.jpg",                                              "3,5",            "",                   "Marmorkirken",                        55.683968,          12.593255));
        Global.array_info.add(new info("tourist", "Frederiks Kirke (Marmorkirken)",                                  "frederiks_kirke_marmorkirken.jpg",                           "0,05",           "Evangelistic Lutheran Church",                   "Marmorkirken",                        55.684974,          12.589592));
        Global.array_info.add(new info("tourist", "Rosenborg Castle",                                                "Rosenborg Castle.jpg",                                         "0,5",            "This is a very interesting castle as it is still original, compared to Christiansborg, which burned a few times.",                   "Nørreport",                       55.68581,           12.57737));
        Global.array_info.add(new info("tourist", "Rundetarn",                                                       "Rundetarn.jpg",                                                "0,3",            "An excellent place to get a bird's eye view of the city.",                   "Nørreport",                       55.681353,          12.575816));
        Global.array_info.add(new info("tourist", "Torvehallerne",                                                   "Torvehallerne.jpg",                                            "0,1",            "The market serves the locals by providing them with all kinds of food.",                     "Nørreport",                       55.683702,          12.569751));
        Global.array_info.add(new info("tourist", "Ny Carlsberg Glyptotek",                                          "Ny Carlsberg Glyptotek.jpg",                                   "0,5",            "Located close to Central ststion and opposite the Tivoli Garden.",                   "Rådhuspladsen",                       55.672914,          12.572398));
        Global.array_info.add(new info("tourist", "Strøget",                                                         "Strøget.jpg",                                                  "0,05",           "Strøget is the place to shop. Since at least the early 1700s, many of the city’s most famous and expensive stores have been located along this 1.1 km strip",                    "Rådhuspladsen",                       55.679123,          12.574884));
        Global.array_info.add(new info("tourist", "The National Museum of Denmark",                                  "The National Museum of Denmark.jpg",                           "1,0",            "The museum was once the Prince's residence and it is now a fantastic museum.",                   "Rådhuspladsen",                       55.674636,          12.574729));
        Global.array_info.add(new info("tourist", "The Little Mermaid",                                              "The Little Mermaid.jpg",                                       "0,8",            "There is about a 15-20 minute walk from Nyhavn to the Little Mermaid.",                  "Østerport",                       55.69286,           12.5992828));
        Global.array_info.add(new info("tourist", "Kastellet",                                                       "Kastellet.jpg",                                                "1,2",            "It is really attractive visiting this place, imagining the situation in past as part of Copenhagen city defence system.",                    "Østerport",                       55.69286,           12.5992828));
        Global.array_info.add(new info("tourist", "Gefionspringvandet",                                              "gefionspringvandet.jpg",                                       "1,2",            "One of the more spectacular sights while visiting Copenhagen is the statue of the Goddess Gefion.",                  "Østerport",                       55.69286,           12.5992828));
        Global.array_info.add(new info("tourist", "8House",                                                          "8House.jpg",                                                   "1,3",            "By-appointment tours are available of this innovative mixed-use building shaped like a figure 8.",                   "Ørestad",                         55.6175636,             12.5715528));
        Global.array_info.add(new info("tourist", "AirBnBird",                                                       "AirBnBird.jpg",                                                "0,3",            "",                   "Nørrebro",                        55.7017624,             12.5426654));
        Global.array_info.add(new info("tourist", "AirBnBird",                                                       "AirBnBird.jpg",                                                "0,3",            "",                   "Skjolds Plads",                       55.7017624,             12.5426654));
        Global.array_info.add(new info("tourist", "Amager Fælled",                                                   "Amager Fælled.jpg",                                            "0,9",            "",                   "Sundby",                      55.6522808,             12.5773943));
        Global.array_info.add(new info("tourist", "Amager Fælled",                                                   "Amager Fælled.jpg",                                            "1,2",            "",                   "Enghave Brygge",                      55.6522808,             12.5773943));
        Global.array_info.add(new info("tourist", "Amager Strandpark",                                               "Amager Strandpark.jpg",                                        "1,2",            "Over 4km of sandy man-made beach on an island with a lagoon, land & water sports, kiosks & cafes.",                  "Femøren",                         55.6545325,             12.6496067));
        Global.array_info.add(new info("tourist", "Amager Strandpark",                                               "Amager Strandpark.jpg",                                        "1,5",            "Over 4km of sandy man-made beach on an island with a lagoon, land & water sports, kiosks & cafes.",                  "Øresund",                         55.6545325,             12.6496067));
        Global.array_info.add(new info("tourist", "Amagertorv",                                                      "Amagertorv.jpg",                                               "0,1",            "",                   "Gammel Strand",                       55.67884,           12.5796779));
        Global.array_info.add(new info("tourist", "Amagertorv",                                                      "Amagertorv.jpg",                                               "0,3",            "",                   "Kongens Nytorv",                      55.67884,           12.5796779));
        Global.array_info.add(new info("tourist", "Amalienborg Palace Museum",                                       "Amalienborg Palace Museum.jpg",                                "0,2",            "Royal-history museum housed in the ornate palace of Christian VIII, with a changing of the guard.",                  "Marmorkirken",                        55.6844016,             12.5923705));
        Global.array_info.add(new info("tourist", "Amalienborg Palace Museum",                                       "Amalienborg Palace Museum.jpg",                                "0,7",            "Royal-history museum housed in the ornate palace of Christian VIII, with a changing of the guard.",                  "Kongens Nytorv",                      55.6844016,             12.5923705));
        Global.array_info.add(new info("tourist", "Ansgar Church",                                                   "Ansgar Church.jpg",                                            "0,7",            "",                   "Nørrebro",                        55.7003614,             12.525341));
        Global.array_info.add(new info("tourist", "Apistemplet",                                                     "Apistemplet.jpg",                                              "1,3",            "",                   "Lindevang",                       55.6729317,             12.5234777));
        Global.array_info.add(new info("tourist", "Apistemplet",                                                     "Apistemplet.jpg",                                              "2,0",            "",                   "Flintholm",                       55.6729317,             12.5234777));
        Global.array_info.add(new info("tourist", "Assistens Cemetery",                                              "Assistens Cemetery.jpg",                                       "0,3",            "Graveyard & park established in 1760, burial place of Soren Kierkegaard & Hans Christian Anderson.",                     "Nørrebros Runddel",                       55.6910102,             12.5501532));
        Global.array_info.add(new info("tourist", "Bananna Park",                                                    "Bananna Park.jpg",                                             "0,4",            "",                   "Skjolds Plads",                       55.6997152,             12.5494521));
        Global.array_info.add(new info("tourist", "Bananna Park",                                                    "Bananna Park.jpg",                                             "0,6",            "",                   "Nørrebros Runddel",                       55.6997152,             12.5494521));
        Global.array_info.add(new info("tourist", "Barbie Museum",                                                   "Barbie Museum.jpg",                                            "1,4",            "",                   "Nuuks Plads",                         55.6973158,             12.5257058));
        Global.array_info.add(new info("tourist", "Barbie Museum",                                                   "Barbie Museum.jpg",                                            "2,1",            "",                   "Flintholm",                       55.6973158,             12.5257058));
        Global.array_info.add(new info("tourist", "Bishop Absalon",                                                  "Bishop Absalon.jpg",                                           "0,03",           "Built in 1902, this equestrian statue on Højbro Plads commemorates city founder Bishop Absalon.",                    "Gammel Strand",                       55.677975,          12.5800008));
        Global.array_info.add(new info("tourist", "Bishop Absalon",                                                  "Bishop Absalon.jpg",                                           "0,3",            "Built in 1902, this equestrian statue on Højbro Plads commemorates city founder Bishop Absalon.",                    "Kongens Nytorv",                      55.677975,          12.5800008));
        Global.array_info.add(new info("tourist", "BLOX",                                                            "BLOX.jpg",                                                     "1,0",            "",                   "Islands Brygge",                      55.67213,           12.5788387));
        Global.array_info.add(new info("tourist", "Børsen",                                                          "Børsen.jpg",                                                   "0,3",            "Imposing, 17th-century, waterfront building & former stock exchange with a striking spire.",                     "Gammel Strand",                       55.6755013,             12.5840128));
        Global.array_info.add(new info("tourist", "Børsen",                                                          "Børsen.jpg",                                                   "0,4",            "Imposing, 17th-century, waterfront building & former stock exchange with a striking spire.",                     "Kongens Nytorv",                      55.6755013,             12.5840128));
        Global.array_info.add(new info("tourist", "Botanical Museum",                                                "Botanical Museum.jpg",                                         "0,5",            "",                   "Nørreport",                       55.688475,          12.5743568));
        Global.array_info.add(new info("tourist", "Botanical Museum",                                                "Botanical Museum.jpg",                                         "0,8",            "",                   "Østerport",                       55.688475,          12.5743568));
        Global.array_info.add(new info("tourist", "Brønshøj Water Tower",                                            "Brønshøj Water Tower.jpg",                                     "1,5",            "",                   "Vanløse",                         55.7003294,             12.4984385));
        Global.array_info.add(new info("tourist", "Bygningskulturens Hus",                                           "Bygningskulturens Hus.jpg",                                    "0,6",            "",                   "Østerport",                       55.6880094,             12.5885041));
        Global.array_info.add(new info("tourist", "Canal Nybrogade",                                                 "Canal Nybrogade.jpg",                                          "0,1",            "",                   "Gammel Strand",                       55.6767325,             12.5772742));
        Global.array_info.add(new info("tourist", "Canal Nybrogade",                                                 "Canal Nybrogade.jpg",                                          "0,5",            "",                   "Rådhuspladsen",                       55.6767325,             12.5772742));
        Global.array_info.add(new info("tourist", "Charlottenborg Palace",                                           "Charlottenborg Palace.jpg",                                    "0,1",            "1672 palace, now a contemporary art museum, with a program of exhibitions, performances & concerts.",                    "Kongens Nytorv",                      55.6798255,             12.5883008));
        Global.array_info.add(new info("tourist", "Charlottenborg Palace",                                           "Charlottenborg Palace.jpg",                                    "0,5",            "1672 palace, now a contemporary art museum, with a program of exhibitions, performances & concerts.",                    "Gammel Strand",                       55.6798255,             12.5883008));
        Global.array_info.add(new info("tourist", "Charlottenlund Palace",                                           "Charlottenlund Palace.jpg",                                    "8,9",            "This former royal residence built in 1733 hosts guided tours, plus concerts & corporate events.",                    "Vanløse",                         55.7504274,             12.5807564));
        Global.array_info.add(new info("tourist", "Christian IV\'s Brewhouse",                                       "Christian IV\'s Brewhouse.jpg",                                "1,1",            "Royal fortification & later brewhouse constructed in 1608, now home to a sculpture collection.",                     "Islands Brygge",                      55.6732212,             12.5793956));
        Global.array_info.add(new info("tourist", "Christian\'s Church",                                             "Christian\'s Church.jpg",                                      "0,2",            "",                   "Christianshavn",                      55.6723736,             12.5872136));
        Global.array_info.add(new info("tourist", "Christian\'s Church",                                             "Christian\'s Church.jpg",                                      "1,0",            "",                   "Islands Brygge",                      55.6723736,             12.5872136));
        Global.array_info.add(new info("tourist", "Christiansborg Palace",                                           "Christiansborg Palace.jpg",                                    "0,18",           "Tours of the royal palace\'s lavish reception rooms & stables, plus 12th-century castle remains.",                   "Gammel Strand",                       55.6762316,             12.5805143));
        Global.array_info.add(new info("tourist", "Christiansborg Palace",                                           "Christiansborg Palace.jpg",                                    "0,4",            "Tours of the royal palace\'s lavish reception rooms & stables, plus 12th-century castle remains.",                   "Kongens Nytorv",                      55.6762316,             12.5805143));
        Global.array_info.add(new info("tourist", "Christianshavn Torv",                                             "Christianshavn Torv.jpg",                                      "0,0",            "",                   "Christianshavn",                      55.6721377,             12.5913986));
        Global.array_info.add(new info("tourist", "Christianshavn Torv",                                             "Christianshavn Torv.jpg",                                      "0,8",            "",                   "Kongens Nytorv",                      55.6721377,             12.5913986));
        Global.array_info.add(new info("tourist", "Christianshavns Kanal",                                           "Christianshavns Kanal.jpg",                                    "0,07",           "",                   "Christianshavn",                      55.6723348,             12.5906069));
        Global.array_info.add(new info("tourist", "Christianshavns Kanal",                                           "Christianshavns Kanal.jpg",                                    "0,8",            "",                   "Kongens Nytorv",                      55.6723348,             12.5906069));
        Global.array_info.add(new info("tourist", "Church of Christ",                                                "Church of Christ.jpg",                                         "0,7",            "",                   "Frederiksberg Allé",                      55.667591,          12.5450214));
        Global.array_info.add(new info("tourist", "Church of Christ",                                                "Church of Christ.jpg",                                         "1,1",            "",                   "Havneholmen (G)",                         55.667591,          12.5450214));
        Global.array_info.add(new info("tourist", "Church of Holmen",                                                "Church of Holmen.jpg",                                         "0,2",            "17th-century church with a carved oak altarpiece & pulpit & a barrel-vaulted ceiling.",                  "Gammel Strand",                       55.6766342,             12.5836015));
        Global.array_info.add(new info("tourist", "Church of Holmen",                                                "Church of Holmen.jpg",                                         "0,3",            "17th-century church with a carved oak altarpiece & pulpit & a barrel-vaulted ceiling.",                  "Kongens Nytorv",                      55.6766342,             12.5836015));

        Global.array_info.add(new info("tourist", "Church of Our Saviour",                                           "Church of Our Saviour.jpg",                                                   "0,1",            "Baroque, 17th-century place of worship with a carillon & steps around the outside of the spire.",                    "Christianshavn",                      55.6729387,             12.5942102));
        Global.array_info.add(new info("tourist",  "Church of Our Saviour",                                           "Church of Our Saviour.jpg",                                                   "0,9",            "Baroque, 17th-century place of worship with a carillon & steps around the outside of the spire.",                    "Kongens Nytorv",                      55.6729387,             12.5942102));
        Global.array_info.add(new info("tourist",  "Church of the Holy Spirit",                                       "Church of the Holy Spirit.jpg",                                                   "0,5",            "Historic church with roots in the 13th century hosting services, concerts & other cultural events.",                     "Rådhuspladsen",                       55.6789995,             12.5767227));
        Global.array_info.add(new info("tourist",  "Church of the Holy Spirit",                                       "Church of the Holy Spirit.jpg",                                                   "0,6",            "Historic church with roots in the 13th century hosting services, concerts & other cultural events.",                     "Nørreport",                       55.6789995,             12.5767227));
        Global.array_info.add(new info("tourist",  "Cisterns in Søndermarken",                                        "Cisterns in Søndermarken.jpg",                                                    "1,0",            "Underground reservoir & dripstone cave transformed into a space for art exhibitions and events.",                    "Frederiksberg Allé",                      55.6694951,             12.5248985));
        Global.array_info.add(new info("tourist",  "Cisterns in Søndermarken",                                        "Cisterns in Søndermarken.jpg",                                                    "1,3",            "Underground reservoir & dripstone cave transformed into a space for art exhibitions and events.",                    "Enghave Place",                       55.6694951,             12.5248985));
        Global.array_info.add(new info("tourist",  "City Hall Square",                                                "city_hall_square.jpg",                                                    "0,0",            "Large open space outside city hall used for concerts, cultural performances & celebrations.",                    "Rådhuspladsen",                       55.6759142,             12.5691285));
        Global.array_info.add(new info("tourist",  "City Hall Square",                                                "city_hall_square.jpg",                                                    "0,5",            "Large open space outside city hall used for concerts, cultural performances & celebrations.",                    "København H (Metro)",                         55.6759142,             12.5691285));
        Global.array_info.add(new info("tourist",  "Classens Have",                                                   "Classens Have.jpg",                                                   "0,5",            "",                   "Østerport",                       55.6978177,             12.5889375));
        Global.array_info.add(new info("tourist",  "Classens Have",                                                   "Classens Have.jpg",                                                   "0,8",            "",                   "Trianglen",                       55.6978177,             12.5889375));
        Global.array_info.add(new info("tourist",  "COPENHAGEN CABLEPARK",                                            "COPENHAGEN CABLEPARK.jpg",                                                    "2,2",            "",                   "Øresund",                         55.680807,          12.620662));
        Global.array_info.add(new info("tourist",  "Copenhagen Cathedral",                                            "Copenhagen Cathedral.jpg",                                                    "0,4",            "CF Hansen\'s grand neoclassical cathedral, home to Thorvaldsen\'s statues of Christ & the apostles.",                    "Nørreport",                       55.6793386,             12.5723587));
        Global.array_info.add(new info("tourist",  "Copenhagen Cathedral",                                            "Copenhagen Cathedral.jpg",                                                    "0,9",            "CF Hansen\'s grand neoclassical cathedral, home to Thorvaldsen\'s statues of Christ & the apostles.",                    "København H (Metro)",                         55.6793386,             12.5723587));
        Global.array_info.add(new info("tourist",  "Copenhagen City Hall",                                            "Copenhagen City Hall.jpg",                                                    "0,5",            "Guided tours of this 1905 building with richly decorated facade, housing Jens Olsen\'s World Clock.",                    "København H (Metro)",                         55.6752833,             12.5701632));
        Global.array_info.add(new info("tourist",  "Copenhagen City Hall",                                            "Copenhagen City Hall.jpg",                                                    "0,6",            "Guided tours of this 1905 building with richly decorated facade, housing Jens Olsen\'s World Clock.",                    "Gammel Strand",                       55.6752833,             12.5701632));
        Global.array_info.add(new info("tourist",  "Copenhagen Denmark Temple",                                       "Copenhagen Denmark Temple.jpg",                                                   "0,7",            "",                   "Nuuks Plads",                         55.6927382,             12.5339466));
        Global.array_info.add(new info("tourist",  "Copenhagen Denmark Temple",                                       "Copenhagen Denmark Temple.jpg",                                                   "0,9",            "",                   "Nørrebro",                        55.6927382,             12.5339466));
        Global.array_info.add(new info("tourist",  "Copenhagen Zoo",                                                  "Copenhagen Zoo.jpg",                                                      "1,0",            "Established zoo with airy Norman Foster-designed elephant house & Arctic habitat for polar bears.",                  "Fasanvej",                        55.6724093,             12.5213581));
        Global.array_info.add(new info("tourist",  "Copenhagen Zoo",                                                  "Copenhagen Zoo.jpg",                                                      "1,2",            "Established zoo with airy Norman Foster-designed elephant house & Arctic habitat for polar bears.",                  "Frederiksberg Allé",                      55.6724093,             12.5213581));
        Global.array_info.add(new info("tourist",  "Danish Police Museum",                                            "Danish Police Museum.jpg",                                                    "0,8",            "",                   "Nørrebros Runddel",                       55.6901224,             12.5608252));
        Global.array_info.add(new info("tourist",  "Danish Police Museum",                                            "Danish Police Museum.jpg",                                                    "1,1",            "",                   "Nuuks Plads",                         55.6901224,             12.5608252));
        Global.array_info.add(new info("tourist",  "Danish War Museum",                                               "Danish War Museum.jpg",                                                   "0,4",            "Military history museum in Christian IV\'s arsenal from early 1600s, with large weaponry collection.",                   "Gammel Strand",                       55.674151,          12.5803648));
        Global.array_info.add(new info("tourist",  "Danish War Museum",                                               "Danish War Museum.jpg",                                                   "0,6",            "Military history museum in Christian IV\'s arsenal from early 1600s, with large weaponry collection.",                   "Kongens Nytorv",                      55.674151,          12.5803648));
        Global.array_info.add(new info("tourist",  "Den Frie Udstilling",                                             "Den Frie Udstilling.jpg",                                                     "0,2",            "Museum in restored, 19th-century, wooden pavilion, hosting contemporary artist group shows.",                    "Østerport",                       55.691558,          12.5874315));
        Global.array_info.add(new info("tourist",  "Den Frie Udstilling",                                             "Den Frie Udstilling.jpg",                                                     "1,5",            "Museum in restored, 19th-century, wooden pavilion, hosting contemporary artist group shows.",                    "Nordhavn",                        55.691558,          12.5874315));
        Global.array_info.add(new info("tourist",  "Den Kongelige Afstøbningssamling",                                "Den Kongelige Afstøbningssamling.jpg",                                                    "0.561857327",            "A collection of thousands of plaster casts of European classical sculptures dating from 600 B.C.",                   "Marmorkirken",                        55.6860807,             12.5974734));
        Global.array_info.add(new info("tourist",  "Den snoede skorsten",                                             "Den snoede skorsten.jpg",                                                     "0,7",            "",                   "Enghave Place",                       55.6663248,             12.534069));
        Global.array_info.add(new info("tourist",  "Den snoede skorsten",                                             "Den snoede skorsten.jpg",                                                     "0,9",            "",                   "Frederiksberg Allé",                      55.6663248,             12.534069));
        Global.array_info.add(new info("tourist",  "Det gamle vandtårn",                                              "Det gamle vandtårn.jpg",                                                      "6,0",            "",                   "Vanløse",                         55.6662179,             12.4031559));
        Global.array_info.add(new info("tourist",  "Det Kinesiske Lysthus",                                           "Det Kinesiske Lysthus.jpg",                                                   "0,6",            "",                   "Frederiksberg",                       55.6755335,             12.5275459));
        Global.array_info.add(new info("tourist",  "Det Kinesiske Lysthus",                                           "Det Kinesiske Lysthus.jpg",                                                   "1,2",            "",                   "Lindevang",                       55.6755335,             12.5275459));
        Global.array_info.add(new info("tourist",  "Det kongelige Biblioteks Have",                                   "Det kongelige Biblioteks Have.jpg",                                                   "0,3",            "Public garden laid out in 1920 with a shallow pool at its center & pathways, benches & hedges.",                     "Gammel Strand",                       55.6746348,             12.5812279));
        Global.array_info.add(new info("tourist",  "Det kongelige Biblioteks Have",                                   "Det kongelige Biblioteks Have.jpg",                                                   "0,5",            "Public garden laid out in 1920 with a shallow pool at its center & pathways, benches & hedges.",                     "Kongens Nytorv",                      55.6746348,             12.5812279));
        Global.array_info.add(new info("tourist",  "DieselHouse",                                                     "DieselHouse.jpg",                                                     "0,6",            "",                   "Havneholmen (G)",                         55.6562736,             12.5551694));
        Global.array_info.add(new info("tourist",  "DieselHouse",                                                     "DieselHouse.jpg",                                                     "1,3",            "",                   "Sluseholmen",                         55.6562736,             12.5551694));
        Global.array_info.add(new info("tourist",  "Eberts Villaby",                                                  "Eberts Villaby.jpg",                                                      "0,9",            "",                   "Amagerbro",                       55.6552446,             12.6063509));
        Global.array_info.add(new info("tourist",  "Eberts Villaby",                                                  "Eberts Villaby.jpg",                                                      "0,9",            "",                   "Lergravsparken",                      55.6552446,             12.6063509));
        Global.array_info.add(new info("tourist",  "Enigma",                                                          "Enigma.jpg",                                                      "0,08",           "",                   "Trianglen",                       55.6996673,             12.5750192));
        Global.array_info.add(new info("tourist",  "Enigma",                                                          "Enigma.jpg",                                                      "1,0",            "",                   "Vibenshus Runddel",                       55.6996673,             12.5750192));
        Global.array_info.add(new info("tourist",  "Experimentarium",                                                 "Experimentarium.jpg",                                                     "1,9",            "Modern, hands-on science & technology museum with learning games for kids, plus harborside ice-rink.",                   "Orientkaj",                       55.7267113,             12.5800939));
        Global.array_info.add(new info("tourist",  "Fountain of Amalie Gardens",                                      "Fountain of Amalie Gardens.jpg",                                                      "0,4",            "",                   "Marmorkirken",                        55.6834986,             12.5950849));
        Global.array_info.add(new info("tourist",  "Fountain of Amalie Gardens",                                      "Fountain of Amalie Gardens.jpg",                                                      "0,7",            "",                   "Kongens Nytorv",                      55.6834986,             12.5950849));
        Global.array_info.add(new info("tourist",  "Frederik\'s Bastion",                                             "Frederik\'s Bastion.jpg",                                                     "1,4",            "",                   "Marmorkirken",                        55.6815094,             12.6106387));
        Global.array_info.add(new info("tourist",  "Frederik\'s Bastion",                                             "Frederik\'s Bastion.jpg",                                                     "2,1",            "",                   "Lergravsparken",                      55.6815094,             12.6106387));
        Global.array_info.add(new info("tourist",  "Frederiksberg Gardens",                                           "Frederiksberg Gardens.jpg",                                                   "0,7",            "18th-century, English-style landscaped gardens, with follies, lakes, woodland & lawns.",                     "Fasanvej",                        55.6747467,             12.5254732));
        Global.array_info.add(new info("tourist",  "Frederiksberg Gardens",                                           "Frederiksberg Gardens.jpg",                                                   "0,8",            "18th-century, English-style landscaped gardens, with follies, lakes, woodland & lawns.",                     "Frederiksberg",                       55.6747467,             12.5254732));
        Global.array_info.add(new info("tourist",  "Frederiksberg Palace",                                            "Frederiksberg Palace.jpg",                                                    "0,9",            "Early 18th-century palace in an Itlaian Baroque style with guided tours of former royal apartments.",                    "Frederiksberg Allé",                      55.6721097,             12.5253483));
        Global.array_info.add(new info("tourist",  "Frederiksberg Palace",                                            "Frederiksberg Palace.jpg",                                                    "1,0",            "Early 18th-century palace in an Itlaian Baroque style with guided tours of former royal apartments.",                    "Fasanvej",                        55.6721097,             12.5253483));
        Global.array_info.add(new info("tourist",  "Frederiksholms Kanal",                                            "Frederiksholms Kanal.jpg",                                                    "0,4",            "",                   "Gammel Strand",                       55.6736346,             12.578331));
        Global.array_info.add(new info("tourist",  "Frederiksholms Kanal",                                            "Frederiksholms Kanal.jpg",                                                    "0,6",            "",                   "Rådhuspladsen",                       55.6736346,             12.578331));
        Global.array_info.add(new info("tourist",  "Garnisonskirken",                                                 "Garnisonskirken.jpg",                                                     "0,3",            "",                   "Marmorkirken",                        55.6818515,             12.5892106));
        Global.array_info.add(new info("tourist",  "Geological Museum",                                               "Geological Museum.jpg",                                                   "0,5",            "",                   "Nørreport",                       55.6876505,             12.5769626));
        Global.array_info.add(new info("tourist",  "Geological Museum",                                               "Geological Museum.jpg",                                                   "0,7",            "",                   "Marmorkirken",                        55.6876505,             12.5769626));
        Global.array_info.add(new info("tourist",  "Ghosttours, kbh",                                                 "Ghosttours, kbh.jpg",                                                     "1,1",            "",                   "Poul Henningsens Plads",                      55.7192175,             12.5774269));
        Global.array_info.add(new info("tourist",  "Ghosttours, kbh",                                                 "Ghosttours, kbh.jpg",                                                     "1,3",            "",                   "Orientkaj",                       55.7192175,             12.5774269));
        Global.array_info.add(new info("tourist",  "Grøndal Kirke",                                                   "Grøndal Kirke.jpg",                                                   "1,3",            "",                   "Flintholm",                       55.6954494,             12.5118902));
        Global.array_info.add(new info("tourist",  "Grøndal Kirke",                                                   "Grøndal Kirke.jpg",                                                   "1,5",            "",                   "Vanløse",                         55.6954494,             12.5118902));
        Global.array_info.add(new info("tourist",  "Grønjordssøen",                                                   "Grønjordssøen.jpg",                                                   "0,6",            "",                   "Sundby",                      55.6514385,             12.5844064));
        Global.array_info.add(new info("tourist",  "Guinness World Records Museum",                                   "Guinness World Records Museum.jpg",                                                   "0,1",            "Family attraction with hands-on exhibits about world records in sports, science & architecture.",                    "Kongens Nytorv",                      55.6802207,             12.5838497));
        Global.array_info.add(new info("tourist",  "Guinness World Records Museum",                                   "Guinness World Records Museum.jpg",                                                   "0,3",            "Family attraction with hands-on exhibits about world records in sports, science & architecture.",                    "Gammel Strand",                       55.6802207,             12.5838497));
        Global.array_info.add(new info("tourist",  "Gustaf Church, Copenhagen",                                       "Gustaf Church, Copenhagen.jpg",                                                   "0,2",            "",                   "Østerport",                       55.6923439,             12.5898118));
        Global.array_info.add(new info("tourist",  "Gustaf Church, Copenhagen",                                       "Gustaf Church, Copenhagen.jpg",                                                   "1,4",            "",                   "Nordhavn",                        55.6923439,             12.5898118));
        Global.array_info.add(new info("tourist",  "H.C. Andersen Eventyrhuset",                                      "H.C. Andersen Eventyrhuset.jpg",                                                      "0,07",           "Family-friendly exhibition about Hans Christian Andersen & his stories, with light & sound effects.",                    "Rådhuspladsen",                       55.6763138,             12.5699918));
        Global.array_info.add(new info("tourist",  "H.C. Andersen Eventyrhuset",                                      "H.C. Andersen Eventyrhuset.jpg",                                                      "0,6",            "Family-friendly exhibition about Hans Christian Andersen & his stories, with light & sound effects.",                    "Gammel Strand",                       55.6763138,             12.5699918));
        Global.array_info.add(new info("tourist",  "H.C. Andersen Eventyrhuset",                                      "H.C. Andersen Eventyrhuset.jpg",                                                      "0,8",            "Family-friendly exhibition about Hans Christian Andersen & his stories, with light & sound effects.",                    "Nørreport",                       55.6763138,             12.5699918));
        Global.array_info.add(new info("tourist",  "Hamad Bin Khalifa Civilisation Centre",                           "Hamad Bin Khalifa Civilisation Centre.jpg",                                                   "0,5",            "",                   "Skjolds Plads",                       55.7084879,             12.5491816));
        Global.array_info.add(new info("tourist",  "Hamad Bin Khalifa Civilisation Centre",                           "Hamad Bin Khalifa Civilisation Centre.jpg",                                                   "0,9",            "",                   "Vibenshus Runddel",                       55.7084879,             12.5491816));
        Global.array_info.add(new info("tourist",  "Hamlet Tours",                                                    "Hamlet Tours.jpg",                                                    "1,3",            "",                   "Skjolds Plads",                       55.7156012,             12.5490632));
        Global.array_info.add(new info("tourist",  "Hamlet Tours",                                                    "Hamlet Tours.jpg",                                                    "1,3",            "",                   "Vibenshus Runddel",                       55.7156012,             12.5490632));
        Global.array_info.add(new info("tourist",  "Hans Tausens Park",                                               "Hans Tausens Park.jpg",                                                   "0,3",            "",                   "Nuuks Plads",                         55.6891793,             12.547872));
        Global.array_info.add(new info("tourist",  "Hans Tausens Park",                                               "Hans Tausens Park.jpg",                                                   "0,5",            "",                   "Nørrebros Runddel",                       55.6891793,             12.547872));
        Global.array_info.add(new info("tourist",  "Hans Tausens Park",                                               "Hans Tausens Park.jpg",                                                   "1,4",            "",                   "Nørrebro",                        55.6891793,             12.547872));
        Global.array_info.add(new info("tourist",  "Havneparken",                                                     "Havneparken.jpg",                                                     "0,7",            "",                   "Islands Brygge",                      55.6652404,             12.5737477));
        Global.array_info.add(new info("tourist",  "Havneparken",                                                     "Havneparken.jpg",                                                     "1,0",            "",                   "Havneholmen (G)",                         55.6652404,             12.5737477));
        Global.array_info.add(new info("tourist",  "Heerup Museum",                                                   "Heerup Museum.jpg",                                                   "1,6",            "",                   "Vanløse",                         55.6828767,             12.4669583));
        Global.array_info.add(new info("tourist",  "Himmelskibet",                                                    "Himmelskibet.jpg",                                                    "1,4",            "",                   "Islands Brygge",                      55.6727241,             12.5678706));
        Global.array_info.add(new info("tourist",  "Hirschsprung Collection",                                         "Hirschsprung Collection.jpg",                                                     "0,6",            "Art gallery open since 1911 exhibiting a major collection of Danish paintings & sculpture.",                     "Østerport",                       55.689993,          12.5773931));
        Global.array_info.add(new info("tourist",  "Hirschsprung Collection",                                         "Hirschsprung Collection.jpg",                                                     "0,8",            "Art gallery open since 1911 exhibiting a major collection of Danish paintings & sculpture.",                     "Marmorkirken",                        55.689993,          12.5773931));
        Global.array_info.add(new info("tourist",  "Højbro Plads",                                                    "Højbro Plads.jpg",                                                    "0,06",           "",                   "Gammel Strand",                       55.6782982,             12.5798786));
        Global.array_info.add(new info("tourist",  "Højbro Plads",                                                    "Højbro Plads.jpg",                                                    "0,3",            "",                   "Kongens Nytorv",                      55.6782982,             12.5798786));
        Global.array_info.add(new info("tourist",  "Hørgen",                                                          "Hørgen.jpg",                                                      "10,9",           "",                   "Vanløse",                         55.7694276,             12.3936651));
        Global.array_info.add(new info("tourist",  "Hørgen",                                                          "Hørgen.jpg",                                                      "11,4",           "",                   "Flintholm",                       55.7694276,             12.3936651));
        Global.array_info.add(new info("tourist",  "ICC Theatre | Improv Comedy Copenhagen",                          "ICC Theatre | Improv Comedy Copenhagen.jpg",                                                      "0,3",            "",                   "Gammel Strand",                       55.6760734,             12.5748079));
        Global.array_info.add(new info("tourist",  "ICC Theatre | Improv Comedy Copenhagen",                          "ICC Theatre | Improv Comedy Copenhagen.jpg",                                                      "0,3",            "",                   "Rådhuspladsen",                       55.6760734,             12.5748079));
        Global.array_info.add(new info("tourist",  "Inderhavnsbroen",                                                 "Inderhavnsbroen.jpg",                                                     "0,6",            "Opened in 2016, this modern bridge features an innovative design & path for pedestrians & bicycles.",                    "Kongens Nytorv",                      55.6785035,             12.5947366));
        Global.array_info.add(new info("tourist",  "Inderhavnsbroen",                                                 "Inderhavnsbroen.jpg",                                                     "0,8",            "Opened in 2016, this modern bridge features an innovative design & path for pedestrians & bicycles.",                    "Marmorkirken",                        55.6785035,             12.5947366));
        Global.array_info.add(new info("tourist",  "Inuksuk",                                                         "Inuksuk.jpg",                                                     "0,6",            "",                   "Østerport",                       55.6940325,             12.5952471));
        Global.array_info.add(new info("tourist",  "Inuksuk",                                                         "Inuksuk.jpg",                                                     "1,0",            "",                   "Marmorkirken",                        55.6940325,             12.5952471));
        Global.array_info.add(new info("tourist",  "Jens Olsen\'s World Clock",                                       "Jens Olsen\'s World Clock.jpg",                                                   "0,1",            "Lauded astronomical clock with intricate dials displaying planetary movements, time, date & more.",                  "Rådhuspladsen",                       55.6756228,             12.5696733));
        Global.array_info.add(new info("tourist",  "Jens Olsen\'s World Clock",                                       "Jens Olsen\'s World Clock.jpg",                                                   "0,5",            "Lauded astronomical clock with intricate dials displaying planetary movements, time, date & more.",                  "København H (Metro)",                         55.6756228,             12.5696733));
        Global.array_info.add(new info("tourist",  "Jerusalem\'s Church",                                             "Jerusalem\'s Church.jpg",                                                     "0,4",            "",                   "Østerport",                       55.6888675,             12.5838281));
        Global.array_info.add(new info("tourist",  "Jesus Church",                                                    "Jesus Church.jpg",                                                    "1,4",            "",                   "Ny Ellebjerg (G)",                        55.6654631,             12.5221597));
        Global.array_info.add(new info("tourist",  "Kastellet",                                                       "Kastellet.jpg",                                                   "0,5",            "Star-shaped 17th-century fortress with ramparts & a museum, regular host to free events & concerts.",                    "Østerport",                       55.691376,          12.594301));
        Global.array_info.add(new info("tourist",  "Kastellet",                                                       "Kastellet.jpg",                                                   "0,7",            "Star-shaped 17th-century fortress with ramparts & a museum, regular host to free events & concerts.",                    "Marmorkirken",                        55.691376,          12.594301));
        Global.array_info.add(new info("tourist",  "Kastelskirken",                                                   "Kastelskirken.jpg",                                                   "0,5",            "",                   "Østerport",                       55.6914087,             12.5929153));
        Global.array_info.add(new info("tourist",  "Kastelskirken",                                                   "Kastelskirken.jpg",                                                   "0,7",            "",                   "Marmorkirken",                        55.6914087,             12.5929153));
        Global.array_info.add(new info("tourist",  "Kastrup Fortress",                                                "Kastrup Fortress.jpg",                                                    "0,3",            "",                   "Femøren",                         55.6485977,             12.6402569));
        Global.array_info.add(new info("tourist",  "Kastrup Fortress",                                                "Kastrup Fortress.jpg",                                                    "1,4",            "",                   "Kastrup",                         55.6485977,             12.6402569));
        Global.array_info.add(new info("tourist",  "Kastrupgård",                                                     "Kastrupgård.jpg",                                                     "0,5",            "",                   "Kastrup",                         55.6346703,             12.6386011));
        Global.array_info.add(new info("tourist",  "Kastrupgård",                                                     "Kastrupgård.jpg",                                                     "0,8",            "",                   "Københavns Lufthavn",                         55.6346703,             12.6386011));
        Global.array_info.add(new info("tourist",  "King\'s New Square",                                              "King\'s New Square.jpg",                                                      "0,1",            "Landmark cobble-stoned square dating to 1907, containing a royal equestrian statue of Christian V.",                     "Kongens Nytorv",                      55.680522,          12.585957));
        Global.array_info.add(new info("tourist",  "King\'s New Square",                                              "King\'s New Square.jpg",                                                      "0,5",            "Landmark cobble-stoned square dating to 1907, containing a royal equestrian statue of Christian V.",                     "Gammel Strand",                       55.680522,          12.585957));
        Global.array_info.add(new info("tourist",  "Knud Rasmussens gravsten",                                        "Knud Rasmussens gravsten.jpg",                                                    "1,1",            "",                   "Ny Ellebjerg (G)",                        55.6596878,             12.5291633));
        Global.array_info.add(new info("tourist",  "Knud Rasmussens gravsten",                                        "Knud Rasmussens gravsten.jpg",                                                    "1,2",            "",                   "Mozarts Plads (G)",                       55.6596878,             12.5291633));
        Global.array_info.add(new info("tourist",  "Københavns Synagoge",                                             "Københavns Synagoge.jpg",                                                     "0,3",            "",                   "Nørreport",                       55.6810141,             12.5737027));
        Global.array_info.add(new info("tourist",  "Kongeskibet Dannebrog",                                           "Kongeskibet Dannebrog.jpg",                                                   "1,0",            "",                   "Marmorkirken",                        55.6890622,             12.6037561));
        Global.array_info.add(new info("tourist",  "Kongeskibet Dannebrog",                                           "Kongeskibet Dannebrog.jpg",                                                   "1,2",            "",                   "Østerport",                       55.6890622,             12.6037561));
        Global.array_info.add(new info("tourist",  "Krystallen",                                                      "Krystallen.jpg",                                                      "0,7",            "",                   "København H (Metro)",                         55.6704901,             12.575187));
        Global.array_info.add(new info("tourist",  "Krystallen",                                                      "Krystallen.jpg",                                                      "0,7",            "",                   "Rådhuspladsen",                       55.6704901,             12.575187));
        Global.array_info.add(new info("tourist",  "KU.BE",                                                           "KU.BE.jpg",                                                   "0,4",            "",                   "Lindevang",                       55.6832708,             12.5056962));
        Global.array_info.add(new info("tourist",  "KU.BE",                                                           "KU.BE.jpg",                                                   "0,4",            "",                   "Flintholm",                       55.6832708,             12.5056962));
        Global.array_info.add(new info("tourist",  "Landbohøjskolens Have",                                           "Landbohøjskolens Have.jpg",                                                   "0,6",            "",                   "Forum",                       55.6807852,             12.5421746));
        Global.array_info.add(new info("tourist",  "Landbohøjskolens Have",                                           "Landbohøjskolens Have.jpg",                                                   "0,6",            "",                   "Frederiksberg",                       55.6807852,             12.5421746));
        Global.array_info.add(new info("tourist",  "Langelinie",                                                      "Langelinie.jpg",                                                      "0,8",            "Quayside promenade, home to the famous Little Mermaid statue, as well as shops & cafes.",                    "Nordhavn",                        55.6993361,             12.6002016));
        Global.array_info.add(new info("tourist",  "Langelinie",                                                      "Langelinie.jpg",                                                      "1,1",            "Quayside promenade, home to the famous Little Mermaid statue, as well as shops & cafes.",                    "Østerport",                       55.6993361,             12.6002016));
        Global.array_info.add(new info("tourist",  "Lenins bolig 1910",                                               "Lenins bolig 1910.jpg",                                                   "0,4",            "",                   "Enghave Place",                       55.67167,           12.544489));
        Global.array_info.add(new info("tourist",  "Lenins bolig 1910",                                               "Lenins bolig 1910.jpg",                                                   "1,4",            "",                   "Havneholmen (G)",                         55.67167,           12.544489));
        Global.array_info.add(new info("tourist",  "Lightvessel No. XIII",                                            "Lightvessel No. XIII.jpg",                                                    "0,5",            "",                   "Gammel Strand",                       55.6728519,             12.5790669));
        Global.array_info.add(new info("tourist",  "Lightvessel No. XIII",                                            "Lightvessel No. XIII.jpg",                                                    "0,7",            "",                   "Rådhuspladsen",                       55.6728519,             12.5790669));
        Global.array_info.add(new info("tourist",  "Lightvessel No. XIII",                                            "Lightvessel No. XIII.jpg",                                                    "0,7",            "",                   "Christianshavn",                      55.6728519,             12.5790669));
        Global.array_info.add(new info("tourist",  "Livetstræ",                                                       "Livetstræ.jpg",                                                   "4,6",            "",                   "Vestamager",                      55.5785031,             12.5871368));
        Global.array_info.add(new info("tourist",  "Livetstræ",                                                       "Livetstræ.jpg",                                                   "5,6",            "",                   "Ørestad",                         55.5785031,             12.5871368));
        Global.array_info.add(new info("tourist",  "Livgardens Historiske Samling",                                   "Livgardens Historiske Samling.jpg",                                                   "0,2",            "",                   "Nørreport",                       55.6851007,             12.5750753));
        Global.array_info.add(new info("tourist",  "Maritime Monument",                                               "Maritime Monument.jpg",                                                   "0,7",            "",                   "Østerport",                       55.693978,          12.5974463));
        Global.array_info.add(new info("tourist",  "Maritime Monument",                                               "Maritime Monument.jpg",                                                   "1,3",            "",                   "Nordhavn",                        55.693978,          12.5974463));
        Global.array_info.add(new info("tourist",  "Medical Museion",                                                 "Medical Museion.jpg",                                                     "0,2",            "Medical museum in 18th-century royal academy with instruments & exhibits from up to 400 years ago.",                     "Marmorkirken",                        55.685937,          12.5918646));
        Global.array_info.add(new info("tourist",  "Medical Museion",                                                 "Medical Museion.jpg",                                                     "0,8",            "Medical museum in 18th-century royal academy with instruments & exhibits from up to 400 years ago.",                     "Kongens Nytorv",                      55.685937,          12.5918646));
        Global.array_info.add(new info("tourist",  "Memorial Anchor, Copenhagen",                                     "Memorial Anchor, Copenhagen.jpg",                                                     "0,1",            "Popular destination featuring colorful 17th- & 18th-century townhouses on a canal with wooden ships.",                   "Kongens Nytorv",                      55.6806423,             12.5875441));
        Global.array_info.add(new info("tourist",  "Memorial Anchor, Copenhagen",                                     "Memorial Anchor, Copenhagen.jpg",                                                     "0,5",            "Popular destination featuring colorful 17th- & 18th-century townhouses on a canal with wooden ships.",                   "Marmorkirken",                        55.6806423,             12.5875441));
        Global.array_info.add(new info("tourist",  "Memorial Park",                                                   "Memorial Park.jpg",                                               "2,4",            "Landscaped park in former military barracks with a memorial wall to Danish WWII resistance fighters.",                   "Orientkaj",                       55.7275288,             12.5670441));
        Global.array_info.add(new info("tourist",  "Møinichen Mansion",                                               "Møinichen Mansion.jpg",                                               "0,3",            "",                   "Gammel Strand",                       55.6805418,             12.5780075));
        Global.array_info.add(new info("tourist",  "Møinichen Mansion",                                               "Møinichen Mansion.jpg",                                               "0,4",            "",                   "Kongens Nytorv",                      55.6805418,             12.5780075));
        Global.array_info.add(new info("tourist",  "Monument to Denmark\'s international activities after 1948",      "Monument to Denmark\'s international activities after 1948.jpg",                                                  "0,6",            "",                   "Østerport",                       55.6922818,             12.5960613));
        Global.array_info.add(new info("tourist",  "Møstings Hus",                                                    "Møstings Hus.jpg",                                                "0,3",            "",                   "Frederiksberg",                       55.6784232,             12.5277786));
        Global.array_info.add(new info("tourist",  "Møstings Hus",                                                    "Møstings Hus.jpg",                                                "1,0",            "",                   "Lindevang",                       55.6784232,             12.5277786));
        Global.array_info.add(new info("tourist",  "Music Museum",                                                    "Music Museum.jpg",                                                "0,05",           "",                   "Forum",                       55.6822971,             12.5524457));
        Global.array_info.add(new info("tourist",  "Music Museum",                                                    "Music Museum.jpg",                                                "0,9",            "",                   "Nuuks Plads",                         55.6822971,             12.5524457));
        Global.array_info.add(new info("tourist",  "National Aquarium Denmark",                                       "National Aquarium Denmark.jpg",                                               "0,6",            "Huge, sleek, whirlpool-shaped aquarium with fresh & sea water wildlife, plus educational displays.",                     "Kastrup",                         55.6381446,             12.6561446));
        Global.array_info.add(new info("tourist",  "National Aquarium Denmark",                                       "National Aquarium Denmark.jpg",                                               "1,0",            "Huge, sleek, whirlpool-shaped aquarium with fresh & sea water wildlife, plus educational displays.",                     "Københavns Lufthavn",                         55.6381446,             12.6561446));
        Global.array_info.add(new info("tourist",  "National Museum of Denmark",                                      "National Museum of Denmark.jpg",                                                  "0,4",            "18th-century mansion with collections and exhibitions on Denmark\'s history, people and culture.",                   "Rådhuspladsen",                       55.6746479,             12.574741));
        Global.array_info.add(new info("tourist",  "National Museum of Denmark",                                      "National Museum of Denmark.jpg",                                                  "0,4",            "18th-century mansion with collections and exhibitions on Denmark\'s history, people and culture.",                   "Gammel Strand",                       55.6746479,             12.574741));
        Global.array_info.add(new info("tourist",  "Naturcenter Amager",                                              "Naturcenter Amager.jpg",                                                  "0,5",            "",                   "Vestamager",                      55.6145272,             12.5755852));
        Global.array_info.add(new info("tourist",  "Naturcenter Amager",                                              "Naturcenter Amager.jpg",                                                  "1,6",            "",                   "Ørestad",                         55.6145272,             12.5755852));
        Global.array_info.add(new info("tourist",  "Naturlegepladsen",                                                "Naturlegepladsen.jpg",                                                "0,9",            "",                   "Ny Ellebjerg (G)",                        55.644704,          12.5197738));
        Global.array_info.add(new info("tourist",  "Naturlegepladsen",                                                "Naturlegepladsen.jpg",                                                "1,0",            "",                   "Mozarts Plads (G)",                       55.644704,          12.5197738));
        Global.array_info.add(new info("tourist",  "Naturmedicinsk",                                                  "Naturmedicinsk.jpg",                                                  "0,4",            "",                   "Vibenshus Runddel",                       55.703168,          12.5595074));
        Global.array_info.add(new info("tourist",  "Naturmedicinsk",                                                  "Naturmedicinsk.jpg",                                                  "0,7",            "",                   "Skjolds Plads",                       55.703168,          12.5595074));
        Global.array_info.add(new info("tourist",  "Nikolaj, Copenhagen Contemporary Art Center",                     "Nikolaj, Copenhagen Contemporary Art Center.jpg",                                                 "0,1",            "Contemporary art museum housed in converted church, with a restaurant, bookshop & free weekly tours.",                   "Gammel Strand",                       55.678644,          12.5815874));
        Global.array_info.add(new info("tourist",  "Nikolaj, Copenhagen Contemporary Art Center",                     "Nikolaj, Copenhagen Contemporary Art Center.jpg",                                                 "0,2",            "Contemporary art museum housed in converted church, with a restaurant, bookshop & free weekly tours.",                   "Kongens Nytorv",                      55.678644,          12.5815874));
        Global.array_info.add(new info("tourist",  "Ny Carlsberg Glyptotek",                                          "Ny Carlsberg Glyptotek.jpg",                                                  "0,4",            "Fine-art museum with antique Mediterranean sculptures, plus 19th-century French & Danish works.",                    "Rådhuspladsen",                       55.67298,           12.572543));
        Global.array_info.add(new info("tourist",  "Ny Carlsberg Glyptotek",                                          "Ny Carlsberg Glyptotek.jpg",                                                  "0,5",            "Fine-art museum with antique Mediterranean sculptures, plus 19th-century French & Danish works.",                    "København H (Metro)",                         55.67298,           12.572543));
        Global.array_info.add(new info("tourist",  "Nyboder",                                                         "Nyboder.jpg",                                                 "0,4",            "",                   "Østerport",                       55.6894407,             12.5871081));
        Global.array_info.add(new info("tourist",  "Nyboder",                                                         "Nyboder.jpg",                                                 "0,4",            "",                   "Marmorkirken",                        55.6894407,             12.5871081));
        Global.array_info.add(new info("tourist",  "Ofelia Plads",                                                    "Ofelia Plads.jpg",                                                "0,5",            "",                   "Marmorkirken",                        55.681557,          12.595602));
        Global.array_info.add(new info("tourist",  "Ofelia Plads",                                                    "Ofelia Plads.jpg",                                                "0,6",            "",                   "Kongens Nytorv",                      55.681557,          12.595602));
        Global.array_info.add(new info("tourist",  "Ørsteds Park",                                                    "Ørsteds Park.jpg",                                                "0,4",            "Children\'s playground with slides, swings, sandpit, pedal go-karts & traditional playthings.",                  "Nørreport",                       55.6810135,             12.5669485));
        Global.array_info.add(new info("tourist",  "Ørsteds Park",                                                    "Ørsteds Park.jpg",                                                "0,5",            "Children\'s playground with slides, swings, sandpit, pedal go-karts & traditional playthings.",                  "Rådhuspladsen",                       55.6810135,             12.5669485));
        Global.array_info.add(new info("tourist",  "Østre Anlæg Park",                                                "Østre Anlæg Park.jpg",                                                "0,6",            "Historic park featuring lakes, sculptures & a popular sledding hill, plus concerts in the summer.",                  "Østerport",                       55.689786,          12.5768615));
        Global.array_info.add(new info("tourist",  "Østre Anlæg Park",                                                "Østre Anlæg Park.jpg",                                                "0,8",            "Historic park featuring lakes, sculptures & a popular sledding hill, plus concerts in the summer.",                  "Marmorkirken",                        55.689786,          12.5768615));
        Global.array_info.add(new info("tourist",  "Our Fallen",                                                      "Our Fallen.jpg",                                                  "0,7",            "",                   "Østerport",                       55.6893237,             12.5952321));
        Global.array_info.add(new info("tourist",  "P.S. Valby Aps",                                                  "P.S. Valby Aps.jpg",                                                  "1,1",            "",                   "Ny Ellebjerg (G)",                        55.6633541,             12.515915));
        Global.array_info.add(new info("tourist",  "Parkmuseerne",                                                    "Parkmuseerne.jpg",                                                "0,6",            "",                   "Nørreport",                       55.6879129,             12.578247));
        Global.array_info.add(new info("tourist",  "Paulis Plads",                                                    "Paulis Plads.jpg",                                                "0,4",            "",                   "Mozarts Plads (G)",                       55.6499783,             12.5279893));
        Global.array_info.add(new info("tourist",  "Paulis Plads",                                                    "Paulis Plads.jpg",                                                "0,8",            "",                   "Ny Ellebjerg (G)",                        55.6499783,             12.5279893));
        Global.array_info.add(new info("tourist",  "Povl Markussen Memorial Plaque",                                  "Povl Markussen Memorial Plaque.jpg",                                                  "0,3",            "",                   "Nørreport",                       55.6804563,             12.5710001));
        Global.array_info.add(new info("tourist",  "Povl Markussen Memorial Plaque",                                  "Povl Markussen Memorial Plaque.jpg",                                                  "0,4",            "",                   "Rådhuspladsen",                       55.6804563,             12.5710001));
        Global.array_info.add(new info("tourist",  "Queen Louise Bridge",                                             "Queen Louise Bridge.jpg",                                                 "0,5",            "",                   "Nørreport",                       55.686718,          12.5640602));
        Global.array_info.add(new info("tourist",  "Queen Louise Bridge",                                             "Queen Louise Bridge.jpg",                                                 "1,2",            "",                   "Nørrebros Runddel",                       55.686718,          12.5640602));
        Global.array_info.add(new info("tourist",  "Ripley\'s Believe It or Not! Museum",                             "Ripley\'s Believe It or Not! Museum.jpg",                                                 "0,06",           "Museum with kitschy oddities on display, including shrunken human heads & rare animal skeletons.",                   "Rådhuspladsen",                       55.6763409,             12.5698934));
        Global.array_info.add(new info("tourist",  "Ripley\'s Believe It or Not! Museum",                             "Ripley\'s Believe It or Not! Museum.jpg",                                                 "0,6",            "Museum with kitschy oddities on display, including shrunken human heads & rare animal skeletons.",                   "København H (Metro)",                         55.6763409,             12.5698934));
        Global.array_info.add(new info("tourist",  "Rosenborg Castle",                                                "Rosenborg Castle.jpg",                                                "0,4",            "Dutch Renaissance palace & gardens, including a museum housing the crown jewels, with guided tours.",                    "Nørreport",                       55.6858274,             12.5772687));
        Global.array_info.add(new info("tourist",  "Rosenborg Castle",                                                "Rosenborg Castle.jpg",                                                "0,7",            "Dutch Renaissance palace & gardens, including a museum housing the crown jewels, with guided tours.",                    "Marmorkirken",                        55.6858274,             12.5772687));
        Global.array_info.add(new info("tourist",  "Rutschebanen / The Roller Coaster",                               "Rutschebanen / The Roller Coaster.jpg",                                               "1,5",            "",                   "Islands Brygge",                      55.6732425,             12.5668446));
        Global.array_info.add(new info("tourist",  "Ryvangen Naturpark",                                              "Ryvangen Naturpark.jpg",                                                  "1,5",            "",                   "Poul Henningsens Plads",                      55.7221315,             12.5659739));
        Global.array_info.add(new info("tourist",  "Ryvangen Naturpark",                                              "Ryvangen Naturpark.jpg",                                                  "2,1",            "",                   "Orientkaj",                       55.7221315,             12.5659739));
        Global.array_info.add(new info("tourist",  "Sacred Heart of Jesus Church",                                    "Sacred Heart of Jesus Church.jpg",                                                "1,0",            "",                   "Frederiksberg Allé",                      55.673159,          12.5575581));
        Global.array_info.add(new info("tourist",  "Saint Jacobs Church",                                             "Saint Jacobs Church.jpg",                                                 "0,4",            "",                   "Trianglen",                       55.7034932,             12.5766896));
        Global.array_info.add(new info("tourist",  "Saint Jacobs Church",                                             "Saint Jacobs Church.jpg",                                                 "0,6",            "",                   "Poul Henningsens Plads",                      55.7034932,             12.5766896));
        Global.array_info.add(new info("tourist",  "Sankt Hans Torv",                                                 "Sankt Hans Torv.jpg",                                                 "0,8",            "",                   "Nørrebros Runddel",                       55.6907893,             12.5606509));
        Global.array_info.add(new info("tourist",  "Sankt Hans Torv",                                                 "Sankt Hans Torv.jpg",                                                 "1,1",            "",                   "Forum",                       55.6907893,             12.5606509));
        Global.array_info.add(new info("tourist",  "Sankt Petri Church",                                              "Sankt Petri Church.jpg",                                                  "0,4",            "Simple church from the mid-1400s featuring a mausoleum with burials from notable families.",                     "Nørreport",                       55.6798777,             12.570872));
        Global.array_info.add(new info("tourist",  "Skuespilhuset",                                                   "Skuespilhuset.jpg",                                               "0,5",            "Striking purpose-built waterside theater for plays, plus classy dining with harbor & city views.",                   "Kongens Nytorv",                      55.680056,          12.5946831));
        Global.array_info.add(new info("tourist",  "Skuespilhuset",                                                   "Skuespilhuset.jpg",                                               "0,6",            "Striking purpose-built waterside theater for plays, plus classy dining with harbor & city views.",                   "Marmorkirken",                        55.680056,          12.5946831));
        Global.array_info.add(new info("tourist",  "Sleeping Louis",                                                  "Sleeping Louis.jpg",                                                  "4,0",            "",                   "Vanløse",                         55.6738307,             12.4312076));
        Global.array_info.add(new info("tourist",  "Sleeping Louis",                                                  "Sleeping Louis.jpg",                                                  "4,4",            "",                   "Flintholm",                       55.6738307,             12.4312076));
        Global.array_info.add(new info("tourist",  "SMK – Statens Museum for Kunst",                                  "SMK – Statens Museum for Kunst.jpg",                                                  "0,6",            "Danish national gallery, with international collections, temporary exhibitions & kids\' activities.",                    "Østerport",                       55.6888127,             12.5783303));
        Global.array_info.add(new info("tourist",  "Søndermarken",                                                    "Søndermarken.jpg",                                                "1,0",            "Serene, scenic park with many old trees & an subterranean art venue in 3 former cisterns.",                  "Frederiksberg Allé",                      55.6703153,             12.524468));
        Global.array_info.add(new info("tourist",  "Søndermarken",                                                    "Søndermarken.jpg",                                                "1,2",            "Serene, scenic park with many old trees & an subterranean art venue in 3 former cisterns.",                  "Fasanvej",                        55.6703153,             12.524468));
        Global.array_info.add(new info("tourist",  "Søren Kierkegaard\'s family house",                               "Søren Kierkegaard\'s family house.jpg",                                               "1,3",            "",                   "Forum",                       55.6776167,             12.5725476));
        Global.array_info.add(new info("tourist",  "Søren Kierkegaard\'s family house",                               "Søren Kierkegaard\'s family house.jpg",                                               "2,0",            "",                   "Enghave Place",                       55.6776167,             12.5725476));
        Global.array_info.add(new info("tourist",  "Stork Fountain",                                                  "Stork Fountain.jpg",                                                  "0,1",            "",                   "Gammel Strand",                       55.6788354,             12.5795731));
        Global.array_info.add(new info("tourist",  "Stork Fountain",                                                  "Stork Fountain.jpg",                                                  "0,3",            "",                   "Kongens Nytorv",                      55.6788354,             12.5795731));
        Global.array_info.add(new info("tourist",  "Storm P. Museum",                                                 "Storm P. Museum.jpg",                                                 "0,5",            "Museum of humorist Storm P\'s cartoons, drawings & paintings, plus his collection of 450 pipes.",                    "Frederiksberg Allé",                      55.674479,          12.532042));
        Global.array_info.add(new info("tourist",  "Storm P. Museum",                                                 "Storm P. Museum.jpg",                                                 "0,7",            "Museum of humorist Storm P\'s cartoons, drawings & paintings, plus his collection of 450 pipes.",                    "Frederiksberg",                       55.674479,          12.532042));
        Global.array_info.add(new info("tourist",  "Strandengen",                                                     "Strandengen.jpg",                                                 "0,1",            "",                   "Sundby",                      55.6442466,             12.5835971));
        Global.array_info.add(new info("tourist",  "Strandengen",                                                     "Strandengen.jpg",                                                 "0,6",            "",                   "Bella Center",                        55.6442466,             12.5835971));
        Global.array_info.add(new info("tourist",  "Strøget",                                                         "Strøget.jpg",                                                 "0,03",           "",                   "Rådhuspladsen",                       55.6764647,             12.5693548));
        Global.array_info.add(new info("tourist",  "Strøget",                                                         "Strøget.jpg",                                                 "0,1",            "",                   "Gammel Strand",                       55.6786621,             12.5771192));
        Global.array_info.add(new info("tourist",  "Submarine",                                                       "Submarine.jpg",                                               "1,0",            "",                   "Marmorkirken",                        55.6869511,             12.6054697));
        Global.array_info.add(new info("tourist",  "Submarine",                                                       "Submarine.jpg",                                               "1,4",            "",                   "Østerport",                       55.6869511,             12.6054697));
        Global.array_info.add(new info("tourist",  "Superkilen Park",                                                 "Superkilen Park.jpg",                                                 "0,3",            "Contemporary urban space with a jogging track, a playground, a basketball court & a skateboard ramp.",                   "Nørrebro",                        55.6993533,             12.5423634));
        Global.array_info.add(new info("tourist",  "Superkilen Park",                                                 "Superkilen Park.jpg",                                                 "0,5",            "Contemporary urban space with a jogging track, a playground, a basketball court & a skateboard ramp.",                   "Skjolds Plads",                       55.6993533,             12.5423634));
        Global.array_info.add(new info("tourist",  "Svanemøllens kloakpumpestation",                                  "Svanemøllens kloakpumpestation.jpg",                                                  "0,6",            "",                   "Poul Henningsens Plads",                      55.7152441,             12.5784531));
        Global.array_info.add(new info("tourist",  "Svanemøllens kloakpumpestation",                                  "Svanemøllens kloakpumpestation.jpg",                                                  "1,1",            "",                   "Orientkaj",                       55.7152441,             12.5784531));
        Global.array_info.add(new info("tourist",  "Sydhavnstippen",                                                  "Sydhavnstippen.jpg",                                                  "1,1",            "",                   "Sluseholmen",                         55.6371124,             12.534825));
        Global.array_info.add(new info("tourist",  "Sydhavnstippen",                                                  "Sydhavnstippen.jpg",                                                  "1,3",            "",                   "Mozarts Plads (G)",                       55.6371124,             12.534825));
        Global.array_info.add(new info("tourist",  "Sydhavnstippen",                                                  "Sydhavnstippen.jpg",                                                  "3,2",            "",                   "Vestamager",                      55.6371124,             12.534825));
        Global.array_info.add(new info("tourist",  "Tårnby Naturskole - Blå Base",                                    "Tårnby Naturskole - Blå Base.jpg",                                                "0,6",            "",                   "Femøren",                         55.6432781,             12.6474983));
        Global.array_info.add(new info("tourist",  "Tårnby Naturskole - Blå Base",                                    "Tårnby Naturskole - Blå Base.jpg",                                                "0,8",            "",                   "Kastrup",                         55.6432781,             12.6474983));
        Global.array_info.add(new info("tourist",  "The boats in Frederiksberg Gardens",                              "The boats in Frederiksberg Gardens.jpg",                                                  "1.3",            "",                   "Lindevang",                       55.6737061,             12.5267393));
        Global.array_info.add(new info("tourist",  "The Christiansborg\'s Tower",                                     "The Christiansborg\'s Tower.jpg",                                                 "0,7",            "",                   "Rådhuspladsen",                       55.6762057,             12.5804317));
        Global.array_info.add(new info("tourist",  "The Christiansborg\'s Tower",                                     "The Christiansborg\'s Tower.jpg",                                                 "1,0",            "",                   "Nørreport",                       55.6762057,             12.5804317));
        Global.array_info.add(new info("tourist",  "The Christiansborg\'s Tower",                                     "The Christiansborg\'s Tower.jpg",                                                 "1,1",            "",                   "Marmorkirken",                        55.6762057,             12.5804317));
        Global.array_info.add(new info("tourist",  "The David Collection",                                            "The David Collection.jpg",                                                "0,4",            "Museum featuring a collection of Islamic, European & Danish early modern paintings & decorative art.",                   "Marmorkirken",                        55.6843137,             12.5822462));
        Global.array_info.add(new info("tourist",  "The David Collection",                                            "The David Collection.jpg",                                                "0,5",            "Museum featuring a collection of Islamic, European & Danish early modern paintings & decorative art.",                   "Kongens Nytorv",                      55.6843137,             12.5822462));
        Global.array_info.add(new info("tourist",  "The Demon",                                                       "The Demon.jpg",                                               "0,3",            "",                   "København H (Metro)",                         55.672751,          12.5697216));
        Global.array_info.add(new info("tourist",  "The Demon",                                                       "The Demon.jpg",                                               "0,8",            "",                   "Gammel Strand",                       55.672751,          12.5697216));
        Global.array_info.add(new info("tourist",  "The Grand Tour of Copenhagen",                                    "The Grand Tour of Copenhagen.jpg",                                                "0,1",            "",                   "Gammel Strand",                       55.6776675,             12.5780018));
        Global.array_info.add(new info("tourist",  "The Grand Tour of Copenhagen",                                    "The Grand Tour of Copenhagen.jpg",                                                "0,4",            "",                   "Kongens Nytorv",                      55.6776675,             12.5780018));
        Global.array_info.add(new info("tourist",  "The King\'s Garden",                                              "The King\'s Garden.jpg",                                                  "0,5",            "Parkland & gardens, established in the 17th century, with lime-tree-lined paths & pavilions.",                   "Nørreport",                       55.6852905,             12.5798452));
        Global.array_info.add(new info("tourist",  "The King\'s Garden",                                              "The King\'s Garden.jpg",                                                  "0,5",            "Parkland & gardens, established in the 17th century, with lime-tree-lined paths & pavilions.",                   "Marmorkirken",                        55.6852905,             12.5798452));
        Global.array_info.add(new info("tourist",  "The Lakes in Copenhagen",                                         "The Lakes in Copenhagen.jpg",                                                 "0,6",            "",                   "Nørreport",                       55.687985,          12.5636735));
        Global.array_info.add(new info("tourist",  "The Lakes in Copenhagen",                                         "The Lakes in Copenhagen.jpg",                                                 "0,9",            "",                   "Forum",                       55.687985,          12.5636735));
        Global.array_info.add(new info("tourist",  "The Little Mermaid",                                              "The Little Mermaid.jpg",                                                  "0,8",            "Iconic bronze mermaid sculpture, by Edvard Eriksen, of a character from H.C. Andersen\'s fairytale.",                    "Østerport",                       55.69286,           12.5992828));
        Global.array_info.add(new info("tourist",  "The Little Mermaid",                                              "The Little Mermaid.jpg",                                                  "1,0",            "Iconic bronze mermaid sculpture, by Edvard Eriksen, of a character from H.C. Andersen\'s fairytale.",                    "Marmorkirken",                        55.69286,           12.5992828));
        Global.array_info.add(new info("tourist",  "The Mystic Exploratorie",                                         "The Mystic Exploratorie.jpg",                                                 "0,1",            "",                   "Kongens Nytorv",                      55.6804351,             12.5836798));
        Global.array_info.add(new info("tourist",  "The Mystic Exploratorie",                                         "The Mystic Exploratorie.jpg",                                                 "0,3",            "",                   "Gammel Strand",                       55.6804351,             12.5836798));
        Global.array_info.add(new info("tourist",  "The Round Tower",                                                 "The Round Tower.jpg",                                                 "0,3",            "17th-century tower with an observatory, planetarium, event hall & spiral ramp instead of stairs.",                   "Nørreport",                       55.681347,          12.5757299));
        Global.array_info.add(new info("tourist",  "The Round Tower",                                                 "The Round Tower.jpg",                                                 "0,4",            "17th-century tower with an observatory, planetarium, event hall & spiral ramp instead of stairs.",                   "Gammel Strand",                       55.681347,          12.5757299));
        Global.array_info.add(new info("tourist",  "The Tower Playground",                                            "The Tower Playground.jpg",                                                "0,4",            "",                   "Trianglen",                       55.6972222,             12.5696944));
        Global.array_info.add(new info("tourist",  "The Tower Playground",                                            "The Tower Playground.jpg",                                                "1,4",            "",                   "Poul Henningsens Plads",                      55.6972222,             12.5696944));
        Global.array_info.add(new info("tourist",  "Thorvaldsens Museum",                                             "Thorvaldsens Museum.jpg",                                                 "0,1",            "Canalside museum built around the neoclassical sculptor Bertel Thorvaldsen\'s burial place.",                    "Gammel Strand",                       55.6767472,             12.5783916));
        Global.array_info.add(new info("tourist",  "Thorvaldsens Museum",                                             "Thorvaldsens Museum.jpg",                                                 "0,5",            "Canalside museum built around the neoclassical sculptor Bertel Thorvaldsen\'s burial place.",                    "Kongens Nytorv",                      55.6767472,             12.5783916));
        Global.array_info.add(new info("tourist",  "Toldhus",                                                         "Toldhus.jpg",                                                 "4,2",            "",                   "Københavns Lufthavn",                         55.5937375,             12.6749105));
        Global.array_info.add(new info("tourist",  "Trinitatis Church",                                               "Trinitatis Church.jpg",                                               "0,3",            "",                   "Nørreport",                       55.6815933,             12.5761475));
        Global.array_info.add(new info("tourist",  "Trinitatis Church",                                               "Trinitatis Church.jpg",                                               "0,4",            "",                   "Gammel Strand",                       55.6815933,             12.5761475));
        Global.array_info.add(new info("tourist",  "Tuborgflasken",                                                   "Tuborgflasken.jpg",                                               "1,5",            "",                   "Poul Henningsens Plads",                      55.7233003,             12.5783946));
        Global.array_info.add(new info("tourist",  "Tuborgflasken",                                                   "Tuborgflasken.jpg",                                               "1,6",            "",                   "Orientkaj",                       55.7233003,             12.5783946));
        Global.array_info.add(new info("tourist",  "Tycho Brahe Planetarium",                                         "Tycho Brahe Planetarium.jpg",                                                 "0,4",            "Astronomy & space research center with an exhibition on the universe & a giant domed screen.",                   "København H (Metro)",                         55.6746508,             12.5580804));
        Global.array_info.add(new info("tourist",  "Tycho Brahe Planetarium",                                         "Tycho Brahe Planetarium.jpg",                                                 "0,7",            "Astronomy & space research center with an exhibition on the universe & a giant domed screen.",                   "Rådhuspladsen",                       55.6746508,             12.5580804));
        Global.array_info.add(new info("tourist",  "Ubåden Sælen",                                                    "Ubåden Sælen.jpg",                                                "1,0",            "",                   "Marmorkirken",                        55.6873104,             12.6053727));
        Global.array_info.add(new info("tourist",  "Ubåden Sælen",                                                    "Ubåden Sælen.jpg",                                                "1,4",            "",                   "Østerport",                       55.6873104,             12.6053727));
        Global.array_info.add(new info("tourist",  "Upcycle Studios",                                                 "Upcycle Studios.jpg",                                                 "0,2",            "",                   "Vestamager",                      55.6202057,             12.5722179));
        Global.array_info.add(new info("tourist",  "Upcycle Studios",                                                 "Upcycle Studios.jpg",                                                 "1,0",            "",                   "Ørestad",                         55.6202057,             12.5722179));
        Global.array_info.add(new info("tourist",  "Valby Park",                                                      "Valby Park.jpg",                                                  "1,2",            "Large park featuring several themed gardens, disc golf, playgrounds & frequent concerts & events.",                  "Mozarts Plads (G)",                       55.6406812,             12.5207815));
        Global.array_info.add(new info("tourist",  "Valby Park",                                                      "Valby Park.jpg",                                                  "1,3",            "Large park featuring several themed gardens, disc golf, playgrounds & frequent concerts & events.",                  "Ny Ellebjerg (G)",                        55.6406812,             12.5207815));
        Global.array_info.add(new info("tourist",  "Varehuset Messen",                                                "Varehuset Messen.jpg",                                                "0,3",            "",                   "Gammel Strand",                       55.6808518,             12.5778396));
        Global.array_info.add(new info("tourist",  "Varehuset Messen",                                                "Varehuset Messen.jpg",                                                "0,4",            "",                   "Kongens Nytorv",                      55.6808518,             12.5778396));
        Global.array_info.add(new info("tourist",  "Ved Stranden",                                                    "Ved Stranden.jpg",                                                "0,07",           "",                   "Gammel Strand",                       55.6776858,             12.5807396));
        Global.array_info.add(new info("tourist",  "Ved Stranden",                                                    "Ved Stranden.jpg",                                                "0,3",            "",                   "Kongens Nytorv",                      55.6776858,             12.5807396));
        Global.array_info.add(new info("tourist",  "Vestre Kirkegård",                                                "Vestre Kirkegård.jpg",                                                "1,4",            "",                   "Mozarts Plads (G)",                       55.6615078,             12.5327503));
        Global.array_info.add(new info("tourist",  "Vestre Kirkegård",                                                "Vestre Kirkegård.jpg",                                                "1,4",            "",                   "Ny Ellebjerg (G)",                        55.6615078,             12.5327503));
        Global.array_info.add(new info("tourist",  "Vodka museum Copenhagen",                                         "Vodka museum Copenhagen.jpg",                                                 "0,6",            "",                   "Havneholmen (G)",                         55.6660919,             12.5533679));
        Global.array_info.add(new info("tourist",  "Wehrmacht Graffiti",                                              "Wehrmacht Graffiti.jpg",                                                  "0,3",            "",                   "København H (Metro)",                         55.6684036,             12.5635844));
        Global.array_info.add(new info("tourist",  "Wehrmacht Graffiti",                                              "Wehrmacht Graffiti.jpg",                                                  "0,8",            "",                   "Havneholmen (G)",                         55.6684036,             12.5635844));
        Global.array_info.add(new info("tourist",  "Weirdwalks Byvandring",                                           "Weirdwalks Byvandring.jpg",                                               "0,2",            "",                   "Rådhuspladsen",                       55.6774893,             12.5728448));
        Global.array_info.add(new info("tourist",  "Weirdwalks Byvandring",                                           "Weirdwalks Byvandring.jpg",                                               "0,4",            "",                   "Gammel Strand",                       55.6774893,             12.5728448));
        Global.array_info.add(new info("tourist",  "Workers Museum",                                                  "Workers Museum.jpg",                                                  "0,1",            "Working culture museum with a 1915 apartment & industrial & labour conditions history exhibitions.",                     "Nørreport",                       55.684741,          12.570408));
        Global.array_info.add(new info("tourist",  "Workers Museum",                                                  "Workers Museum.jpg",                                                  "0,9",            "Working culture museum with a 1915 apartment & industrial & labour conditions history exhibitions.",                     "Rådhuspladsen",                       55.684741,          12.570408));
        Global.array_info.add(new info("tourist",  "Zoological Museum",                                               "Zoological Museum.jpg",                                               "0,5",            "Natural history museum with hands-on exhibits such as dinosaur skeletons, plus a cafe on weekends.",                     "Vibenshus Runddel",                       55.702512,          12.558926));
        Global.array_info.add(new info("tourist",  "Zoological Museum",                                               "Zoological Museum.jpg",                                               "0,6",            "Natural history museum with hands-on exhibits such as dinosaur skeletons, plus a cafe on weekends.",                     "Skjolds Plads",                       55.702512,          12.558926));
        Global.array_info.add(new info("tourist",  "Zoological Museum",                                               "Zoological Museum.jpg",                                               "1,3",            "Natural history museum with hands-on exhibits such as dinosaur skeletons, plus a cafe on weekends.",                     "Poul Henningsens Plads",                      55.702512,          12.558926));
        Global.array_info.add(new info("tourist",  "Zoological Museum",                                               "Zoological Museum.jpg",                                               "2,4",            "Natural history museum with hands-on exhibits such as dinosaur skeletons, plus a cafe on weekends.",                     "Orientkaj",                       55.702512,          12.558926));
        Global.array_info.add(new info("tourist",  "交代式前の衛兵ルート",                                              "交代式前の衛兵ルート.jpg",                                                  "0,3",            "",                   "Gammel Strand",                       55.6804812,             12.5784243));
        Global.array_info.add(new info("tourist",   "交代式前の衛兵ルート",                                              "交代式前の衛兵ルート.jpg",                                                  "0,4",            "",                   "Kongens Nytorv",                      55.6804812,             12.5784243));








        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "10,0", "Copenhagen Airport","Aksel Møllers Have"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Amagerbro"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "3,0", "Copenhagen Airport","Amager Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "40,0", "Copenhagen Airport","Bella Center"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Christianshavn"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "60,0", "Copenhagen Airport","DR Byen"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "87,0", "Copenhagen Airport","Enghave Plads"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "60,0", "Copenhagen Airport","Enghave Brygge (G)"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "70,0", "Copenhagen Airport","Fasanvej"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Flintholm"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Femøren"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Forum"                 ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Frederiksberg"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "4,0", "Copenhagen Airport","Frederiksberg Allé"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Gammel Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "10,0", "Copenhagen Airport","Havneholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Islands Brygge"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "40,0", "Copenhagen Airport","Kastrup"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Kongens Nytorv"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "60,0", "Copenhagen Airport","København H"           ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "70,0", "Copenhagen Airport","Københavns Lufthavn"   ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Lergravsparken"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Lindevang"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "40,0", "Copenhagen Airport","Marmorkirken"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Mozarts Plads (G)"     ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "60,0", "Copenhagen Airport","Nordhavn"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "70,0", "Copenhagen Airport","Aksel Møllers Have"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Amagerbro"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "3,0", "Copenhagen Airport","Amager Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Bella Center"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Christianshavn"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "10,0", "Copenhagen Airport","DR Byen"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "40,0", "Copenhagen Airport","Enghave Plads"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Enghave Brygge (G)"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "60,0", "Copenhagen Airport","Fasanvej"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "220,0", "Copenhagen Airport","Flintholm"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Femøren"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "10,0", "Copenhagen Airport","Forum"                 ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "10,0", "Copenhagen Airport","Frederiksberg"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Frederiksberg Allé"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Gammel Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "40,0", "Copenhagen Airport","Havneholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Islands Brygge"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "60,0", "Copenhagen Airport","Kastrup"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "10,0", "Copenhagen Airport","Kongens Nytorv"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","København H"           ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Københavns Lufthavn"   ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "40,0", "Copenhagen Airport","Lergravsparken"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "50,0", "Copenhagen Airport","Lindevang"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "10,0", "Copenhagen Airport","Marmorkirken"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "20,0", "Copenhagen Airport","Mozarts Plads (G)"     ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "30,0", "Copenhagen Airport","Nordhavn"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Nuuks Plads"           ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Ny Ellebjerg (G)"      ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Nørreport"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Nørrebros Rundddel"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Nørrebro"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Orientkaj"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Poul Henningsens Plads",55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Rådhuspladsen"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Skjold Plads"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Sluseholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Sundby"                ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Trianglen"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Vanløse"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Vestamager"            ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Vibenshus Runddel"     ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Ørestad"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Øresund"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Hall of Glory", "none",  "90,0", "Copenhagen Airport","Østerport"             ,55.628818, 12.64417));
    }
    private void init_arrayAdapter() {
        for(int i = 0; i < Global.array_state.size(); i ++){
            mStrings.add(Global.array_state.get(i).getName());
        }
    }

    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {

                for(int i = 0; i< Global.array_state.size();i++) {
                    if (Global.array_state.get(i).getName().equals(mSimpleArrayListAdapter.getItem(position))){
                        showPOPupDialog(Global.array_state.get(i));
                        break;
                    }
                }
        }

        @Override
        public void onNothingSelected() {
            Toast.makeText(MainActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent( MainActivity.this, info_viewActivity.class);

        switch (v.getId()){
            case R.id.btn_food:
                intent.putExtra("TYPE","Food");
                intent.putExtra("NAME",selected_station.getName());
                startActivity(intent);
                break;
            case R.id.btn_route:

                String uri = "geo:" + String.valueOf(selected_station.getLat()) + "," + String.valueOf(selected_station.getLng()) + "?q="
                        + String.valueOf(selected_station.getLat())
                        + "," + String.valueOf(selected_station.getLng()) + "(" + selected_station.getName()+")";
                    Uri.parse(uri);
                 intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri));
                intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                startActivity(intent);
                break;
            case R.id.btn_tourist:
                intent.putExtra("TYPE","Tourist");
                intent.putExtra("NAME",selected_station.getName());
                startActivity(intent);
                break;
        }
    }

    public static MainActivity getInstance(){
        return  mself;
    }

    public static String getToday(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String str = sdf.format(currentTime);
        String sub_id = String.valueOf(currentTime.getHours())+String.valueOf(currentTime.getMinutes());
        return currentTime.toString();
    }

}
