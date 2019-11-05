package social.media.railwaystation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements OnClickableAreaClickedListener {
    float x = 0;
    float y = 0;
    ImageView myImageView;
    Bitmap myBitmap;
    Paint myRectPaint = new Paint();
    int x1 = 0;
    int y1 = 0;
    int x2 = 30;
    int y2 = 30;
    int[] viewCoords = new int[2];

    RelativeLayout layout;
    private static final float GESTURE_THRESHOLD_DP = 16.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add image
        myImageView = (ImageView) findViewById(R.id.img_map);
        myImageView.setImageResource(R.drawable.map);
        layout = (RelativeLayout)findViewById(R.id.main_view);
//        layout.addView(new CustomView(MainActivity.this));
        // Create your image

        myImageView.getLocationOnScreen(viewCoords);


        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(myImageView);
        myImageView.setOnTouchListener(handleTouch);
        photoViewAttacher.setMinimumScale(1.5f);
        photoViewAttacher.setScale(1.9f, 592.9f, 405.9f, true);
//        photoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//            @Override
//            public void onViewTap(View view, float x, float y) {
//                Toast.makeText(MainActivity.this,x+ " : " + y, Toast.LENGTH_LONG).show();
//            }
//        });
//        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//                Toast.makeText(MainActivity.this,x+ " : " + y, Toast.LENGTH_LONG).show();
//            }
//        });
//        photoViewAttacher.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
//            @Override
//            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
//                Log.e("Scale", String.valueOf(scaleFactor) + ": " + focusX + " :" + focusY);
//            }
//        });

        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(photoViewAttacher, this);
        // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);
    }

    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof State) {
            String text = ((State) item).getName();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            /*
            ShapeDrawable sd = new ShapeDrawable(new OvalShape());
            sd.setIntrinsicHeight(100);
            sd.setIntrinsicWidth(100);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sd.getPaint().setColor(getColor(R.color.colorText_a));
            }
            ImageView iv = (ImageView) findViewById(R.id.img_map);
            iv.setBackground(sd);

             */
/*
            BitmapFactory.Options myOptions = new BitmapFactory.Options();
            myOptions.inDither = true;
            myOptions.inScaled = false;
            myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
            myOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ico_go,myOptions);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);


            Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


            Canvas canvas = new Canvas(mutableBitmap);
            canvas.drawCircle(60, 50, 25, paint);

            ImageView imageView = (ImageView)findViewById(R.id.img_map);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(mutableBitmap);

 */

            myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);
            Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempCanvas = new Canvas(tempBitmap);

//Draw the image bitmap into the cavas
            tempCanvas.drawBitmap(myBitmap, 0, 0, null);

//Draw everything else you want into the canvas, in this example a rectangle with rounded edges
//            tempCanvas.drawRoundRect(new RectF(x1,y1,x2,y2), 2, 2, new Paint());
            x = 200;
            y = 870;
            float pix_x = convertDpToPixel(x,MainActivity.this);
            float pix_y = convertDpToPixel(y,MainActivity.this);
//            float dp_x = convertPixelsToDp(500,MainActivity.this);
//            float dp_y = convertPixelsToDp(2175,MainActivity.this);
            Toast.makeText(MainActivity.this,pix_x + ":" + pix_y,Toast.LENGTH_SHORT).show();
            Paint paint;
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            tempCanvas.drawCircle(pix_x, pix_y, 20, paint);


//Attach the canvas to the ImageView
            myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        }
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @NonNull
    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();

        clickableAreas.add(new ClickableArea(0, 0, 10, 10, new State("Lower Austria")));
        clickableAreas.add(new ClickableArea(200, 870, 50, 50, new State("G-1")));
        clickableAreas.add(new ClickableArea(700, 126, 50, 50, new State("Vienna")));

        clickableAreas.add(new ClickableArea(685, 270, 50, 50, new State("Burgenland")));
        clickableAreas.add(new ClickableArea(420, 350, 50, 50, new State("Carinthia")));
        clickableAreas.add(new ClickableArea(370, 245, 50, 50, new State("Salzburg")));

        clickableAreas.add(new ClickableArea(170, 280, 50, 50, new State("Tyrol")));
        clickableAreas.add(new ClickableArea(30, 280, 50, 50, new State("Vorarlberg")));
        clickableAreas.add(new ClickableArea(570, 250, 50, 50, new State("Styria")));

        return clickableAreas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public class CustomView extends View {

        Bitmap mBitmap;
        Paint paint;

        public CustomView(Context context) {
            super(context);
            mBitmap = Bitmap.createBitmap(400, 800, Bitmap.Config.ARGB_8888);
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(x, y, 50, paint);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x = event.getX();
                y = event.getY();
                invalidate();
            }
            return false;
        }
    }


    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {





            int x = (int) event.getX();
            int y = (int) event.getY();
            int imageX = x - viewCoords[0]; // viewCoords[0] is the X coordinate
            int imageY = y - viewCoords[1];

            Log.e("x:",String.valueOf(imageX));
            Log.e("Y ",String.valueOf(imageY));

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Toast.makeText(MainActivity.this,x + ":down:" + y, Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "touched down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Toast.makeText(MainActivity.this,x + ":Move:" + y, Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    break;
                case MotionEvent.ACTION_UP:
//                    Toast.makeText(MainActivity.this,x + ":up:" + y, Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "touched up");
                    break;
            }

            return true;
        }
    };



}