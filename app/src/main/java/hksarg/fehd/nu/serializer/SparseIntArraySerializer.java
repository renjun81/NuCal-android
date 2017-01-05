package hksarg.fehd.nu.serializer;


import android.util.SparseIntArray;

import com.activeandroid.serializer.TypeSerializer;

import org.json.JSONArray;
import org.json.JSONObject;

final public class SparseIntArraySerializer extends TypeSerializer {
    @Override
    public Class<?> getDeserializedType() {
        return SparseIntArray.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public Object serialize(Object data) {
        if ( data != null ) {
            JSONArray jsonArray = new JSONArray();

            try {
                SparseIntArray spArray = (SparseIntArray) data;
                int nCount = spArray.size();
                JSONObject json;
                for (int i = 0; i < nCount; i++) {
                    json = new JSONObject();
                    json.put("food_id", spArray.keyAt(i));
                    json.put("food_amount", spArray.valueAt(i));
                    jsonArray.put(json);
                }
            }
            catch(Exception e) {}

            return jsonArray.toString();
        }
        return null;
    }

    @Override
    public SparseIntArray deserialize(Object data) {
        if ( data != null ) {
            SparseIntArray spArray = new SparseIntArray();

            try {
                JSONArray jsonArray = new JSONArray((String)data);
                JSONObject json;
                int nCount = jsonArray.length();
                for ( int i = 0; i < nCount; i++ ) {
                    json = jsonArray.getJSONObject(i);
                    spArray.put(json.getInt("food_id"), json.getInt("food_amount"));
                }
            }
            catch (Exception e) {}

            return spArray;
        }
        return null;
    }
}
