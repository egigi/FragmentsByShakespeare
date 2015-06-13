package com.mycompany.fragmentlayout;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //if the screen is in landscape mode, we can show the dialog
            // in-line with the list so we don't need this activity
            finish();
            return;
        }

        if(saveInstanceState == null){
            //during initial setup, plug in the details fragment
            DetailsFragment details = new DetailsFragment();
            details.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}
