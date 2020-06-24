package de.cketti.safecontentresolver.sample;


import java.io.File;
import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import okio.BufferedSink;
import okio.Okio;


public class SampleApplication extends Application {
    private static final String INTERNAL_FILE_NAME = "internal.dat";


    @Override
    public void onCreate() {
        super.onCreate();

        disableFileUriExposedException();
        createInternalFileIfNecessary();
    }

    private void disableFileUriExposedException() {
        // We're lazy and just disable all StrictMode.VmPolicy checks
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
    }

    private void createInternalFileIfNecessary() {
        File internalFile = getInternalFile(this);
        if (!internalFile.exists()) {
            createInternalFile(internalFile);
            writeSecretToInternalFile(internalFile);
        }
    }

    private void createInternalFile(File internalFile) {
        try {
            boolean success = internalFile.createNewFile();
            if (!success) {
                throw new RuntimeException("File wasn't created");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating file", e);
        }
    }

    private void writeSecretToInternalFile(File internalFile) {
        try {
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(internalFile));
            try {
                bufferedSink.writeUtf8("secret");
            } finally {
                bufferedSink.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing file", e);
        }
    }

    static File getInternalFile(Context context) {
        File filesDir = context.getFilesDir();
        return new File(filesDir, INTERNAL_FILE_NAME);
    }
}
