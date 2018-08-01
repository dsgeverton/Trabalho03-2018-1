package pooa20181.iff.edu.br.trabalho03_2018_1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import io.realm.Realm;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Oficina;

public class OficinaDetalhesActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private Realm realm;
    private Oficina oficina;
    private String idOficina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oficina_detalhes);

        this.mViewHolder.nomeOficina = findViewById(R.id.edtTextInfoNomeOficina);
        this.mViewHolder.ruaOficina = findViewById(R.id.edtTextInfoRuaOficina);
        this.mViewHolder.bairroOficina = findViewById(R.id.edtTextinfoBairroOficina);
        this.mViewHolder.municipioOficina = findViewById(R.id.edtTextInfoMunicipioOficina);
        this.mViewHolder.latitudeOficina = findViewById(R.id.edtTextInfoLatitudeOficina);
        this.mViewHolder.longitudeOficina = findViewById(R.id.edtTextInfoLongitudeOficina);
        this.mViewHolder.salvar = findViewById(R.id.buttonSalvarOficina);
        this.mViewHolder.scroll = findViewById(R.id.scrollViewOficina);
        this.mViewHolder.linear = findViewById(R.id.linearOficina);
        this.mViewHolder.excluir = findViewById(R.id.excluirOficina);
        this.mViewHolder.habilitarEdicao = findViewById(R.id.switchHabilitarEdicaoOficina);

        this.mViewHolder.excluir.setOnClickListener(this);
        this.mViewHolder.salvar.setOnClickListener(this);

        Intent intent = getIntent();
        idOficina = (String) intent.getSerializableExtra("id");
        if (idOficina.equals("0"))
        {
            this.mViewHolder.habilitarEdicao.setVisibility(View.INVISIBLE);
            this.mViewHolder.habilitarEdicao.setActivated(false);
            this.mViewHolder.habilitarEdicao.setSplitTrack(false);
            this.mViewHolder.excluir.setVisibility(View.INVISIBLE);
            this.mViewHolder.excluir.setClickable(false);
        }

        povoate();

        this.mViewHolder.habilitarEdicao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mViewHolder.nomeOficina.setEnabled(true);
                    mViewHolder.ruaOficina.setEnabled(true);
                    mViewHolder.bairroOficina.setEnabled(true);
                    mViewHolder.municipioOficina.setEnabled(true);
                    mViewHolder.latitudeOficina.setEnabled(true);
                    mViewHolder.longitudeOficina.setEnabled(true);
                    mViewHolder.salvar.setEnabled(true);
                } else {
                    atualizar();
                    povoate();
                    mViewHolder.nomeOficina.setEnabled(false);
                    mViewHolder.ruaOficina.setEnabled(false);
                    mViewHolder.bairroOficina.setEnabled(false);
                    mViewHolder.municipioOficina.setEnabled(false);
                    mViewHolder.latitudeOficina.setEnabled(false);
                    mViewHolder.longitudeOficina.setEnabled(false);
                    mViewHolder.salvar.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSalvarOficina){

            if(     mViewHolder.nomeOficina.getText().toString().equals("") ||
                    mViewHolder.ruaOficina.getText().toString().equals("") ||
                    mViewHolder.bairroOficina.getText().toString().equals("") ||
                    mViewHolder.municipioOficina.getText().toString().equals("") ||
                    mViewHolder.latitudeOficina.getText().toString().equals("") ||
                    mViewHolder.longitudeOficina.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Existem campos em branco!", Toast.LENGTH_SHORT).show();
            } else{
                atualizar();
                finish();
            }
        }

        if (id == R.id.excluirOficina){
            excluir();
        }
    }

    public void povoate() {

        realm = Realm.getDefaultInstance();
        oficina = realm.where(Oficina.class).equalTo("id", idOficina).findFirst();
        realm.close();

        if (!idOficina.equals("0")){
            mViewHolder.nomeOficina.setEnabled(false);
            mViewHolder.ruaOficina.setEnabled(false);
            mViewHolder.bairroOficina.setEnabled(false);
            mViewHolder.municipioOficina.setEnabled(false);
            mViewHolder.latitudeOficina.setEnabled(false);
            mViewHolder.longitudeOficina.setEnabled(false);
            mViewHolder.salvar.setEnabled(false);

            mViewHolder.nomeOficina.setText(oficina.getNome());
            mViewHolder.ruaOficina.setText(oficina.getRua());
            mViewHolder.bairroOficina.setText(oficina.getBairro());
            mViewHolder.municipioOficina.setText(oficina.getMunicipio());
            mViewHolder.latitudeOficina.setText(oficina.getLatitude());
            mViewHolder.longitudeOficina.setText(oficina.getLongitude());
            mViewHolder.salvar.setText("Atualizar");
        }
    }

    public void atualizar() {

        String token;
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (idOficina.equals("0")) {
            oficina = realm.where(Oficina.class).equalTo("id", idOficina).findFirst();
            if (oficina == null) {
                token = getRandomHexString();
                oficina = new Oficina();
                oficina.setId(token);
            }
        }

        oficina.setNome(mViewHolder.nomeOficina.getText().toString());
        oficina.setRua(mViewHolder.ruaOficina.getText().toString());
        oficina.setBairro(mViewHolder.bairroOficina.getText().toString());
        oficina.setMunicipio(mViewHolder.municipioOficina.getText().toString());
        oficina.setLatitude(mViewHolder.latitudeOficina.getText().toString());
        oficina.setLongitude(mViewHolder.longitudeOficina.getText().toString());

        realm.copyToRealmOrUpdate(oficina);
        realm.commitTransaction();
        realm.close();
    }

    private void excluir() {

        new AlertDialog.Builder(this).setTitle("Deletar Oficina").
                setMessage("Tem certeza que deseja excluir esta Oficina?").
                setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        oficina.deleteFromRealm();
                        realm.commitTransaction();
                        realm.close();
                        finish();
                    }
                }).setNegativeButton("Não", null).show();
    }

    private static class ViewHolder{
        EditText nomeOficina, ruaOficina, bairroOficina, municipioOficina, latitudeOficina, longitudeOficina;
        Button salvar;
        Switch habilitarEdicao;
        TextView excluir;
        ScrollView scroll;
        LinearLayout linear;
    }

    public String getRandomHexString(){
        int numchars = 6;
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numchars);
    }
}
