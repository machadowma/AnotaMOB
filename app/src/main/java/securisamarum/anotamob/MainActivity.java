package securisamarum.anotamob;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageButton mainButton;
    private SQLiteDatabase bancoDados;
    private ListView mainList;
    private ArrayList<Integer> cicloIds;
    private ArrayList<String> cicloDatas;
    private Integer cicloId;
    private ArrayList<Ciclo> listCiclo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mainList = (ListView) findViewById(R.id.mainList);
        criarBancoDados();
        listarCiclos();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                incluirCiclo();
            }
        });

        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                cicloId = cicloIds.get(i);
                AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
                msgBox.setTitle(R.string.excluir);
                msgBox.setIcon(android.R.drawable.ic_menu_delete);
                msgBox.setMessage(R.string.certeza_excluir_ciclo);
                msgBox.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removerCiclo(cicloId);
                    }
                });
                msgBox.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                msgBox.show();
                return true;
                */
                cicloId = cicloIds.get(i);
                AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                View my_view = getLayoutInflater().inflate(R.layout.excluir_ciclo_alert, null);
                builder.setView(my_view);
                builder.setTitle(R.string.excluir);
                builder.setIcon(android.R.drawable.ic_menu_delete);
                final RadioButton radioButtonCertezaExcluir = (RadioButton) my_view.findViewById(R.id.radioButtonCertezaExcluir);
                Button buttonCertezaExcluir = (Button) my_view.findViewById(R.id.buttonCertezaExcluir);
                Button buttonCancelaExcluir = (Button) my_view.findViewById(R.id.buttonCancelaExcluir);
                final AlertDialog dialog = builder.create();
                buttonCancelaExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                buttonCertezaExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(radioButtonCertezaExcluir.isChecked()) {
                            removerCiclo(cicloId);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirCiclo(cicloIds.get(i));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sobre) {
            AlertDialog.Builder sobreBox = new AlertDialog.Builder(MainActivity.this);
            sobreBox.setTitle(R.string.info);
            sobreBox.setIcon(android.R.drawable.ic_menu_info_details);
            sobreBox.setMessage(R.string.sobre_msg);
            sobreBox.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            sobreBox.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarCiclos();
    }

    private void criarBancoDados(){
        try {
            bancoDados = openOrCreateDatabase("anotamob", MODE_PRIVATE, null);
            //bancoDados.execSQL("DROP TABLE anotacao");
            //bancoDados.execSQL("DROP TABLE ciclo");
            //bancoDados.execSQL("DROP TABLE codigo_anotacao");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS ciclo (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , data TIMESTAMP NOT NULL )");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS codigo_anotacao (" +
                    " codigo INTEGER PRIMARY KEY" +
                    " , descricao VARCHAR)");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS anotacao (" +
                    " id INTEGER PRIMARY KEY" +
                    " , id_ciclo INTEGER NOT NULL" +
                    " , dia INTEGER NOT NULL" +
                    " , data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    " , codigo INTEGER NOT NULL" +
                    " , relacao INTEGER NOT NULL DEFAULT 0" +
                    " , observacao VARCHAR NOT NULL DEFAULT 0" +
                    " , FOREIGN KEY (codigo) REFERENCES codigo_anotacao (codigo)" +
                    ", FOREIGN KEY (id_ciclo) REFERENCES cliclo (id) )");
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM codigo_anotacao", null);
            if (cursor.moveToFirst()) {
                // Não fazer nada. Os códigos já estão cadastrados.
            } else {
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (1,'Sangramento.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (2,'Manchas de sangue.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (3,'Dias em que não se observa muco e existe uma sensação de secura.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (4,'Dias de muco. Indica possível fertilidade. Também usado para o dia após o sexo na fase pré-ovulatória.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (5,'Padrão Básico de Infertilidade ou a partir do quarto dia após o pico. Seco ou muco que não é molhado ou escorregadio.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (6,'Pico.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (7,'Um dia após o pico. Possivelmente fértil. Seco ou muco que não é molhado ou escorregadio.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (8,'Dois dias após o pico. Possivelmente fértil. Seco ou muco que não é molhado ou escorregadio.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (9,'Três dias após o pico. Possivelmente fértil. Seco ou muco que não é molhado ou escorregadio.')");
                bancoDados.execSQL(" INSERT INTO codigo_anotacao (codigo, descricao) values (10,'Relação')");
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void incluirCiclo(){
        Intent intent = new Intent(this,IncluirCicloActivity.class);
        startActivity(intent);
    }

    private void listarCiclos(){
        try {
            bancoDados = openOrCreateDatabase("anotamob", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,strftime('%d/%m/%Y', data) as data FROM ciclo ORDER BY strftime('%Y%m%d', data) DESC", null);
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaData = cursor.getColumnIndex("data");
            cicloIds = new ArrayList<Integer>();
            cicloDatas = new ArrayList<String>();
            listCiclo = new ArrayList();
            if(cursor.moveToFirst()) {
                do {
                    Integer id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                    String dataStr = cursor.getString(cursor.getColumnIndex("data"));
                    Ciclo ciclo = new Ciclo(id, dataStr);
                    listCiclo.add(ciclo);
                    cicloIds.add(Integer.parseInt(cursor.getString(indiceColunaId)));
                } while (cursor.moveToNext());
            }
            MainAdapter adapter = new MainAdapter(
                    this,
                    R.layout.linha_ciclo,
                    listCiclo
            );
            mainList.setAdapter(adapter);
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void removerCiclo(Integer cicloId){
        try {
            bancoDados = openOrCreateDatabase("anotamob",MODE_PRIVATE,null);
            bancoDados.execSQL("DELETE FROM anotacao WHERE id_ciclo ="+cicloId.toString());
            bancoDados.execSQL("DELETE FROM ciclo WHERE id ="+cicloId.toString());
            bancoDados.close();
            Toast.makeText(MainActivity.this, R.string.ciclo_excluido, Toast.LENGTH_SHORT).show();
            listarCiclos();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirCiclo(Integer cicloId){
        //Intent intentCiclo = new Intent(this,CicloActivity.class);
        Intent intentCiclo = new Intent(this,CicloActivity.class);
        intentCiclo.putExtra("cicloId",cicloId);
        startActivity(intentCiclo);
    }

}
