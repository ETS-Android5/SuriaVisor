package uci.suriavisor.presentation.playerlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import uci.suriavisor.presentation.CustomViews.IPlayerActivity;
import com.xilema.suriavisor.R;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import uci.suriavisor.presentation.util.L;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

public class PlayerView extends FrameLayout implements IVLCVout.Callback, IVLCVout.OnNewVideoLayoutListener
{

    public int widthCustom, heightCustom;
    public boolean isMute = false;

    public void onConfigurationChanged(Configuration newConfig)
    {
        //throw new RuntimeException("Stub!");
        super.onConfigurationChanged(newConfig);
    }

    private class LocalBinder extends Binder
    {
        PlayerView getService()
        {
            return PlayerView.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();

    /*
    @Override
    public void onHardwareAccelerationError(IVLCVout vlcVout)
    {}*/

    @Override
    public void onSurfacesCreated(IVLCVout vout)
    {
        Log.e("onSurfacesCreated", "cuando se ejecuta esto.");
    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vout)
    {
    }

    @Override
    public void onNewVideoLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen)
    {
        if (width * height == 0)
            return;

        // store video size
        mVideoWidth = width;
        mVideoHeight = height;
        Log.e("SIZESSSS",String.valueOf(width)+","+String.valueOf(height)+","+String.valueOf(visibleWidth)+","+String.valueOf(visibleHeight));
        //volver a poner esto para que se redimension.
        //setSize(mVideoWidth, mVideoHeight);
        //quitar esto
        // int tempVideoHeight =  mVideoHeight/2;
        setSize(mVideoWidth, mVideoHeight);
    }

    /*************
     * Surface
     *************/
    public void setSize(int width, int height)
    {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        if(mSurfaceHolder == null || mSurface == null)
            return;

        // get screen size
        int w = 500;
        int h = 250;

        int w2 = 500;
        int h2 = 250;

        if(getContext() instanceof  Activity)
        {
            w = ((Activity) getContext()).getWindow().getDecorView().getWidth();
            h = ((Activity) getContext()).getWindow().getDecorView().getHeight();

            w2 = ((Activity) getContext()).getWindow().getDecorView().getWidth();
            h2 = ((Activity) getContext()).getWindow().getDecorView().getHeight();
        }

        // getWindow().getDecorView() doesn't always take orientation into
        // account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait)
        {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        ViewGroup.LayoutParams lp = mSurface.getLayoutParams();
        /*
        lp.width = w;
        lp.height = h;
        */
        //quitar esto
        if(widthCustom> 0 && heightCustom> 0)
        {
            lp.width = w2/widthCustom;
            lp.height = h2/heightCustom;
        }
        else
        {
            lp.width = w;
            lp.height = h;
        }
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();
    }

    private static final String TAG = "PlayerView";

    public interface OnChangeListener {

        public void onBufferChanged(float buffer);

        public void onLoadComplet();

        public void onError();

        public void onEnd();
    }

    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_HORIZONTAL = 1;
    private static final int SURFACE_FIT_VERTICAL = 2;
    private static final int SURFACE_FILL = 3;
    private static final int SURFACE_16_9 = 4;
    private static final int SURFACE_4_3 = 5;
    private static final int SURFACE_ORIGINAL = 6;
    private int mCurrentSize = SURFACE_BEST_FIT;

    public LibVLC getLibVLC()
    {
        return mLibVLC;
    }

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer = null;
    private Media media;

    // Whether fallback from HW acceleration to SW decoding was done.
    private boolean mDisabledHardwareAcceleration = false;
    private int mPreviousHardwareAccelerationMode;

    public SurfaceView getmSurface()
    {
        return mSurface;
    }

    private SurfaceView mSurface;
    //private SurfaceView mSubtitlesSurface;

    private SurfaceHolder mSurfaceHolder;
    //private SurfaceHolder mSubtitlesSurfaceHolder;

    private FrameLayout mSurfaceFrame;

    // size of the video
    private int mVideoHeight;
    private int mVideoWidth;
    private int mVideoVisibleHeight;
    private int mVideoVisibleWidth;
    private int mSarNum;
    private int mSarDen;
    private Handler mHandler;
    private OnChangeListener mOnChangeListener;
    private boolean mCanSeek = false;


    public String getUrl()
    {
        return url;
    }

    private String url;

    public PlayerView(Context context) {
        super(context);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IVLCVout getVLCVout()
    {
        return mMediaPlayer.getVLCVout();
    }

    public void releasePlayer()
    {
        if (mLibVLC == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        mSurfaceHolder = null;
        mLibVLC.release();
        mLibVLC = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    private static class MyPlayerListener implements MediaPlayer.EventListener
    {
        private WeakReference<PlayerView> mOwner;

        public MyPlayerListener(PlayerView owner)
        {
            mOwner = new WeakReference<PlayerView>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event)
        {
            PlayerView player = mOwner.get();
            switch(event.type)
            {
                case MediaPlayer.Event.EndReached:
                {
                    //Log.d(TAG, "MediaPlayerEndReached");
                    player.releasePlayer();
                    ((IPlayerActivity) player.getContext()).showMessage("Inténtelo más tarde.");
                    //((Activity) player.getContext()).finish();
                    break;
                }
                case MediaPlayer.Event.Playing:
                {
                    ((IPlayerActivity) player.getContext()).hideLoading();
                    //quitar esto e implementar lo de coger el tama;o del flujo
                    //Log.e("MediaPlayer.Event", "MediaPlayer.Event.Playing +++ "+String.valueOf(event.getBuffering()));
                    player.setSize(640,480);

                    // Configuracion de las camaras externas
                    //player.setSize(320,240);
                }
                case MediaPlayer.Event.Paused:
                {

                }
                case MediaPlayer.Event.Stopped:
                {
                    //player.play();
                    Log.e("Event.Stopped", "MediaPlayer.Event.Stopped +++ "+String.valueOf(event.getBuffering()));
                    //player.start();
                }
                case MediaPlayer.Event.Buffering:
                {
                    //Log.e("MediaPlayer.Buffering", "MediaPlayer.Event.Buffering +++ "+String.valueOf(event.getBuffering()));
                }
                case MediaPlayer.Event.EncounteredError:
                {
                    //Log.e("EncounteredError", "MediaPlayer.Event.EncounteredError +++ "+String.valueOf(event.getBuffering()));
                }
                default:
                    break;
            }
        }
    }

    public void initPlayer(String url)
    {
        /*
        try
        {
            mLibVLC.init(getContext().getApplicationContext());
        } catch (LibVlcException e) {
            throw new RuntimeException("PlayerView Init Failed");
        }
        mLibVLC.getMediaList().clear();
        mLibVLC.getMediaList().add(url);
        this.url = url;*/
        try
        {
            //mLibVLC.init(getContext().getApplicationContext());
            // Create LibVLC
            // TODO: make this more robust, and sync with audio demo
            ArrayList<String> options = new ArrayList<String>();
            //options.add("--subsdec-encoding <encoding>");

            //options.add("--no-drop-late-frames");
            //options.add("--no-skip-frames");

            options.add("--aout=opensles");
            options.add("--rtsp-tcp");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            //options.add("--rtsp-user=visor");
            //options.add("--rtsp-pwd=ANhjXvZV1Eg=");
            //options.add("--rtsp-port=1731");
            //ArrayList<String> options = {"--sout-rtsp-user", "visor", "--sout-rtsp-pwd", "ANhjXvZV1Eg="};
            /*
                --sout-rtsp-user <username>
                --sout-rtsp-pwd <password>
            */
            //Log.e("array",options.toString());
            mDisabledHardwareAcceleration = false;
            //mLibVLC = new LibVLC(options);

            //options.add("--file-caching=2000");
            //options.add("--loop");
            //options.add("--network-caching=5000");
            //options.add("--clock-jitter=0");
            //options.add("--live-caching=5000");
            //options.add("--clock-synchro=0");
            //options.add("--drop-late-frames");
            //options.add("--skip-frames");
            //options.add("--repeat");
            //options.add("--sout-mux-caching=5000");
            //options.add("--sout-ts-dts-delay 4000");

            mLibVLC = new LibVLC(getContext(),options);
        }
        catch (Exception e)
        {
            throw new RuntimeException("PlayerView Init Failed");
        }

        // Create media player
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mMediaPlayer.setEventListener(mPlayerListener);

        // Set up video output
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.setVideoView(mSurface);
        vout.addCallback(this);
        vout.attachViews();

        media = new Media(mLibVLC, Uri.parse(url));
        //media = new Media(mLibVLC, Uri.parse("rtsp://10.54.13.19:1731/Lab_304"));
        //media = new Media(mLibVLC, Uri.parse("rtsp://10.54.13.11/p"));
        media.setHWDecoderEnabled(true, false);
        mMediaPlayer.setMedia(media);

        this.url = url;
    }

    private void init()
    {/*
        LayoutInflater.from(getContext()).inflate(R.layout.view_player, this);
        mHandler = new Handler();
        //video view
        mSurface = (SurfaceView) findViewById(R.id.player_surface);
        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mLibVLC.setVerboseMode(Constants.CONFIG.DEBUG);
        mSurfaceFrame = (FrameLayout) findViewById(R.id.player_surface_frame);*/
        LayoutInflater.from(getContext()).inflate(R.layout.view_player, this);
        mHandler = new Handler();

        //video view
        mSurface = (SurfaceView) findViewById(R.id.player_surface);
        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mSurfaceFrame = (FrameLayout) findViewById(R.id.player_surface_frame);
    }
/*
    private final Callback mSurfaceCallback = new Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (format == PixelFormat.RGBX_8888)
                L.d(TAG, "Pixel format is RGBX_8888");
            else if (format == PixelFormat.RGB_565)
                L.d(TAG, "Pixel format is RGB_565");
            else if (format == ImageFormat.YV12)
                L.d(TAG, "Pixel format is YV12");
            else
                L.d(TAG, "Pixel format is other/unknown");
            if (mLibVLC != null) {
                mLibVLC.attachSurface(holder.getSurface(), PlayerView.this);
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mLibVLC != null)
                mLibVLC.detachSurface();
        }
    };

    @SuppressWarnings("unused")
    private final Callback mSubtitlesSurfaceCallback = new Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mLibVLC != null)
                mLibVLC.attachSubtitlesSurface(holder.getSurface());
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mLibVLC != null)
                mLibVLC.detachSubtitlesSurface();
        }
    };

    @Override
    public void setSurfaceSize(int width, int height, int visible_width, int visible_height, int sar_num, int sar_den) {
        if (width * height == 0) {
            return;
        }
        // store video size
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisibleHeight = visible_height;
        mVideoVisibleWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                changeSurfaceSize();
            }
        });
    }*/

    public void setOnChangeListener(OnChangeListener listener) {
        mOnChangeListener = listener;
    }

    public void changeSurfaceSize()
    {
        int sw;
        int sh;
        // get screen size
        sw = getWidth();
        sh = getHeight();
        double dw = sw, dh = sh;
        boolean isPortrait;
        isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }
        // sanity check
        if (dw * dh == 0 || mVideoWidth * mVideoHeight == 0) {
            L.e(TAG, "Invalid surface size");
            return;
        }
        // compute the aspect ratio
        double ar, vw;
        if (mSarDen == mSarNum) {
			/* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth;
            ar = (double) mVideoVisibleWidth / (double) mVideoVisibleHeight;
        } else {
			/* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * (double) mSarNum / mSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;

        switch (mCurrentSize) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_FIT_HORIZONTAL:
                dh = dw / ar;
                break;
            case SURFACE_FIT_VERTICAL:
                dw = dh * ar;
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoVisibleHeight;
                dw = vw;
                break;
        }

        SurfaceView surface;
        //SurfaceView subtitlesSurface;
        SurfaceHolder surfaceHolder;
        //SurfaceHolder subtitlesSurfaceHolder;
        FrameLayout surfaceFrame;

        surface = mSurface;
        //subtitlesSurface = mSubtitlesSurface;
        surfaceHolder = mSurfaceHolder;
        //subtitlesSurfaceHolder = mSubtitlesSurfaceHolder;
        surfaceFrame = mSurfaceFrame;

        // force surface buffer size
        surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        //subtitlesSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        android.view.ViewGroup.LayoutParams lp = surface.getLayoutParams();
        lp.width = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
        surface.setLayoutParams(lp);
        //subtitlesSurface.setLayoutParams(lp);

        // set frame size (crop if necessary)
        lp = surfaceFrame.getLayoutParams();
        lp.width = (int) Math.floor(dw);
        lp.height = (int) Math.floor(dh);
        surfaceFrame.setLayoutParams(lp);

        surface.invalidate();

    }
/*
    private void handleHardwareAccelerationError()
    {
        mLibVLC.stop();
        mDisabledHardwareAcceleration = true;
        mPreviousHardwareAccelerationMode = mLibVLC.getHardwareAcceleration();
        mLibVLC.setHardwareAcceleration(LibVLC.HW_ACCELERATION_DISABLED);
        start();
    }*/

    public void start()
    {
        try
        {
            mMediaPlayer.play();
            mCanSeek = mMediaPlayer.isSeekable();
            mSurface.setKeepScreenOn(true);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Inténtelo más tarde.", Toast.LENGTH_SHORT).show();
            ((Activity)getContext()).finish();
        }
		/*
		 * WARNING: hack to avoid a crash in mediacodec on KitKat. Disable
		 * hardware acceleration if the media has a ts extension.
		 */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH)
        {
            String locationLC = url.toLowerCase(Locale.ENGLISH);
            if (locationLC.endsWith(".ts") || locationLC.endsWith(".tts") || locationLC.endsWith(".m2t") || locationLC.endsWith(".mts") || locationLC.endsWith(".m2ts"))
            {
                mDisabledHardwareAcceleration = true;
                media.setHWDecoderEnabled(false, true);
            }
        }
        if(!mDisabledHardwareAcceleration)
            media.setHWDecoderEnabled(true, false);
    }

    public void play()
    {
        try
        {
            mMediaPlayer.play();
            mCanSeek = mMediaPlayer.isSeekable();
            mSurface.setKeepScreenOn(true);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Inténtelo más tarde.", Toast.LENGTH_SHORT).show();
            ((Activity)getContext()).finish();
        }
    }

    public void pause()
    {
        if(mMediaPlayer !=null)
            mMediaPlayer.pause();
        mSurface.setKeepScreenOn(false);
    }

    public void stop()
    {
        if(mMediaPlayer !=null)
            mMediaPlayer.stop();
        mSurface.setKeepScreenOn(false);
    }

    public long getTime()
    {
        return mMediaPlayer.getTime();
    }

    public long getLength()
    {
        return mMediaPlayer.getLength();
    }

    public void setTime(long time)
    {
        mMediaPlayer.setTime(time);
    }
/*
    public void setNetWorkCache(int time)
    {
        mMediaPlayer.setNetworkCaching(time);
    }

    public String pathToUrl(String path)
    {
        return LibVLC.PathToURI(path);
    }*/

    public boolean canSeekable()
    {
        if(mMediaPlayer!=null)
        {
            mCanSeek = mMediaPlayer.isSeekable();
        }
        return mCanSeek;
    }

    public boolean isPlaying()
    {
        if(mMediaPlayer!=null)
        {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public static void startService(Context context)
    {
        context.startService(getServiceIntent(context));
    }

    private static Intent getServiceIntent(Context context)
    {
        return new Intent(context, PlayerView.class);
    }

    @MainThread
    public boolean hasMedia()  {
        return hasCurrentMedia();
    }

    private boolean hasCurrentMedia()
    {
        return  mMediaPlayer!=null;
    }

    @MainThread
    public boolean switchToVideo()
    {
        return true;
    }

    public boolean isSeekable()
    {
        return mMediaPlayer.isSeekable();
    }

    public int getPlayerState()
    {
        return mMediaPlayer.getPlayerState();
    }

    public int getVolume()
    {
        return mMediaPlayer.getVolume();
    }

    public void setVolume(int volume)
    {
        mMediaPlayer.setVolume(volume);
    }

    public void seek(int delta)
    {
        // unseekable stream
        if (mMediaPlayer.getLength() <= 0 || !mCanSeek)
            return;

        long position = mMediaPlayer.getTime() + delta;
        if (position < 0)
            position = 0;
        mMediaPlayer.setTime(position);
    }
/*
    private final Handler eventHandler = new VideoPlayerHandler(this);

    private static class VideoPlayerHandler extends WeakHandler<PlayerView>
    {
        public VideoPlayerHandler(PlayerView owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayerView playerView = getOwner();
            if (playerView == null)
                return;

            switch (msg.getData().getInt("event")) {
                case EventHandler.MediaPlayerNothingSpecial:
                    break;
                case EventHandler.MediaPlayerOpening:
                    break;
                case EventHandler.MediaParsedChanged:
                    L.d(TAG, "MediaParsedChanged");
                    break;
                case EventHandler.MediaPlayerPlaying:
                    L.d(TAG, "MediaPlayerPlaying");
                    if (playerView.mOnChangeListener != null) {
                        playerView.mOnChangeListener.onLoadComplet();
                    }
                    break;
                case EventHandler.MediaPlayerPaused:
                    L.d(TAG, "MediaPlayerPaused");
                    break;
                case EventHandler.MediaPlayerStopped:
                    L.d(TAG, "MediaPlayerStopped");
                    break;
                case EventHandler.MediaPlayerEndReached:
                    L.d(TAG, "MediaPlayerEndReached");
                    if (playerView.mOnChangeListener != null) {
                        playerView.mOnChangeListener.onEnd();
                    }
                    break;
                case EventHandler.MediaPlayerVout:
                    break;
                case EventHandler.MediaPlayerPositionChanged:
                    if (!playerView.mCanSeek) {
                        playerView.mCanSeek = true;
                    }
                    break;
                case EventHandler.MediaPlayerEncounteredError:
                    L.d(TAG, "MediaPlayerEncounteredError");
                    if (playerView.mOnChangeListener != null) {
                        playerView.mOnChangeListener.onError();
                    }
                    break;
                case EventHandler.HardwareAccelerationError:
                    L.d(TAG, "HardwareAccelerationError");
                    if (playerView.mOnChangeListener != null && playerView.mDisabledHardwareAcceleration) {
                        playerView.stop();
                        playerView.mOnChangeListener.onError();
                    } else {
                        playerView.handleHardwareAccelerationError();
                    }
                    break;
                case EventHandler.MediaPlayerTimeChanged:
                    // avoid useless error logs
                    break;
                case EventHandler.MediaPlayerBuffering:
                    L.d(TAG, "MediaPlayerBuffering");
                    if (playerView.mOnChangeListener != null) {
                        playerView.mOnChangeListener.onBufferChanged(msg.getData().getFloat("data"));
                    }
                    break;
                default:
                    L.d(TAG, String.format("Event not handled (0x%x)", msg.getData().getInt("event")));
                    break;
            }
        }
    }*/
}
