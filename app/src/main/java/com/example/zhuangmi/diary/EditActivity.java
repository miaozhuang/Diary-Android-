package com.example.zhuangmi.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Y510P on 10/17/2015.
 */
public class EditActivity extends Activity {
    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DiaryDbAdapter mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdiary);
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);//?

        Button confirmButton = (Button) findViewById(R.id.confirm);//?

        mRowId = null;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String title = extras.getString(DiaryDbAdapter.KEY_TITLE);
            String body = extras.getString(DiaryDbAdapter.KEY_BODY);
            mRowId = extras.getLong(DiaryDbAdapter.KEY_ROWID);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String title = mTitleText.getText().toString();
                String body = mBodyText.getText().toString();
                if (mRowId != null) {
                    mDbHelper.updateDiary(mRowId, title, body);
                } else
                    mDbHelper.createDiary(title, body);
                Intent mIntent = new Intent();
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });


    }
}
