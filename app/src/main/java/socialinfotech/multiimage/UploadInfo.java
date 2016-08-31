package socialinfotech.multiimage;

/**
 * Created by pankaj on 31/08/16.
 */

import android.widget.ProgressBar;

import java.io.File;



import android.widget.ProgressBar;


import java.io.File;


public class UploadInfo {
    private final static String TAG = UploadInfo.class.getSimpleName();


    public enum UplaodState {
        NOT_STARTED,
        QUEUED,
        DOWNLOADING,
        COMPLETE, CANCEL, RETRY
    }

    private volatile UplaodState mDownloadState = UplaodState.NOT_STARTED;
    private File mFilename;
    private File mThumbnile;
    private volatile Integer mProgress;
    private volatile ProgressBar mProgressBar;


    public UploadInfo(File thumb,File filename) {
        this.mFilename = filename;
        this.mThumbnile=thumb;
        this.mProgress = 0;
        this.mProgressBar = null;

    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public void setUploadState(UplaodState state) {
        mDownloadState = state;
    }

    public UplaodState getUploadState() {
        return mDownloadState;
    }

    public Integer getProgress() {
        return mProgress;
    }

    public void setProgress(Integer progress) {
        this.mProgress = progress;
    }


    public File getFilename() {
        return mFilename;
    }

    public File getmThumbnile() {
        return mThumbnile;
    }


}