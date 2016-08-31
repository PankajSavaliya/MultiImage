package socialinfotech.multiimage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sw926.imagefileselector.ImageFileSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddStatusActivity extends AppCompatActivity {

    @InjectView(R.id.list)
    RecyclerView mRecycler;
    api git;

    //recylcer view...................
    private RecyclerView.LayoutManager mLayoutManager;
    private AddAdapter mAdapter;
    private ProgressDialog progress;

    //image upload......................
    private ArrayList<String> uplaoded_image;
    private ArrayList<UploadInfo> uplaod_image;
    private int position;


    private final static String TAG = "ICA";
    private ImageFileSelector mImageFileSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        set_Recycle();
    }

    private void set_Recycle() {
        mAdapter = new AddAdapter(AddStatusActivity.this);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
        initImage();
    }

    private void initImage() {
        mImageFileSelector = new ImageFileSelector(AddStatusActivity.this);
        mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(final String file) {
                if (!TextUtils.isEmpty(file)) {
                    mAdapter.add(new UploadInfo(new File(file), new File(file)));
                } else {
                    Toast.makeText(AddStatusActivity.this, "select image file error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(AddStatusActivity.this, "select image file error", Toast.LENGTH_LONG).show();
            }
        });
        mImageFileSelector.setOutPutImageSize(500, 500);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_upload) {
            Upload();
            return true;
        } else if (id == R.id.action_add) {
            PickerDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Upload() {
        uplaod_image = mAdapter.getData();
        if (uplaod_image.size() > 0) {
            uplaoded_image = new ArrayList<String>();
            new SendTask().execute();

        } else {
            Toast.makeText(AddStatusActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
        }
    }


    //upload image...................................................................................
    public class SendTask extends AsyncTask<String, Integer, Observable<image>> {
        private File filePath;
        private UploadInfo mInfo;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (uplaoded_image.size() == 0) {
                position = 0;
                Show_image();
            }
            runOnUiThread(changeMessage);
            mInfo = uplaod_image.get(position);
            filePath = mInfo.getFilename();

        }

        @Override
        protected rx.Observable<image> doInBackground(String... params) {
            mInfo.setUploadState(UploadInfo.UplaodState.DOWNLOADING);
            ProgressedTypedFile.Listener listener = new ProgressedTypedFile.Listener() {
                @Override
                public void onUpdateProgress(int percentage) {
                    publishProgress(percentage);
                }
            };

            ProgressedTypedFile typedFile = new ProgressedTypedFile("image/jpeg", filePath, listener);
            return MyRestAdapter.getInstance().getImageName(typedFile);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(rx.Observable<image> imageObservable) {
            super.onPostExecute(imageObservable);
            //Log.e("result", imageObservable. + "pankaj");

            imageObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<image>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mInfo.setUploadState(UploadInfo.UplaodState.RETRY);
                            Hide_image();
                        }

                        @Override
                        public void onNext(image image) {

                            uplaoded_image.add(makeImageArray(image.getImage_name()));
                            mInfo.setUploadState(UploadInfo.UplaodState.COMPLETE);
                            if (uplaoded_image.size() < uplaod_image.size()) {
                                position++;
                                new SendTask().execute();
                            } else {
                                Hide_image();
                                //
                                Log.e("all image uploaded", uplaoded_image + "");
                            }
                        }
                    });


        }
    }


    public void Show_image() {
        progress = new ProgressDialog(this);
        runOnUiThread(changeMessage);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.show();
    }

    private void Hide_image() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }


    private Runnable changeMessage = new Runnable() {
        @Override
        public void run() {
            int i = position + 1;
            progress.setMessage(getString(R.string.uploading, i, uplaod_image.size()));
        }
    };


    private void PickerDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddStatusActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    mImageFileSelector.takePhoto(AddStatusActivity.this);
                } else if (items[item].equals("Choose from Library")) {
                    mImageFileSelector.selectImage(AddStatusActivity.this);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageFileSelector.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImageFileSelector.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageFileSelector.onRestoreInstanceState(savedInstanceState);

    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static String makeImageArray(image.ImageNameEntity image_name) {
        JSONObject array = new JSONObject();
        try {
            array.put("original", image_name.getOriginal());
            array.put("large", image_name.getLarge());
            array.put("mini", image_name.getMini());
            array.put("image_info", image_name.getImage_info());
            array.put("thumbnail", image_name.getThumbnail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return array.toString();
    }
}
