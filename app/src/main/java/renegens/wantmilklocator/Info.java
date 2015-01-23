package renegens.wantmilklocator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class Info extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageButton backButton = (ImageButton) findViewById(R.id.imageView);
		TextView ratebutton = (TextView) findViewById(R.id.idrate);
		TextView shareButton = (TextView) findViewById(R.id.shareit);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

		ratebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("market://details?id=" + getPackageName());
				Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(myAppLinkToMarket);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(Info.this, " Unable to find market app", Toast.LENGTH_LONG).show();
				}
			}
		});

		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=renegens.wantmilklocator");
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
				startActivity(Intent.createChooser(intent, "Share"));

			}
		});

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
