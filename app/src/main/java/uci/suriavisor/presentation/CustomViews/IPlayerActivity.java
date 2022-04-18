package uci.suriavisor.presentation.CustomViews;


/**
 * Created by Miguel on 06/09/2016.
 */
public interface IPlayerActivity
{
    void showMessage(String msg);
    void hideLoading();
    void stoppedRecord(String idCamera);
    void startedRecord(String idCamera);
    void playCamera(String idCamera, String urlStreaming);
    //void showDialogMark(String idCamera, String currentTime);
}
