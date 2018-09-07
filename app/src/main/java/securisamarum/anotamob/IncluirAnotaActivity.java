package securisamarum.anotamob;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncluirAnotaActivity extends AppCompatActivity {
    SQLiteDatabase bancoDados;
    Integer cicloId;

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
        setContentView(R.layout.activity_incluir_anota);

        Intent intent = getIntent();
        cicloId = Integer.parseInt(intent.getStringExtra("cicloId"));

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        // Instance of ImageAdapter Class
        gridView.setAdapter(new AnotaAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    bancoDados = openOrCreateDatabase("anotamob",MODE_PRIVATE,null);

                    Cursor cursor = bancoDados.rawQuery("SELECT MAX(dia)+1 FROM anotacao WHERE id_ciclo="+cicloId.toString(),null);
                    cursor.moveToFirst();
                    String dia = cursor.getString(0);

                    cursor = bancoDados.rawQuery("SELECT descricao FROM codigo_anotacao WHERE codigo="+Integer.toString(i+1),null);
                    cursor.moveToFirst();
                    String descricao = cursor.getString(0);

                    cursor = bancoDados.rawQuery("SELECT strftime('%Y-%m-%d', data) as data FROM ciclo WHERE id="+cicloId.toString(),null);
                    cursor.moveToFirst();
                    String data_ciclo = cursor.getString(0);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(data_ciclo));
                    c.add(Calendar.DATE, Integer.parseInt(dia)-1);
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date resultdate = new Date(c.getTimeInMillis());
                    data_ciclo = sdf.format(resultdate);

                    String sql = "INSERT INTO anotacao (id_ciclo,dia,data,codigo,relacao,observacao) " +
                            "VALUES ("+cicloId.toString()+","+dia+",'"+data_ciclo+"',"+Integer.toString(i+1)+",0,'"+descricao+"')";

                    bancoDados.execSQL(sql);
                    Toast.makeText(IncluirAnotaActivity.this, R.string.anota_incluida, Toast.LENGTH_SHORT).show();
                    bancoDados.close();

                    finish();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder diagBox = new AlertDialog.Builder(IncluirAnotaActivity.this);
                diagBox.setTitle(R.string.signific_simb);
                diagBox.setIcon(android.R.drawable.ic_menu_info_details);
                diagBox.setMessage(observacoes[i]);
                diagBox.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                diagBox.show();
                return true;
            }
        });

        gridView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });
    }

}
