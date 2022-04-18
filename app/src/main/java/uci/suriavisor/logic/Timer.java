package uci.suriavisor.logic;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by Miguel on 17/11/2016.
 */
public class Timer extends CountDownTimer
{
    Object o;
    VisorController visorController;
    public Timer(long millisInFuture, long countDownInterval, Object o)
    {
        super(millisInFuture, countDownInterval);
        visorController = VisorController.getInstance();
        this.o = o;
    }

    @Override
    public void onFinish()
    {
        if(o instanceof Server)
        {
            visorController.webSocketTimeOut();
        }
    }

    @Override
    public void onTick(long millisUntilFinished)
    {
        System.out.println("Timer In Activity: " + (millisUntilFinished / 1000));
    }
}
