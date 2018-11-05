package securisamarum.anotamob;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class CicloActivity extends AppCompatActivity {
    private Integer cicloId;
    private SQLiteDatabase bancoDados;
    private TextView cicloTextData;
    private ArrayList <Integer> anotaIds;
    private List<Anotacao> listAnota;
    private ListView anotaListView;
    private ImageView imageViewAbrirGrafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciclo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        cicloTextData = (TextView) findViewById(R.id.cicloTextData);
        anotaListView = (ListView) findViewById(R.id.anotaList);
        imageViewAbrirGrafico = (ImageView) findViewById(R.id.imageViewAbrirGrafico);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incluirAnota();
            }
        });

        Intent intent = getIntent();
        cicloId = intent.getIntExtra("cicloId",0);

        anotaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alterarAnota(i);
            }
        });

        imageViewAbrirGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGrafico();
            }
        });

        carregarCiclo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCiclo();
    }

    private void carregarCiclo(){
        try{
            bancoDados = openOrCreateDatabase("anotamob",MODE_PRIVATE,null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,strftime('%d/%m/%Y', data) as data FROM ciclo WHERE id = "+cicloId.toString(),null);
            if(cursor.moveToFirst()) {
                String data = cursor.getString(cursor.getColumnIndex("data"));
                cicloTextData.setText(data);
            }

            cursor = bancoDados.rawQuery("SELECT id, id_ciclo, dia, strftime('%d/%m/%Y', data) as data, codigo, relacao, observacao FROM anotacao where id_ciclo = "+cicloId.toString()+" ORDER BY dia DESC",null);
            cursor.moveToFirst();
            listAnota = new ArrayList();
            anotaIds = new ArrayList<Integer>();
            do {
                Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                Integer cod = Integer.parseInt(cursor.getString(cursor.getColumnIndex("codigo")));
                String diaStr = cursor.getString(cursor.getColumnIndex("dia"));
                String dataStr = cursor.getString(cursor.getColumnIndex("data"));
                String obs = cursor.getString(cursor.getColumnIndex("observacao"));
                Integer relacao = Integer.parseInt(cursor.getString(cursor.getColumnIndex("relacao")));
                Anotacao anotacao = new Anotacao(id, cod, diaStr, dataStr, obs, relacao);

                listAnota.add(anotacao);
                anotaIds.add(id);
            } while ( cursor.moveToNext());

            CicloAdapter adapter = new CicloAdapter(
                    this,
                    R.layout.linha_anotacao,
                    listAnota
            );

            anotaListView.setAdapter(adapter);

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void incluirAnota(){
        Intent intent = new Intent(this,IncluirAnotaActivity.class);
        intent.putExtra("cicloId",cicloId.toString());
        startActivity(intent);
    }

    private void alterarAnota(Integer id){
        Intent intent = new Intent(this,AlterarAnotaActivity.class);
        intent.putExtra("anotaId",anotaIds.get(id).toString());
        startActivity(intent);
    }

    public void setAnotaRelacao(Integer id ,Integer relacao){
        String sql = "UPDATE anotacao SET relacao = "+Integer.toString(relacao)+" WHERE id = "+Integer.toString(id);
        try{
            bancoDados = openOrCreateDatabase("anotamob",MODE_PRIVATE,null);
            bancoDados.execSQL(sql);
            bancoDados.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirGrafico(){
        Intent intent = new Intent(this,GraficoActivity.class);
        intent.putExtra("cicloId",cicloId.toString());
        startActivity(intent);
    }

}

