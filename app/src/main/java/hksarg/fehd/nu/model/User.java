package hksarg.fehd.nu.model;

import android.database.Cursor;
import android.graphics.Bitmap;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.nio.ByteBuffer;
import java.util.List;

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

    @Column(name="name", unique = true, onUniqueConflict = Column.ConflictAction.FAIL)
    public String name;

    @Column(name="gender")
    public int gender;

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

    @Column(name="is_active")
    public boolean isActive;

    @Column(name="avatar")
    public byte[] avatar;

    @Column(name="avatar_config")
    protected String avatarConfig;

    @Column(name="avatar_width")
    protected int avatarWidth;

    @Column(name="avatar_height")
    protected int avatarHeight;

    public void setAvatar(Bitmap bitmap) {
        if ( bitmap != null && bitmap.getWidth() > 0 ) {
            avatarConfig = bitmap.getConfig().name();
            avatarWidth = bitmap.getWidth();
            avatarHeight = bitmap.getHeight();
            int size = bitmap.getRowBytes() * bitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            bitmap.copyPixelsToBuffer(byteBuffer);
            this.avatar = byteBuffer.array();
        }
    }

    public Bitmap getAvatar() {
        if ( avatar != null && avatarConfig != null ) {
            Bitmap.Config configBmp = Bitmap.Config.valueOf(avatarConfig);
            Bitmap bitmap = Bitmap.createBitmap(avatarWidth, avatarHeight, configBmp);
            ByteBuffer buffer = ByteBuffer.wrap(avatar);
            bitmap.copyPixelsFromBuffer(buffer);
            return bitmap;
        }

        return null;
    }

    public int dailyEnergyRequired() {
        float alpha = 0, beta = 0, gamma = 0;
        if ( age >= 7 && age <= 10 ) {

            if ( activityLevel == ACTIVITY_LEVEL_LOW )
                gamma = 1.55f;
            else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                gamma = 1.75f;
            else
                gamma = 1.95f;

            if ( gender == GENDER_MALE ) {
                alpha = 22.7f;
                beta = 495;
            }
            else {  // female
                alpha = 22.5f;
                beta = 499;
                gamma = gamma - 0.05f;
            }
        }
        else if ( age >= 11 && age <= 13 ) {

            if ( activityLevel == ACTIVITY_LEVEL_LOW )
                gamma = 1.55f;
            else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                gamma = 1.75f;
            else
                gamma = 1.95f;

            if ( gender == GENDER_MALE ) {
                alpha = 17.5f;
                beta = 651;
            }
            else {
                alpha =12.2f;
                beta = 746;
                gamma = gamma - 0.05f;
            }
        }
        else if ( age >= 14 && age <= 17 ) {

            if ( gender == GENDER_MALE ) {
                alpha = 17.5f;
                beta = 651;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.60f;
                else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                    gamma = 1.80f;
                else
                    gamma = 2.05f;
            }
            else {
                alpha = 12.2f;
                beta = 746;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.45f;
                else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                    gamma = 1.65f;
                else
                    gamma = 1.85f;
            }
        }
        else if ( age >= 18 && age <= 49 ) {

            if ( gender == GENDER_MALE ) {
                alpha = 15.3f;
                beta = 679;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.55f;
                else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                    gamma = 1.78f;
                else
                    gamma = 2.10f;
            }
            else {
                alpha = 14.7f;
                beta = 496;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.56f;
                else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                    gamma = 1.64f;
                else
                    gamma = 1.82f;
            }
        }
        else if ( age >= 50 && age <= 59 ) {

            if ( gender == GENDER_MALE ) {
                alpha = 11.6f;
                beta = 879;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.55f;
                else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                    gamma = 1.78f;
                else
                    gamma = 2.10f;
            }
            else {
                alpha = 8.7f;
                beta = 829;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.56f;
                else if ( activityLevel == ACTIVITY_LEVEL_MEDIUM )
                    gamma = 1.64f;
                else
                    gamma = 1.82f;
            }
        }
        else if ( age >= 60 && age <= 69 ) {

            if ( gender == GENDER_MALE ) {
                alpha = 13.5f;
                beta = 487;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.53f;
                else
                    gamma = 1.66f;
            }
            else {
                alpha = 10.5f;
                beta = 596;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.54f;
                else
                    gamma = 1.64f;
            }
        }
        else if ( age >= 70 && age < 79 ) {

            if ( gender == GENDER_MALE ) {
                alpha = 13.5f;
                beta = 487;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.51f;
                else
                    gamma = 1.64f;
            }
            else {
                alpha = 10.5f;
                beta = 596;

                if ( activityLevel == ACTIVITY_LEVEL_LOW )
                    gamma = 1.51f;
                else
                    gamma = 1.62f;
            }
        }
        else if ( age >= 80 ) {

            if ( gender == GENDER_MALE ) {
                alpha = 13.5f;
                beta = 487;
                gamma = 1.49f;
            }
            else {
                alpha = 10.5f;
                beta = 596;
                gamma = 1.49f;
            }
        }

        if ( age >= 18 )
            gamma = gamma * 0.95f;

        return (int)((alpha * weight + beta) * gamma);
    }

    public float energyIntake() {
        if ( energyRequired == 0 ) {
            energyRequired = dailyEnergyRequired();
            energyRequired = energyRequired == 0 ? Integer.MAX_VALUE : energyRequired;
        }
        return energyRequired;
    }

    public float proteinIntake() {
        return energyRequired * 0.12f / 4;
    }

    public float totalFatIntake() {
        return energyRequired * 0.27f / 9;
    }

    public float saturatedFatIntake() {
        return energyRequired * 0.09f / 9;
    }

    public float transFatIntake() {
        return energyRequired * 0.01f / 9;
    }

    public float carbohydrateIntake() {
        return energyRequired * 0.60f / 4;
    }

    public float fibreIntake() {
        return 25;
    }

    public float sugarIntake() {
        return energyRequired * 0.10f / 4;
    }

    public float sodiumIntake() {
        return 2000;
    }

    public float cholesterolIntake() {
        return 300;
    }

    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(User.class).getTableName();
        // Query all items without any conditions
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").from(User.class).toSql();
        // Execute query on the underlying ActiveAndroid SQLite database
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

    public static List<User> getAll() {
        return new Select().from(User.class).execute();
    }

    public boolean setAsDefaultUser() {
        new Update(User.class).set("is_active=0").execute();

        this.isActive = true;
        Long id = save();
        if ( id != null && id.longValue() > 0) {
            return true;
        }

        return false;
    }

    public static User getDefaultUser() {
        return new Select().from(User.class).where("is_active=1").executeSingle();
    }
}
