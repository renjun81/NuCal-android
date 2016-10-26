package hksarg.fehd.nutab;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    EditText    edtCarboTotal, edtCarboDietary, edtCarboSugar;

    EditText    edtSodium;

    Food m_food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);

        findViewById(R.id.btnLeft).setVisibility(View.GONE);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        edtName = (EditText) findViewById(R.id.edtName);
        opt100g = findViewById(R.id.opt100g);
        opt100ml = findViewById(R.id.opt100ml);
        optCustom = findViewById(R.id.optCustom);
        edtPackageSize = (EditText) findViewById(R.id.edtPackageSize);
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

        edtCholesterol = (EditText) findViewById(R.id.edtCholestrol);

        carboSpinner= (Spinner) findViewById(R.id.carbo_spinner);
        edtCarboTotal = (EditText) findViewById(R.id.edtCarboTotal);
        edtCarboDietary = (EditText) findViewById(R.id.edtCarboDietary);
        edtCarboSugar = (EditText) findViewById(R.id.edtCarboSugar);

        edtSodium = (EditText) findViewById(R.id.edtSodium);

        opt100g.setOnClickListener(packageOptListener);
        opt100ml.setOnClickListener(packageOptListener);
        optCustom.setOnClickListener(packageOptListener);
        optUnitG.setOnClickListener(this);
        optUnitMl.setOnClickListener(this);
        optUnitKCal.setOnClickListener(this);
        optUnitKJ.setOnClickListener(this);

//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
//        carb_spinner.setAdapter(adapter);

        ArrayAdapter<String> adptSpnCategory = new ArrayAdapter<String>(this,R.layout.item_simple_spinner, getResources().getStringArray(R.array.array_carbohydrates));
        adptSpnCategory.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        carboSpinner.setAdapter(adptSpnCategory);

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
    }

    private void showFoodInfo() {
        edtName.setText(m_food.name);
        edtPackageSize.setText(m_food.packageSize + "");
        if ( m_food.packageSize == 100 ) {
            if ( m_food.packageUnit == Food.PACKAGE_UNIT_G )
                opt100g.performClick();
            else
                opt100ml.performClick();
        }
        else {
            optCustom.performClick();
        }

        edtEnergy.setText(m_food.energySize + "");
        if ( m_food.energyUnit == Food.ENERGY_UNIT_KCAL )
            setTwoOption(optUnitKCal, optUnitKJ, true);
        else
            setTwoOption(optUnitKCal, optUnitKJ, false);

        edtProtein.setText(format(m_food.protein));
        edtTotalFat.setText(format(m_food.saturatedFat + m_food.transFat));
        edtSaturatedFat.setText(format(m_food.saturatedFat));
        edtTransFat.setText(format(m_food.transFat));
        edtCholesterol.setText(format(m_food.cholesterol));
        carboSpinner.setSelection(m_food.carbohydrateType);
        edtCarboTotal.setText(format(m_food.carboDietary + m_food.carboSugar));
        edtCarboDietary.setText(format(m_food.carboDietary));
        edtCarboSugar.setText(format(m_food.carboSugar));
        edtSodium.setText(format(m_food.sodium));
    }

    private void saveFood() {
        m_food.name = edtName.getText().toString();
        if ( m_food.packageSize == 0 ) {
            m_food.packageSize = parseInt(edtPackageSize.getText().toString());
        }
        m_food.energySize = parseInt(edtEnergy.getText().toString());
        m_food.protein = parseFloat(edtProtein.getText().toString());

        float totalFat = parseFloat(edtTotalFat.getText().toString());
        m_food.saturatedFat = parseFloat(edtSaturatedFat.getText().toString());
        m_food.transFat = parseFloat(edtTransFat.getText().toString());
        m_food.cholesterol = parseFloat(edtCholesterol.getText().toString());

        float totalCarbo = parseFloat(edtCarboTotal.getText().toString());
        m_food.carbohydrateType = carboSpinner.getSelectedItemPosition();
        m_food.carboDietary = parseFloat(edtCarboDietary.getText().toString());
        m_food.carboSugar = parseFloat(edtCarboSugar.getText().toString());
        m_food.sodium = parseFloat(edtSodium.getText().toString());

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
                m_food.energyUnit = Food.ENERGY_UNIT_KCAL;
                setTwoOption(optUnitKCal, optUnitKJ, true);
                break;

            case R.id.optUnitKJ:
                m_food.energyUnit = Food.ENERGY_UNIT_KJ;
                setTwoOption(optUnitKCal, optUnitKJ, false);
                break;

            case R.id.btnRight:
                onBackPressed();
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
        return value < 0.01 ? "" : String.format("%.02f", value);
    }

}
