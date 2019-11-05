package at.lukle.clickableareas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements OnClickableAreaClickedListener {

    private final String TAG = getClass().getSimpleName();
    PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add image
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.high_resolution_map);

        // Create your image

        photoViewAttacher = new PhotoViewAttacher(image);
        photoViewAttacher.setMinimumScale(1.3f);
        photoViewAttacher.setScale(2.5f, 592.9f, 405.9f, true);
        // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(photoViewAttacher, this);
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);
    }

    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof State) {
            String text = ((State) item).getName();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();

        clickableAreas.add(new ClickableArea(76, 345, 10, 10, new State("G-1")));
        clickableAreas.add(new ClickableArea(136, 345, 10, 10, new State("G-2")));
        clickableAreas.add(new ClickableArea(198, 345, 10, 10, new State("G-3")));
        clickableAreas.add(new ClickableArea(258, 345, 10, 10, new State("G-4")));
        clickableAreas.add(new ClickableArea(446, 345, 10, 10, new State("G-5")));
        clickableAreas.add(new ClickableArea(534, 345, 10, 10, new State("G-6")));
        clickableAreas.add(new ClickableArea(660, 384, 10, 10, new State("G-7")));
        clickableAreas.add(new ClickableArea(660, 453, 10, 10, new State("G-8")));
        clickableAreas.add(new ClickableArea(660, 495, 10, 10, new State("G-9")));
        clickableAreas.add(new ClickableArea(660, 537, 10, 10, new State("G-10")));
        clickableAreas.add(new ClickableArea(660, 578, 10, 10, new State("G-11")));
        clickableAreas.add(new ClickableArea(660, 621, 10, 10, new State("G-12")));
        clickableAreas.add(new ClickableArea(660, 663, 10, 10, new State("G-13")));

        clickableAreas.add(new ClickableArea(334, 321, 40, 40, new State("W-1")));
        clickableAreas.add(new ClickableArea(607, 321, 55, 55, new State("W-2")));

        clickableAreas.add(new ClickableArea(76, 327, 10, 10, new State("Y-1")));
        clickableAreas.add(new ClickableArea(136, 327, 10, 10, new State("Y-2")));
        clickableAreas.add(new ClickableArea(198, 327, 10, 10, new State("Y-3")));
        clickableAreas.add(new ClickableArea(258, 327, 10, 10, new State("Y-4")));
        clickableAreas.add(new ClickableArea(446, 327, 10, 10, new State("Y-5")));
        clickableAreas.add(new ClickableArea(534, 327, 10, 10, new State("Y-6")));
        clickableAreas.add(new ClickableArea(674, 375, 10, 10, new State("Y-7")));
        clickableAreas.add(new ClickableArea(703, 405, 10, 10, new State("Y-8")));
        clickableAreas.add(new ClickableArea(733, 435, 10, 10, new State("Y-9")));
        clickableAreas.add(new ClickableArea(762, 464, 10, 10, new State("Y-10")));
        clickableAreas.add(new ClickableArea(792, 493, 10, 10, new State("Y-11")));
        clickableAreas.add(new ClickableArea(821, 523, 10, 10, new State("Y-12")));
        clickableAreas.add(new ClickableArea(850, 552, 10, 10, new State("Y-13")));
        clickableAreas.add(new ClickableArea(880, 582, 10, 10, new State("Y-14")));

        clickableAreas.add(new ClickableArea(763, 61, 10, 10, new State("B-1")));
        clickableAreas.add(new ClickableArea(734, 91, 10, 10, new State("B-2")));
        clickableAreas.add(new ClickableArea(637, 218, 10, 10, new State("B-3")));
        clickableAreas.add(new ClickableArea(650, 269, 10, 10, new State("B-4")));
        clickableAreas.add(new ClickableArea(576, 421, 10, 10, new State("B-5")));
        clickableAreas.add(new ClickableArea(523, 443, 10, 10, new State("B-6")));
        clickableAreas.add(new ClickableArea(464, 446, 10, 10, new State("B-7")));

        clickableAreas.add(new ClickableArea(259, 621, 10, 10, new State("G-1")));
        clickableAreas.add(new ClickableArea(288, 592, 10, 10, new State("G-2")));
        clickableAreas.add(new ClickableArea(317, 562, 10, 10, new State("G-3")));
        clickableAreas.add(new ClickableArea(346, 532, 10, 10, new State("G-4")));
        clickableAreas.add(new ClickableArea(378, 501, 10, 10, new State("G-5")));
        
        clickableAreas.add(new ClickableArea(342, 262, 10, 10, new State("R-1")));
        clickableAreas.add(new ClickableArea(357, 216, 10, 10, new State("R-2")));
        clickableAreas.add(new ClickableArea(384, 179, 10, 10, new State("R-3")));
        clickableAreas.add(new ClickableArea(424, 151, 10, 10, new State("R-4")));
        clickableAreas.add(new ClickableArea(468, 138, 10, 10, new State("R-5")));
        clickableAreas.add(new ClickableArea(515, 139, 10, 10, new State("R-6")));
        clickableAreas.add(new ClickableArea(560, 155, 10, 10, new State("R-7")));
        clickableAreas.add(new ClickableArea(596, 184, 10, 10, new State("R-8")));
        clickableAreas.add(new ClickableArea(621, 224, 10, 10, new State("R-9")));
        clickableAreas.add(new ClickableArea(634, 270, 10, 10, new State("R-10")));
        clickableAreas.add(new ClickableArea(568, 406, 10, 10, new State("R-11")));
        clickableAreas.add(new ClickableArea(519, 428, 10, 10, new State("R-12")));
        clickableAreas.add(new ClickableArea(467, 429, 10, 10, new State("R-13")));
        clickableAreas.add(new ClickableArea(417, 412, 10, 10, new State("R-14")));
        clickableAreas.add(new ClickableArea(375, 380, 10, 10, new State("R-15")));
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
}
