package com.ysq.android.utils.tools;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class YImageUtils {
    public static final String CONTENT = "content";
    public static final String FILE = "file";

    /**
     * 根据uri获取图片, 分辨率为1080*1920
     *
     * @param context 查找图片的Context
     * @param uri     图片的Uri
     * @return 图片
     */
    public static Bitmap getBitmap(Context context, Uri uri) {
        // uri指向的文件路径
        String filePath = getFilePath(context, uri);
        if (null == filePath) {
            return null;
        }
        return getBitmap(filePath, 1080, 1920);
    }

    /**
     * 按照指定大小进行压缩，建议使用
     *
     * @param bitmap  原图
     * @param maxSize 自定大小,单位kb
     * @return 压缩后的图片
     */
    public static Bitmap compress(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        if (maxSize * 1024 > (float) out.toByteArray().length) {
            return bitmap;
        }
        float zoom = (float) Math.sqrt(maxSize * 1024 / (float) out.toByteArray().length);
        out.reset();
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();

        result.compress(Bitmap.CompressFormat.JPEG, 100, out);
        while (out.toByteArray().length > maxSize * 1024) {
            System.out.println(out.toByteArray().length);
            matrix.setScale(0.95f, 0.95f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        return result;
    }

    /**
     * 获取文件的路径
     *
     * @param context 查找文件的Context
     * @param uri     文件的Uri
     * @return 文件的绝对路径
     */
    private static String getFilePath(Context context, Uri uri) {
        String filePath = null;
        if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{Images.Media.DATA}, null, null,
                    null);
            if (null == cursor) {
                return null;
            }
            try {
                if (cursor.moveToNext()) {
                    filePath = cursor.getString(cursor.getColumnIndex(Images.Media.DATA));
                }
            } finally {
                cursor.close();
            }
        }
        // 从文件中选择
        if (FILE.equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 根据指定分辨率对图片进行等比压缩, 图片质量变化太大，不建议使用
     *
     * @param srcPath 图片路径
     * @param hh      最大高度
     * @param ww      最大宽度
     * @return 压缩后的图片
     */
    private static Bitmap getBitmap(String srcPath, float hh, float ww) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

}
