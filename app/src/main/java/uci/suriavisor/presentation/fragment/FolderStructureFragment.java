package uci.suriavisor.presentation.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.logic.Zone;
import com.unnamed.b.atv.model.TreeNode;
import uci.suriavisor.presentation.MainActivity;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;
import uci.suriavisor.presentation.holder.IconTreeItemHolder;
import com.unnamed.b.atv.view.AndroidTreeView;
import uci.suriavisor.presentation.holder.SelectableHeaderHolder;

import java.util.LinkedList;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class FolderStructureFragment extends Fragment
{
    private TextView statusBar;
    private AndroidTreeView tView;

    private VisorController visorController = VisorController.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        //statusBar = (TextView) rootView.findViewById(R.id.status_bar);

       /* TreeNode root = TreeNode.root();

        TreeNode computerRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, "My Computer"));
        TreeNode myDocuments = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "My Documents"));
        TreeNode downloads = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Downloads"));
        fillDownloadsFolder(downloads);

        myDocuments.addChild(downloads);

        root.addChildren(computerRoot);

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(IconTreeItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);
        tView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.addView(tView.getView());*/
        bundle = getActivity().getIntent().getExtras();

        buildTree(containerView);

        if (savedInstanceState != null)
        {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state))
            {
                tView.restoreState(state);
            }
        }

        return rootView;
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.btMaxm:
                tView.expandAll();
                break;
        }
        return true;
    }

    private int counter = 0;

    private void fillDownloadsFolder(TreeNode node) {
        TreeNode downloads = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Downloads" + (counter++)));
        node.addChild(downloads);
        if (counter < 5) {
            fillDownloadsFolder(downloads);
        }
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener()
    {
        @Override
        public void onClick(TreeNode node, Object value)
        {
            if(bundle.get("entryType")!=null && bundle.get("entryType").equals("addCamera") && node.getTypeElement().equals("camera"))
            {
                int posArea = (int) bundle.get("posArea");
                visorController.getCustomViews().get(visorController.getCurrentView()).getIdsCameras().set(posArea, node.getIdElement());
                getActivity().finish();
                visorController.showCustomView(visorController.getCurrentView());
            }
            //IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            //statusBar.setText("Last clicked: " + item.text);
        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            Toast.makeText(getActivity(), "Long click: " + item.text, Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    private void buildTree(ViewGroup containerView)
    {
        if (visorController.isReadyAllCameras() && visorController.isReadyAllZones())
        {
            TreeNode root = TreeNode.root();

            TreeNode treeNode = null;
            for (int i = 0; i < visorController.getTreeElements().size(); i++)
            {
                if (visorController.getTreeElements().get(i).getParentZoneId().equals(String.valueOf(-1)))
                {
                    /*treeNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, visorController.getTreeElements().
                            get(i).getName())).setViewHolder(new SelectableHeaderHolder(getActivity()));*/
                    treeNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_location_city, visorController.getTreeElements().
                            get(i).getName()));
                    treeNode.setTypeElement("zone");
                    //Log.i("Va a entrar al recursiv", "Va a entrar al recursivo");
                    treeNode.addChildren(recursiveTree(visorController.getAdyacents().get(i)));
                    treeNode.setIdElement(visorController.getTreeElements().get(i).getId());
                   /* treeNode.setClickListener(new TreeNode.TreeNodeClickListener()
                    {
                        @Override
                        public void onClick(TreeNode node, Object value)
                        {
                            clickOnItem(node);
                        }
                    });*/
                    root.addChildren(treeNode);
                }
            }

            if (!root.getChildren().isEmpty())
            {
                tView = new AndroidTreeView(getActivity(), root);
                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                tView.setDefaultViewHolder(IconTreeItemHolder.class);
                tView.setDefaultNodeClickListener(nodeClickListener);
                tView.setDefaultNodeLongClickListener(nodeLongClickListener);
                containerView.addView(tView.getView());
            }else {
                Log.e("buildTree: ", "***   No hay arbol...   ***");
                TextView textView = (TextView) getActivity().findViewById(R.id.tvShowTree);
                textView.setVisibility(View.VISIBLE);
            }
        }else {
            Log.e("buildTree: ", "***   No hay arbol...   ***");
            TextView textView = (TextView) getActivity().findViewById(R.id.tvShowTree);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private LinkedList<TreeNode> recursiveTree(LinkedList<Integer> elements)
    {
        //Log.i("Entró al recursiv", "Entró al recursivo");
        TreeNode treeNode = null;
        LinkedList<TreeNode> treeNodes = new LinkedList<>();
        Log.i("elements.size();", String.valueOf(elements.size()));
        for (int i = 0; i < elements.size(); i++)
        {
            if (visorController.getTreeElements().get(elements.get(i)) instanceof Zone)
            {
                /*treeNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, visorController.getTreeElements().
                        get(elements.get(i)).getName())).setViewHolder(new SelectableHeaderHolder(getActivity()));*/
                treeNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_location_city, visorController.getTreeElements().
                        get(elements.get(i)).getName()));
                treeNode.setTypeElement("zone");
                treeNode.addChildren(recursiveTree(visorController.getAdyacents().get(elements.get(i))));
            }
            else
            {
                /*treeNode =  new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo_library, visorController.getTreeElements().
                        get(elements.get(i)).getName())).setViewHolder(new SelectableHeaderHolder(getActivity()));*/
                treeNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_hangout_video, visorController.getTreeElements().
                        get(elements.get(i)).getName()));
                String nameCamera = visorController.getTreeElements().get(elements.get(i)).getName();
                treeNode.setTypeElement("camera");
            }
            treeNode.setIdElement(visorController.getTreeElements().get(elements.get(i)).getId());
            /*treeNode.setClickListener(new TreeNode.TreeNodeClickListener()
            {
                @Override
                public void onClick(TreeNode node, Object value)
                {
                    clickOnItem(node);
                }
            });*/
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
