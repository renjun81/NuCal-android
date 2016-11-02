package hksarg.fehd.nutab;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


public class CustomTextWatcher  implements TextWatcher {
    protected EditText mEditText;

    public CustomTextWatcher(EditText e) {
        mEditText = e;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if ( s.length() > 0 )
            mEditText.setBackgroundResource(R.drawable.txtfield_normal);
        else
            mEditText.setBackgroundResource(R.drawable.txtfield_error);
    }
}
