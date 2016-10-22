package hksarg.fehd.nutab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final SharedPreferences sp=getSharedPreferences("nutrition.txt", Context.MODE_PRIVATE);
        boolean isSetProfile = sp.getBoolean("set_profile",false);

        View btnProfile= findViewById(R.id.user_profile);
        findViewById(R.id.btnLeft).setVisibility(View.GONE);
        ImageView btnRight = (ImageView) findViewById(R.id.btnRight);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.menu4d_title);

        if ( getIntent().getBooleanExtra("is_help", false) ) {
            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            btnProfile.setVisibility(View.GONE);
        }
        else {
            if(isSetProfile){
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
            else {
                btnRight.setVisibility(View.GONE);
                btnProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("login", true);
                        editor.commit();
                        Intent intent = new Intent(HelpActivity.this, UserProfileActivity.class);
                        intent.putExtra("back", "back");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.help_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.home_menu:
//                onBackPressed();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
