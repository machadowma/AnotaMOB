package securisamarum.anotamob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GraficoAdapter extends RecyclerView.Adapter<GraficoHolder> {

    private final List<Anotacao> mAnotacoes;
    private ArrayList<String> comment;

    public GraficoAdapter(ArrayList anotacoes) {
        mAnotacoes = anotacoes;
        comment = new ArrayList<String>();
    }

    @Override
    public GraficoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GraficoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linha_grafico, parent, false));
    }

    @Override
    public void onBindViewHolder(GraficoHolder holder, final int position) {
        holder.recycleDia.setText(mAnotacoes.get(position).getDiaStr());
        holder.recycleData.setText(mAnotacoes.get(position).getDataStr());
        switch(mAnotacoes.get(position).getCodigo()) {
            case 1:
                holder.recycleColor.setBackgroundResource(R.color.color1);
                holder.recycleColorLabel.setText(R.string.color_label_1);
                holder.recycleSymbol.setText(R.string.symbol_1);
                break;
            case 2:
                holder.recycleColor.setBackgroundResource(R.color.color2);
                holder.recycleColorLabel.setText(R.string.color_label_2);
                holder.recycleSymbol.setText(R.string.symbol_2);
                break;
            case 3:
                holder.recycleColor.setBackgroundResource(R.color.color3);
                holder.recycleColorLabel.setText(R.string.color_label_3);
                holder.recycleSymbol.setText(R.string.symbol_3);
                break;
            case 4:
                holder.recycleColor.setBackgroundResource(R.color.color4);
                holder.recycleColorLabel.setText(R.string.color_label_4);
                holder.recycleSymbol.setText(R.string.symbol_4);
                break;
            case 5:
                holder.recycleColor.setBackgroundResource(R.color.color5);
                holder.recycleColorLabel.setText(R.string.color_label_5);
                holder.recycleSymbol.setText(R.string.symbol_5);
                break;
            case 6:
                holder.recycleColor.setBackgroundResource(R.color.color6);
                holder.recycleColorLabel.setText(R.string.color_label_6);
                holder.recycleSymbol.setText(R.string.symbol_6);
                break;
            case 7:
                holder.recycleColor.setBackgroundResource(R.color.color7);
                holder.recycleColorLabel.setText(R.string.color_label_7);
                holder.recycleSymbol.setText(R.string.symbol_7);
                break;
            case 8:
                holder.recycleColor.setBackgroundResource(R.color.color8);
                holder.recycleColorLabel.setText(R.string.color_label_8);
                holder.recycleSymbol.setText(R.string.symbol_8);
                break;
            case 9:
                holder.recycleColor.setBackgroundResource(R.color.color9);
                holder.recycleColorLabel.setText(R.string.color_label_9);
                holder.recycleSymbol.setText(R.string.symbol_9);
                break;
            default:
                holder.recycleColor.setBackgroundResource(R.color.colorWhite);
                break;
        }
        switch(mAnotacoes.get(position).getRelacao()) {
            case 1:
                holder.recycleHeartLayout.setVisibility(LinearLayout.VISIBLE);
                break;
            default:
                holder.recycleHeartLayout.setVisibility(LinearLayout.INVISIBLE );
                break;
        }
        comment.add(mAnotacoes.get(position).getObservacao());
        holder.recycleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder msgBox = new AlertDialog.Builder(view.getContext());
                msgBox.setTitle(R.string.comments);
                msgBox.setIcon(android.R.drawable.ic_dialog_info);
                msgBox.setMessage(comment.get(position));
                msgBox.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                msgBox.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAnotacoes != null ? mAnotacoes.size() : 0;
    }

    public void insertItem(Anotacao anotacao) {
        mAnotacoes.add(anotacao);
        notifyItemInserted(getItemCount());
    }

}
