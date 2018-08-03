package pooa20181.iff.edu.br.trabalho03_2018_1.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Mecanico;
import pooa20181.iff.edu.br.trabalho03_2018_1.util.PermissionUtils;

public class MecanicoDetalhesActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, OnMapReadyCallback {

    private ViewHolder mViewHolder = new ViewHolder();
    private Realm realm;
    private Mecanico mecanico;
    private String idMecanico;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat maskData = new SimpleDateFormat("dd/MM/yyyy");

    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;

    // permissions
    String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };


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

        mViewHolder.latitudeMecanico.setEnabled(false);
        mViewHolder.longitudeMecanico.setEnabled(false);
        this.mViewHolder.excluir.setOnClickListener(this);
        this.mViewHolder.salvar.setOnClickListener(this);

        Intent intent = getIntent();
        idMecanico = (String) intent.getSerializableExtra("id");
        if (idMecanico.equals("0")) {
            this.mViewHolder.habilitarEdicao.setVisibility(View.INVISIBLE);
            this.mViewHolder.habilitarEdicao.setActivated(false);
            this.mViewHolder.habilitarEdicao.setSplitTrack(false);
            this.mViewHolder.excluir.setVisibility(View.INVISIBLE);
            this.mViewHolder.excluir.setClickable(false);

        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        povoate();

        this.mViewHolder.habilitarEdicao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mViewHolder.nomeMecanico.setEnabled(true);
                    mViewHolder.funcaoMecanico.setEnabled(true);
                    mViewHolder.dataNascimentoMecanico.setEnabled(true);
                    mViewHolder.ruaMecanico.setEnabled(true);
                    mViewHolder.bairroMecanico.setEnabled(true);
                    mViewHolder.municipioMecanico.setEnabled(true);
                    mViewHolder.salvar.setEnabled(true);
                } else {
                    buscar();
                    atualizar();
                    povoate();
                    mViewHolder.nomeMecanico.setEnabled(false);
                    mViewHolder.funcaoMecanico.setEnabled(false);
                    mViewHolder.dataNascimentoMecanico.setEnabled(false);
                    mViewHolder.ruaMecanico.setEnabled(false);
                    mViewHolder.bairroMecanico.setEnabled(false);
                    mViewHolder.municipioMecanico.setEnabled(false);
                    mViewHolder.salvar.setEnabled(false);
                }
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        callConnection();
        PermissionUtils.validate(this, 0, permissoes);

        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng addressLocation = new LatLng(Double.parseDouble(mViewHolder.latitudeMecanico.getText().toString()), Double.parseDouble(mViewHolder.longitudeMecanico.getText().toString()));
        mMap.addMarker(new MarkerOptions().position(addressLocation).title("Marker in Address registered"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(addressLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(addressLocation, 14.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("Posição"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 550.0f));
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermissionUtils.validate(this, 0, permissoes);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSalvarMecanico) {
            buscar();
            atualizar();
            finish();
        }

        if (id == R.id.excluirMecanico) {
            excluir();
        }
    }

    public void povoate() {

        realm = Realm.getDefaultInstance();
        mecanico = realm.where(Mecanico.class).equalTo("id", idMecanico).findFirst();
        realm.close();

        if (!idMecanico.equals("0")) {
            mViewHolder.nomeMecanico.setEnabled(false);
            mViewHolder.funcaoMecanico.setEnabled(false);
            mViewHolder.dataNascimentoMecanico.setEnabled(false);
            mViewHolder.ruaMecanico.setEnabled(false);
            mViewHolder.bairroMecanico.setEnabled(false);
            mViewHolder.municipioMecanico.setEnabled(false);
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

        if (    mViewHolder.nomeMecanico.getText().toString().equals("") ||
                mViewHolder.ruaMecanico.getText().toString().equals("") ||
                mViewHolder.funcaoMecanico.getText().toString().equals("") ||
                mViewHolder.dataNascimentoMecanico.getText().toString().equals("") ||
                mViewHolder.bairroMecanico.getText().toString().equals("") ||
                mViewHolder.municipioMecanico.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Existem campos em branco!", Toast.LENGTH_SHORT).show();
        } else {
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

            realm.copyToRealmOrUpdate(mecanico);
            realm.commitTransaction();
            realm.close();
        }
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

    private static class ViewHolder {
        EditText nomeMecanico, funcaoMecanico, dataNascimentoMecanico, ruaMecanico, bairroMecanico, municipioMecanico, latitudeMecanico, longitudeMecanico;
        Button salvar;
        Switch habilitarEdicao;
        TextView excluir;
        ScrollView scroll;
        LinearLayout linear;
        Fragment fragment;
    }

    public String getRandomHexString() {
        int numchars = 6;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numchars);
    }

    private synchronized void callConnection() {
        Log.i("LOG", "callConnection()");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void buscar() {

        if ((mViewHolder.latitudeMecanico.getText() == null) &&
                (mViewHolder.longitudeMecanico.getText() == null) &&
                (mViewHolder.ruaMecanico.getText() == null)) {
            Toast.makeText(this, "Existem campos em branco!", Toast.LENGTH_LONG).show();

        } else {
            Log.i("LOG", "Criando busca");

            StringBuilder resultAddress = new StringBuilder();

            try {
                Address endereco = getEndereco(mViewHolder.ruaMecanico.getText().toString());
                Log.i("LOG", "Atualizar " + endereco.getThoroughfare());

                for (int i = 0, tam = endereco.getMaxAddressLineIndex(); i < tam; i++) {
                    resultAddress.append(endereco.getAddressLine(i));
                    resultAddress.append(i < tam - 1 ? ", " : "");
                    Log.i("LOG", "Result " + resultAddress);
                }
                mViewHolder.ruaMecanico.setText(endereco.getThoroughfare());
                mViewHolder.municipioMecanico.setText(endereco.getSubAdminArea());
                mViewHolder.latitudeMecanico.setText(String.valueOf(endereco.getLatitude()));
                mViewHolder.longitudeMecanico.setText(String.valueOf(endereco.getLongitude()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Address getEndereco(String streetName) throws IOException {

        Geocoder geocoder;
        Address endereco;
        List<Address> enderecos;
        geocoder = new Geocoder(getApplicationContext());
        enderecos = geocoder.getFromLocationName(streetName, 5);
        if (enderecos.size() > 0)
            Log.i("LOG", "Endereços ---> " + String.valueOf(enderecos.size()));
        endereco = enderecos.get(0);
        return endereco;
    }

    public void onResume() {
        super.onResume();

        if (googleApiClient != null && googleApiClient.isConnected())
            startLocationUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (googleApiClient != null) {
            stopLocationUpdate();
        }
    }

    private void initLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        initLocationRequest();
        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    private void stopLocationUpdate() {
        //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override

    public void onConnected(@Nullable Bundle bundle) {
        Log.i("LOG", "UpdateLocationActivity.onConnected(" + bundle + ")");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            startLocationUpdate();
                        }
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "UpdateLocationActivity.onConnectionSuspended(" + i + ")");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOG", "UpdateLocationActivity.onConnectionFailed(" + connectionResult + ")");

    }

}
