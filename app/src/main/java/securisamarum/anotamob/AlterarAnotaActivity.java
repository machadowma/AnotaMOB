package securisamarum.anotamob;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AlterarAnotaActivity extends AppCompatActivity {
    private Integer anotaId, anotaRelacao, cicloId, codigo_simbolo;
    private SQLiteDatabase bancoDados;
    private TextView textViewDia,textViewData, linkExcluirAnota;
    private ImageView imageSimbolo,imageRelacao;
    private EditText editTextObs;
    private Button buttonAlterarAnota;
    public Integer[] mThumbIds = {
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,
            R.drawable.img_9
    };
    public String[] observacoes = {
            "Sangramento."
            ,"Manchas de sangue."
            ,"Dias em que não se observa muco e existe uma sensação de secura."
            ,"Dias de muco. Indica possível fertilidade. Também usado para o dia após o sexo na fase pré-ovulatória."
            ,"Padrão Básico de Infertilidade ou a partir do quarto dia após o pico. Seco ou muco que não é molhado ou escorregadio."
            ,"Pico."
            ,"Um dia após o pico. Possivelmente fértil. Seco ou muco que não é molhado ou escorregadio."
            ,"Dois dias após o pico. Possivelmente fértil. Seco ou muco que não é molhado ou escorregadio."
            ,"Três dias após o pico. Possivelmente fértil. Seco ou muco que não é molhado ou escorregadio."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_anota);

        textViewDia = (TextView) findViewById(R.id.textViewDia) ;
        textViewData = (TextView) findViewById(R.id.textViewData) ;
        linkExcluirAnota = (TextView) findViewById(R.id.linkExcluirAnota) ;
        imageSimbolo = (ImageView) findViewById(R.id.imageSimbolo) ;
        imageRelacao = (ImageView) findViewById(R.id.imageRelacao) ;
        editTextObs = (EditText) findViewById(R.id.editTextObs) ;
        buttonAlterarAnota = (Button) findViewById(R.id.buttonAlterarAnota) ;

        Intent intent = getIntent();
        anotaId = Integer.parseInt(intent.getStringExtra("anotaId"));
        try {
            bancoDados = openOrCreateDatabase("anotamob", MODE_PRIVATE, null);
            String sql = "SELECT id, id_ciclo, dia, strftime('%d/%m/%Y', data) as data, codigo, relacao, observacao FROM anotacao where id = " + anotaId.toString();
            Cursor cursor = bancoDados.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                cicloId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id_ciclo")));
                anotaRelacao = Integer.parseInt(cursor.getString(cursor.getColumnIndex("relacao")));
                textViewDia.setText(cursor.getString(cursor.getColumnIndex("dia")));
                textViewData.setText(cursor.getString(cursor.getColumnIndex("data")));
                editTextObs.setText(cursor.getString(cursor.getColumnIndex("observacao")));
                imageSimbolo.setImageResource(mThumbIds[cursor.getInt(cursor.getColumnIndex("codigo"))-1]);
                codigo_simbolo = cursor.getInt(cursor.getColumnIndex("codigo"));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("relacao"))) == 1) {
                    imageRelacao.setImageResource(R.drawable.coracao_sim);
                } else {
                    imageRelacao.setImageResource(R.drawable.coracao_nao);
                }
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageRelacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(anotaRelacao==1){
                    anotaRelacao = 0;
                    imageRelacao.setImageResource(R.drawable.coracao_nao);
                } else {
                    anotaRelacao = 1;
                    imageRelacao.setImageResource(R.drawable.coracao_sim);
                }
            }
        });

        imageSimbolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(AlterarAnotaActivity.this);
                View my_view = getLayoutInflater().inflate(R.layout.alterar_anota_alert_simb, null);
                builder.setView(my_view);
                GridView gridView = (GridView) my_view.findViewById(R.id.grid_view_alt_simb);
                gridView.setAdapter(new AnotaAdapter(AlterarAnotaActivity.this));
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                final AlertDialog dialog = builder.create();
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        imageSimbolo.setImageResource(mThumbIds[i]);
                        codigo_simbolo = i+1;
                        editTextObs.setText(observacoes[i]);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        linkExcluirAnota.setPaintFlags(linkExcluirAnota.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        linkExcluirAnota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirAnotacao();
            }
        });

        buttonAlterarAnota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarAnotacao();
            }
        });
    }

    private void alterarAnotacao() {
        try {
            bancoDados = openOrCreateDatabase("anotamob", MODE_PRIVATE, null);
            String sql = "UPDATE anotacao set codigo = "+Integer.toString(codigo_simbolo)+", relacao = "+Integer.toString(anotaRelacao)+", observacao = '"+editTextObs.getText().toString()+"' where id = " + anotaId.toString();
            bancoDados.execSQL(sql);
            bancoDados.close();
            Toast.makeText(this, R.string.anota_alterada, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void excluirAnotacao(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(AlterarAnotaActivity.this);
        builder .setTitle(R.string.excluir);
        builder .setIcon(android.R.drawable.ic_menu_delete);
        builder.setMessage(R.string.certeza_excluir_anotacao);
        builder .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    bancoDados = openOrCreateDatabase("anotamob", MODE_PRIVATE, null);
                    String sql = "SELECT max(dia) as max_dia FROM anotacao where id_ciclo = " + cicloId.toString();
                    Cursor cursor = bancoDados.rawQuery(sql, null);
                    if (cursor.moveToFirst()) {
                        Integer max_dia = Integer.parseInt(cursor.getString(0));
                        if(1 == max_dia) {
                            alertErro(R.string.nao_exclui_anotaca_1);
                        } else if(max_dia != Integer.parseInt(textViewDia.getText().toString())) {
                            //alertErro(R.string.nao_exclui_anotaca_meio);
                            bancoDados.execSQL("DELETE FROM anotacao WHERE id ="+anotaId);
                            bancoDados.execSQL("UPDATE anotacao SET dia = dia-1,data=DATETIME(data, '-1 days') WHERE id_ciclo ="+cicloId+" AND dia>"+textViewDia.getText().toString());
                            Toast.makeText(AlterarAnotaActivity.this, R.string.anota_excluida, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            bancoDados.execSQL("DELETE FROM anotacao WHERE id ="+anotaId);
                            Toast.makeText(AlterarAnotaActivity.this, R.string.anota_excluida, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    bancoDados.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });
        builder .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private void alertErro(int msg) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(AlterarAnotaActivity.this);
        builder .setTitle(R.string.erro);
        builder .setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(msg);
        builder .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

}

