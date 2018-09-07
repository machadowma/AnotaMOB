package securisamarum.anotamob;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CicloAdapter extends BaseAdapter{
    private final List<Anotacao> anotacoes;
    private SQLiteDatabase bancoDados;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public CicloAdapter(Context context, int resource, List<Anotacao> anotacoes ) {
        this.context = context;
        this.resource = resource;
        this.anotacoes = anotacoes;
    }

    @Override
    public int getCount() {
        return anotacoes.size();
    }

    @Override
    public Object getItem(int position) {
        return anotacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater  layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        final Anotacao anotacao = anotacoes.get(position);

        TextView obsTxtView = (TextView) view.findViewById(R.id.lista_anotacao_observacao);
        TextView diaTxtView = (TextView) view.findViewById(R.id.lista_anotacao_dia);
        TextView dataTxtView = (TextView) view.findViewById(R.id.lista_anotacao_data);
        ImageView simbImgView = (ImageView) view.findViewById(R.id.lista_anotacao_imagem);
        final ImageView coraImgView = (ImageView) view.findViewById(R.id.lista_anotacao_coracao);

        obsTxtView.setText(anotacao.getObservacao());
        diaTxtView.setText(anotacao.getDiaStr());
        dataTxtView.setText(anotacao.getDataStr());
        simbImgView.setImageResource(anotacao.getImagem());
        coraImgView.setImageResource(anotacao.getCoracao());

        coraImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //anotacao.getId()
                if(anotacao.getRelacao()==1){
                    anotacao.setRelacao(0);
                    Toast.makeText(((CicloActivity)context), R.string.relacao_nao, Toast.LENGTH_SHORT).show();
                } else {
                    anotacao.setRelacao(1);
                    Toast.makeText(((CicloActivity)context), R.string.relacao_sim, Toast.LENGTH_SHORT).show();
                }

                anotacao.setCoracao();
                coraImgView.setImageResource(anotacao.getCoracao());
                if(context instanceof CicloActivity){
                    ((CicloActivity)context).setAnotaRelacao(anotacao.getId(),anotacao.getRelacao());
                }
                Vibrator v = (Vibrator) ((CicloActivity)context).getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);

            }
        });

        return view;
    }
}
