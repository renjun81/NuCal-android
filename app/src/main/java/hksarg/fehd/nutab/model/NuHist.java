package hksarg.fehd.nutab.model;

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

    public long userId;

    public void putFood(int foodId, int amount) {
        if ( foodList == null )
            foodList = new SparseIntArray();

        foodList.put(foodId, amount);
    }

    public String getRecordTimeString(){
        if ( recordTime == null )
            return "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy MMM dd hh:mm a", Locale.getDefault());
        return fmt.format(recordTime);
    }

    public static List<NuHist> getAll() {
        return new Select().from(NuHist.class).execute();
    }
}
