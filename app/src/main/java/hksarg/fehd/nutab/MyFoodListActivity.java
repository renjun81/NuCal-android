package hksarg.fehd.nutab;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MyFoodListActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner spinner;
    ImageView image_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_list);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.menu2b_t20_list);
        ImageView btnLeft = (ImageView) findViewById(R.id.btnLeft);
        btnLeft.setImageResource(R.drawable.btn_edit_draw);
        btnLeft.setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);

        image_spinner= (ImageView) findViewById(R.id.image_spinner);
        spinner= (Spinner) findViewById(R.id.spinner);

        findViewById(R.id.btnTransmit).setOnClickListener(this);
        findViewById(R.id.btnMyIntake).setOnClickListener(this);

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
                        break;
                    case 2:
                        image_spinner.setVisibility(View.VISIBLE);
                        image_spinner.setImageResource(R.drawable.icon_lowsalt);
                        break;
                    case 3:
                        image_spinner.setVisibility(View.VISIBLE);
                        image_spinner.setImageResource(R.drawable.icon_lowsugar);
                        break;
                    case 4:
                        image_spinner.setVisibility(View.VISIBLE);
                        image_spinner.setImageResource(R.drawable.icon_3low);
                        break;
                    default:
                        image_spinner.setVisibility(View.INVISIBLE);
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
            case R.id.btnLeft:
                break;

            case R.id.btnRight:
                onBackPressed();
                break;

            case R.id.btnTransmit:
                break;

            case R.id.btnMyIntake:
                break;
        }
    }

    public void ShowDialog(){
            final Dialog dialog = new Dialog(MyFoodListActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_message);
            dialog.setTitle("setDetails");
            Button button1 = (Button) dialog.findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();

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
}
