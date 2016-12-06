package hksarg.fehd.nutab;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.xdroid.widget.SoftKeyboard;

import hksarg.fehd.nutab.model.Food;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener {

    EditText    edtName;

    View        opt100g, opt100ml, optCustom;
    EditText    edtPackageSize;
    View        optUnitG, optUnitMl;
    TextView    tvPackageUnit;

    EditText    edtEnergy;
    View        optUnitKCal, optUnitKJ;

    EditText    edtProtein;
    EditText    edtTotalFat, edtSaturatedFat, edtTransFat;

    EditText    edtCholesterol;

    Spinner     carboSpinner;
    EditText    edtCarbohydrate, edtDietaryFibre, edtSugar;

    EditText    edtSodium;

    View asteriskName, asteriskPackageSize;
    View asteriskCarbo, asteriskFibre, asteriskSugar;
    View asteriskTotalFat, asteriskSaturatedFat, asteriskTransFat;

    View        btnSave;

    Food m_food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);

        findViewById(R.id.btnLeft).setVisibility(View.GONE);
        findViewById(R.id.btnRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.gotoHome(AddFoodActivity.this);
            }
        });

        edtName = (EditText) findViewById(R.id.edtName);
        asteriskName = findViewById(R.id.asteriskName);
        opt100g = findViewById(R.id.opt100g);
        opt100ml = findViewById(R.id.opt100ml);
        optCustom = findViewById(R.id.optCustom);
        edtPackageSize = (EditText) findViewById(R.id.edtPackageSize);
        asteriskPackageSize = findViewById(R.id.asteriskPackageSize);
        optUnitG = findViewById(R.id.optUnitG);
        optUnitMl = findViewById(R.id.optUnitMl);
        tvPackageUnit = (TextView) findViewById(R.id.tvPackageUnit);

        edtEnergy = (EditText) findViewById(R.id.edtEnergy);
        optUnitKCal = findViewById(R.id.optUnitKCal);
        optUnitKJ = findViewById(R.id.optUnitKJ);
        tvPackageUnit = (TextView) findViewById(R.id.tvPackageUnit);

        edtProtein = (EditText) findViewById(R.id.edtProtein);
        edtTotalFat = (EditText) findViewById(R.id.edtTotalFat);
        edtSaturatedFat = (EditText) findViewById(R.id.edtSaturatedFat);
        edtTransFat = (EditText) findViewById(R.id.edtTransFat);

        asteriskTotalFat = findViewById(R.id.asteriskTotalFat);
        asteriskSaturatedFat = findViewById(R.id.asteriskSaturatedFat);
        asteriskTransFat = findViewById(R.id.asteriskTransFat);

        edtCholesterol = (EditText) findViewById(R.id.edtCholestrol);

        carboSpinner= (Spinner) findViewById(R.id.carbo_spinner);
        edtCarbohydrate = (EditText) findViewById(R.id.edtCarbohydrate);
        edtDietaryFibre = (EditText) findViewById(R.id.edtDietaryFibre);
        edtSugar = (EditText) findViewById(R.id.edtSugar);
        asteriskCarbo = findViewById(R.id.asteriskCarbo);
        asteriskFibre = findViewById(R.id.asteriskFibre);
        asteriskSugar = findViewById(R.id.asteriskSugar);

        edtSodium = (EditText) findViewById(R.id.edtSodium);
        btnSave = findViewById(R.id.btnSave);

        opt100g.setOnClickListener(packageOptListener);
        opt100ml.setOnClickListener(packageOptListener);
        optCustom.setOnClickListener(packageOptListener);
        optUnitG.setOnClickListener(this);
        optUnitMl.setOnClickListener(this);
        optUnitKCal.setOnClickListener(this);
        optUnitKJ.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        TextWatcher fatWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                float totalFat = parseFloat(edtTotalFat.getText().toString());
                float saturatedFat = parseFloat(edtSaturatedFat.getText().toString());
                float transFat = parseFloat(edtTransFat.getText().toString());
                if ( totalFat > 0 && totalFat < saturatedFat + transFat ) {
                    edtTotalFat.setBackgroundResource(R.drawable.txtfield_error);
                    edtSaturatedFat.setBackgroundResource(R.drawable.txtfield_error);
                    edtTransFat.setBackgroundResource(R.drawable.txtfield_error);
                    asteriskTotalFat.setVisibility(View.VISIBLE);
                    asteriskSaturatedFat.setVisibility(View.VISIBLE);
                    asteriskTransFat.setVisibility(View.VISIBLE);
                }
                else {
                    edtTotalFat.setBackgroundResource(R.drawable.txtfield_normal);
                    edtSaturatedFat.setBackgroundResource(R.drawable.txtfield_normal);
                    edtTransFat.setBackgroundResource(R.drawable.txtfield_normal);
                    asteriskTotalFat.setVisibility(View.INVISIBLE);
                    asteriskSaturatedFat.setVisibility(View.INVISIBLE);
                    asteriskTransFat.setVisibility(View.INVISIBLE);
                }
            }
        };
        edtTotalFat.addTextChangedListener(fatWatcher);
        edtSaturatedFat.addTextChangedListener(fatWatcher);
        edtTransFat.addTextChangedListener(fatWatcher);

        final Handler handler = new Handler();
        SoftKeyboard.getInstance((ViewGroup)findViewById(R.id.llCarbo)).setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        checkCarbohydrate();
                    }
                } );
            }

            @Override
            public void onSoftKeyboardShow() {
            }
        });

        ArrayAdapter<String> adptSpnCategory = new ArrayAdapter<String>(this,R.layout.item_simple_spinner, getResources().getStringArray(R.array.array_carbohydrates));
        adptSpnCategory.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        carboSpinner.setAdapter(adptSpnCategory);
        carboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_food.carbohydrateType = position;
                checkCarbohydrate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        long foodId = getIntent().getLongExtra("food_id", 0);
        m_food = Food.load(Food.class, foodId);
        if ( m_food == null ) {
            tvTitle.setText(R.string.menu2a_t20_add);
            m_food = new Food();
            m_food.packageSize = 100;
            opt100g.performClick();
        }
        else {
            tvTitle.setText(R.string.menu2a_t20_edit);
            showFoodInfo();
        }

        edtName.addTextChangedListener(new CustomTextWatcher(edtName, asteriskName));
        edtPackageSize.addTextChangedListener(new CustomTextWatcher(edtPackageSize, asteriskPackageSize));
    }

    private void showFoodInfo() {
        edtName.setText(m_food.name);
        edtPackageSize.setText(format(m_food.packageSize));
        if ( m_food.packageSize == 100 ) {
            if ( m_food.packageUnit == Food.PACKAGE_UNIT_G )
                opt100g.performClick();
            else
                opt100ml.performClick();
        }
        else {
            optCustom.performClick();
        }

        edtEnergy.setText(format(m_food.energySize));
        if ( m_food.energyUnit == Food.ENERGY_UNIT_KCAL )
            setTwoOption(optUnitKCal, optUnitKJ, true);
        else
            setTwoOption(optUnitKCal, optUnitKJ, false);

        edtProtein.setText(format(m_food.protein));
        edtTotalFat.setText(format(m_food.totalFat));
        edtSaturatedFat.setText(format(m_food.saturatedFat));
        edtTransFat.setText(format(m_food.transFat));
        edtCholesterol.setText(format(m_food.cholesterol));
        carboSpinner.setSelection(m_food.carbohydrateType);
        edtCarbohydrate.setText(format(m_food.carbohydrate));
        edtDietaryFibre.setText(format(m_food.dietaryFibre));
        edtSugar.setText(format(m_food.sugar));
        edtSodium.setText(format(m_food.sodium));
    }

    private boolean checkCarbohydrate() {

        edtCarbohydrate.setBackgroundResource(R.drawable.txtfield_normal);
        asteriskCarbo.setVisibility(View.INVISIBLE);
        edtDietaryFibre.setBackgroundResource(R.drawable.txtfield_normal);
        asteriskFibre.setVisibility(View.INVISIBLE);
        edtSugar.setBackgroundResource(R.drawable.txtfield_normal);
        asteriskSugar.setVisibility(View.INVISIBLE);

        float carbo = parseFloat(edtCarbohydrate.getText().toString());
        float fiber = parseFloat(edtDietaryFibre.getText().toString());
        float sugar = parseFloat(edtSugar.getText().toString());

        if ( m_food.carbohydrateType == Food.CARBO_TYPE_TOTAL ) {

            if (edtDietaryFibre.length() == 0 ) {
                edtDietaryFibre.setBackgroundResource(R.drawable.txtfield_error);
                asteriskFibre.setVisibility(View.VISIBLE);

                if ( edtCarbohydrate.length() == 0 )
                    AppConfig.showMessageDialog(this, R.string.menu2a_d02);
                else
                    AppConfig.showMessageDialog(this, R.string.menu2a_d03);

                return false;
            }

            if( edtCarbohydrate.length() > 0 && carbo < fiber + sugar ) {
                edtCarbohydrate.setBackgroundResource(R.drawable.txtfield_error);
                asteriskCarbo.setVisibility(View.VISIBLE);
                if ( edtDietaryFibre.length() > 0 ) {
                    edtDietaryFibre.setBackgroundResource(R.drawable.txtfield_error);
                    asteriskFibre.setVisibility(View.VISIBLE);
                }
                if ( edtSugar.length() > 0 ) {
                    edtSugar.setBackgroundResource(R.drawable.txtfield_error);
                    asteriskSugar.setVisibility(View.VISIBLE);
                }

                if ( edtSugar.length() > 0 )
                    AppConfig.showMessageDialog(this, R.string.menu2a_d04);
                else
                    AppConfig.showMessageDialog(this, R.string.menu2a_d05);

                return false;
            }
        }
        else {
            if ( edtCarbohydrate.length() > 0 && edtSugar.length() > 0 && carbo < sugar ) {
                edtCarbohydrate.setBackgroundResource(R.drawable.txtfield_error);
                asteriskCarbo.setVisibility(View.VISIBLE);
                edtSugar.setBackgroundResource(R.drawable.txtfield_error);
                asteriskSugar.setVisibility(View.VISIBLE);
                AppConfig.showMessageDialog(this, R.string.menu2a_d06);
                return false;
            }
        }

        return true;
    }

    private void saveFood() {
        m_food.name = edtName.getText().toString();
        if (TextUtils.isEmpty(m_food.name) ) {
            edtName.setBackgroundResource(R.drawable.txtfield_error);
            asteriskName.setVisibility(View.VISIBLE);
            AppConfig.showMessageDialog(this, R.string.menu2a_d10);
            return;
        }
        if ( m_food.getId() == null ) {
            Food food = new Select().from(Food.class).where("name='" + m_food.name + "'").executeSingle();
            if ( food != null ) {
                edtName.setBackgroundResource(R.drawable.txtfield_error);
                asteriskName.setVisibility(View.VISIBLE);
                AppConfig.showMessageDialog(this, R.string.menu2a_d11);
                return;
            }
        }

        if ( m_food.packageSize == 0 ) {
            m_food.packageSize = parseFloat(edtPackageSize.getText().toString());
            if ( m_food.packageSize == 0 ) {
                edtPackageSize.setBackgroundResource(R.drawable.txtfield_error);
                asteriskPackageSize.setVisibility(View.VISIBLE);
                AppConfig.showMessageDialog(this, R.string.menu2a_d12);
                return;
            }
        }

        if ( edtEnergy.length() == 0 && edtProtein.length() == 0 &&
                edtTotalFat.length() == 0 && edtSaturatedFat.length() == 0 && edtTransFat.length() == 0 &&
                edtCarbohydrate.length() == 0 && edtCholesterol.length() == 0 &&
                edtDietaryFibre.length() == 0 && edtSugar.length() == 0 && edtSodium.length() == 0 ){
            AppConfig.showMessageDialog(this, R.string.menu2a_d14);
            return;
        }

        float energySize = parseFloat(edtEnergy.getText().toString());
        float protein = parseFloat(edtProtein.getText().toString());
        float totalFat = parseFloat(edtTotalFat.getText().toString());
        float saturatedFat = parseFloat(edtSaturatedFat.getText().toString());
        float transFat = parseFloat(edtTransFat.getText().toString());
        float cholesterol = parseFloat(edtCholesterol.getText().toString());
        float carbohydrate = parseFloat(edtCarbohydrate.getText().toString());
        float dietaryFibre = parseFloat(edtDietaryFibre.getText().toString());
        float sugar = parseFloat(edtSugar.getText().toString());
        float sodium = parseFloat(edtSodium.getText().toString());

        if ( totalFat > 0 && totalFat < saturatedFat + transFat ) {
            AppConfig.showMessageDialog(this, R.string.menu2a_d07);
            edtTotalFat.setBackgroundResource(R.drawable.txtfield_error);
            asteriskTotalFat.setVisibility(View.VISIBLE);
            if ( edtSaturatedFat.length() > 0 ) {
                edtSaturatedFat.setBackgroundResource(R.drawable.txtfield_error);
                asteriskSaturatedFat.setVisibility(View.VISIBLE);
            }
            if ( edtTransFat.length() > 0 ) {
                edtTransFat.setBackgroundResource(R.drawable.txtfield_error);
                asteriskTransFat.setVisibility(View.VISIBLE);
            }
            return;
        }

        if ( !checkCarbohydrate() )
            return;

        float carbo;
        if ( m_food.carbohydrateType == Food.CARBO_TYPE_TOTAL )
            carbo = Math.max(0, carbohydrate - dietaryFibre);
        else
            carbo = Math.max(carbohydrate, dietaryFibre + sugar);

        if ( protein + Math.max(totalFat, saturatedFat + transFat) + cholesterol/1000.f + carbo + sodium/1000.f > m_food.packageSize ) {
            AppConfig.showMessageDialog(this, R.string.menu2a_d13);
            return;
        }

        if ( energySize > 0 && edtProtein.length() > 0 && edtTotalFat.length() > 0 && edtCarbohydrate.length() > 0 ) {
            if ( (m_food.energyUnit == Food.ENERGY_UNIT_KCAL) && (
                energySize <= (protein * 4 + totalFat *9 + carbohydrate * 4)* 0.7 ||
                energySize >= (protein * 4 + totalFat *9 + carbohydrate * 4)* 1.3 )
                || (m_food.energyUnit == Food.ENERGY_UNIT_KJ) && (
                energySize <= (protein * 17 + totalFat *37 + carbohydrate * 17)* 0.7 ||
                energySize >= (protein * 17 + totalFat *37 + carbohydrate * 17)* 1.3 ) )
            {
                AppConfig.showMessageDialog(this, R.string.menu2a_d15);
                return;
            }
        }

        if ( edtEnergy.length() > 0 )
            m_food.energySize = energySize;
        if ( edtProtein.length() > 0 )
            m_food.protein = protein;
        if ( edtTotalFat.length() > 0 )
            m_food.totalFat = totalFat;
        if ( edtSaturatedFat.length() > 0 )
            m_food.saturatedFat = saturatedFat;
        if ( edtTransFat.length() > 0 )
            m_food.transFat = transFat;
        if ( edtCholesterol.length() > 0 )
            m_food.cholesterol = cholesterol;
        if ( edtCarbohydrate.length() > 0 )
            m_food.carbohydrate = carbohydrate;
        if ( edtDietaryFibre.length() > 0 )
            m_food.dietaryFibre = dietaryFibre;
        if ( edtSugar.length() > 0 )
            m_food.sugar = sugar;
        if ( edtSodium.length() > 0 )
            m_food.sodium = sodium;

        if ( m_food.save() > 0 ) {
            final Dialog dialog = new Dialog(AddFoodActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_confirm);
            TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
            tvMessage.setText(R.string.menu2a_d16);
            dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    m_food = new Food();
                    m_food.packageSize = 100;
                    showFoodInfo();
                }
            });
            dialog.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    finish();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.optUnitG:
                m_food.packageUnit = Food.PACKAGE_UNIT_G;
                setTwoOption(optUnitG, optUnitMl, true);
                break;

            case R.id.optUnitMl:
                m_food.packageUnit = Food.PACKAGE_UNIT_ML;
                setTwoOption(optUnitG, optUnitMl, false);
                break;

            case R.id.optUnitKCal:
                setTwoOption(optUnitKCal, optUnitKJ, true);
                if ( m_food.energyUnit == Food.ENERGY_UNIT_KJ ) {
                    m_food.energyUnit = Food.ENERGY_UNIT_KCAL;
                    float energy = parseFloat(edtEnergy.getText().toString());
                    energy = energy / 4.184f;
                    edtEnergy.setText(format(energy));
                }
                break;

            case R.id.optUnitKJ:
                setTwoOption(optUnitKCal, optUnitKJ, false);
                if ( m_food.energyUnit == Food.ENERGY_UNIT_KCAL ) {
                    m_food.energyUnit = Food.ENERGY_UNIT_KJ;
                    float energy = parseFloat(edtEnergy.getText().toString());
                    energy = energy * 4.184f;
                    edtEnergy.setText(format(energy));
                }
                break;

            case R.id.btnSave:
                saveFood();
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

    View.OnClickListener packageOptListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            opt100g.setBackgroundResource(R.drawable.btn_left_off);
            opt100ml.setBackgroundResource(R.drawable.btn_mid_off);
            optCustom.setBackgroundResource(R.drawable.btn_right_off);

            switch (v.getId()) {
                case R.id.opt100g:
                    m_food.packageUnit = Food.PACKAGE_UNIT_G;
                    m_food.packageSize = 100;
                    opt100g.setBackgroundResource(R.drawable.btn_left_on);
                    edtPackageSize.setEnabled(false);
                    edtPackageSize.setText("100");
                    optUnitG.setVisibility(View.GONE);
                    optUnitMl.setVisibility(View.GONE);
                    tvPackageUnit.setVisibility(View.VISIBLE);
                    tvPackageUnit.setText(R.string.menu2a_text5c_g);
                    break;

                case R.id.opt100ml:
                    m_food.packageUnit = Food.PACKAGE_UNIT_ML;
                    m_food.packageSize = 100;
                    opt100ml.setBackgroundResource(R.drawable.btn_mid_on);
                    edtPackageSize.setEnabled(false);
                    edtPackageSize.setText("100");
                    optUnitG.setVisibility(View.GONE);
                    optUnitMl.setVisibility(View.GONE);
                    tvPackageUnit.setVisibility(View.VISIBLE);
                    tvPackageUnit.setText(R.string.menu2a_text5c_ml);
                    break;

                case R.id.optCustom:
                    m_food.packageSize = 0;
                    optCustom.setBackgroundResource(R.drawable.btn_right_on);
                    edtPackageSize.setEnabled(true);
                    optUnitG.setVisibility(View.VISIBLE);
                    optUnitMl.setVisibility(View.VISIBLE);
                    tvPackageUnit.setVisibility(View.GONE);
                    break;

            }
        }
    };

    private int parseInt(String str) {
        int ret = 0;
        try {
            ret = Integer.parseInt(str);
        }
        catch(Exception e) {}
        return ret;
    }
    private float parseFloat(String str) {
        float ret = 0;
        try {
            ret = Float.parseFloat(str);
        }
        catch (Exception e){}

        return ret;
    }

    private String format(float value) {
        return value < 0 ? "" : String.format("%.02f", value);
    }

}
