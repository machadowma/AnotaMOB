package securisamarum.anotamob;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraficoHolder extends RecyclerView.ViewHolder {
    public TextView recycleDia,recycleData,recycleColorLabel,recycleSymbol,recycleIntercourse;
    public LinearLayout recycleColor,recycleHeartLayout;
    public ImageView recycleComment;


    public GraficoHolder(View itemView) {
        super(itemView);
        recycleDia = (TextView) itemView.findViewById(R.id.recycleDia);
        recycleData = (TextView) itemView.findViewById(R.id.recycleData);
        recycleColor = (LinearLayout) itemView.findViewById(R.id.recycleColor);
        recycleColorLabel = (TextView) itemView.findViewById(R.id.recycleColorLabel);
        recycleSymbol = (TextView) itemView.findViewById(R.id.recycleSymbol);
        recycleIntercourse = (TextView) itemView.findViewById(R.id.recycleIntercourse);
        recycleHeartLayout = (LinearLayout) itemView.findViewById(R.id.recycleHeartLayout);
        recycleComment = (ImageView) itemView.findViewById(R.id.recycleComment);
    }
}