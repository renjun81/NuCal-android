package hksarg.fehd.nutab;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.Toast;

import java.util.List;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import hksarg.fehd.nutab.bluetoothclassic.Constants;
import hksarg.fehd.nutab.bluetoothclassic.DeviceListActivity;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import hksarg.fehd.nutab.model.Food;
import hksarg.fehd.nutab.model.User;
import hksarg.fehd.nutab.bluetoothclassic.BluetoothChatService;


public class MyFoodListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BLUETOOTH = 3;
    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private Context mContext;

    Spinner spinner;
    ImageView image_spinner;

    RecyclerView        m_recyclerView;
    RecyclerViewAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_list);

        mContext = this;

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
                bluetoothTransmit();
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

    private void bluetoothTransmit() {

        if ( mBluetoothAdapter == null ) {
            // Get local Bluetooth adapter
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                AppConfig.showMessageDialog(this, R.string.err_bluetooth_not_supported_device);
                return;
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
                // Otherwise, setup the chat session
            } else {
                selectBluetoothMode();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BLUETOOTH:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    selectBluetoothMode();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    AppConfig.showMessageDialog(this, R.string.bt_not_enabled_leaving);
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    private void sendBluetoothMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(mContext, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);

                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.e("###########", readMessage);
                    break;
                case Constants.MESSAGE_CONNECTED:
                    // save the connected device's name
                    String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    sendBluetoothMessage("###############");
                    break;
                case Constants.MESSAGE_CONNECT_FAILED:
                case Constants.MESSAGE_CONNECT_CLOSED:
//                    if (null != activity) {
//                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
//                                Toast.LENGTH_SHORT).show();
//                    }
                    break;
            }
        }
    };

    public void selectBluetoothMode() {

        //if (mChatService == null)
        mChatService = new BluetoothChatService(mContext, mHandler);
        mChatService.start();

        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        tvMessage.setText(R.string.msg_select_bluetooth_mode);
        btnYes.setText(R.string.master_device);
        btnNo.setText(R.string.slave_device);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

                Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

                if (mBluetoothAdapter.getScanMode() !=
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivity(discoverableIntent);
                }

            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
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
