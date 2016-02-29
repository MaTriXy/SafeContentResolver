package de.cketti.safecontentresolver.sample;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInternalFile();
            }
        });
    }

    private void shareInternalFile() {
        File internalFile = SampleApplication.getInternalFile(getApplicationContext());
        Uri streamUri = Uri.fromFile(internalFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("x-test/madeup");
        shareIntent.putExtra(Intent.EXTRA_STREAM, streamUri);
        shareIntent.setPackage(getPackageName());

        startActivity(shareIntent);
    }
}
