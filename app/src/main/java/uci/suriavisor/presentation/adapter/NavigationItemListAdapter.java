package uci.suriavisor.presentation.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;

import java.util.List;


public class NavigationItemListAdapter extends BaseAdapter
{
    private Activity activity;
    List<String> arrayItms;
    List<Integer> arrayItmsIcon;

    public NavigationItemListAdapter(Activity activity, List<String> arrayItms, List<Integer> arrayItmsIcon)
    {
        super();
        this.activity = activity;
        this.arrayItms = arrayItms;
        this.arrayItmsIcon = arrayItmsIcon;
    }

    //Retorna objeto nav_Item del array list
    @Override
    public Object getItem(int position)
    {
        return arrayItms.get(position);
    }

    public int getCount()
    {
        return arrayItms.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflator = activity.getLayoutInflater();
        View view = inflator.inflate(R.layout.item_list_drawer, null);

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.drawer_item_layout);
        switch (Constants.THEME_ID)
        {
            case R.style.AppTheme:
                relativeLayout.setBackgroundColor(view.getResources().getColor(R.color.background_light));
                break;
            case R.style.AppThemeDark:
                relativeLayout.setBackgroundColor(view.getResources().getColor(R.color.background_dark));
                break;
        }

        TextView item_title = (TextView) view.findViewById(R.id.title_item);
        item_title.setText(arrayItms.get(position));
        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);
        icon.setImageResource(arrayItmsIcon.get(position));
        return view;
    }
}
