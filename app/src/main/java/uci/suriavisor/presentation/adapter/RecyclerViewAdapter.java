package uci.suriavisor.presentation.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.CustomView;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.ViewsActivity;
import uci.suriavisor.presentation.util.Constants;

import com.xilema.suriavisor.R;

import java.util.List;

/**
 * Created by Miguel on 26/05/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewsViewHolder>
{
    public static class ViewsViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
    {
        CardView cv;
        TextView viewName;
        ImageView viewPhoto;

        ViewsViewHolder(final View itemView)
        {
            super(itemView);
            //itemView.setOnClickListener(this);
            cv = (CardView) itemView.findViewById(R.id.CardView_View);
            viewName = (TextView) itemView.findViewById(R.id.TextView_nameView);

            //code de jose
            Button btDelView = (Button) itemView.findViewById(R.id.button_deleteView);
            Button btEdiView = (Button) itemView.findViewById(R.id.button_editView);

            if (Constants.THEME_ID == R.style.AppTheme){
                btDelView.setBackgroundResource(R.drawable.ic_delete_forever_black_18dp);
                btEdiView.setBackgroundResource(R.drawable.ic_edit_black);
            }

            viewPhoto = (ImageView)itemView.findViewById(R.id.view_photo);
            viewPhoto.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    VisorController.getInstance().showCustomView(getAdapterPosition());
                }
            });
            Button deleteBtn = (Button)itemView.findViewById(R.id.button_deleteView);
            deleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Eliminar")
                            .setMessage("¿Desea eliminar la vista seleccionada?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    VisorController.getInstance().deleteCustomView(getAdapterPosition());
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {

                                }
                            })
                            .show();
                }
            });
            Button editBtn = (Button)itemView.findViewById(R.id.button_editView);
            editBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    VisorController.getInstance().showEditDialog(getAdapterPosition());
                }
            });
//            Button setBtn = (Button)itemView.findViewById(R.id.button_setView);
//            setBtn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    VisorController.getInstance().setDefaultView(getAdapterPosition());
//                }
//            });
        }
        /*@Override
        public void onClick(View view)
        {
            Log.e("Pos", "onClick " + getPosition() + view.toString());
        }*/
    }

    private List<CustomView> customViews;

    public RecyclerViewAdapter(List<CustomView> customViews)
    {
        this.customViews = customViews;
    }

    @Override
    public int getItemCount()
    {
        return customViews.size();
    }

    @Override
    public ViewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false);
        ViewsViewHolder vvh = new ViewsViewHolder(v);
        return vvh;
    }

    @Override
    public void onBindViewHolder(ViewsViewHolder personViewHolder, int i)
    {
        personViewHolder.viewName.setText(customViews.get(i).name);
        personViewHolder.viewPhoto.setImageResource(customViews.get(i).getPhotoId());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
