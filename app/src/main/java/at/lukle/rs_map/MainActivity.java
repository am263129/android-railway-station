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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import at.lukle.rs_map.searchspinner.SimpleArrayListAdapter;
import at.lukle.rs_map.station_info.MapsActivity;
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
    ImageView map,position;
    Bitmap tempBitmap;
    Canvas tempCanvas;
    Dialog dialog;
    State selected_station;
    public static MainActivity mself;
    private SearchableSpinner mSearchableSpinner;
    private SimpleArrayListAdapter mSimpleArrayListAdapter;
    private ArrayList<String> mStrings =  new ArrayList<>();
    int addintional_y = 298;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mself = this;
        init_data();
        init_arrayAdapter();
        map = (ImageView) findViewById(R.id.imageView);
        map.setImageResource(R.drawable.high_resolution_map_2);
        position = (ImageView)findViewById(R.id.img_position);
        mSearchableSpinner = (SearchableSpinner) findViewById(R.id.SearchableSpinner);
        mSimpleArrayListAdapter = new SimpleArrayListAdapter(this, mStrings);
        mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
        mSearchableSpinner.setOnItemSelectedListener(mOnItemSelectedListener);

        photoViewAttacher = new PhotoViewAttacher(map);
        photoViewAttacher.setMinimumScale(1.3f);
        photoViewAttacher.setMaximumScale(3.0f);
        photoViewAttacher.setScale(2.0f, 592.9f, 405.9f, true);

        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(photoViewAttacher, this);
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);

        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.high_resolution_map_2);
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
//        Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
//        dialog.setContentView(R.layout.popup_dialog);
//        getWindow().setGravity(Gravity.BOTTOM);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.show();

//        Dialog dialog = new Dialog(this);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        dialog.setContentView(R.layout.popup_dialog);
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.BOTTOM;
//        lp.windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setAttributes(lp);


        ValueAnimator animator = ValueAnimator.ofInt(0, 120);
        animator.setDuration(2000);
        animator.setRepeatCount(10);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value > 60)
                    value = 120-value;
                Float radius = Float.parseFloat(String.valueOf(value));
                tempCanvas.drawBitmap(myBitmap, 0, 0, null);
                final Paint paint;
                paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);
                tempCanvas.drawCircle(convertDpToPixel(((State) item).getCenter_X()+2,MainActivity.this), convertDpToPixel(((State) item).getCenter_Y() +2 +addintional_y,MainActivity.this), radius, paint);
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
        ImageView time_1_1 = (ImageView)dialog.findViewById(R.id.time_1_1);
        ImageView time_1_2 = (ImageView)dialog.findViewById(R.id.time_1_2);
        ImageView time_2_1 = (ImageView)dialog.findViewById(R.id.time_2_1);
        switch (item.getTrainName()){
            case "M1":
                train_name_1.setImageResource(R.drawable.m1);
                time_1_1.setImageResource(R.drawable.m1_6min);
                break;
            case "M2":
                train_name_1.setImageResource(R.drawable.m2);
                time_1_1.setImageResource(R.drawable.m2_6min);
                break;
            case "M3":
                train_name_1.setImageResource(R.drawable.m3);
                time_1_1.setImageResource(R.drawable.m3_4min);
                break;
            case "M4":
                train_name_1.setImageResource(R.drawable.m4);
                time_1_1.setImageResource(R.drawable.m4_2min);
                break;
            case "M1,M2":
                train_name_1.setImageResource(R.drawable.m1_m2);
                time_1_1.setImageResource(R.drawable.m1_3min);
                time_1_2.setVisibility(View.VISIBLE);
                time_1_2.setImageResource(R.drawable.m2_3min);
                break;
            case "M3,M4":
                train_name_1.setImageResource(R.drawable.m3_m4);
                time_1_2.setVisibility(View.VISIBLE);
                time_1_1.setImageResource(R.drawable.m3_4min);
                time_1_2.setImageResource(R.drawable.m4_2min);
                break;
            case "M1,M2,M3":
                train_2.setVisibility(View.VISIBLE);
                train_name_1.setImageResource(R.drawable.m1_m2);
                train_name_2.setImageResource(R.drawable.m3);
                time_1_2.setVisibility(View.VISIBLE);
                time_1_1.setImageResource(R.drawable.m1_3min);
                time_1_2.setImageResource(R.drawable.m2_3min);
                time_2_1.setImageResource(R.drawable.m3_4min);
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
        Global.array_state.add(new State( "Aksel Møllers Have"    , 340,260   ,345,265  , 55.686376, 12.533187,    "M3"));
        Global.array_state.add(new State( "Amagerbro"             , 700,400	,707,409  ,   55.663542, 12.602702,  "M2"));
        Global.array_state.add(new State( "Amager Strand"         , 790,490	,795,497  ,   55.65612,  12.631895,  "M2"));
        Global.array_state.add(new State( "Bella Center"          , 660,570	,663,582  ,   55.638067, 12.582899,  "M1"));
        Global.array_state.add(new State( "Christianshavn"        , 670,370	,670,383  ,   55.672098, 12.591663,  "M1,M2"));
        Global.array_state.add(new State( "DR Byen"               , 660,490	,663,498  ,   55.655848, 12.589044,  "M1"));
        Global.array_state.add(new State( "Enghave Plads"         , 410,410	,420,416  ,   55.667265, 12.545814,  "M3"));
        Global.array_state.add(new State( "Enghave Brygge (G)"    , 340,530	,350,536  ,   55.654212, 12.557069,  "M4"));
        Global.array_state.add(new State( "Fasanvej"              , 250,320	,261,339  ,   55.681675, 12.5231,    "M1,M2"));
        Global.array_state.add(new State( "Flintholm"             , 130,320	,140,339  ,   55.685642, 12.499116,  "M1,M2"));
        Global.array_state.add(new State( "Femøren"               , 820,520	,824,526  ,   55.645223, 12.638361,  "M2"));
        Global.array_state.add(new State( "Forum"                 , 440,320	,448,339  ,   55.681825, 12.552412,  "M1,M2"));
        Global.array_state.add(new State( "Frederiksberg"         , 330,320	,353,339  ,   55.681216, 12.531711,  "M1,M2,M3"));
        Global.array_state.add(new State( "Frederiksberg Allé"    , 370,380	,379,383  ,   55.673697, 12.540408,  "M3"));
        Global.array_state.add(new State( "Gammel Strand"         , 560,400	,575,417  ,   55.677765, 12.57959,   "M3,M4"));
        Global.array_state.add(new State( "Havneholmen (G)"       , 370,500	,381,506  ,   55.661299, 12.558911,  "M4"));
        Global.array_state.add(new State( "Islands Brygge"        , 660,450	,663,456  ,   55.663423, 12.585136,  "M1"));
        Global.array_state.add(new State( "Kastrup"               , 850,550	,854,555  ,   55.635673, 12.647003,  "M2"));
        Global.array_state.add(new State( "Kongens Nytorv"        , 600,320	,634,348  ,   55.679434, 12.585232,  "M1,M2,M3"));
        Global.array_state.add(new State( "København H"           , 460,420	,469,440  ,   55.671929, 12.564114,  "M3,M4"));
        Global.array_state.add(new State( "Københavns Lufthavn"   , 880,580	,883,585  ,   55.62957,  12.649375,  "M2"));
        Global.array_state.add(new State( "Lergravsparken"        , 730,430	,736,438  ,   55.662233, 12.616295,  "M2"));
        Global.array_state.add(new State( "Lindevang"             , 190,320	,200,338  ,   55.683482, 12.51312,   "M1,M2"));
        Global.array_state.add(new State( "Marmorkirken"          , 630,270	,646,273  ,   55.685242, 12.588634,  "M3,M4"));
        Global.array_state.add(new State( "Mozarts Plads (G)"     , 280,590	,290,596  ,   55.648892, 12.534109,  "M4"));
        Global.array_state.add(new State( "Nordhavn"              , 730,90	,737,94   ,   55.705255, 12.590928,  "M4"));
        Global.array_state.add(new State( "Nuuks Plads"           , 350,210	,359,219  ,   55.688773, 12.542854,  "M3"));
        Global.array_state.add(new State( "Ny Ellebjerg (G)"      , 250,620	,261,626  ,   55.652933, 12.516034,  "M4"));
        Global.array_state.add(new State( "Nørreport"             , 530,320	,537,339  ,   55.683685, 12.571571,  "M1,M2"));
        Global.array_state.add(new State( "Nørrebros Rundddel"    , 380,170	,388,182  ,   55.694034, 12.548916,  "M3"));
        Global.array_state.add(new State( "Nørrebro"              , 420,150	,426,154  ,   55.70071,  12.537855,  "M3"));
        Global.array_state.add(new State( "Orientkaj"             , 760,60	,766,64   ,   55.711799, 12.595137,  "M4"));
        Global.array_state.add(new State( "Poul Henningsens Plads", 560,150	,562,158  ,   55.709263, 12.57665,   "M3"));
        Global.array_state.add(new State( "Rådhuspladsen"         , 510,420	,524,439  ,   55.676464, 12.568818,  "M3,M4"));
        Global.array_state.add(new State( "Skjold Plads"          , 460,130	,471,141  ,   55.703278, 55.703278,  "M3"));
        Global.array_state.add(new State( "Sluseholmen (G)"       , 310,560	,320,566  ,   55.645548, 12.544731 , "M3"));
        Global.array_state.add(new State( "Sundby"                , 660,530	,663,540  ,   55.645212, 12.585742,  "M1"));
        Global.array_state.add(new State( "Trianglen"             , 590,180	,599,188  ,   55.699252, 12.576081,  "M3"));
        Global.array_state.add(new State( "Vanløse"               , 70,320	,79,339   ,   55.687348, 12.491533,  "M1,M2"));
        Global.array_state.add(new State( "Vestamager"            , 660,660	,663,667  ,   55.619366, 12.575491,  "M1"));
        Global.array_state.add(new State( "Vibenshus Runddel"     , 510,130	,519,142  ,   55.70638,  12.563979,  "M3"));
        Global.array_state.add(new State( "Ørestad"               , 660,620	,662,624  ,   55.628978, 12.579393,  "M1"));
        Global.array_state.add(new State( "Øresund"               , 760,460	,166,467  ,   55.661347, 12.628824,  "M2"));
        Global.array_state.add(new State( "Østerport"             , 620,220	,633,225  ,   55.6932,   12.585403,  "M3,M4"));

        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","1,0", "Copenhagen Airport","Aksel Møllers Have"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","20,0", "Copenhagen Airport","Amagerbro"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","3,0", "Copenhagen Airport","Amager Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","4,0", "Copenhagen Airport","Bella Center"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","5,0", "Copenhagen Airport","Christianshavn"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","6,0", "Copenhagen Airport","DR Byen"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","7,0", "Copenhagen Airport","Enghave Plads"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","8,0", "Copenhagen Airport","Enghave Brygge (G)"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","9,0", "Copenhagen Airport","Fasanvej"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","10,0", "Copenhagen Airport","Flintholm"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","20,0", "Copenhagen Airport","Femøren"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","30,0", "Copenhagen Airport","Forum"                 ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","40,0", "Copenhagen Airport","Frederiksberg"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","50,0", "Copenhagen Airport","Frederiksberg Allé"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","1,0", "Copenhagen Airport","Gammel Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","123,0", "Copenhagen Airport","Havneholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","12,0", "Copenhagen Airport","Islands Brygge"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","2,0", "Copenhagen Airport","Kastrup"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","34,0", "Copenhagen Airport","Kongens Nytorv"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","12,0", "Copenhagen Airport","København H"           ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","34,0", "Copenhagen Airport","Københavns Lufthavn"   ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","5,0", "Copenhagen Airport","Lergravsparken"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","44,0", "Copenhagen Airport","Lindevang"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","56,0", "Copenhagen Airport","Marmorkirken"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","23,0", "Copenhagen Airport","Mozarts Plads (G)"     ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","234,0", "Copenhagen Airport","Nordhavn"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","11,0", "Copenhagen Airport","Aksel Møllers Have"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","3,0", "Copenhagen Airport","Amagerbro"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","5,0", "Copenhagen Airport","Amager Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","67,0", "Copenhagen Airport","Bella Center"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","44,0", "Copenhagen Airport","Christianshavn"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","23,0", "Copenhagen Airport","DR Byen"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","3,0", "Copenhagen Airport","Enghave Plads"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","12,0", "Copenhagen Airport","Enghave Brygge (G)"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","23,0", "Copenhagen Airport","Fasanvej"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","11,0", "Copenhagen Airport","Flintholm"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","12,0", "Copenhagen Airport","Femøren"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","15,0", "Copenhagen Airport","Forum"                 ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","34,0", "Copenhagen Airport","Frederiksberg"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","25,0", "Copenhagen Airport","Frederiksberg Allé"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png",  "34,0", "Copenhagen Airport","Gammel Strand"      ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","33,0", "Copenhagen Airport","Havneholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","345,0", "Copenhagen Airport","Islands Brygge"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","1,0", "Copenhagen Airport","Kastrup"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","20,0", "Copenhagen Airport","Kongens Nytorv"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","30,0", "Copenhagen Airport","København H"           ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","40,0", "Copenhagen Airport","Københavns Lufthavn"   ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","50,0", "Copenhagen Airport","Lergravsparken"        ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","60,0", "Copenhagen Airport","Lindevang"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","70,0", "Copenhagen Airport","Marmorkirken"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","80,0", "Copenhagen Airport","Mozarts Plads (G)"     ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Nordhavn"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Nuuks Plads"           ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Ny Ellebjerg (G)"      ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Nørreport"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Nørrebros Rundddel"    ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Nørrebro"              ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Orientkaj"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Poul Henningsens Plads",55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Rådhuspladsen"         ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Skjold Plads"          ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Sluseholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Sundby"                ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Trianglen"             ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Vanløse"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Vestamager"            ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Vibenshus Runddel"     ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Ørestad"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Øresund"               ,55.628818, 12.64417));
        Global.array_info.add(new info("restaurant","Copenhagen Airport", "https://www.google.com.bd/images/srpr/logo11w.png","90,0", "Copenhagen Airport","Østerport"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "10,0", "Copenhagen Airport","Aksel Møllers Have"    ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Amagerbro"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "3,0", "Copenhagen Airport","Amager Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "40,0", "Copenhagen Airport","Bella Center"          ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Christianshavn"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "60,0", "Copenhagen Airport","DR Byen"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "87,0", "Copenhagen Airport","Enghave Plads"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "60,0", "Copenhagen Airport","Enghave Brygge (G)"    ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "70,0", "Copenhagen Airport","Fasanvej"              ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Flintholm"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Femøren"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Forum"                 ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Frederiksberg"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "4,0", "Copenhagen Airport","Frederiksberg Allé"    ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Gammel Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "10,0", "Copenhagen Airport","Havneholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Islands Brygge"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "40,0", "Copenhagen Airport","Kastrup"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Kongens Nytorv"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "60,0", "Copenhagen Airport","København H"           ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "70,0", "Copenhagen Airport","Københavns Lufthavn"   ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Lergravsparken"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Lindevang"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "40,0", "Copenhagen Airport","Marmorkirken"          ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Mozarts Plads (G)"     ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "60,0", "Copenhagen Airport","Nordhavn"              ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "70,0", "Copenhagen Airport","Aksel Møllers Have"    ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Amagerbro"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "3,0", "Copenhagen Airport","Amager Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Bella Center"          ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Christianshavn"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "10,0", "Copenhagen Airport","DR Byen"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "40,0", "Copenhagen Airport","Enghave Plads"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Enghave Brygge (G)"    ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "60,0", "Copenhagen Airport","Fasanvej"              ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "220,0", "Copenhagen Airport","Flintholm"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Femøren"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "10,0", "Copenhagen Airport","Forum"                 ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "10,0", "Copenhagen Airport","Frederiksberg"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Frederiksberg Allé"    ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Gammel Strand"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "40,0", "Copenhagen Airport","Havneholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Islands Brygge"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "60,0", "Copenhagen Airport","Kastrup"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "10,0", "Copenhagen Airport","Kongens Nytorv"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","København H"           ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Københavns Lufthavn"   ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "40,0", "Copenhagen Airport","Lergravsparken"        ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "50,0", "Copenhagen Airport","Lindevang"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "10,0", "Copenhagen Airport","Marmorkirken"          ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "20,0", "Copenhagen Airport","Mozarts Plads (G)"     ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "30,0", "Copenhagen Airport","Nordhavn"              ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Nuuks Plads"           ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Ny Ellebjerg (G)"      ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Nørreport"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Nørrebros Rundddel"    ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Nørrebro"              ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Orientkaj"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Poul Henningsens Plads",55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Rådhuspladsen"         ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Skjold Plads"          ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Sluseholmen (G)"       ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Sundby"                ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Trianglen"             ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Vanløse"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Vestamager"            ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Vibenshus Runddel"     ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Ørestad"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Øresund"               ,55.628818, 12.64417));
        Global.array_info.add(new info("tourist","Hall of Glory", "https://www.google.com.bd/images/srpr/logo11w.png",  "90,0", "Copenhagen Airport","Østerport"             ,55.628818, 12.64417));
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

}
