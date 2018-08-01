package pooa20181.iff.edu.br.trabalho03_2018_1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.List;
import io.realm.Realm;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;
import pooa20181.iff.edu.br.trabalho03_2018_1.adapter.ClickRecyclerViewListener;
import pooa20181.iff.edu.br.trabalho03_2018_1.adapter.OficinaAdapter;
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Oficina;

public class OficinaActivity extends AppCompatActivity implements ClickRecyclerViewListener{

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecanico);

        realm = Realm.getDefaultInstance();
        FloatingActionButton fab = findViewById(R.id.fabOficina);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OficinaActivity.this, OficinaDetalhesActivity.class);
                intent.putExtra("id",0);
                startActivity(intent);
            }
        });
    }

    private List<Oficina> getOficinas(){
        return realm.where(Oficina.class).findAll();
    }

    @Override
    public void onClick(Object object) {
        Oficina oficina = (Oficina) object;
        Intent intent = new Intent(OficinaActivity.this, OficinaDetalhesActivity.class);
        intent.putExtra("id", oficina.getId());
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.rv_Oficina);
        recyclerView.setAdapter(new OficinaAdapter(getOficinas(),this, this));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
    }

    @Override
    public void finish(){
        realm.close();
    }
}
