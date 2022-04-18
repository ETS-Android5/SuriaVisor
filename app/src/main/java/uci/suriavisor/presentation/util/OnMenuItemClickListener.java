package uci.suriavisor.presentation.util;

import android.view.MenuItem;
import android.widget.PopupMenu;

import com.xilema.suriavisor.R;

/**
 * Created by Miguel on 08/11/2016.
 */
public class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener
{
    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            case R.id.lang_java:
                return true;
            case R.id.lang_android:
                return true;
            case R.id.lang_python:
                return true;
            case R.id.lang_ruby:
                return true;
        }
        return false;
    }
}
