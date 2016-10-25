package hksarg.fehd.nutab.model;

import android.database.Cursor;
import android.graphics.Bitmap;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import org.parceler.Parcel;

import java.nio.ByteBuffer;
import java.util.List;

@Parcel(analyze = User.class)
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

    public long setAsDefaultUser() {
        new Update(User.class).set("is_active=0").execute();
        this.isActive = true;
        return save();
    }
}
