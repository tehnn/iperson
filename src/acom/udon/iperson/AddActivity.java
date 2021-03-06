package acom.udon.iperson;

import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends ActionBarActivity {

	EditText txtName, txtAge;
	boolean isNewPeople = false;

	Context context;
	SQLiteDatabase db;
	String name1 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		txtName = (EditText) findViewById(R.id.editText1);
		txtAge = (EditText) findViewById(R.id.editText2);
		context = getApplicationContext();

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			name1 = extras.getString("name1");
			isNewPeople = true;
			txtName.setText(name1);

			db = context.openOrCreateDatabase("db.db",
					SQLiteDatabase.OPEN_READWRITE, null);
			SQLiteCursor cur = (SQLiteCursor) db.rawQuery(
					"select name,age from person where name='" + name1 + "'",
					null);

			cur.moveToFirst();
			String t_age = cur.getString(1);

			txtAge.setText(t_age);

			cur.close();

			db.close();
		}

	}// end onCreate

	public void Save(View v) {

		if (!isNewPeople) {
			// / insert
			String name = txtName.getText().toString();
			String t_age = txtAge.getText().toString();
			int age = Integer.parseInt(t_age);

			db = context.openOrCreateDatabase("db.db",
					SQLiteDatabase.OPEN_READWRITE, null);

			db.setLocale(Locale.getDefault());
			String SQL;
			SQL = "INSERT INTO person (name, age) VALUES " + "('" + name + "',"
					+ age + ")";

			db.execSQL(SQL);
			db.close();

		} else {
			// update

			String txt_name = txtName.getText().toString();
			String t_age = txtAge.getText().toString();
			int age = Integer.parseInt(t_age);

			db = context.openOrCreateDatabase("db.db",
					SQLiteDatabase.OPEN_READWRITE, null);

			db.setLocale(Locale.getDefault());
			String SQL;
			SQL = "update person set name='" + txt_name + "', age=" + age
					+ " where name = '" + name1 + "'";

			db.execSQL(SQL);
			db.close();

		}

		finish();
	}

	public void Del(View v) {

		db = context.openOrCreateDatabase("db.db",
				SQLiteDatabase.OPEN_READWRITE, null);

		db.setLocale(Locale.getDefault());
		String SQL;
		SQL = "delete from person where name = '" + name1 + "'";

		db.execSQL(SQL);
		db.close();

		finish();
	}

	public void Cancel(View v) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
