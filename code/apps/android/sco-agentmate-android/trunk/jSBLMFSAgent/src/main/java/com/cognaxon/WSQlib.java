package com.cognaxon;

public class WSQlib {

    public static native int[] ReadImageFromFile(int[] dimension, String fileName);

    public static native void SaveImageToFile(int[] data, int width, int height, String fileName, int type);

    public static native void WriteWSQ_bitrate(double bitrate);

    public static native double ReadWSQ_bitrate();

    public static native void WriteWSQ_ppi(int ppi);

    public static native int ReadWSQ_ppi();

    public static native void WriteWSQ_comment(String comment);

    public native String ReadWSQ_comment();

    public static native void WriteTIFFcompression(int tiff_compression);

    public static native void WriteTIFFpredictor(int tiff_predictor);

    public static native byte[] WSQ_decode_stream(byte[] input, int[] imageProperties);

    public static native byte[] WSQ_encode_stream(byte[] input, int width, int height, double bitrate, int ppi, String comment_text);

    public static native void Set_Path_of_WSQ_library(String path_of_WSQ_library);

    public static native String GenerateWSQLibrarySerialNumber();

    public static native int UnlockWSQLibrary(String authorization_code);

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("WSQ_library_android");
    }
}
