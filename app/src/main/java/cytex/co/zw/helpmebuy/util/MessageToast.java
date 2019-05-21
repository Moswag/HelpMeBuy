package cytex.co.zw.helpmebuy.util;

import android.content.Context;
import android.widget.Toast;

public class MessageToast {

    public static void show(Context context, String mesage){
        Toast.makeText(context,mesage,Toast.LENGTH_LONG).show();
    }
}
