package com.example.myadrapp;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.util.Log;

public class FileCopyTest {

    public static final String SrcFile1 = "/sdcard/Android4.3_r2.1.tar.gz";
    public static final String DesFile1 = "/sdcard/Des_Android4.3_r2.1.tar.gz";

    public static final String SrcFile2 = "/sdcard/bluegriffon-1.6.2-Ubuntu12.04-x86_64.tar.bz2";
    public static final String DesFile2 = "/sdcard/Des_bluegriffon-1.6.2-Ubuntu12.04-x86_64.tar.bz2";

    public static final int BUFFER_SIZE_8192 = 8192;
    public static final int BUFFER_SIZE_100K = 100 * 1024;
    public static final int BUFFER_SIZE_1M = 1000 * 1024;

    public static final int BUFFER_SIZE = BUFFER_SIZE_8192;

    public static final String SrcFile = SrcFile2;
    public static final String DesFile = DesFile2;

    public static void copyFileByStream() {

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        Log.i("leiyong","copyFileByStream START time " + System.currentTimeMillis());

        try {
            inputStream = new FileInputStream(SrcFile);
            outputStream = new FileOutputStream(DesFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        Log.i("leiyong", "copyFileByStream END time " + System.currentTimeMillis());

    }

    public static void copyFileByBufferedStream() {

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        Log.i("leiyong","copyFileByBufferedStream START time " + System.currentTimeMillis());

        try {
            inputStream = new FileInputStream(SrcFile);
            outputStream = new FileOutputStream(DesFile);

            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
                bufferedOutputStream.flush();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        Log.i("leiyong","copyFileByBufferedStream END time " + System.currentTimeMillis());

    }

    public static void copyFileByChannel() {

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        Log.i("leiyong","copyFileByChannel START time " + System.currentTimeMillis());

        try {
            inputStream = new FileInputStream(SrcFile);
            outputStream = new FileOutputStream(DesFile);
            inChannel = inputStream.getChannel();
            outChannel = outputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            while ((inChannel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                if (byteBuffer.hasRemaining()) {
                    outChannel.write(byteBuffer);
                }
                byteBuffer.clear();
            }

//            inChannel.transferTo(0, inChannel.size(), outChannel);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (inChannel != null) {
                    try {
                        inChannel.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        Log.i("leiyong","copyFileByChannel END time " + System.currentTimeMillis());

    }

    public static void main(String[] args) {

        copyFileByStream();
        // copyFileByBufferedStream();
        // copyFileByChannel();
    }

}