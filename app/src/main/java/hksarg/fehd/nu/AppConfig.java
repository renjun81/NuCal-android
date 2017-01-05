package hksarg.fehd.nu;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class AppConfig {

    public static void showMessageDialog(Context context, int nMsgResId){
        String szMsg = context.getResources().getString(nMsgResId);
        showMessageDialog(context, szMsg);
    }

    public static void showMessageDialog(Context context, String szMsg){
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMsg.setText(szMsg);
        dialog.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void gotoHome(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }
}
