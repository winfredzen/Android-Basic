package nigelhenshaw.com.cameraintenttutorial;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

/**
 * create by wangzhen 2022/7/18
 */
public class SingleImageActivity extends Activity {

    public static final String IMAGE_FILE_LOCATION = "image_file_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                width, height
        );
        imageView.setLayoutParams(params);

        setContentView(imageView);

        File imageFile = new File(
                getIntent().getStringExtra(IMAGE_FILE_LOCATION)
        );

        SingleImageBitmapWorkerTask workerTask = new SingleImageBitmapWorkerTask(imageView, width, height);
        workerTask.execute(imageFile);

    }

}
