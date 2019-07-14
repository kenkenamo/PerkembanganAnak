package com.niken.tkalfiizzah.activity.berinilai;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.BuildConfig;
import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.CustomDialog;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.Utils;
import com.niken.tkalfiizzah.base.BaseActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityBeriNilaiBinding;

import java.io.File;
import java.io.IOException;

/**
 * Created by Firman on 7/6/2019.
 */
public class BeriNilaiActivity extends BaseActivity<ActivityBeriNilaiBinding, BeriNilaiViewModel> implements OnExecuteListener<Boolean> {
    private static final String TAG = BeriNilaiActivity.class.getSimpleName();
    private final int REQUEST_PERMISSION_CODE = 10;
    private String[] REQUEST_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private BeriNilaiViewModel viewModel;

    public static void start(Context context, String c_JADWAL, String c_SISWA, String namaSiswa, int nilai) {
        Intent starter = new Intent(context, BeriNilaiActivity.class);
        starter.putExtra("C_JADWAL", c_JADWAL);
        starter.putExtra("C_SISWA", c_SISWA);
        starter.putExtra("V_NAMA", namaSiswa);
        starter.putExtra("C_NILAI", nilai);
        context.startActivity(starter);
    }


    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_beri_nilai;
    }

    @Override
    public BeriNilaiViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new BeriNilaiViewModel(this, this);
        return viewModel;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        String c_JADWAL = i.getStringExtra("C_JADWAL");
        String c_SISWA = i.getStringExtra("C_SISWA");
        String namaSiswa = i.getStringExtra("V_NAMA");
        int nilai = i.getIntExtra("C_NILAI", 0);
        viewModel.setData(c_JADWAL, c_SISWA, namaSiswa);
        viewModel.getNilai().set(nilai);

        AndroidNetworking.get(Constant.URL_IMAGE)
                .addQueryParameter("C_JADWAL", c_JADWAL)
                .addQueryParameter("C_SISWA", c_SISWA)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        viewModel.setImage(Uri.EMPTY);
                        if (!isDestroyed())
                            getBinding().image.setImageBitmap(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        getBinding().tambahFoto.setOnClickListener(v -> {
            if (!hasPermissions(this, REQUEST_PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, REQUEST_PERMISSION_CODE);
                Toast.makeText(this, "Mohon izinkan penggunaan kamera dan file.", Toast.LENGTH_SHORT).show();
                return;
            }
            CharSequence[] charSequences = {"Ambil Foto", "Pilih dari Galeri", "Batal"};
            CustomDialog.get(this)
                    .title("Pilih")
                    .items(charSequences, (dialog, which) -> {
                        if (which == 0) {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                File imageFile = null;
                                try {
                                    imageFile = Utils.createImageFile(this, "foto-siswa");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Uri photoURI = FileProvider.getUriForFile(this,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        imageFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(intent, 1);

                            } else {
                                File imageFile = null;
                                try {
                                    imageFile = Utils.createImageFile(this, "foto-siswa");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                                startActivityForResult(intent, 1);

                            }

//                            File f = File.createTempFile( "take-image", ".jpg",Utils.getImagePath(this));
//                            if (!f.exists()) {
//                                try {
//                                    f.createNewFile();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        } else if (which == 1) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        } else {
                            dialog.dismiss();
                        }
                    }).show();
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:

                if (grantResults.length > 0) {
                    StringBuilder permissionsDenied = new StringBuilder();
                    for (String per : REQUEST_PERMISSIONS) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied.append("\n").append(per);

                        }

                    }
                    if (!permissionsDenied.toString().isEmpty()) {
                        CustomDialog.get(this, R.style.AppTheme_DialogTheme)
                                .title("Perizinan")
                                .message("Mohon izinkan penggunaan kamera dan file.")
                                .addPositiveButton(R.string.ya, null)
                                .show();
                    }
                }
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: resultCode=" + resultCode + ", requestCode=" + requestCode + ", data=" + data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File imageFile = Utils.getImagePath(this).listFiles()[0];
                viewModel.setImage(Uri.fromFile(imageFile));
//                try {
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                    bitmap = getResizedBitmap(bitmap, 400);
//                    IDProf.setImageBitmap(bitmap);
//                    BitMapToString(bitmap);
//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                viewModel.setImage(Uri.parse(picturePath));
//                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
//                thumbnail = getResizedBitmap(thumbnail, 400);
//                Log.w("path of image from gallery......******************.........", picturePath + "");
//                IDProf.setImageBitmap(thumbnail);
//                BitMapToString(thumbnail);
            }
        }
    }

    @Override
    public void onExecuted(Boolean aBoolean) {
        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(Throwable throwable) {

    }

//    public String BitMapToString(Bitmap userImage1) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
//        byte[] b = baos.toByteArray();
//        String Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
//        return Document_img1;
//    }


}
