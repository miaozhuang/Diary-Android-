package com.example.zhuangmi.diary;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private DiaryDbAdapter mDbHelper;
    private Cursor mDiaryCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();
        renderListView();

    }

    private void renderListView() {
        mDiaryCursor = mDbHelper.getAllNotes();
        startManagingCursor(mDiaryCursor);
        String[] from = new String[] { DiaryDbAdapter.KEY_TITLE,
                DiaryDbAdapter.KEY_CREATED };
        int[] to = new int[] { R.id.text1, R.id.created };
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.eachrow, mDiaryCursor, from, to);
        ListView lv = (ListView)getListView();
        lv.setAdapter(notes);
       // setListAdapter(notes);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case INSERT_ID:
                createDiary();
                return true;
            case DELETE_ID:
                mDbHelper.deleteDiary(getListView().getSelectedItemId());
                renderListView();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDiary() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, EditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onListItemClick(ListView a, View v, int position, long id) {
        super.onListItemClick(a, v, position, id);
        Cursor c = mDiaryCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, EditActivity.class);
        i.putExtra(DiaryDbAdapter.KEY_ROWID, id);
        i.putExtra(DiaryDbAdapter.KEY_TITLE, c.getString(c
                .getColumnIndexOrThrow(DiaryDbAdapter.KEY_TITLE)));
        i.putExtra(DiaryDbAdapter.KEY_BODY, c.getString(c
                .getColumnIndexOrThrow(DiaryDbAdapter.KEY_BODY)));
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        renderListView();
    }

}
