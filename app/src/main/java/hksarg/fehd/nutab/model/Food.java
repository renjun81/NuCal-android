package hksarg.fehd.nutab.model;

import android.support.annotation.ColorInt;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Table(name = "tbl_food")
public class Food extends Model {

    public static final int PACKAGE_UNIT_G = 0;
    public static final int PACKAGE_UNIT_ML = 1;

    public static final int ENERGY_UNIT_KCAL = 0;
    public static final int ENERGY_UNIT_KJ = 1;

    public static final int CARBO_TYPE_GENERAL = 0;
    public static final int CARBO_TYPE_TOTAL = 1;
    public static final int CARBO_TYPE_AVAILABLE = 2;

    public static final int FOOD_ALL = 0;
    public static final int FOOD_LOW_FAT = 1;
    public static final int FOOD_LOW_SALT = 2;
    public static final int FOOD_LOW_SUGAR = 3;
    public static final int FOOD_THREE_LOW = 4;

    @Column(name = "name")
    public String name;

    @Column(name="package_size")
    public int packageSize;

    @Column(name="package_unit")
    public int packageUnit;

    @Column(name="energy_size")
    public int energySize;

    @Column(name="energy_unit")
    public int energyUnit;

    @Column(name="protein")
    public float protein;

    @Column(name="saturated_fat")
    public float saturatedFat;

    @Column(name="trans_fat")
    public float transFat;

    @Column(name="cholesterol")
    public float cholesterol;

    @Column(name="carbohydrate_type")
    public int carbohydrateType;

    @Column(name="carbo_dietary")
    public float carboDietary;

    @Column(name="carbo_sugar")
    public float carboSugar;

    @Column(name="sodium")
    public float sodium;

    @Column(name="is_selected")
    public boolean isSelected;

    public boolean isFoodType(int foodType) {
        switch(foodType) {
            case FOOD_LOW_FAT:
                if ( packageUnit == PACKAGE_UNIT_G)
                    return (saturatedFat + transFat) * 100 / packageSize < 3;
                else
                    return (saturatedFat + transFat) * 100 / packageSize < 1.5;

            case FOOD_LOW_SALT:
                return sodium * 100 / packageSize < 120;

            case FOOD_LOW_SUGAR:
                return (carboDietary + carboSugar) * 100 / packageSize < 5;

            case FOOD_THREE_LOW:
                return isFoodType(FOOD_LOW_FAT) & isFoodType(FOOD_LOW_SALT) & isFoodType(FOOD_LOW_SUGAR);
        }

        return true;
    }

    public static int getSelectedCount() {
        String query = new Select().from(Food.class).where("is_selected=1").toCountSql();
        return SQLiteUtils.intQuery(query, null);
    }

    public static List<Food> getFoodList(int foodType) {
        List<Food> foodList = new Select().from(Food.class).execute();
        if ( foodType == FOOD_ALL || foodList == null)
            return foodList;

        Iterator<Food> itr = foodList.iterator();
        ArrayList<Food> retList = new ArrayList<Food>();
        Food food;
        while ( itr.hasNext() ) {
            food = itr.next();
            if ( food.isFoodType(foodType) )
                retList.add(food);
        }

        return retList;
    }
}