package pooa20181.iff.edu.br.trabalho03_2018_1.util;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class GeoLocation extends AppCompatActivity {

    public Address location(String rua){
        List<Address> enderecos = null;
        Address endereco;
        Geocoder geocoder;
        geocoder = new Geocoder(getApplicationContext());

        try {
            enderecos = geocoder.getFromLocationName(rua,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert enderecos != null;
        endereco = enderecos.get(0);

        return endereco;
    }
}
