package securisamarum.anotamob;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class GraficoActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAnotacoes;
    private GraficoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SQLiteDatabase bancoDados;
    private Integer cicloId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        Intent intent = getIntent();
        cicloId = Integer.parseInt(intent.getStringExtra("cicloId"));

        recyclerViewAnotacoes = (RecyclerView) findViewById(R.id.recycler_view_anotacoes);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAnotacoes.setLayoutManager(mLayoutManager);
        mAdapter = new GraficoAdapter(new ArrayList<>(0));
        recyclerViewAnotacoes.setAdapter(mAdapter);
        recyclerViewAnotacoes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewAnotacoes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerViewAnotacoes.setHasFixedSize(true);
        try {
            bancoDados = openOrCreateDatabase("anotamob", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, id_ciclo, dia, strftime('%d/%m', data) as data, codigo, relacao, observacao FROM anotacao where id_ciclo = "+cicloId.toString()+" ORDER BY dia",null);
            if(cursor.moveToFirst()) {
                do {
                    Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                    Integer cod = Integer.parseInt(cursor.getString(cursor.getColumnIndex("codigo")));
                    String diaStr = cursor.getString(cursor.getColumnIndex("dia"));
                    String dataStr = cursor.getString(cursor.getColumnIndex("data"));
                    String obs = cursor.getString(cursor.getColumnIndex("observacao"));
                    Integer relacao = Integer.parseInt(cursor.getString(cursor.getColumnIndex("relacao")));
                    Anotacao anotacao = new Anotacao(id, cod, diaStr, dataStr, obs, relacao);
                    mAdapter.insertItem(anotacao);
                } while (cursor.moveToNext());
                recyclerViewAnotacoes.smoothScrollToPosition(mAdapter.getItemCount()-1);
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
