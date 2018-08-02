package pooa20181.iff.edu.br.trabalho03_2018_1.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    public static boolean validate(Activity activity, int requestCode, String... permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (! (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) ) {
                list.add(permission);
            }
        }
        if (list.isEmpty()) {
            return true;
        }

        String[] newPermissions = new String[list.size()];
        list.toArray(newPermissions);
        ActivityCompat.requestPermissions(activity, newPermissions, 1);

        return false;
    }
}