package com.example.patcher;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends Activity {
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static String[] PERMISSIONS = new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"};

    private Button bantenna, bstartgame, bexitt;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SharedPreferences sharedPreferences = getSharedPreferences("c", 0);
        if (VERSION.SDK_INT >= 15) {
            Object obj = null;
            for (String checkSelfPermission : PERMISSIONS) {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(checkSelfPermission) == PackageManager.PERMISSION_DENIED) {
                        obj = Integer.valueOf(1);
                    }
                }
            }
            if (obj != null) {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 101);
                }
            }
        }

        bantenna = findViewById(R.id.radaron);
        bantenna.setOnClickListener(new OnClickListener() {
            private Boolean isActive = true;

            public void onClick(View v) {

                if (this.isActive) {
                    try {
                        raf = new RandomAccessFile("/storage/emulated/0/Android/data/com.dts.freefireth/files/contentcache/Compulsory/android/gameassetbundles/avatar/assetindexer.ChmtrFErUImPUvnstlQ98bJ4L9I~3D", "rw");
                        write(31496, "00 00 00 7F 43 00 00 80");
                        write(20684, "00 00 00 7F 43 00 00 80");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bantenna.setTextColor(Color.parseColor("#FF00FF00"));
                    bantenna.setText("Antenna: ON");
                    this.isActive = false;
                    Toast.makeText(getApplicationContext(), "Antenna: ON", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    raf = new RandomAccessFile("/storage/emulated/0/Android/data/com.dts.freefireth/files/contentcache/Compulsory/android/gameassetbundles/avatar/assetindexer.ChmtrFErUImPUvnstlQ98bJ4L9I~3D", "rw");
                    write(31496, "00 00 00 00 00 00 00 80");
                    write(20684, "00 00 00 00 00 00 00 80");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bantenna.setTextColor(Color.parseColor("#FFFF0000"));
                bantenna.setText("Antenna: OFF");
                this.isActive = true;
                Toast.makeText(getApplicationContext(), "Antenna: OFF", Toast.LENGTH_SHORT).show();
            }
        });

        bstartgame = findViewById(R.id.startml);
        bstartgame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    MainActivity.this.startActivity(MainActivity.this.getPackageManager().getLaunchIntentForPackage("com.dts.freefireth"));
                    Toast.makeText(getApplicationContext(), "Start Free Fire", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Error: Free Fire is not installed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        bexitt = findViewById(R.id.exit);
        bexitt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    private static RandomAccessFile raf;

    private static byte[] Hex2b(String str) {
        if (str.contains(" ")) {
            str = str.replace(" ", "");
        }
        if (str == null) {
            throw new IllegalArgumentException("hex == null");
        } else if (str.length() % 2 != 0) {
            throw new IllegalArgumentException("Unexpected hex string: " + str);
        } else {
            byte[] bArr = new byte[(str.length() / 2)];
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = (byte) ((decodeHexDigit(str.charAt(i * 2)) << 4) + decodeHexDigit(str.charAt((i * 2) + 1)));
            }
            return bArr;
        }
    }

    private static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'a' && c <= 'f') {
            return (c - 97) + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 65) + 10;
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c);
    }

    private static void write(int i, String str) {
        try {
            raf.seek(i);
            raf.write(Hex2b(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] PERMISSIONS, int[] grantResults) {
        if (requestCode == 100) {
            for (int i2 = 0; i2 < PERMISSIONS.length; i2++) {
                if (grantResults[i2] == -1) {
                    Builder builder = new Builder(this);
                    builder.setMessage("We cannot continue without needed permission!");
                    builder.setCancelable(true);
                    builder.create().show();
                }
            }
        }
    }
}
