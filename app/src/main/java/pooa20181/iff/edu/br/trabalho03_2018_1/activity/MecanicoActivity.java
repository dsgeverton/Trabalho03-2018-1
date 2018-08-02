package pooa20181.iff.edu.br.trabalho03_2018_1.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.List;
import io.realm.Realm;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;
import pooa20181.iff.edu.br.trabalho03_2018_1.adapter.ClickRecyclerViewListener;
import pooa20181.iff.edu.br.trabalho03_2018_1.adapter.MecanicoAdapter;
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Mecanico;

public class MecanicoActivity extends AppCompatActivity implements ClickRecyclerViewListener {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecanico);

        realm = Realm.getDefaultInstance();
        FloatingActionButton fab = findViewById(R.id.fabMecanico);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MecanicoActivity.this, MecanicoDetalhesActivity.class);
                intent.putExtra("id","0"                                                 );
                startActivity(intent);
            }
        });
    }
    private List<Mecanico> getMecanicos(){
        return realm.where(Mecanico.class).findAll();
    }
    @Override
    public void onClick(Object object) {
        Mecanico mecanico = (Mecanico) object;
        Intent intent = new Intent(MecanicoActivity.this, MecanicoDetalhesActivity.class);
        intent.putExtra("id", mecanico.getId());
        startActivity(intent);
    }
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.rv_Mecanicos);
        recyclerView.setAdapter(new MecanicoAdapter(getMecanicos(),this, this));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
    }
    @Override
    public void finish(){
        super.finish();
        realm.close();
    }
}
