package draughts.com.draughts;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener {

    private DraughtsView mDraughtsView;
    private EditText mETBoardSize;
    private int minBoardSize = 8;
    private int maxBoardSize = 20;
    private int mSize = minBoardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_reset_game).setOnClickListener(this);
        mDraughtsView = (DraughtsView) findViewById(R.id.draught_view);
        mETBoardSize = (EditText) findViewById(R.id.et_board_size);
        mETBoardSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mETBoardSize.getText().toString().equals("")){
                    return;
                }
                mSize = Integer.parseInt(mETBoardSize.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (mSize < minBoardSize) {
            mSize = minBoardSize;
            mETBoardSize.setText(getResources().getString(R.string.lbl_min_size_board));
        } else if (mSize > maxBoardSize) {
            mSize = maxBoardSize;
            mETBoardSize.setText(getResources().getString(R.string.lbl_max_size_board));
        }
        mDraughtsView.resetGame(mSize);
    }
}
