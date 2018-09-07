package securisamarum.anotamob;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainAdapter extends BaseAdapter{
    private final List<Ciclo> ciclos;
    private SQLiteDatabase bancoDados;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public MainAdapter(Context context, int resource, List<Ciclo> ciclos ) {
        this.context = context;
        this.resource = resource;
        this.ciclos = ciclos;
    }

    @Override
    public int getCount() {
        return ciclos.size();
    }

    @Override
    public Object getItem(int position) {
        return ciclos.get(position);
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

        final Ciclo ciclo = ciclos.get(position);

        TextView ciclo_data = (TextView) view.findViewById(R.id.ciclo_data);

        ciclo_data.setText(ciclo.getDataStr());

        return view;
    }
}
