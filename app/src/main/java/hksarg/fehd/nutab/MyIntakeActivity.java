package hksarg.fehd.nutab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import hksarg.fehd.nutab.model.Food;
import hksarg.fehd.nutab.model.NuHist;
import hksarg.fehd.nutab.model.User;

public class MyIntakeActivity extends AppCompatActivity {

    TextView tvDailyEnergy, tvRecordedTime;
    LinearLayout llFoods, llIndexContainer;
    View        llIndexHeader, btnCalculate, btnSave;

    User        m_user;
    List<Food>  m_foods;
    List<FoodViewHolder> m_foodItemViews;

    NuHist m_nuHist;

    String UNIT_GRAM;
    String UNIT_ML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UNIT_GRAM = getResources().getString(R.string.menu2a_text5c_g);
        UNIT_ML = getResources().getString(R.string.menu2a_text5c_ml);

        long nuHistId = getIntent().getLongExtra("nu_hist_id", 0);
        m_nuHist = NuHist.load(NuHist.class, nuHistId);
        if ( m_nuHist != null ) {
            setContentView(R.layout.activity_my_intake_show);
        }
        else {
            setContentView(R.layout.activity_my_intake_calc);
        }

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        findViewById(R.id.btnLeft).setVisibility(View.INVISIBLE);
        findViewById(R.id.btnRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvDailyEnergy = (TextView) findViewById(R.id.tvDailyEnergy);
        tvRecordedTime = (TextView) findViewById(R.id.tvRecordedTime);
        llFoods = (LinearLayout) findViewById(R.id.llFoods);
        llIndexContainer = (LinearLayout) findViewById(R.id.llIndexContainer);
        llIndexHeader = findViewById(R.id.llIndexHeader);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.btnSave);

        ImageView   ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        TextView    tvUserName = (TextView) findViewById(R.id.tvUserName);
        m_user = User.getDefaultUser();
        if ( m_user == null ) {
            finish();
            return;
        }
        tvUserName.setText(m_user.name);
        Bitmap bmp = m_user.getAvatar();
        if ( bmp != null )
            ivAvatar.setImageBitmap(m_user.getAvatar());

        if ( m_nuHist == null ) {

            m_foods = Food.getSelectedFoods();
            if ( m_foods == null || m_foods.size() == 0 ) {
                finish();
                return;
            }

            m_nuHist = new NuHist();

            btnCalculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calculateNutritions();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_nuHist.userId = m_user.getId();
                    m_nuHist.recordTime = new Date();
                    m_nuHist.save();
                    finish();
                    startActivity(new Intent(MyIntakeActivity.this, MyIntakeHistoryActivity.class));
                }
            });

            tvTitle.setText(R.string.menu3a_t30_add);
            llIndexHeader.setVisibility(View.INVISIBLE);
            btnSave.setVisibility(View.GONE);

            m_foodItemViews = new ArrayList<FoodViewHolder>();
            m_foodItemViews.clear();
            Iterator<Food> itr = m_foods.iterator();
            while (itr.hasNext()) {
                Food food = itr.next();
                View item = LayoutInflater.from(this).inflate(R.layout.item_intake_food_edit, null);
                m_foodItemViews.add(new FoodViewHolder(item, food));
                llFoods.addView(item);
            }
        }
        else {
            tvTitle.setText(R.string.menu3a_t30_read);
            tvRecordedTime.setText(m_nuHist.getRecordTimeString());

            int nCount = m_nuHist.foodList.size();
            for ( int i = 0; i < nCount; i++) {
                Food food = Food.load(Food.class, m_nuHist.foodList.keyAt(i));
                if ( food != null ) {
                    int amount = m_nuHist.foodList.valueAt(i);
                    View item = LayoutInflater.from(this).inflate(R.layout.item_intake_food_view, null);
                    TextView tvFoodName = (TextView) item.findViewById(R.id.tvFoodName);
                    TextView tvAmount = (TextView) item.findViewById(R.id.tvAmount);
                    TextView tvPackageUnit = (TextView) item.findViewById(R.id.tvPackageUnit);
                    tvFoodName.setText(food.name);
                    String szUnit = food.packageUnit == Food.PACKAGE_UNIT_G ? UNIT_GRAM : UNIT_ML;
                    tvAmount.setText(amount + szUnit);
                    tvPackageUnit.setText(food.packageSize + szUnit);
                    llFoods.addView(item);
                }
            }

            generateIndexViews();
        }
    }

    private void calculateNutritions() {

        Iterator<FoodViewHolder> itr = m_foodItemViews.iterator();
        while( itr.hasNext() ) {
            FoodViewHolder holder = itr.next();
            String strAmount = holder.edtAmount.getText().toString();
            if (TextUtils.isEmpty(strAmount)) {
                return;
            }
            m_nuHist.putFood(holder.m_food.getId().intValue(), Integer.parseInt(strAmount));
        }

        btnCalculate.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);

        generateIndexViews();
    }

    private void generateIndexViews() {
        llIndexHeader.setVisibility(View.VISIBLE);
        llIndexContainer.removeAllViews();

        IndexViewHolder holder;
        holder = new IndexViewHolder(this, R.string.menu3a_t30_t2_row0_col1, "", 10);
        llIndexContainer.addView(holder.container);

        holder = new IndexViewHolder(this, R.string.menu3a_t30_t2_row1_col1, "", 10);
        llIndexContainer.addView(holder.container);

        holder = new IndexViewHolder(this, R.string.menu3a_t30_t2_row2_col1, "", 10);
        llIndexContainer.addView(holder.container);


    }

    class FoodViewHolder {
        TextView tvFoodName;
        EditText edtAmount;
        TextView tvAmountUnit;
        TextView tvPackageUnit;
        Food    m_food;
        public FoodViewHolder(View view, Food food) {
            tvFoodName = (TextView) view.findViewById(R.id.tvFoodName);
            edtAmount = (EditText) view.findViewById(R.id.edtAmount);
            tvAmountUnit = (TextView) view.findViewById(R.id.tvAmountUnit);
            tvPackageUnit = (TextView) view.findViewById(R.id.tvPackageUnit);

            m_food = food;
            tvFoodName.setText(m_food.name);
            String szUnit = m_food.packageUnit == Food.PACKAGE_UNIT_G ? UNIT_GRAM : UNIT_ML;
            tvAmountUnit.setText(szUnit);
            tvPackageUnit.setText(m_food.packageSize + szUnit);
        }
    }

    class IndexViewHolder {
        View container;
        TextView tvIndexName;
        TextView tvAmount;
        TextView tvPercent;
        ProgressBar pgPercent;
        public IndexViewHolder(Context context, int nResIndexName, String szAmount, int percent) {
            container = LayoutInflater.from(context).inflate(R.layout.item_intake_index, null);
            tvIndexName = (TextView) container.findViewById(R.id.tvIndexName);
            tvAmount = (TextView) container.findViewById(R.id.tvAmount);
            tvPercent = (TextView) container.findViewById(R.id.tvPercent);
            pgPercent = (ProgressBar) container.findViewById(R.id.progress);

            tvIndexName.setText(nResIndexName);
            tvAmount.setText(szAmount);
            tvPercent.setText(percent + "%");
            pgPercent.setProgress(percent);
            if ( percent > 100 ) {

            }
            else {

            }
        }
    }
}
