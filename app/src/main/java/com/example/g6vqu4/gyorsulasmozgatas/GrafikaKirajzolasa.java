package com.example.g6vqu4.gyorsulasmozgatas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
//import android.support.annotation.MainThread;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.lang.Math;


public class GrafikaKirajzolasa extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = GrafikaKirajzolasa.class.getSimpleName();

    private MainThread thread;
    private Bitmap MozgatandoFigura = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

    private int  maxX = 480, maxY = 800;
    private float lassulas=0.9f , xpoz=200, ypoz=200, erox=0, eroy=0;
    private boolean correction = true;

    public GrafikaKirajzolasa(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
    }
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(TAG, "Surface destroying failure: " + e);
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        erox = x-xpoz;
        eroy = y-ypoz;
        if (erox == 0 || eroy == 0)
        {
            correction = false;
        }
        else
        {
            correction = true;
        }


        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(this.MozgatandoFigura, xpoz - (this.MozgatandoFigura.getWidth() / 2), ypoz - (this.MozgatandoFigura.getHeight() / 2), null);
    }


    public void mozgat() {
        if (eroy>0 && ypoz+eroy<maxY)
        {
            this.ypoz+=eroy;
            eroy = eroy*lassulas;
        }
        if(eroy<0 && ypoz+eroy>0)
        {
            this.ypoz+=eroy;
            eroy = eroy*lassulas;
        }
        if (Math.abs(eroy) < lassulas)
        {
            eroy = 0;
            if (correction)
            {
                erox = 0;
            }

        }
        else
        {
            if (ypoz+eroy>maxY || ypoz+eroy<0)
            {
                eroy = -1*eroy;
            }
        }

        if (erox>0 && xpoz+erox<maxX)
        {
            this.xpoz+=erox;
            erox = erox*lassulas;
        }
        if(erox<0 && xpoz+erox>0)
        {
            this.xpoz+=erox;
            erox = erox*lassulas;
        }
        if (Math.abs(erox) < lassulas)
        {
            erox = 0;
            if (correction)
            {
                eroy = 0;
            }
        }
        else
        {
            if (xpoz+erox>maxX || xpoz+erox<0)
            {
                erox = -1*erox;
            }
        }

		/*try {
			thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }


}
