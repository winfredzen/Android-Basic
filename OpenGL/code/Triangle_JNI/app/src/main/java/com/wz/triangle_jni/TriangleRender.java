package com.wz.triangle_jni;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * create by wangzhen 2022/4/18
 */
public class TriangleRender implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        NativeLibrary.init(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        NativeLibrary.step();
    }
}
