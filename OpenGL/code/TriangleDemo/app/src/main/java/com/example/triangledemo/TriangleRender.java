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
public class TriangleRender implements GLSurfaceView.Renderer {
    private static final String TAG = "TriangleRender";

    private static final String vertexShaderSource = "#version 300 es\n"
            + "layout (location = 0) in vec3 aPos;\n"
            + "void main()\n"
            + "{\n"
            + "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n"
            + "}";

    private static final String fragmentShaderSource = "#version 300 es\n"
            + "out vec4 FragColor;\n"
            + "void main()\n"
            + "{\n"
            + "FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n"
            + "}";

    float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };

    private FloatBuffer floatBuffer;

    //顶点缓冲对象：Vertex Buffer Object，VBO
    int[] vbo = new int[1];
    //顶点数组对象：Vertex Array Object，VAO
    int[] vao = new int[1];

    public TriangleRender() {
        floatBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(vertices);
        floatBuffer.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //顶点着色器
        int vertexShader = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER);
        GLES30.glShaderSource(vertexShader, vertexShaderSource);
        GLES30.glCompileShader(vertexShader);
        //检测在调用glCompileShader后编译是否成功了
        final int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(vertexShader, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            Log.v(TAG, "检测编译时错误: " + GLES30.glGetShaderInfoLog(vertexShader));
        }

        //片段着色器
        int fragmentShader = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);
        GLES30.glShaderSource(fragmentShader, fragmentShaderSource);
        GLES30.glCompileShader(fragmentShader);
        //检测在调用glCompileShader后编译是否成功了
        GLES30.glGetShaderiv(fragmentShader, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            Log.v(TAG, "检测编译时错误: " + GLES30.glGetShaderInfoLog(fragmentShader));
        }

        //创建程序对象
        int program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glLinkProgram(program);
        //检测链接着色器程序是否失败
        final int[] linkStatus = new int[1];
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Log.v(TAG, "检测链接着色器程序: " + GLES30.glGetProgramInfoLog(program));
        }

        //
        GLES30.glUseProgram(program);

        GLES30.glGenBuffers(1, vbo, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo[0]);
        //把之前定义的顶点数据复制到缓冲的内存中
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, floatBuffer, GLES30.GL_STATIC_DRAW);//把数据存储到GPU中

        //将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0);
        //启用顶点位置属性
        GLES30.glEnableVertexAttribArray(0);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glBindVertexArray(vao[0]);

        //6. 开始绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);

        //7. 解绑VAO
        GLES30.glBindVertexArray(0);

    }


}
