package de.cketti.safecontentresolver.sample;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.shareFileButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInternalFile();
            }
        });
        findViewById(R.id.shareAllowedContentButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAllowedContent();
            }
        });
        findViewById(R.id.shareBlockedContentButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBlockedContent();
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

    private void shareAllowedContent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("x-test/madeup");
        Uri contentUri = Uri.parse("content://" + SampleContentProvider.AUTHORITY + "/dummy");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setPackage(getPackageName());

        startActivity(shareIntent);
    }

    private void shareBlockedContent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("x-test/madeup");
        Uri contentUri = Uri.parse("content://" + SampleInternalContentProvider.AUTHORITY + "/dummy");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setPackage(getPackageName());

        startActivity(shareIntent);
    }
}
