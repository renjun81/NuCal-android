package hksarg.fehd.nutab;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


public class CustomTextWatcher  implements TextWatcher {
    protected EditText mEditText;
    protected View mAsteriskView;

    public CustomTextWatcher(EditText editText, View asteriskView) {
        mEditText = editText;
        mAsteriskView = asteriskView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if ( s.length() > 0 ) {
            mEditText.setBackgroundResource(R.drawable.txtfield_normal);
            mAsteriskView.setVisibility(View.INVISIBLE);
        }
        else {
            mEditText.setBackgroundResource(R.drawable.txtfield_error);
            mAsteriskView.setVisibility(View.VISIBLE);
        }
    }
}
