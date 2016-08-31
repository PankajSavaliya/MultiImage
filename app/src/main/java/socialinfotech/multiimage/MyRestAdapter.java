package socialinfotech.multiimage;

/**
 * Created by pankaj on 31/08/16.
 */

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;


public class MyRestAdapter {

    private api myService;
    private static MyRestAdapter myRestAdapter;

    public MyRestAdapter() {
        myService = restAdapter().create(api.class);
    }

    public static synchronized MyRestAdapter getInstance() {
        if (myRestAdapter == null) {
            myRestAdapter = new MyRestAdapter();
        }
        return myRestAdapter;
    }

    public RestAdapter restAdapter() {

        RestAdapter restAdapter;

        if (BuildConfig.DEBUG) {
            restAdapter = new retrofit.RestAdapter.Builder().setEndpoint(api.API)
                    .setLogLevel(RestAdapter.LogLevel.BASIC).setClient(new OkClient(new OkHttpClient()))
                    .build();
        } else {
            restAdapter = new retrofit.RestAdapter.Builder().setEndpoint(api.API)
                    .setClient(new OkClient(new OkHttpClient()))
                    .build();
        }

        return restAdapter;
    }

    //file upload upload
    public Observable<image> getImageName(ProgressedTypedFile typedFile) {
        return myService.getImageName(typedFile);
    }


}
