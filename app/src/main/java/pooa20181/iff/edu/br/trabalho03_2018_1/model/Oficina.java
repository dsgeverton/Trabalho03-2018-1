package pooa20181.iff.edu.br.trabalho03_2018_1.model;

import java.io.Serializable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Oficina extends RealmObject implements Serializable{

    @PrimaryKey
    private int id;
    private String nome;
    private String rua;
    private String bairro;
    private String municipio;
    private String latitude;
    private String longitude;

    public Oficina() {}

    public Oficina(int id, String nome, String rua, String bairro, String municipio, String latitude, String longitude) {
        this.id = id;
        this.nome = nome;
        this.rua = rua;
        this.bairro = bairro;
        this.municipio = municipio;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRua() {
        return rua;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
