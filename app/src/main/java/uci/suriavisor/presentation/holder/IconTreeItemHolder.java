package uci.suriavisor.presentation.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.Camera;
import uci.suriavisor.logic.VisorController;
import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;
import uci.suriavisor.presentation.SearchActivity;
import uci.suriavisor.presentation.SingleFragmentActivity;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import uci.suriavisor.presentation.materialdialogs.Theme;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;
import uci.suriavisor.presentation.CustomViews.ViewOneCameraActivity;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem>
{
    private TextView tvValue;
    private PrintView arrowView;
    private TreeNode node;

    public IconTreeItemHolder(Context context)
    {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_icon_node, null, false);
        this.node = node;
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);



        view.findViewById(R.id.btAddToView).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*TreeNode newFolder = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "New Folder"));
                getTreeView().addNode(node, newFolder);*/
                //agregar a vista
            }
        });

        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*getTreeView().removeNode(node);*/
                //quitar de la vista
            }
        });

        //if My computer
        if (node.getLevel() == 1)
        {
            view.findViewById(R.id.btn_delete).setVisibility(View.GONE);
        }
        if(node.getTypeElement().equals("zone"))
        {
            view.findViewById(R.id.btn_play).setVisibility(View.GONE);
            view.findViewById(R.id.btAddToView).setVisibility(View.GONE);
        }
        else if(node.getTypeElement().equals("camera"))
        {
            arrowView.setIconText(context.getResources().getString(R.string.ic_empty ));
            final Camera c = VisorController.getInstance().getCameraById(node.getIdElement());
            if (c.isActive())
            {
                if(Constants.THEME_ID == R.style.AppTheme){
                    view.findViewById(R.id.btn_play).setBackgroundResource(R.drawable.ic_videocam_black_24dp);
                }else {
                    view.findViewById(R.id.btn_play).setBackgroundResource(R.drawable.ic_videocam_white_24dp);
                }
                view.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //reproducir camera
                        Intent i = new Intent(context, ViewOneCameraActivity.class);
                        //VisorController.getInstance().customViewTemporal.getIdsCameras().add(c.getId());
                        VisorController.getInstance().customViewTemporal.getIdsCameras().add(0,c.getId());
                        i.putExtra("indexCustomView","-1");
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }
                });
            }
            else
            {
                if(Constants.THEME_ID == R.style.AppTheme){
                    view.findViewById(R.id.btn_play).setBackgroundResource(R.drawable.ic_videocam_off_black_24dp);
                }else {
                    view.findViewById(R.id.btn_play).setBackgroundResource(R.drawable.ic_videocam_off_white);
                }

            }
            if(Constants.THEME_ID == R.style.AppTheme){
                view.findViewById(R.id.btAddToView).setBackgroundResource(R.drawable.ic_add_to_queue_black_18dp);
            }else {
                view.findViewById(R.id.btAddToView).setBackgroundResource(R.drawable.ic_add_to_queue_white);
            }

            view.findViewById(R.id.btAddToView).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    VisorController visorController = VisorController.getInstance();
                    if (visorController.getContext() instanceof SingleFragmentActivity)
                    {
                        SingleFragmentActivity sf = (SingleFragmentActivity) visorController.getContext();
                        Bundle bundle = sf.getIntent().getExtras();
                        if (bundle.get("entryType") != null && bundle.get("entryType").equals("addCamera"))
                        {
                            int posArea = (int) bundle.get("posArea");
                            //visorController.getCustomViews().get(visorController.getCurrentView()).getIdsCameras().set(posArea, node.getIdElement());
                            if (visorController.getCustomViews().get(visorController.getCurrentView()).getIdsCameras().contains(node.getIdElement())){
                                Toast.makeText(sf, "Esa cámara ya está insertada.", Toast.LENGTH_SHORT).show();
                            }else {
                                visorController.getCustomViews().get(visorController.getCurrentView()).getIdsCameras().set(posArea, node.getIdElement());
                            }
                            sf.finish();
                            visorController.showCustomView(visorController.getCurrentView());
                        }
                        else
                        {
                            if(visorController.getCustomViews().size()>0)
                            {
                                String[] words = new String[visorController.getCustomViews().size()];
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
        view.findViewById(R.id.btn_delete).setVisibility(View.GONE);

        return view;
    }

    @Override
    public void toggle(boolean active)
    {
        if(!node.getTypeElement().equals("camera") )
            arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    public static class IconTreeItem
    {
        public int icon;
        public String text;

        public IconTreeItem(int icon, String text)
        {
            this.icon = icon;
            this.text = text;
        }
    }
}
