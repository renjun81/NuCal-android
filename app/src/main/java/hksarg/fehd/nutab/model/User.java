package hksarg.fehd.nutab.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "tbl_user")
public class User extends Model{

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    public static final int WEIGHT_UNIT_LBS = 0;
    public static final int WEIGHT_UNIT_KG = 1;

    public static final int HEIGHT_UNIT_FEET = 0;
    public static final int HEIGHT_UNIT_METER = 1;

    public static final int ACTIVITY_LEVEL_LOW = 0;
    public static final int ACTIVITY_LEVEL_MEDIUM = 1;
    public static final int ACTIVITY_LEVEL_HIGH = 2;

    @Column(name = "name")
    public String   name;

    @Column(name="gender")
    public int  gender;

    @Column(name="weight")
    public float weight;

    @Column(name="weight_unit")
    public int weightUnit;

    @Column(name="height")
    public float height;

    @Column(name="height_unit")
    public float heightUnit;

    @Column(name="age")
    public int age;

    @Column(name="activity_level")
    public int activityLevel;

    @Column(name="is_asian")
    public boolean isAsian;

    @Column(name="energy_required")
    public int energyRequired;
}
