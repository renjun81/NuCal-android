package hksarg.fehd.nutab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.parceler.Parcels;

import java.util.List;

import hksarg.fehd.nutab.model.User;


public class UserListActivity extends AppCompatActivity {

    Context     m_context;
    ListView    m_listUsers;
    ListViewAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        m_context = this;

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.menu4b_t40_edit);

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
                Intent intent = new Intent(UserListActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.btnAddUser).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(m_context, UserProfileActivity.class);
                intent.putExtra("open_mode", UserProfileActivity.OPEN_FOR_ADD_USER);
                startActivity(intent);
            }
        });

        m_adapter = new ListViewAdapter(m_context);
        m_listUsers = (ListView) findViewById(R.id.listUsers);
        m_listUsers.setAdapter(m_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        m_adapter.notifyDataSetChanged();
    }


    class ListViewAdapter extends BaseSwipeAdapter {

        private Context mContext;
        private List<User> m_data;

        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
            this.m_data = User.getAll();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipeView;
        }

        private void deleteRow(final View v, final User user) {
            final int initialHeight = v.getMeasuredHeight();
            TranslateAnimation anim = new TranslateAnimation(v.getLeft(), v.getLeft() + v.getWidth(), v.getTop(), v.getTop());
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
//            Animation anim = new Animation() {
//                @Override
//                protected void applyTransformation(float interpolatedTime, Transformation t) {
//                    if (interpolatedTime == 1) {
//                        v.setVisibility(View.GONE);
//                    }
//                    else {
//                        v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
//                        v.requestLayout();
//                    }
//                }
//
//                @Override
//                public boolean willChangeBounds() {
//                    return true;
//                }
//            };

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    user.delete();
                    if ( user.isActive ) {
                        User au = new Select().from(User.class).orderBy("RANDOM()").executeSingle();
                        au.setAsDefaultUser();
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            anim.setDuration(800);
            v.startAnimation(anim);
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            User user = getItem(position);
            Log.e("UserList", "position - " + user.getId());
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_user, null);
            final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                }
            });
            ((RadioButton)v.findViewById(R.id.optActive)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    User user = (User) swipeLayout.getTag();
                    if ( isChecked && !user.isActive) {
                        user.setAsDefaultUser();
                        notifyDataSetChanged();
                    }
                }
            });
            v.findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = (User) swipeLayout.getTag();
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    Log.e("UserList", "click - " + user.getId());
                    intent.putExtra("user_id", user.getId());
                    intent.putExtra("open_mode", UserProfileActivity.OPEN_FOR_CHG_USER);
                    mContext.startActivity(intent);
                }
            });
            v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close(true);
                    if ( m_data.size() > 1 ) {
                        User user = (User) swipeLayout.getTag();
                        deleteRow(swipeLayout, user);
                    }
                }
            });
            return v;
        }

        @Override
        public void fillValues(int position, View convertView) {
            RadioButton optActive = (RadioButton) convertView.findViewById(R.id.optActive);
            ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
            TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            User user = getItem(position);
            convertView.setTag(user);
            optActive.setChecked(user.isActive);
            ivAvatar.setImageBitmap(user.getAvatar());
            tvUserName.setText(user.name);
        }

        @Override
        public int getCount() {
            return m_data == null ? 0 : m_data.size();
        }

        @Override
        public User getItem(int position) {
            return m_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            m_data = User.getAll();
            super.notifyDataSetChanged();
        }
    }
}
