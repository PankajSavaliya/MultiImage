package socialinfotech.multiimage;

import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import rx.Observable;

/**
 * Created by pankaj on 31/08/16.
 */
public interface api {
    String API="http://www.example/api/v1";

    @Multipart
    @POST("/upload")
    Observable<image> getImageName(@Part("mimage") ProgressedTypedFile attachments);
}
