package com.example.android.camera2.basic.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 /2\u00020\u0001:\u0001/B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u000bH\u0002J \u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 H\u0002J\b\u0010\"\u001a\u00020#H\u0002J\b\u0010$\u001a\u00020\u001eH\u0002J&\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010\'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0016J\u001a\u0010-\u001a\u00020.2\u0006\u0010\u0019\u001a\u00020&2\b\u0010+\u001a\u0004\u0018\u00010,H\u0016R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0017\u0010\u0013\u001a\u0004\b\u0014\u0010\u0016\u00a8\u00060"}, d2 = {"Lcom/example/android/camera2/basic/fragments/ImageViewerFragment;", "Landroidx/fragment/app/Fragment;", "()V", "args", "Lcom/example/android/camera2/basic/fragments/ImageViewerFragmentArgs;", "getArgs", "()Lcom/example/android/camera2/basic/fragments/ImageViewerFragmentArgs;", "args$delegate", "Landroidx/navigation/NavArgsLazy;", "bitmapList", "", "Landroid/graphics/Bitmap;", "bitmapOptions", "Landroid/graphics/BitmapFactory$Options;", "bitmapTransformation", "Landroid/graphics/Matrix;", "getBitmapTransformation", "()Landroid/graphics/Matrix;", "bitmapTransformation$delegate", "Lkotlin/Lazy;", "isDepth", "", "()Z", "isDepth$delegate", "addItemToViewPager", "view", "Landroidx/viewpager2/widget/ViewPager2;", "item", "decodeBitmap", "buffer", "", "start", "", "length", "imageViewFactory", "Landroid/widget/ImageView;", "loadInputBuffer", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "", "Companion", "app_debug"})
public final class ImageViewerFragment extends androidx.fragment.app.Fragment {
    
    /**
     * AndroidX navigation arguments
     */
    private final androidx.navigation.NavArgsLazy args$delegate = null;
    
    /**
     * Default Bitmap decoding options
     */
    private final android.graphics.BitmapFactory.Options bitmapOptions = null;
    
    /**
     * Bitmap transformation derived from passed arguments
     */
    private final kotlin.Lazy bitmapTransformation$delegate = null;
    
    /**
     * Flag indicating that there is depth data available for this image
     */
    private final kotlin.Lazy isDepth$delegate = null;
    
    /**
     * Data backing our Bitmap viewpager
     */
    private final java.util.List<android.graphics.Bitmap> bitmapList = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.android.camera2.basic.fragments.ImageViewerFragment.Companion Companion = null;
    private static final java.lang.String TAG = null;
    
    /**
     * Maximum size of [Bitmap] decoded
     */
    private static final int DOWNSAMPLE_SIZE = 1024;
    
    /**
     * These are the magic numbers used to separate the different JPG data chunks
     */
    private static final java.lang.Integer[] JPEG_DELIMITER_BYTES = {-1, -39};
    
    public ImageViewerFragment() {
        super();
    }
    
    /**
     * AndroidX navigation arguments
     */
    private final com.example.android.camera2.basic.fragments.ImageViewerFragmentArgs getArgs() {
        return null;
    }
    
    /**
     * Bitmap transformation derived from passed arguments
     */
    private final android.graphics.Matrix getBitmapTransformation() {
        return null;
    }
    
    /**
     * Flag indicating that there is depth data available for this image
     */
    private final boolean isDepth() {
        return false;
    }
    
    private final android.widget.ImageView imageViewFactory() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Utility function used to read input file into a byte array
     */
    private final byte[] loadInputBuffer() {
        return null;
    }
    
    /**
     * Utility function used to add an item to the viewpager and notify it, in the main thread
     */
    private final boolean addItemToViewPager(androidx.viewpager2.widget.ViewPager2 view, android.graphics.Bitmap item) {
        return false;
    }
    
    /**
     * Utility function used to decode a [Bitmap] from a byte array
     */
    private final android.graphics.Bitmap decodeBitmap(byte[] buffer, int start, int length) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/example/android/camera2/basic/fragments/ImageViewerFragment$Companion;", "", "()V", "DOWNSAMPLE_SIZE", "", "JPEG_DELIMITER_BYTES", "", "[Ljava/lang/Integer;", "TAG", "", "findNextJpegEndMarker", "jpegBuffer", "", "start", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Utility function used to find the markers indicating separation between JPEG data chunks
         */
        private final int findNextJpegEndMarker(byte[] jpegBuffer, int start) {
            return 0;
        }
    }
}