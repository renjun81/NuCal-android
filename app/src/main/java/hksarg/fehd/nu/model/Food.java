package hksarg.fehd.nu.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Table(name = "tbl_food")
public class Food extends Model{

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

    @Expose
    @Column(name = "name", unique = true, onUniqueConflict = Column.ConflictAction.FAIL)
    public String name;

    @Expose
    @Column(name="package_size")
    public float packageSize;

    @Expose
    @Column(name="package_unit")
    public int packageUnit;

    @Expose
    @Column(name="energy_size")
    public float energySize;

    @Expose
    @Column(name="energy_unit")
    public int energyUnit;

    @Expose
    @Column(name="protein")
    public float protein;

    @Expose
    @Column(name="total_fat")
    public float totalFat;

    @Expose
    @Column(name="saturated_fat")
    public float saturatedFat;

    @Expose
    @Column(name="trans_fat")
    public float transFat;

    @Expose
    @Column(name="cholesterol")
    public float cholesterol;

    @Expose
    @Column(name="carbohydrate_type")
    public int carbohydrateType;

    @Expose
    @Column(name="carbohydrate")
    public float carbohydrate;

    @Expose
    @Column(name="dietary_fibre")
    public float dietaryFibre;

    @Expose
    @Column(name="sugar")
    public float sugar;

    @Expose
    @Column(name="sodium")
    public float sodium;

    @Expose
    @Column(name="is_selected")
    public boolean isSelected;

    public Food() {
        energySize = -1;
        protein = -1;
        totalFat = -1;
        saturatedFat = -1;
        transFat = -1;
        cholesterol = -1;
        carbohydrate = -1;
        dietaryFibre = -1;
        sugar = -1;
        sodium = -1;
    }

    public boolean isFoodType(int foodType) {
        switch(foodType) {
            case FOOD_LOW_FAT:
                if ( packageUnit == PACKAGE_UNIT_G)
                    return totalFat >= 0 && totalFat/packageSize < 3.f/100;
                else
                    return totalFat >= 0 && totalFat/packageSize < 1.5/100;

            case FOOD_LOW_SALT:
                return sodium >= 0 && (sodium/1000)/packageSize < 0.12/100;

            case FOOD_LOW_SUGAR:
                return sugar >= 0 && sugar/packageSize < 5.f/100;

            case FOOD_THREE_LOW:
                return isFoodType(FOOD_LOW_FAT) && isFoodType(FOOD_LOW_SALT) && isFoodType(FOOD_LOW_SUGAR);
        }

        return true;
    }

    public static int getSelectedCount() {
        String query = new Select().from(Food.class).where("is_selected=1").toCountSql();
        return SQLiteUtils.intQuery(query, null);
    }

    public static List<Food> getSelectedFoods() {
        return new Select().from(Food.class).where("is_selected=1").execute();
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