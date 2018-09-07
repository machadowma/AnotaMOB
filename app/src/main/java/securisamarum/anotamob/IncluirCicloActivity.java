package securisamarum.anotamob;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class IncluirCicloActivity extends AppCompatActivity {
    private Button incluirCicloButton;
    private EditText incluirCicloText;
    private DatePickerDialog datePickerDialog;
    private String date_sql;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_ciclo);

        findViewsById();
        incluirCicloText.setInputType(InputType.TYPE_NULL);
        date_sql = "";

        incluirCicloText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(IncluirCicloActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date_sql = year + "-";
                        if((monthOfYear+1) < 10){
                            date_sql = date_sql + "0" + (monthOfYear+1) + "-";
                        } else {
                            date_sql = date_sql + "" + (monthOfYear+1) + "-";
                        }
                        if(dayOfMonth < 10){
                            date_sql  = date_sql + "0" + dayOfMonth;
                        } else {
                            date_sql  = date_sql + "" + dayOfMonth;
                        }

                        incluirCicloText.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        incluirCicloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incluirCiclo();
            }
        });

    }

    private void findViewsById(){
        incluirCicloButton = (Button) findViewById(R.id.incluirCicloButton);
        incluirCicloText = (EditText) findViewById(R.id.incluirCicloText);

    }

    private void incluirCiclo(){
        if(date_sql.equals("")) {
            Toast.makeText(this, R.string.informe_data_ini_ciclo, Toast.LENGTH_SHORT).show();
        } else {
            try{
                date_sql = date_sql;
                bancoDados = openOrCreateDatabase("anotamob",MODE_PRIVATE,null);
                bancoDados.execSQL("INSERT INTO ciclo (data) VALUES ('"+date_sql+"')");
                Cursor cursor = bancoDados.rawQuery("select seq from sqlite_sequence where name='ciclo'",null);
                cursor.moveToFirst();
                String seq = cursor.getString(0);
                bancoDados.execSQL("INSERT INTO anotacao (id_ciclo,dia,data,codigo,relacao,observacao) " +
                        "VALUES ("+seq+",1,'"+date_sql+"',1,0,'Sangramento.')");
                Toast.makeText(this, R.string.ciclo_incluido, Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}

