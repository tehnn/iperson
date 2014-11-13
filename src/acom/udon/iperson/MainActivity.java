package acom.udon.iperson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	SQLiteDatabase db;
	Context context;
	ListView mListview;

	ArrayList<String> mList = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		mListview = (ListView) findViewById(R.id.listView1);

		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String val = (String) parent.getItemAtPosition(position);

				Intent intent = new Intent(getApplicationContext(),
						AddActivity.class);
				intent.putExtra("name1", val);
				startActivity(intent);

			}
		});

	}// end oncreate

	public void AddPerson(View v) {
		Intent intent = new Intent(context, AddActivity.class);
		startActivity(intent);
	}

	public void CopyDb(View v) {

		// copy db.db จาก assests ไปที่ เพคดก0

		String mPackage = context.getPackageName();
		String DB_PATH = "/data/data/" + mPackage + "/databases/db.db";

		OutputStream myOutput = null;
		InputStream myInput = null;
		try {
			myInput = context.getAssets().open("db.db");

			myOutput = new FileOutputStream(DB_PATH);
			copyFile(myInput, myOutput);

			Log.d(context.getPackageName(), "ตั้งค่าฐานข้อมูลสำเร็จ");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// ส่วนดึงข้อมูลจาก DB
		mList.clear();
		db = context.openOrCreateDatabase("db.db",
				SQLiteDatabase.OPEN_READWRITE, null);

		db.setLocale(Locale.getDefault());
		SQLiteCursor cur = (SQLiteCursor) db.rawQuery(
				"select name from person", null);

		if (cur.getCount() > 0) {
			cur.moveToFirst();
			do {
				String res = cur.getString(0);
				mList.add(res);
			} while (cur.moveToNext());
		}

		adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, mList);
		mListview.setAdapter(adapter);
		db.close();

		// จบดึงข้อมูลจาก DB

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_init_db) {

			return true;
		}

		if (id == R.id.action_export_db) {

			String mPackage = getApplicationContext().getPackageName();
			String DB_PATH = "/data/data/" + mPackage + "/databases/db.db";
			String SD_CARD = "/mnt/sdcard/db_export.db";
			OutputStream myOutput = null;
			InputStream myInput = null;
			try {
				myInput = new FileInputStream(DB_PATH);
				myOutput = new FileOutputStream(SD_CARD);
				copyFile(myInput, myOutput);
				Log.d(getPackageName(), "Export OK");
				Toast.makeText(getApplicationContext(), "OK",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	} // end selectected

	// copyFile Method
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		out.close();
		out.flush();
		in.close();
	}// End copyFile Method

}// end class
