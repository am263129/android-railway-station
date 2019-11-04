package social.media.railwaystation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add image
        ImageView image = (ImageView) findViewById(R.id.img_map);
        image.setImageResource(R.drawable.map);

        // Create your image

        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(image);
        photoViewAttacher.setScale(0.99f);
        photoViewAttacher.
        photoViewAttacher.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                Log.e("Scale",String.valueOf(scaleFactor)+": "+focusX + " :"+ focusY);
            }
        });
        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(photoViewAttacher, this);
        // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);
    }

    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof Station) {
            String text = ((Station) item).getName();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();

        clickableAreas.add(new ClickableArea(600, 100, 500, 500, new Station("Lower Austria")));
        clickableAreas.add(new ClickableArea(440, 125, 500, 500, new Station("Upper Austria")));
        clickableAreas.add(new ClickableArea(700, 126, 500, 500, new Station("Vienna")));

        clickableAreas.add(new ClickableArea(685, 270, 500, 500, new Station("Burgenland")));
        clickableAreas.add(new ClickableArea(420, 350, 500, 500, new Station("Carinthia")));
        clickableAreas.add(new ClickableArea(370, 245, 500, 500, new Station("Salzburg")));

        clickableAreas.add(new ClickableArea(170, 280, 500, 500, new Station("Tyrol")));
        clickableAreas.add(new ClickableArea(30, 280, 500, 500, new Station("Vorarlberg")));
        clickableAreas.add(new ClickableArea(570, 250, 500, 500, new Station("Styria")));

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
