package hksarg.fehd.nutab.model;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Table(name="nu_hist")
public class NuHist extends Model {

    @Column(name="record_time")
    public Date recordTime;

    @Column(name="food_list")
    public SparseIntArray  foodList;

    @Column(name="user_id")
    public long userId;

    public void putFood(int foodId, int amount) {
        if ( foodList == null )
            foodList = new SparseIntArray();

        foodList.put(foodId, amount);
    }

    public String getRecordTimeString(Context context){
        if ( recordTime == null )
            return "";
        Locale current = context.getResources().getConfiguration().locale;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy MMM dd hh:mm a", Locale.getDefault());
        if ( current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
            fmt = new SimpleDateFormat("yyyy年MMMdd日 hh:mm a", Locale.getDefault());
        return fmt.format(recordTime);
    }

    public static List<NuHist> getAllByUser(long userId) {
        return new Select().from(NuHist.class).where("user_id=" + userId).execute();
    }
}
