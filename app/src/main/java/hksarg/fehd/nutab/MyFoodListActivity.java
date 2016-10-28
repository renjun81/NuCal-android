package hksarg.fehd.nutab;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import hksarg.fehd.nutab.model.User;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import java.util.List;

import hksarg.fehd.nutab.model.Food;

public class MyFoodListActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner spinner;
    ImageView image_spinner;

    RecyclerView        m_recyclerView;
    RecyclerViewAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_list);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.menu2b_t20_list);
        ImageView btnLeft = (ImageView) findViewById(R.id.btnLeft);
        btnLeft.setImageResource(R.drawable.btn_edit_draw);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.btnRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView   ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        TextView    tvUserName = (TextView) findViewById(R.id.tvUserName);
        User user = User.getDefaultUser();
        if ( user == null ) {
            finish();
            return;
        }
        tvUserName.setText(user.name);
        Bitmap bmp = user.getAvatar();
        if ( bmp != null )
            ivAvatar.setImageBitmap(user.getAvatar());


        image_spinner= (ImageView) findViewById(R.id.image_spinner);
        spinner= (Spinner) findViewById(R.id.spinner);

        findViewById(R.id.btnTransmit).setOnClickListener(this);
        findViewById(R.id.btnMyIntake).setOnClickListener(this);

        m_recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Item Decorator:
        m_recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        m_recyclerView.setItemAnimator(new FadeInLeftAnimator());

        m_adapter = new RecyclerViewAdapter();
        m_adapter.setData(Food.getFoodList(Food.FOOD_ALL));
        m_recyclerView.setAdapter(m_adapter);

//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,spinner_item);
//        spinner.setAdapter(adapter);
        ArrayAdapter<String> adptSpnCategory = new ArrayAdapter<String>(this,R.layout.item_simple_spinner, getResources().getStringArray(R.array.array_sort));
        adptSpnCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adptSpnCategory);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        image_spinner.setVisibility(View.VISIBLE);
                        image_spinner.setImageResource(R.drawable.icon_lowfat);
                        m_adapter.setData(Food.getFoodList(Food.FOOD_LOW_FAT));
                        break;
                    case 2:
                        image_spinner.setVisibility(View.VISIBLE);
                        image_spinner.setImageResource(R.drawable.icon_lowsalt);
                        m_adapter.setData(Food.getFoodList(Food.FOOD_LOW_SALT));
                        break;
                    case 3:
                        image_spinner.setVisibility(View.VISIBLE);
                        image_spinner.setImageResource(R.drawable.icon_lowsugar);
                        m_adapter.setData(Food.getFoodList(Food.FOOD_LOW_SUGAR));
                        break;
                    case 4:
                        image_spinner.setVisibility(View.VISIBLE);
                        image_spinner.setImageResource(R.drawable.icon_3low);
                        m_adapter.setData(Food.getFoodList(Food.FOOD_THREE_LOW));
                        break;
                    default:
                        image_spinner.setVisibility(View.INVISIBLE);
                        m_adapter.setData(Food.getFoodList(Food.FOOD_ALL));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btnTransmit:
                break;

            case R.id.btnMyIntake:
                if ( Food.getSelectedCount() == 0 ) {
                    AppConfig.showMessageDialog(this, R.string.menu3a_d00);
                }
                else {
                    startActivity(new Intent(this, MyIntakeActivity.class));
                }
                break;
        }
    }

    public void ShowDialog1(){
        final Dialog dialog = new Dialog(MyFoodListActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_turnon_ble);
        Button button1 = (Button) dialog.findViewById(R.id.cancel);
        Button turn_on = (Button) dialog.findViewById(R.id.turn_on);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        turn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.FoodViewHolder> {

        class FoodViewHolder extends RecyclerView.ViewHolder {

            View        trash, btnGo;

            CheckBox    chkSelected;
            TextView    tvFoodName;
            View        icFat, icSalt, icSugar, icBest;

            public FoodViewHolder(View itemView) {
                super(itemView);
                chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);
                tvFoodName = (TextView) itemView.findViewById(R.id.tvFoodName);
                icFat = itemView.findViewById(R.id.icFat);
                icSalt = itemView.findViewById(R.id.icSalt);
                icSugar = itemView.findViewById(R.id.icSugar);
                icBest = itemView.findViewById(R.id.icBest);
                trash = itemView.findViewById(R.id.trash);
                btnGo = itemView.findViewById(R.id.btnGo);
            }

            public void bindData(final Food food) {
                tvFoodName.setText(food.name);
                chkSelected.setChecked(food.isSelected);
                icFat.setVisibility(food.isFoodType(Food.FOOD_LOW_FAT) ? View.VISIBLE : View.INVISIBLE);
                icSalt.setVisibility(food.isFoodType(Food.FOOD_LOW_SALT) ? View.VISIBLE : View.INVISIBLE);
                icSugar.setVisibility(food.isFoodType(Food.FOOD_LOW_SUGAR) ? View.VISIBLE : View.INVISIBLE);
                icBest.setVisibility(food.isFoodType(Food.FOOD_THREE_LOW) ? View.VISIBLE : View.INVISIBLE);

                chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        food.isSelected = isChecked;
                        food.save();
                    }
                });

                btnGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AddFoodActivity.class);
                        intent.putExtra("food_id", food.getId());
                        v.getContext().startActivity(intent);
                    }
                });

                trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        food.delete();
                        m_data.remove(food);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        private List<Food> m_data;
        public void setData(List<Food> foodList) {
            m_data = foodList;
            notifyDataSetChanged();
        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FoodViewHolder viewHolder, int position) {
            Food food = m_data.get(position);
            viewHolder.bindData(food);
        }

        @Override
        public int getItemCount() {
            return m_data == null ? 0 : m_data.size();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipeView;
        }


    }
}
