package com.kaiman.sports.utils;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

/**
 * Created by yqritc on 2015/06/11
 */
public class ScalableVideoView extends TextureView implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnVideoSizeChangedListener {

    public void playVideo(@RawRes int resId) {
        try {
            this.setRawData(resId);
            this.setLooping(true);
            this.prepare(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    start();
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected MediaPlayer mMediaPlayer;
    protected ImageView.ScaleType mScalableType = ImageView.ScaleType.CENTER_CROP;

    public ScalableVideoView(Context context) {
        this(context, null);
    }

    public ScalableVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs == null) {
            return;
        }

        int[] set = {
                android.R.attr.scaleType, // idx 0
        };

        TypedArray a = context.obtainStyledAttributes(attrs, set);
        if (a == null) {
            return;
        }

        int scaleType = a.getInt(0, ImageView.ScaleType.CENTER_CROP.ordinal());
        a.recycle();
        mScalableType =  ImageView.ScaleType.values()[scaleType];
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mMediaPlayer == null) {
            return;
        }

        if (isPlaying()) {
            stop();
        }
        release();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        scaleVideoSize(width, height);
    }

    private void scaleVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth == 0 || videoHeight == 0) {
            return;
        }

        Size viewSize = new Size(getWidth(), getHeight());
        Size videoSize = new Size(videoWidth, videoHeight);
        ScaleManager scaleManager = new ScaleManager(viewSize, videoSize);
        Matrix matrix = scaleManager.getScaleMatrix(mScalableType);
        if (matrix != null) {
            setTransform(matrix);
        }
    }

    private void initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            setSurfaceTextureListener(this);
        } else {
            reset();
        }
    }

    public void setRawData(@RawRes int id) throws IOException {
        AssetFileDescriptor afd = getResources().openRawResourceFd(id);
        setDataSource(afd);
    }

    public void setAssetData(@NonNull String assetName) throws IOException {
        AssetManager manager = getContext().getAssets();
        AssetFileDescriptor afd = manager.openFd(assetName);
        setDataSource(afd);
    }

    private void setDataSource(@NonNull AssetFileDescriptor afd) throws IOException {
        setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
    }

    public void setDataSource(@NonNull String path) throws IOException {
        initializeMediaPlayer();
        mMediaPlayer.setDataSource(path);
    }

    public void setDataSource(@NonNull Context context, @NonNull Uri uri,
                              @Nullable Map<String, String> headers) throws IOException {
        initializeMediaPlayer();
        mMediaPlayer.setDataSource(context, uri, headers);
    }

    public void setDataSource(@NonNull Context context, @NonNull Uri uri) throws IOException {
        initializeMediaPlayer();
        mMediaPlayer.setDataSource(context, uri);
    }

    public void setDataSource(@NonNull FileDescriptor fd, long offset, long length)
            throws IOException {
        initializeMediaPlayer();
        mMediaPlayer.setDataSource(fd, offset, length);
    }

    public void setDataSource(@NonNull FileDescriptor fd) throws IOException {
        initializeMediaPlayer();
        mMediaPlayer.setDataSource(fd);
    }

    public void setScalableType(ImageView.ScaleType scalableType) {
        mScalableType = scalableType;
        scaleVideoSize(getVideoWidth(), getVideoHeight());
    }

    public void prepare(@Nullable MediaPlayer.OnPreparedListener listener)
            throws IOException, IllegalStateException {
        mMediaPlayer.setOnPreparedListener(listener);
        mMediaPlayer.prepare();
    }

    public void prepareAsync(@Nullable MediaPlayer.OnPreparedListener listener)
            throws IllegalStateException {
        mMediaPlayer.setOnPreparedListener(listener);
        mMediaPlayer.prepareAsync();
    }

    public void prepare() throws IOException, IllegalStateException {
        prepare(null);
    }

    public void prepareAsync() throws IllegalStateException {
        prepareAsync(null);
    }

    public void setOnErrorListener(@Nullable MediaPlayer.OnErrorListener listener) {
        mMediaPlayer.setOnErrorListener(listener);
    }

    public void setOnCompletionListener(@Nullable MediaPlayer.OnCompletionListener listener) {
        mMediaPlayer.setOnCompletionListener(listener);
    }

    public void setOnInfoListener(@Nullable MediaPlayer.OnInfoListener listener) {
        mMediaPlayer.setOnInfoListener(listener);
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void stop() {
        mMediaPlayer.stop();
    }

    public void reset() {
        mMediaPlayer.reset();
    }

    public void release() {
        reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public class ScaleManager {

        private Size mViewSize;
        private Size mVideoSize;

        public ScaleManager(Size viewSize, Size videoSize) {
            mViewSize = viewSize;
            mVideoSize = videoSize;
        }

        public Matrix getScaleMatrix(ImageView.ScaleType scalableType) {
            switch (scalableType) {
                case FIT_XY:
                    return fitXY();
                case FIT_CENTER:
                    return fitCenter();
                case FIT_START:
                    return fitStart();
                case FIT_END:
                    return fitEnd();
                case CENTER:
                    return getOriginalScale(PivotPoint.CENTER);
                case CENTER_CROP:
                    return getCropScale(PivotPoint.CENTER);
                case CENTER_INSIDE:
                    return centerInside();
                default:
                    return null;
            }
        }

        private Matrix getMatrix(float sx, float sy, float px, float py) {
            Matrix matrix = new Matrix();
            matrix.setScale(sx, sy, px, py);
            return matrix;
        }

        private Matrix getMatrix(float sx, float sy, PivotPoint pivotPoint) {
            switch (pivotPoint) {
                case LEFT_TOP:
                    return getMatrix(sx, sy, 0, 0);
                case LEFT_CENTER:
                    return getMatrix(sx, sy, 0, mViewSize.getHeight() / 2f);
                case LEFT_BOTTOM:
                    return getMatrix(sx, sy, 0, mViewSize.getHeight());
                case CENTER_TOP:
                    return getMatrix(sx, sy, mViewSize.getWidth() / 2f, 0);
                case CENTER:
                    return getMatrix(sx, sy, mViewSize.getWidth() / 2f, mViewSize.getHeight() / 2f);
                case CENTER_BOTTOM:
                    return getMatrix(sx, sy, mViewSize.getWidth() / 2f, mViewSize.getHeight());
                case RIGHT_TOP:
                    return getMatrix(sx, sy, mViewSize.getWidth(), 0);
                case RIGHT_CENTER:
                    return getMatrix(sx, sy, mViewSize.getWidth(), mViewSize.getHeight() / 2f);
                case RIGHT_BOTTOM:
                    return getMatrix(sx, sy, mViewSize.getWidth(), mViewSize.getHeight());
                default:
                    throw new IllegalArgumentException("Illegal PivotPoint");
            }
        }

        private Matrix getNoScale() {
            float sx = mVideoSize.getWidth() / (float) mViewSize.getWidth();
            float sy = mVideoSize.getHeight() / (float) mViewSize.getHeight();
            return getMatrix(sx, sy, PivotPoint.LEFT_TOP);
        }

        private Matrix getFitScale(PivotPoint pivotPoint) {
            float sx = (float) mViewSize.getWidth() / mVideoSize.getWidth();
            float sy = (float) mViewSize.getHeight() / mVideoSize.getHeight();
            float minScale = Math.min(sx, sy);
            sx = minScale / sx;
            sy = minScale / sy;
            return getMatrix(sx, sy, pivotPoint);
        }

        private Matrix fitXY() {
            return getMatrix(1, 1, PivotPoint.LEFT_TOP);
        }

        private Matrix fitStart() {
            return getFitScale(PivotPoint.LEFT_TOP);
        }

        private Matrix fitCenter() {
            return getFitScale(PivotPoint.CENTER);
        }

        private Matrix fitEnd() {
            return getFitScale(PivotPoint.RIGHT_BOTTOM);
        }

        private Matrix getOriginalScale(PivotPoint pivotPoint) {
            float sx = mVideoSize.getWidth() / (float) mViewSize.getWidth();
            float sy = mVideoSize.getHeight() / (float) mViewSize.getHeight();
            return getMatrix(sx, sy, pivotPoint);
        }

        private Matrix getCropScale(PivotPoint pivotPoint) {
            float sx = (float) mViewSize.getWidth() / mVideoSize.getWidth();
            float sy = (float) mViewSize.getHeight() / mVideoSize.getHeight();
            float maxScale = Math.max(sx, sy);
            sx = maxScale / sx;
            sy = maxScale / sy;
            return getMatrix(sx, sy, pivotPoint);
        }

        private Matrix startInside() {
            if (mVideoSize.getHeight() <= mViewSize.getWidth()
                    && mVideoSize.getHeight() <= mViewSize.getHeight()) {
                // video is smaller than view size
                return getOriginalScale(PivotPoint.LEFT_TOP);
            } else {
                // either of width or height of the video is larger than view size
                return fitStart();
            }
        }

        private Matrix centerInside() {
            if (mVideoSize.getHeight() <= mViewSize.getWidth()
                    && mVideoSize.getHeight() <= mViewSize.getHeight()) {
                // video is smaller than view size
                return getOriginalScale(PivotPoint.CENTER);
            } else {
                // either of width or height of the video is larger than view size
                return fitCenter();
            }
        }

        private Matrix endInside() {
            if (mVideoSize.getHeight() <= mViewSize.getWidth()
                    && mVideoSize.getHeight() <= mViewSize.getHeight()) {
                // video is smaller than view size
                return getOriginalScale(PivotPoint.RIGHT_BOTTOM);
            } else {
                // either of width or height of the video is larger than view size
                return fitEnd();
            }
        }
    }

    public enum PivotPoint {
        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM
    }

    public class Size {

        private int mWidth;
        private int mHeight;

        public Size(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }

}
