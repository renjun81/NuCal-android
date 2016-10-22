package hksarg.fehd.nutab;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import hksarg.fehd.nutab.model.User;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView   ivAvatar;
    EditText    edtName;
    View        optMale, optFemale;

    EditText    edtWeight;
    View        optLBS, optKG;

    EditText    edtHeightFeet, edtHeightInch, edtHeightMeter;
    View        optFeet, optMeter;

    SeekBar     seekbar;
    TextView    tvAge;

    ImageView   activity_low,activity_medium,activity_high;

    EditText    edtEnergyRequired;
    TextView    tvBMI;

    View bmiMeter;
    ImageView ivBMIBoard, ivBMIPointer;
    View llEnergyRequired;
    int bdWidth, bdHeight, ptWidth, ptHeight;

    float m_previousRotation = 0;

    User m_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.menu4b_t40_edit);

        ImageView btnChangeUser = (ImageView) findViewById(R.id.btnLeft);
        btnChangeUser.setImageResource(R.drawable.btn_changeuser_draw);
        btnChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, UserListActivity.class));
                finish();
            }
        });

        View btnHome = findViewById(R.id.btnRight);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extra=getIntent().getExtras();
                if(extra!=null){
                    Intent intent = new Intent(UserProfileActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    onBackPressed();
                }
            }
        });

        edtName = (EditText) findViewById(R.id.edtName);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        optMale = findViewById(R.id.optMale);
        optFemale = findViewById(R.id.optFemale);

        edtWeight = (EditText) findViewById(R.id.edtWeight);
        optLBS = findViewById(R.id.optLBS);
        optKG = findViewById(R.id.optKG);

        edtHeightFeet = (EditText) findViewById(R.id.edtHeightFeet);
        edtHeightInch = (EditText) findViewById(R.id.edtHeightInch);
        edtHeightMeter = (EditText) findViewById(R.id.edtHeightMeter);
        optFeet = findViewById(R.id.optFeet);
        optMeter = findViewById(R.id.optMeter);

        seekbar= (SeekBar) findViewById(R.id.age_seek_bar);
        tvAge = (TextView) findViewById(R.id.tvAge);

        activity_low= (ImageView) findViewById(R.id.activity_low);
        activity_medium= (ImageView) findViewById(R.id.activity_medium);
        activity_high= (ImageView) findViewById(R.id.activity_high);

        edtEnergyRequired = (EditText) findViewById(R.id.edtEnergyRequired);
        tvBMI = (TextView) findViewById(R.id.tvBMI);

        bmiMeter = findViewById(R.id.bmiMeter);
        ivBMIBoard = (ImageView) findViewById(R.id.ivBMIBoard);
        ivBMIPointer = (ImageView) findViewById(R.id.ivBMIPointer);
        llEnergyRequired = findViewById(R.id.llEnergyRequired);

        Drawable d = getResources().getDrawable(R.drawable.pointer_normal);
        ptWidth = d.getIntrinsicWidth();
        ptHeight = d.getIntrinsicHeight();
        d = getResources().getDrawable(R.drawable.balance_asian);
        bdWidth = d.getIntrinsicWidth();
        bdHeight = d.getIntrinsicHeight();
        int ptTopMargin = bdHeight / 5;
        int hideSize = ptTopMargin + ptHeight - bdHeight;
        RelativeLayout.LayoutParams rlParam = (RelativeLayout.LayoutParams) bmiMeter.getLayoutParams();
        rlParam.bottomMargin = - hideSize;
        rlParam = (RelativeLayout.LayoutParams) ivBMIPointer.getLayoutParams();
        rlParam.topMargin = ptTopMargin;

        ivAvatar.setOnClickListener(this);
        optMale.setOnClickListener(this);
        optFemale.setOnClickListener(this);
        optLBS.setOnClickListener(this);
        optKG.setOnClickListener(this);
        optFeet.setOnClickListener(this);
        optMeter.setOnClickListener(this);
        activity_low.setOnClickListener(this);
        activity_medium.setOnClickListener(this);
        activity_high.setOnClickListener(this);
        findViewById(R.id.btnDecAge).setOnClickListener(this);
        findViewById(R.id.btnIncAge).setOnClickListener(this);
        findViewById(R.id.asian_bmi).setOnClickListener(this);
        findViewById(R.id.non_asian_bmi).setOnClickListener(this);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvAge.setText(i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        m_user = new User();

        edtName.setText("Tester");
        optMeter.performClick();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.ivAvatar:
                break;

            case R.id.optMale:
                m_user.gender = User.GENDER_MALE;
                setTwoOption(optMale, optFemale, true);
                break;

            case R.id.optFemale:
                m_user.gender = User.GENDER_FEMALE;
                setTwoOption(optMale, optFemale, false);
                break;

            case R.id.optLBS:
                m_user.weightUnit = User.WEIGHT_UNIT_LBS;
                setTwoOption(optLBS, optKG, true);
                try {
                    if ( m_user.weightUnit == User.WEIGHT_UNIT_KG ) {
                        double weight = Double.parseDouble(edtWeight.getText().toString());
                        weight = weight * 2.20462;
                        edtWeight.setText(weight + "");
                    }
                }
                catch (Exception e){}
                break;

            case R.id.optKG:
                m_user.weightUnit = User.WEIGHT_UNIT_KG;
                setTwoOption(optLBS, optKG, false);
                try {
                    if ( m_user.weightUnit == User.WEIGHT_UNIT_LBS ) {
                        double weight = Double.parseDouble(edtWeight.getText().toString());
                        weight = weight * 0.453592;
                        edtWeight.setText(weight + "");
                    }
                }
                catch (Exception e){}
                break;

            case R.id.optFeet:
                m_user.heightUnit = User.HEIGHT_UNIT_FEET;
                setTwoOption(optFeet, optMeter, true);
                edtHeightFeet.setVisibility(View.VISIBLE);
                edtHeightInch.setVisibility(View.VISIBLE);
                edtHeightMeter.setVisibility(View.GONE);
                try {
                    if (edtHeightMeter.getText().toString().length() > 0) {
                        double height = Double.parseDouble(edtHeightMeter.getText().toString());
                        double inches=height*39.3701;
                        int foot= (int) (inches/12);
                        int inch= (int) (inches%12);
                        edtHeightFeet.setText(foot+"");
                        edtHeightInch.setText(inch+"");
                    }
                }
                catch (Exception e){}
                break;

            case R.id.optMeter:
                m_user.heightUnit = User.HEIGHT_UNIT_METER;
                setTwoOption(optFeet, optMeter, false);
                edtHeightFeet.setVisibility(View.GONE);
                edtHeightInch.setVisibility(View.GONE);
                edtHeightMeter.setVisibility(View.VISIBLE);
                try{
                    int total_inch=0;
                    float feet = 0, inch = 0;
                    if(edtHeightFeet.getText().toString().length()>0)
                        feet = Float.parseFloat(edtHeightFeet.getText().toString());
                    if (edtHeightInch.getText().toString().length()>0)
                        inch = Float.parseFloat(edtHeightInch.getText().toString());
                    edtHeightMeter.setText( (feet*12+inch)*0.0254 + "");
                }
                catch (Exception e){}
                break;

            case R.id.btnDecAge:
                try {
                    int i = Integer.parseInt(tvAge.getText().toString());
                    if(i==0){

                    }
                    else {
                        i--;
                        tvAge.setText(i + "");
                        seekbar.setProgress(i);
                    }
                }
                catch (Exception e){}
                break;

            case R.id.btnIncAge:
                try {
                    int i = Integer.parseInt(tvAge.getText().toString());
                    if(i==120){

                    }
                    else {
                        i++;
                        tvAge.setText(i + "");
                        seekbar.setProgress(i);
                    }
                }
                catch (Exception e){}
                break;

            case R.id.activity_low:
                setActivityLevel(User.ACTIVITY_LEVEL_LOW);
                break;

            case R.id.activity_medium:
                setActivityLevel(User.ACTIVITY_LEVEL_MEDIUM);
                break;

            case R.id.activity_high:
                setActivityLevel(User.ACTIVITY_LEVEL_HIGH);
                break;

            case R.id.asian_bmi:
                ivBMIBoard.setImageResource(R.drawable.balance_asian);
                updateBMI();
                break;

            case R.id.non_asian_bmi:
                ivBMIBoard.setImageResource(R.drawable.balance_nonasian);
                updateBMI();
                break;
        }
    }

    public void setTwoOption(View optLeft, View optRight, boolean onLeft){
        if ( onLeft ) {
            optLeft.setBackgroundResource(R.drawable.btn_left_on);
            optRight.setBackgroundResource(R.drawable.btn_right_off);
        }
        else {
            optLeft.setBackgroundResource(R.drawable.btn_left_off);
            optRight.setBackgroundResource(R.drawable.btn_right_on);
        }
    }

    private void updateBMI() {
        float weight = parseFloat(edtWeight.getText().toString());
        if ( m_user.weightUnit == User.WEIGHT_UNIT_LBS )
            weight = weight * 0.453592f;
        float height = parseFloat(edtHeightMeter.getText().toString());
        float feet = parseFloat(edtHeightFeet.getText().toString());
        float inch = parseFloat(edtHeightInch.getText().toString());
        if ( m_user.heightUnit == User.HEIGHT_UNIT_FEET )
            height = ( feet * 12 + inch ) * 0.0254f;

        if ( edtName.getText().toString().trim().length() == 0 || weight == 0 || height == 0 ) {
            final Dialog dialog = new Dialog(UserProfileActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_message);
            dialog.setTitle("setDetails");
            TextView tv = (TextView) dialog.findViewById(R.id.tv);
            tv.setText(R.string.menu4b_d08);
            Button button1 = (Button) dialog.findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
        else {
            // -19.5 == 18.5 BMI
            //  19.5 == 23   BMI
            //  53 (max) 14.64 ~ 26.86
            float unit = 19.5f * 2 / ( 23 - 18.5f );
            float bmi = weight / (height * height);
            float newRotation = unit * (bmi - 20.25f);
            newRotation = newRotation > 53 ? 53 : newRotation;
            newRotation = newRotation < -53 ? - 53 : newRotation;
            RotateAnimation rotAnim = new RotateAnimation(m_previousRotation, newRotation, ptWidth / 2, ptHeight / 2);
            rotAnim.setDuration(1000);
            rotAnim.setStartOffset(200);
            rotAnim.setFillAfter(true);
            ivBMIPointer.startAnimation(rotAnim);

            tvBMI.setText(String.format("BMI: %.1f", bmi));

            m_previousRotation = newRotation;
        }
    }

    private float parseFloat(String str) {
        float ret = 0;
        try {
            ret = Float.parseFloat(str);
        }
        catch (Exception e){}

        return ret;
    }

    public void setActivityLevel(int activityLevel){
        m_user.activityLevel = activityLevel;
        switch (activityLevel){
            case User.ACTIVITY_LEVEL_LOW:
                activity_low.setImageResource(R.drawable.btn_pal_low_on);
                activity_medium.setImageResource(R.drawable.btn_pal_medium_off);
                activity_high.setImageResource(R.drawable.btn_pal_high_off);
                break;
            case User.ACTIVITY_LEVEL_MEDIUM:
                activity_low.setImageResource(R.drawable.btn_pal_low_off);
                activity_medium.setImageResource(R.drawable.btn_pal_medium_on);
                activity_high.setImageResource(R.drawable.btn_pal_high_off);;
                break;
            case User.ACTIVITY_LEVEL_HIGH:
                activity_low.setImageResource(R.drawable.btn_pal_low_off);
                activity_medium.setImageResource(R.drawable.btn_pal_medium_off);
                activity_high.setImageResource(R.drawable.btn_pal_high_on);
                break;
        }
    }

}
