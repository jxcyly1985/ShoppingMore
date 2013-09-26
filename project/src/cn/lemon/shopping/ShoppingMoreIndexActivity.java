package cn.lemon.shopping;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShoppingMoreIndexActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_more_index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_more_index, menu);
		return true;
	}

}
