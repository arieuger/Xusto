package dam.xusto;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.util.Locale;

public class MainActivity extends Activity implements TarefaDescargaXML.Cliente, SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private static final int DESCARGA_TENDAS = 10;
    private static SQLiteDatabase db;
    private TabHost tabs;
    private ListView lvTendas;
    private Spinner spProductosTenda;
    private SearchView svTendas;
    private SimpleCursorAdapter sca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creación da base de datos
        if (db == null) {
            db = new XustoDBOpenHelper(this).getWritableDatabase();
        }

        tabs = (TabHost)findViewById(R.id.tabHost);
        tabs.setup();

        lvTendas = (ListView) findViewById(R.id.lvTendas);
        svTendas = (SearchView) findViewById(R.id.svTendas);
        spProductosTenda = (Spinner) findViewById(R.id.spProductosTenda);

        TabHost.TabSpec spec = tabs.newTabSpec("tabTendas");
        spec.setContent(R.id.tabTendas);
        spec.setIndicator(getResources().getString(R.string.tabSpecTenda));
        tabs.addTab(spec);


        spec = tabs.newTabSpec("tabProdutos");
        spec.setContent(R.id.tabProdutos);
        spec.setIndicator(getResources().getString(R.string.tabSpecProduto));
        tabs.addTab(spec);

        encherLvTendasSpProductos(Tenda.getAllCursor());
        lvTendas.setTextFilterEnabled(true);    // para a búsqueda con searchview
        lvTendas.setOnItemClickListener(this);
        svTendas.setOnQueryTextListener(this);
    }

    // MÉTODOS PARA A IMPLEMENTACIÓN DE SEARCHVIEW
    // TODO: Implementación da barra de búsqueda
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void encherLvTendasSpProductos(Cursor c) {
        if (c.getCount() == 0) {
            // lanzar descarga
            new TarefaDescargaXML(this, DESCARGA_TENDAS).execute(Servizo.urlDescargaTendas());

        } else {
            String[] from = new String[]{"nome","enderezo"};
            int[] to = new int[]{android.R.id.text1, android.R.id.text2};

            sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,c,from,to,0);
            lvTendas.setAdapter(sca);

            // Cambiamos o sca para que, no spinner de produtos, non se vexan as direccións
            SimpleCursorAdapter sca2 = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,c,from,to,0);
            spProductosTenda.setAdapter(sca2);
        }


    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        if(resultado == null) {
            Toast.makeText(this, "Problemas de conexión" , Toast.LENGTH_LONG).show();
            return;
        }
        if (tipoDescarga == DESCARGA_TENDAS) {

            Tenda.crearDendeXML(resultado);
            encherLvTendasSpProductos(Tenda.getAllCursor());
        }


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, TendaActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
