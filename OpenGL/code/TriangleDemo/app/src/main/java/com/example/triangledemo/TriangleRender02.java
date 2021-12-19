package com.example.triangledemo;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * create by wangzhen 2021/12/14
 */
public class TriangleRender02 implements GLSurfaceView.Renderer {
    private static final String TAG = "TriangleRender02";

    //每个FLOAT占据4个字节
    private static final int BYTES_PER_FLOAT = 4;
    //顶点数据FloatBuffer
    private FloatBuffer vertexData;
    //存储链接程序的id
    private int program;
    //颜色常量和位置
    private static final String U_COLOR = "u_Color";
    private int uColorLocation;
    //属性常量和位置
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    //每个position的由x y z组成
    private static final int POSITION_COMPONENT_COUNT = 3;

    private static final String vertexShaderSource = "attribute vec4 a_Position;\n"
            + "void main()\n"
            + "{\n"
            + "   gl_Position = a_Position;\n"
            + "}";

    private static final String fragmentShaderSource = "precision mediump float; \n"
            + "uniform vec4 u_Color;\n"
            + "void main()\n"
            + "{\n"
            + "    gl_FragColor = u_Color;\n"
            + "}";

    float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };

    public TriangleRender02() {
        /**
         * 1.通过allocateDirect分配本地的内存
         * 2.按照本地字节组织（其它如，大端序、小端序，但重要的是一个平台要使用同样的排序）
         */
        vertexData = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
        vertexData.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //顶点着色器
        //1.创建一个新的着色器
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        //2.上传和编译着色器
        GLES20.glShaderSource(vertexShader, vertexShaderSource);
        GLES20.glCompileShader(vertexShader);
        //3.取出编译状态，检测在调用glCompileShader后编译是否成功了
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            Log.v(TAG, "检测编译时错误: " + GLES20.glGetShaderInfoLog(vertexShader));
        }

        //片段着色器
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderSource);
        GLES20.glCompileShader(fragmentShader);
        //检测在调用glCompileShader后编译是否成功了
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            Log.v(TAG, "检测编译时错误: " + GLES20.glGetShaderInfoLog(fragmentShader));
        }

        //创建程序对象
        program = GLES20.glCreateProgram();
        //附加上着色器
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        //连接程序
        GLES20.glLinkProgram(program);

        GLES20.glUseProgram(program);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Log.v(TAG, "检测链接着色器程序: " + GLES20.glGetProgramInfoLog(program));
        }

        //告诉OpenGL在绘制任何东西到屏幕上的时候使用这里定义的程序
        GLES20.glUseProgram(program);

        //获取uniform的位置，并把位置存入uColorLocation
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        //获取属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        //关联属性与顶点数据
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, vertexData);
        //使能顶点数组，调用后，OpenGL就知道去哪里找它所需要的数据了
        GLES20.glEnableVertexAttribArray(aPositionLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        //更新着色器代码中u_Color的值
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

    }
}
