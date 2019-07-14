package com.niken.tkalfiizzah;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Utils {

    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_BLUETOOTH = 3;

    public static int dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int dayOfWeek(String date, String pattern) throws ParseException {
        return dayOfWeek(parseDate(date, pattern));
    }

    public static long parseCommaToLong(String parse, long defaultLong) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        Number number = defaultLong;
        try {
            number = format.parse(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return number.longValue();
    }

    public static String formatIDR(long idr) {
        DecimalFormat kursIDR = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator(',');
        kursIDR.setDecimalFormatSymbols(formatRp);

        return kursIDR.format(idr);
    }

    public static Date parseDate(String source, String pattern) throws ParseException {
        if (source == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateFormat.parse(source);
    }

    public static Date parseDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.getTime();
    }

    public static String formatDate(long source, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateFormat.format(source);
    }

    public static String formatDate(Date source, String pattern) {
        if (source == null)
            return "A/N";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateFormat.format(source);
    }

    public static String reformatDate(String source, String patternSource, String patternResult) {
        if (source == null)
            return "A/N";
        Date date = null;
        try {
            date = parseDate(source, patternSource);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate(date, patternResult);
    }

    public static String addZeroTime(int time) {
        return time > 9 ? String.valueOf(time) : "0" + time;
    }

    public static int getConnectionStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return TYPE_UNKNOWN;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, 0);
            }
        }
    }

    public static int dpToPx(int dp, Context mContext) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static Spanned fromHTML(String html) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }

        return Html.fromHtml(html);
    }

    public static Spanned reformatHTML(String text, String replace) {
        String html = String.format(text, replace);
        return fromHTML(html);
    }

    public static File createImageFile(Context context, String imageFileName) throws IOException {

        File files = getImagePath(context);

        for (File file : files.listFiles())
            file.delete();

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                files     /* directory */
        );
    }

    public static File getImagePath(Context context) {

        String dirPath = context.getExternalCacheDir().getAbsolutePath() +
                File.separator + "images";
        File imageFile = new File(dirPath);

        if (!imageFile.exists())
            imageFile.mkdirs();
        return imageFile;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        if (true) {
            int MAX_ALLOWED_HEIGHT = 800;
            int MAX_ALLOWED_WIDTH = 800;

            if (maxSize > 0)
                MAX_ALLOWED_HEIGHT = maxSize;

            if (maxSize > 0)
                MAX_ALLOWED_WIDTH = maxSize;


            int outWidth;
            int outHeight;

            int inWidth = image.getWidth();
            int inHeight = image.getHeight();
            if (inWidth > inHeight) {
                outWidth = MAX_ALLOWED_WIDTH;
                outHeight = (inHeight * MAX_ALLOWED_HEIGHT) / inWidth;
            } else {
                outHeight = MAX_ALLOWED_HEIGHT;
                outWidth = (inWidth * MAX_ALLOWED_WIDTH) / inHeight;
            }

            return Bitmap.createScaledBitmap(image, outWidth, outHeight, false);

        }
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String imageAbsolutePath) throws IOException {
        ExifInterface ei = new ExifInterface(imageAbsolutePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static String encodeImage(Uri uri) {
        Bitmap bm = null;
        try {
            bm = modifyOrientation(BitmapFactory.decodeFile(uri.getPath()), uri.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bm = Utils.getResizedBitmap(bm, 800);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap decodeImage(String base64) {
        byte[] b = Base64.decode(base64, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}
