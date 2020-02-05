package com.technology.circles.apps.omanmade.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.technology.circles.apps.omanmade.R;

public class ClusterRender extends DefaultClusterRenderer<ClusterLocation> {

    private Context context;

    public ClusterRender(Context context, GoogleMap map, ClusterManager<ClusterLocation> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterLocation item, MarkerOptions markerOptions) {

        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setBackground(ContextCompat.getDrawable(context, R.drawable.cluster_item_bg));
        iconGenerator.setContentPadding(10,3,10,3);
        iconGenerator.setTextAppearance(R.style.iconGenText);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(item.getTitle())));

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ClusterLocation> cluster, MarkerOptions markerOptions) {
        View view = LayoutInflater.from(context).inflate(R.layout.cluster_view,null);
        TextView textView = view.findViewById(R.id.tvCount);
        if (cluster.getSize()>=100)
        {
            textView.setText("+100");
        }else
            {
                textView.setText(String.valueOf(cluster.getSize()));
            }
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setBackground(null);
        iconGenerator.setContentView(view);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()));



    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterLocation> cluster) {
        return cluster.getSize()>1;
    }
}
