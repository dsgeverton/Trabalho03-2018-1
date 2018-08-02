package pooa20181.iff.edu.br.trabalho03_2018_1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import io.realm.Realm;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Mecanico;
import pooa20181.iff.edu.br.trabalho03_2018_1.util.GeoLocation;

public class MecanicoDetalhesActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewHolder mViewHolder = new ViewHolder();
    private Realm realm;
    private Mecanico mecanico;
    private String idMecanico;
    SimpleDateFormat maskData = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecanico_detalhes);

        this.mViewHolder.nomeMecanico = findViewById(R.id.edtTextInfoNomeMecanico);
        this.mViewHolder.funcaoMecanico = findViewById(R.id.edtTextInfoFuncaoMecanico);
        this.mViewHolder.dataNascimentoMecanico = findViewById(R.id.editTextInfoDataNascimentoMecanico);
        this.mViewHolder.ruaMecanico = findViewById(R.id.edtTextInfoRuaMecanico);
        this.mViewHolder.bairroMecanico = findViewById(R.id.edtTextinfoBairroMecanico);
        this.mViewHolder.municipioMecanico = findViewById(R.id.edtTextInfoMunicipioMecanico);
        this.mViewHolder.latitudeMecanico = findViewById(R.id.edtTextInfoLatitudeMecanico);
        this.mViewHolder.longitudeMecanico = findViewById(R.id.edtTextInfoLongitudeMecanico);
        this.mViewHolder.salvar = findViewById(R.id.buttonSalvarMecanico);
        this.mViewHolder.scroll = findViewById(R.id.scrollViewMecanico);
        this.mViewHolder.linear = findViewById(R.id.linearMecanico);
        this.mViewHolder.excluir = findViewById(R.id.excluirMecanico);
        this.mViewHolder.habilitarEdicao = findViewById(R.id.switchHabilitarEdicaoMecanico);

        this.mViewHolder.excluir.setOnClickListener(this);
        this.mViewHolder.salvar.setOnClickListener(this);

        Intent intent = getIntent();
        idMecanico = (String) intent.getSerializableExtra("id");
        if (idMecanico.equals("0"))
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
                    mViewHolder.nomeMecanico.setEnabled(true);
                    mViewHolder.funcaoMecanico.setEnabled(true);
                    mViewHolder.dataNascimentoMecanico.setEnabled(true);
                    mViewHolder.ruaMecanico.setEnabled(true);
                    mViewHolder.bairroMecanico.setEnabled(true);
                    mViewHolder.municipioMecanico.setEnabled(true);
                    mViewHolder.latitudeMecanico.setEnabled(true);
                    mViewHolder.longitudeMecanico.setEnabled(true);
                    mViewHolder.salvar.setEnabled(true);
                } else {
                    atualizar();
                    povoate();
                    mViewHolder.nomeMecanico.setEnabled(false);
                    mViewHolder.funcaoMecanico.setEnabled(false);
                    mViewHolder.dataNascimentoMecanico.setEnabled(false);
                    mViewHolder.ruaMecanico.setEnabled(false);
                    mViewHolder.bairroMecanico.setEnabled(false);
                    mViewHolder.municipioMecanico.setEnabled(false);
                    mViewHolder.latitudeMecanico.setEnabled(false);
                    mViewHolder.longitudeMecanico.setEnabled(false);
                    mViewHolder.salvar.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSalvarMecanico){

            if(     mViewHolder.nomeMecanico.getText().toString().equals("") ||
                    mViewHolder.ruaMecanico.getText().toString().equals("") ||
                    mViewHolder.funcaoMecanico.getText().toString().equals("") ||
                    mViewHolder.dataNascimentoMecanico.getText().toString().equals("") ||
                    mViewHolder.bairroMecanico.getText().toString().equals("") ||
                    mViewHolder.municipioMecanico.getText().toString().equals("") ||
                    mViewHolder.latitudeMecanico.getText().toString().equals("") ||
                    mViewHolder.longitudeMecanico.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Existem campos em branco!", Toast.LENGTH_SHORT).show();
            } else{
                atualizar();
                finish();
            }
        }

        if (id == R.id.excluirMecanico){
            excluir();
        }
    }

    public void povoate() {

        realm = Realm.getDefaultInstance();
        mecanico = realm.where(Mecanico.class).equalTo("id", idMecanico).findFirst();
        realm.close();

        if (!idMecanico.equals("0")){
            mViewHolder.nomeMecanico.setEnabled(false);
            mViewHolder.funcaoMecanico.setEnabled(false);
            mViewHolder.dataNascimentoMecanico.setEnabled(false);
            mViewHolder.ruaMecanico.setEnabled(false);
            mViewHolder.bairroMecanico.setEnabled(false);
            mViewHolder.municipioMecanico.setEnabled(false);
            mViewHolder.latitudeMecanico.setEnabled(false);
            mViewHolder.longitudeMecanico.setEnabled(false);
            mViewHolder.salvar.setEnabled(false);

            mViewHolder.nomeMecanico.setText(mecanico.getNome());
            mViewHolder.funcaoMecanico.setText(mecanico.getFuncao());
            mViewHolder.dataNascimentoMecanico.setText(mecanico.getDataNascimento().toString());
            mViewHolder.ruaMecanico.setText(mecanico.getRua());
            mViewHolder.bairroMecanico.setText(mecanico.getBairro());
            mViewHolder.municipioMecanico.setText(mecanico.getMunicipio());
            mViewHolder.latitudeMecanico.setText(mecanico.getLatitude());
            mViewHolder.longitudeMecanico.setText(mecanico.getLongitude());
            mViewHolder.salvar.setText(R.string.atualizar);
        }
    }

    public void atualizar() {

        String token;
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (idMecanico.equals("0")) {
            mecanico = realm.where(Mecanico.class).equalTo("id", idMecanico).findFirst();
            if (mecanico == null) {
                token = getRandomHexString();
                mecanico = new Mecanico();
                mecanico.setId(token);
            }
        }

        mecanico.setNome(mViewHolder.nomeMecanico.getText().toString());
        mecanico.setFuncao(mViewHolder.funcaoMecanico.getText().toString());
        try {
            mecanico.setDataNascimento(maskData.parse(mViewHolder.dataNascimentoMecanico.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mecanico.setRua(mViewHolder.ruaMecanico.getText().toString());
        mecanico.setBairro(mViewHolder.bairroMecanico.getText().toString());
        mecanico.setMunicipio(mViewHolder.municipioMecanico.getText().toString());
        mecanico.setLatitude(mViewHolder.latitudeMecanico.getText().toString());
        mecanico.setLongitude(mViewHolder.longitudeMecanico.getText().toString());

        Address localizacao = new GeoLocation().location(mViewHolder.ruaMecanico.getText().toString());

        mecanico.setLatitude(String.valueOf(localizacao.getLatitude()));
        mecanico.setLongitude(String.valueOf(localizacao.getLongitude()));

        realm.copyToRealmOrUpdate(mecanico);
        realm.commitTransaction();
        realm.close();
    }

    private void excluir() {

        new AlertDialog.Builder(this).setTitle("Deletar Mecânico").
                setMessage("Tem certeza que deseja excluir este Mecânico?").
                setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        mecanico.deleteFromRealm();
                        realm.commitTransaction();
                        realm.close();
                        finish();
                    }
                }).setNegativeButton("Não", null).show();
    }

    private static class ViewHolder{
        EditText nomeMecanico, funcaoMecanico, dataNascimentoMecanico, ruaMecanico, bairroMecanico, municipioMecanico, latitudeMecanico, longitudeMecanico;
        Button salvar;
        Switch habilitarEdicao;
        TextView excluir;
        ScrollView scroll;
        LinearLayout linear;
    }

    public String getRandomHexString(){
        int numchars = 6;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numchars);
    }


}
