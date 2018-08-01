package pooa20181.iff.edu.br.trabalho03_2018_1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewHolder.rbOficina = findViewById(R.id.rbOficina);
        mViewHolder.rbMecanico = findViewById(R.id.rbMecanico);
        mViewHolder.rgOpcao = findViewById(R.id.rgOpcao);
        mViewHolder.btnEntrar = findViewById(R.id.btnEntrar);

        mViewHolder.btnEntrar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if ( id == R.id.btnEntrar ) {
            Intent intent;

            if ( mViewHolder.rbMecanico.isChecked()){
                intent = new Intent(this, MecanicoActivity.class);
                startActivity(intent);
            } else if ( mViewHolder.rbOficina.isChecked() ){
                intent = new Intent(this, OficinaActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Nenhuma opção selecionada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class ViewHolder{
        RadioButton rbOficina, rbMecanico;
        RadioGroup rgOpcao;
        Button btnEntrar;
    }
}
