package hksarg.fehd.nutab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import hksarg.fehd.nutab.model.NuHist;
import hksarg.fehd.nutab.model.User;

public class MyIntakeHistoryActivity extends AppCompatActivity {

    User    m_user;
    ListView  m_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intake_history);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.menu3b_t30_list);

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
                Intent intent = new Intent(MyIntakeHistoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

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

        m_listView = (ListView) findViewById(R.id.listView);
        m_listView.setAdapter(new ListViewAdapter(this));
        m_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NuHist item = ((ListViewAdapter)parent.getAdapter()).getItem(position);
                Intent intent = new Intent(MyIntakeHistoryActivity.this, MyIntakeActivity.class);
                intent.putExtra("nu_hist_id", item.getId());
                startActivity(intent);
            }
        });
    }


    class ListViewAdapter extends BaseSwipeAdapter {

        private Context mContext;
        private List<NuHist> m_data;

        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
            notifyDataSetChanged();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipeView;
        }

        private void deleteRow(final View v, final NuHist itemData) {
            final int initialHeight = v.getMeasuredHeight();
            TranslateAnimation anim = new TranslateAnimation(0, v.getWidth(), 0, 0);
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
                    itemData.delete();
                    notifyDataSetChanged();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            anim.setDuration(400);
            v.startAnimation(anim);
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_intake_history, null);
            final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                }
            });
            v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close(true);
                    NuHist itemData = (NuHist) swipeLayout.getTag();
                    deleteRow(swipeLayout, itemData);
                }
            });
            return v;
        }

        @Override
        public void fillValues(int position, View convertView) {
            TextView tvRecordedTime = (TextView) convertView.findViewById(R.id.tvRecordedTime);
            NuHist itemData = getItem(position);
            tvRecordedTime.setText(itemData.getRecordTimeString(tvRecordedTime.getContext()));
            convertView.setTag(itemData);
        }

        @Override
        public int getCount() {
            return m_data == null ? 0 : m_data.size();
        }

        @Override
        public NuHist getItem(int position) {
            return m_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            m_data = NuHist.getAllByUser(m_user.getId());
            super.notifyDataSetChanged();
        }
    }
}
