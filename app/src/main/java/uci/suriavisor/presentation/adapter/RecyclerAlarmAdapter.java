package uci.suriavisor.presentation.adapter;

import android.content.res.Resources;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.util.Constants;

import com.xilema.suriavisor.R;

import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Miguel on 26/05/2016.
 */
public class RecyclerAlarmAdapter extends RecyclerView.Adapter<RecyclerAlarmAdapter.ViewsViewHolder>
{

    public static class ViewsViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
    {
        TextView tvTime, tvNameSensor, tvNameCamera, tvNameAlarm;
        Button btDelete;
        CardView cardView;

        ViewsViewHolder(final View itemView)
        {
            super(itemView);
            //itemView.setOnClickListener(this);
            tvTime = (TextView)itemView.findViewById(R.id.TextView_timeAlarm);
            tvNameAlarm = (TextView) itemView.findViewById(R.id.TextView_nameAlarm);
            tvNameSensor = (TextView)itemView.findViewById(R.id.TextView_nameSensor);
            tvNameCamera = (TextView)itemView.findViewById(R.id.TextView_nameCamera);
            btDelete = (Button)itemView.findViewById(R.id.button_deleteAlarm);
            btDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    VisorController.getInstance().deleteAlarm(getAdapterPosition());
                }
            });
        }
        /*@Override
        public void onClick(View view)
        {
            Log.e("Pos", "onClick " + getPosition() + view.toString());
        }*/
    }

    private LinkedList<JSONObject> alarms = new LinkedList<>();
    public RecyclerAlarmAdapter(LinkedList<JSONObject> alarms)
    {
        this.alarms = alarms;
    }

    @Override
    public int getItemCount()
    {
        return alarms.size();
    }

    @Override
    public ViewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alarm, viewGroup, false);
        ViewsViewHolder vvh = new ViewsViewHolder(v);
        return vvh;
    }

    @Override
    public void onBindViewHolder(ViewsViewHolder personViewHolder, int i)
    {
        try
        {
            personViewHolder.tvTime.setText(alarms.get(i).get("time").toString());
            String name = VisorController.getInstance().getSensorById(alarms.get(i).get("idSensor").toString()).getId();
            personViewHolder.tvNameSensor.setText(name);
            name = VisorController.getInstance().getCameraById(alarms.get(i).get("idCamera").toString()).getName();
            personViewHolder.tvNameCamera.setText(name);
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
