package uci.suriavisor.presentation.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.Camera;
import uci.suriavisor.logic.TreeElement;
import uci.suriavisor.logic.VisorController;
import uci.suriavisor.logic.Zone;
import uci.suriavisor.presentation.CustomViews.ViewOneCameraActivity;
import uci.suriavisor.presentation.SearchActivity;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;

import java.util.LinkedList;

/**
 * Created by Miguel on 26/05/2016.
 */
public class RecyclerTreeElementAdapter extends RecyclerView.Adapter<RecyclerTreeElementAdapter.ViewsViewHolder>
{
    VisorController visorController= VisorController.getInstance();

    public static class ViewsViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
    {
        RelativeLayout rlItemTreeElement;
        ImageButton ibImage, ibPlayCamera;
        TextView tvNameTreeElement;
        ImageButton btAddToView;

        ViewsViewHolder(final View itemView)
        {
            super(itemView);
            //itemView.setOnClickListener(this);
            rlItemTreeElement = (RelativeLayout)itemView.findViewById(R.id.rl_item_tree_element);

            ibImage = (ImageButton)itemView.findViewById(R.id.ImageButton_image);
            tvNameTreeElement = (TextView)itemView.findViewById(R.id.TextView_nameTreeElement);
            ibPlayCamera = (ImageButton)itemView.findViewById(R.id.ImageButton_playCamera);
            btAddToView = (ImageButton)itemView.findViewById(R.id.button_addToView);
        }
        /*@Override
        public void onClick(View view)
        {
            Log.e("Pos", "onClick " + getPosition() + view.toString());
        }*/
    }

    //RecyclerTreeElementAdapter

    private LinkedList<TreeElement> treeElementsFound = new LinkedList<>();
    public RecyclerTreeElementAdapter(LinkedList<TreeElement> treeElementsFound)
    {
        this.treeElementsFound = treeElementsFound;
    }

    @Override
    public int getItemCount()
    {
        return treeElementsFound.size();
    }

    @Override
    public ViewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tree_element, viewGroup, false);
        ViewsViewHolder vvh = new ViewsViewHolder(v);

        // code del buscar
        ImageButton ibtSetView = (ImageButton) v.findViewById(R.id.ImageButton_image);
        ImageButton ibtPlayCam = (ImageButton) v.findViewById(R.id.ImageButton_playCamera);
        ImageButton ibtAddToView = (ImageButton) v.findViewById(R.id.button_addToView);

        if (Constants.THEME_ID == R.style.AppTheme)
        {
            ibtSetView.setBackgroundResource(R.drawable.ic_location_city_black);
            ibtPlayCam.setBackgroundResource(R.drawable.ic_videocam_black_36dp);
            ibtAddToView.setBackgroundResource(R.drawable.ic_add_to_queue_black_36dp);
        }

        return vvh;
    }

    @Override
    public void onBindViewHolder(ViewsViewHolder personViewHolder, int i)
    {
        try
        {
            personViewHolder.tvNameTreeElement.setText(treeElementsFound.get(i).getName());
            final TreeElement treeElement = treeElementsFound.get(i);
            if(treeElementsFound.get(i) instanceof Zone)
            {
                if (Constants.THEME_ID == R.style.AppTheme){
                    personViewHolder.ibImage.setBackgroundResource(R.drawable.ic_location_city_black);
                }else{
                    personViewHolder.ibImage.setBackgroundResource(R.drawable.ic_location_city_white_36dp);
                }
                personViewHolder.ibPlayCamera.setVisibility(View.INVISIBLE);
                personViewHolder.btAddToView.setVisibility(View.INVISIBLE);
                personViewHolder.rlItemTreeElement.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(VisorController.getInstance().getContext() instanceof SearchActivity)
                        {
                            LinkedList<TreeElement> treeElementsFound = VisorController.getInstance().childOfZoneById(treeElement.getId());
                            ((SearchActivity) VisorController.getInstance().getContext()).showSearchResult(treeElementsFound);
                        }
                    }
                });
            }
            else
            {
                final Camera c = VisorController.getInstance().getCameraById(treeElement.getId());
                if (Constants.THEME_ID == R.style.AppTheme){
                    personViewHolder.ibImage.setBackgroundResource(R.drawable.ic_videocam_black_36dp);
                }else{
                    personViewHolder.ibImage.setBackgroundResource(R.drawable.ic_videocam_white_36dp);
                }
                personViewHolder.ibPlayCamera.setVisibility(View.VISIBLE);
                personViewHolder.ibPlayCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //reproducir camera
                        Intent i = new Intent(VisorController.getInstance().getContext(), ViewOneCameraActivity.class);
                        //VisorController.getInstance().customViewTemporal.getIdsCameras().add(c.getId());
                        VisorController.getInstance().customViewTemporal.getIdsCameras().add(0,c.getId());
                        i.putExtra("indexCustomView","-1");
                        VisorController.getInstance().getContext().startActivity(i);
                        ((Activity) VisorController.getInstance().getContext()).finish();
                    }
                });
                personViewHolder.btAddToView.setVisibility(View.VISIBLE);
                personViewHolder.btAddToView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //VisorController.getInstance().showPopupViews(1);
                        if(visorController.getContext() instanceof SearchActivity)
                        {
                            SearchActivity sa = (SearchActivity) visorController.getContext();
                            Bundle bundle = sa.getIntent().getExtras();
                            if (bundle!= null && bundle.get("entryType") != null && bundle.get("entryType").equals("addCamera"))
                            {
                                int posArea = (int) bundle.get("posArea");
                                //visorController.getCustomViews().get(visorController.getCurrentView()).getIdsCameras().set(posArea, treeElement.getId());
                                if (visorController.getCustomViews().get(visorController.getCurrentView()).getIdsCameras().contains(treeElement.getId())){
                                    Toast.makeText(sa, "Esa cámara ya está insertada.", Toast.LENGTH_SHORT).show();
                                }else {
                                    visorController.getCustomViews().get(visorController.getCurrentView()).getIdsCameras().set(posArea, treeElement.getId());
                                    Toast.makeText(sa, "Insertada la cámara " + c.getName() + " correctamente.", Toast.LENGTH_SHORT).show();
                                }
                                sa.finish();
                                visorController.showCustomView(visorController.getCurrentView());
                            }
                            else
                            {
                                if(VisorController.getInstance().getCustomViews().size()>0)
                                {
                                    String[] words = new String[VisorController.getInstance().getCustomViews().size()];
                                    for (int i = 0; i < words.length; i++)
                                    {
                                        words[i] = VisorController.getInstance().getCustomViews().get(i).getName();
                                    }
                                    int color = VisorController.getInstance().getContext().getResources().getColor(R.color.background_dark);
                                    if (Constants.THEME_ID == R.style.AppTheme)
                                        color = VisorController.getInstance().getContext().getResources().getColor(R.color.background_light);
                                    new MaterialDialog.Builder(VisorController.getInstance().getContext())
                                            .title("Vistas")
                                            .items(words)
                                            .itemsCallback(new MaterialDialog.ListCallback()
                                            {
                                                @Override
                                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text)
                                                {
                                                    VisorController.getInstance().showPopupViews(which, c);
                                                }
                                            })
                                            .backgroundColor(color)
                                            .show();
                                }
                                else
                                    visorController.showMessage("No existen vistas creadas.");
                            }
                        }
                    }
                });
            }
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
