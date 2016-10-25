package hksarg.fehd.nutab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xdroid.widget.LocaleHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private ViewFlipper viewFlipper;

    private ImageView ivTitleBar, ivAddFood, ivFoodList, ivIntake, ivHistory, ivProfile, ivSetting;
    private ImageView ivBread, ivChips, ivSugar, ivSalt, ivOil, ivMilk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        ivTitleBar = (ImageView) findViewById(R.id.titleBar);
        ivAddFood = (ImageView) findViewById(R.id.add_food_iv);
        ivFoodList = (ImageView) findViewById(R.id.my_food_list);
        ivIntake = (ImageView) findViewById(R.id.my_intake_iv);
        ivHistory = (ImageView) findViewById(R.id.my_intake_history_iv);
        ivProfile = (ImageView) findViewById(R.id.user_profile);
        ivSetting = (ImageView) findViewById(R.id.setting);

        ivAddFood.setOnClickListener(this);
        ivFoodList.setOnClickListener(this);
        ivIntake.setOnClickListener(this);
        ivHistory.setOnClickListener(this);

        ivProfile.setOnClickListener(this);
        ivSetting.setOnClickListener(this);

        findViewById(R.id.about_tv).setOnClickListener(this);
        findViewById(R.id.help_iv).setOnClickListener(this);

        ivBread = (ImageView) findViewById(R.id.ivBread);
        ivChips = (ImageView) findViewById(R.id.ivChips);
        ivSugar = (ImageView) findViewById(R.id.ivSugar);
        ivSalt = (ImageView) findViewById(R.id.ivSalt);
        ivOil = (ImageView) findViewById(R.id.ivOil);
        ivMilk = (ImageView) findViewById(R.id.ivMilk);

        viewFlipper = (ViewFlipper) findViewById(R.id.flipper1);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        viewFlipper.setFlipInterval(9000);

        viewFlipper.startFlipping();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.add_food_iv:
                startActivity(new Intent(MainActivity.this, AddFoodActivity.class));
                break;

            case R.id.my_food_list:
                startActivity(new Intent(MainActivity.this, MyFoodListActivity.class));
                break;

            case R.id.my_intake_iv:
                {
                    AppConfig.showMessageDialog(this, R.string.menu3a_d00);

                    //startActivity(new Intent(MainActivity.this, MyIntakeActivity.class));
                }
                break;

            case R.id.my_intake_history_iv:
                startActivity(new Intent(MainActivity.this, MyIntakeHistoryActivity.class));
                break;

            //////////////////////////

            case R.id.about_tv:
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                break;

            case R.id.user_profile:
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            break;

            case R.id.setting: {
                showSettingDialog();
            }
            break;

            case R.id.help_iv: {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                intent.putExtra("is_help", true);
                startActivity(intent);
            }
            break;
        }
    }

    public void showSettingDialog() {

        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting);
        dialog.setTitle("setDetails");

        Button button1 = (Button) dialog.findViewById(R.id.button1);
        Button button2 = (Button) dialog.findViewById(R.id.button2);
        Button button3 = (Button) dialog.findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                updateLocale(Locale.ENGLISH);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                updateLocale(Locale.TRADITIONAL_CHINESE);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                updateLocale(Locale.SIMPLIFIED_CHINESE);
            }
        });
        dialog.show();
    }

    private void updateLocale(Locale locale) {
        LocaleHelper.setLocale(mContext, locale);

        ivTitleBar.setImageResource(R.drawable.main_header);

        ivAddFood.setImageResource(R.drawable.btn_addfood_draw);
        ivFoodList.setImageResource(R.drawable.btn_foodlist_draw);
        ivIntake.setImageResource(R.drawable.btn_intake_draw);
        ivHistory.setImageResource(R.drawable.btn_history_draw);

        ivProfile.setImageResource(R.drawable.btn_profile_draw);
        ivSetting.setImageResource(R.drawable.btn_setting_draw);

        ivBread.setImageResource(R.drawable.banner_bread);
        ivChips.setImageResource(R.drawable.banner_chips);
        ivSugar.setImageResource(R.drawable.banner_sugar);
        ivMilk.setImageResource(R.drawable.banner_milk);
        ivOil.setImageResource(R.drawable.banner_oil);
        ivSalt.setImageResource(R.drawable.banner_salt);
    }
}
