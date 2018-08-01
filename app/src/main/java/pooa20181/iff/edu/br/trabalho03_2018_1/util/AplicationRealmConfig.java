package pooa20181.iff.edu.br.trabalho03_2018_1.util;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AplicationRealmConfig extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
        builder.name("trabalho0320181.realm");
        builder.schemaVersion(0);
        builder.deleteRealmIfMigrationNeeded();
        RealmConfiguration realmConfiguration = builder.build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
