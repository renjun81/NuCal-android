package hksarg.fehd.nutab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import hksarg.fehd.nutab.model.User;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        String query = new Select("COUNT(*)").from(User.class).toSql();
        int nUsers = SQLiteUtils.intQuery(query, null);
        boolean isSetProfile = nUsers > 0;

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
                        Intent intent = new Intent(HelpActivity.this, UserProfileActivity.class);
                        intent.putExtra("open_mode", UserProfileActivity.OPEN_FOR_NEW_USER);
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
