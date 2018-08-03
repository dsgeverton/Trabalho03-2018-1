package pooa20181.iff.edu.br.trabalho03_2018_1.activity;

import android.Manifest;
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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TabWidget;
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
import java.util.List;
import java.util.Random;
import io.realm.Realm;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Oficina;
import pooa20181.iff.edu.br.trabalho03_2018_1.util.PermissionUtils;

public class OficinaDetalhesActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private ViewHolder mViewHolder = new ViewHolder();
    private Realm realm;
    private Oficina oficina;
    private String idOficina;

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

        mViewHolder.latitudeOficina.setEnabled(false);
        mViewHolder.longitudeOficina.setEnabled(false);
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
                    mViewHolder.nomeOficina.setEnabled(true);
                    mViewHolder.ruaOficina.setEnabled(true);
                    mViewHolder.bairroOficina.setEnabled(true);
                    mViewHolder.municipioOficina.setEnabled(true);
                    mViewHolder.salvar.setEnabled(true);
                } else {
                    buscar();
                    atualizar();
                    povoate();
                    mViewHolder.nomeOficina.setEnabled(false);
                    mViewHolder.ruaOficina.setEnabled(false);
                    mViewHolder.bairroOficina.setEnabled(false);
                    mViewHolder.municipioOficina.setEnabled(false);
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
        LatLng addressLocation = new LatLng(Double.parseDouble(mViewHolder.latitudeOficina.getText().toString()), Double.parseDouble(mViewHolder.longitudeOficina.getText().toString()));
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
        if (id == R.id.buttonSalvarOficina){
            buscar();
            atualizar();
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
            mViewHolder.salvar.setEnabled(false);

            mViewHolder.nomeOficina.setText(oficina.getNome());
            mViewHolder.ruaOficina.setText(oficina.getRua());
            mViewHolder.bairroOficina.setText(oficina.getBairro());
            mViewHolder.municipioOficina.setText(oficina.getMunicipio());
            mViewHolder.latitudeOficina.setText(oficina.getLatitude());
            mViewHolder.longitudeOficina.setText(oficina.getLongitude());
            mViewHolder.salvar.setText(R.string.atualizar);
        }
    }

    public void atualizar() {

        if(     mViewHolder.nomeOficina.getText().toString().equals("") ||
                mViewHolder.ruaOficina.getText().toString().equals("") ||
                mViewHolder.bairroOficina.getText().toString().equals("") ||
                mViewHolder.municipioOficina.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Existem campos em branco!", Toast.LENGTH_SHORT).show();
        } else {
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

            finish();
        }
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
        Button salvar, location;
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

    private synchronized void callConnection() {
        Log.i("LOG", "callConnection()");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void buscar() {

        if ((mViewHolder.latitudeOficina.getText() == null) &&
                (mViewHolder.longitudeOficina.getText() == null) &&
                (mViewHolder.ruaOficina.getText() == null)) {
            Toast.makeText(this, "Existem campos em branco!", Toast.LENGTH_LONG).show();

        } else {
            Log.i("LOG", "Criando busca");

            StringBuilder resultAddress = new StringBuilder();

            try {
                Address endereco = getEndereco(mViewHolder.ruaOficina.getText().toString());

                if (endereco == null){
                    new AlertDialog.Builder(this).setTitle("Alerta").setMessage("O endereço não foi encontrado").setPositiveButton("OK", null);
                } else {
                    Log.i("LOG", "Atualizar " + endereco.getThoroughfare());

                    for (int i = 0, tam = endereco.getMaxAddressLineIndex(); i < tam; i++) {
                        resultAddress.append(endereco.getAddressLine(i));
                        resultAddress.append(i < tam - 1 ? ", " : "");
                        Log.i("LOG", "Result " + resultAddress);
                    }
                    mViewHolder.ruaOficina.setText(endereco.getThoroughfare());
                    mViewHolder.municipioOficina.setText(endereco.getSubAdminArea());
                    mViewHolder.latitudeOficina.setText(String.valueOf(endereco.getLatitude()));
                    mViewHolder.longitudeOficina.setText(String.valueOf(endereco.getLongitude()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Address getEndereco(String streetName) throws IOException {

        Geocoder geocoder;
        Address endereco = null;
        List<Address> enderecos;
        geocoder = new Geocoder(getApplicationContext());
        enderecos = geocoder.getFromLocationName(streetName, 5);
        if (enderecos.size() > 0){
            Log.i("LOG", "Endereços ---> " + String.valueOf(enderecos.size()));
            endereco = enderecos.get(0);
        }
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
