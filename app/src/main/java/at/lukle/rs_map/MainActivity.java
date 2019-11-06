package at.lukle.rs_map;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import at.lukle.rs_map.station_info.MapsActivity;
import at.lukle.rs_map.station_info.info_viewActivity;
import at.lukle.rs_map.util.Global;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements OnClickableAreaClickedListener,  View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    PhotoViewAttacher photoViewAttacher;
    Bitmap myBitmap;
    ImageView map,position;
    Bitmap tempBitmap;
    Canvas tempCanvas;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_data();
        map = (ImageView) findViewById(R.id.imageView);
        map.setImageResource(R.drawable.high_resolution_map);
        position = (ImageView)findViewById(R.id.img_position);
        photoViewAttacher = new PhotoViewAttacher(map);
        photoViewAttacher.setMinimumScale(1.3f);
        photoViewAttacher.setScale(2.5f, 592.9f, 405.9f, true);

        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(photoViewAttacher, this);
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);

        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.high_resolution_map);
        tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmap);


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
    }



    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof State) {
            Log.e("Time","milestone1");
            String text = ((State) item).getName();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            Log.e("Time","milestone2");
            showPOPupDialog();
            Log.e("Time","milestone3");
//
//            tempCanvas.drawBitmap(myBitmap, 0, 0, null);
//
//            Paint paint;
//            paint = new Paint();
//            paint.setColor(Color.RED);
//            paint.setStyle(Paint.Style.FILL);
//            tempCanvas.drawCircle(76, 345, 20, paint);
//
//
////Attach the canvas to the ImageView
//            map.setImageResource(new BitmapDrawable(getResources(), tempBitmap));
        }
    }

    private void showPOPupDialog() {
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
        dialog.show();
    }


    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();


        for (int i = 0; i < Global.array_state.size(); i ++){
            clickableAreas.add(new ClickableArea(Global.array_state.get(i).getArea_X(), Global.array_state.get(i).getArea_Y(), 30, 30, Global.array_state.get(i)));
        }
        return clickableAreas;
    }

    private void init_data() {
        Global.array_state.add(new State( "Aksel Møllers Have"    , 340,260   ,345,265  ,    "M3"));
        Global.array_state.add(new State( "Amagerbro"             , 700,400	,707,409  ,    "M2"));
        Global.array_state.add(new State( "Amager Strand"         , 790,490	,795,497  ,    "M2"));
        Global.array_state.add(new State( "Bella Center"          , 660,570	,663,582  ,    "M1"));
        Global.array_state.add(new State( "Christianshavn"        , 670,370	,670,383  ,    "M1,M2"));
        Global.array_state.add(new State( "DR Byen"               , 660,490	,663,498  ,    "M1"));
        Global.array_state.add(new State( "Enghave Plads"         , 410,410	,420,416  ,    "M3"));
        Global.array_state.add(new State( "Enghave Brygge (G)"    , 340,530	,350,536  ,    "M4"));
        Global.array_state.add(new State( "Fasanvej"              , 250,320	,261,339  ,    "M1,M2"));
        Global.array_state.add(new State( "Flintholm"             , 130,320	,140,339  ,    "M1,M2"));
        Global.array_state.add(new State( "Femøren"               , 820,520	,824,526  ,    "M2"));
        Global.array_state.add(new State( "Forum"                 , 440,320	,448,339  ,    "M1,M2"));
        Global.array_state.add(new State( "Frederiksberg"         , 330,320	,353,339  ,    "M1,M2,M3"));
        Global.array_state.add(new State( "Frederiksberg Allé"    , 370,380	,379,383  ,    "M3"));
        Global.array_state.add(new State( "Gammel Strand"         , 560,400	,575,417  ,    "M3,M4"));
        Global.array_state.add(new State( "Havneholmen (G)"       , 370,500	,381,506  ,    "M4"));
        Global.array_state.add(new State( "Islands Brygge"        , 660,450	,663,456  ,    "M1"));
        Global.array_state.add(new State( "Kastrup"               , 850,550	,854,555  ,    "M2"));
        Global.array_state.add(new State( "Kongens Nytorv"        , 600,320	,635,349  ,    "M1,M2,M3"));
        Global.array_state.add(new State( "København H"           , 460,420	,469,440  ,    "M3,M4"));
        Global.array_state.add(new State( "Københavns Lufthavn"   , 880,580	,883,585  ,    "M2"));
        Global.array_state.add(new State( "Lergravsparken"        , 730,430	,736,438  ,    "M2"));
        Global.array_state.add(new State( "Lindevang"             , 190,320	,200,338  ,    "M1,M2"));
        Global.array_state.add(new State( "Marmorkirken"          , 630,270	,646,273  ,    "M3,M4"));
        Global.array_state.add(new State( "Mozarts Plads (G)"     , 280,590	,290,596  ,    "M4"));
        Global.array_state.add(new State( "Nordhavn"              , 730,90	,737,94   ,    "M4"));
        Global.array_state.add(new State( "Nuuks Plads"           , 350,210	,359,219  ,    "M3"));
        Global.array_state.add(new State( "Ny Ellebjerg (G)"      , 250,620	,261,626  ,    "M4"));
        Global.array_state.add(new State( "Nørreport"             , 530,320	,537,339  ,    "M1,M2"));
        Global.array_state.add(new State( "Nørrebros Rundddel"    , 380,170	,388,182  ,    "M3"));
        Global.array_state.add(new State( "Nørrebro"              , 420,150	,426,154  ,    "M3"));
        Global.array_state.add(new State( "Orientkaj"             , 760,60	,766,64   ,    "M4"));
        Global.array_state.add(new State( "Poul Henningsens Plads", 560,150	,562,158  ,    "M3"));
        Global.array_state.add(new State( "Rådhuspladsen"         , 510,420	,524,439  ,    "M3,M4"));
        Global.array_state.add(new State( "Skjold Plads"          , 460,130	,471,141  ,    "M3"));
        Global.array_state.add(new State( "Sluseholmen (G)"       , 310,560	,320,566  ,    "M3"));
        Global.array_state.add(new State( "Sundby"                , 660,530	,663,540  ,    "M1"));
        Global.array_state.add(new State( "Trianglen"             , 590,180	,599,188  ,    "M3"));
        Global.array_state.add(new State( "Vanløse"               , 70,320	,79,339   ,    "M1,M2"));
        Global.array_state.add(new State( "Vestamager"            , 660,660	,663,667  ,    "M1"));
        Global.array_state.add(new State( "Vibenshus Runddel"     , 510,130	,519,142  ,    "M3"));
        Global.array_state.add(new State( "Ørestad"               , 660,620	,662,624  ,    "M1"));
        Global.array_state.add(new State( "Øresund"               , 760,460	,166,467  ,    "M2"));
        Global.array_state.add(new State( "Østerport"             , 620,220	,633,225  ,    "M3,M4"));



    }

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
                startActivity(intent);
                break;
            case R.id.btn_route:
                intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tourist:
                intent.putExtra("TYPE","Tourist");
                startActivity(intent);
                break;
        }
    }
}
