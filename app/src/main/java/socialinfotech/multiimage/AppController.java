package socialinfotech.multiimage;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by pankaj on 31/08/16.
 */
public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(AppController.this);
    }
}
