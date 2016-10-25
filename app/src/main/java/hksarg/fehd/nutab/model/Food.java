package hksarg.fehd.nutab.model;

import android.support.annotation.ColorInt;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


@Table(name = "tbl_food")
public class Food extends Model {

    public static final int PACKAGE_UNIT_G = 0;
    public static final int PACKAGE_UNIT_ML = 1;

    public static final int ENERGY_UNIT_KCAL = 0;
    public static final int ENERGY_UNIT_KJ = 1;

    public static final int CARBO_TYPE_GENERAL = 0;
    public static final int CARBO_TYPE_TOTAL = 1;
    public static final int CARBO_TYPE_AVAILABLE = 2;

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

}