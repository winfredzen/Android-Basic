package com.example.opengl01;

import static android.opengl.GLES10.GL_LINES;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.example.opengl01.utils.LoggerConfig;
import com.example.opengl01.utils.ShaderHelper;
import com.example.opengl01.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * create by wangzhen 2021/12/13
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    //每个顶点由2个分量组成
    private static final int POSITION_COMPONENT_COUNT = 2;
    //每个Float 4个字节
    private static final int BYTE_PER_FLOAT = 4;
    //FloatBuffer在本地内存中存储数据
    private final FloatBuffer vertexData;
    private final Context context;
    private int program;
    //颜色
    private static final String U_COLOR = "u_Color";
    //颜色在OpenGL程序对象中的位置
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    public AirHockeyRenderer(Context context) {
        this.context = context;

//        float[] tableVertices = {
//                0f, 0f,
//                0f, 14f,
//                9f, 14f,
//                9f, 0f
//        };

//        float[] tableVerticesWithTriangles = {
//                //三角形1
//                0f, 0f,
//                9f, 14f,
//                0f, 14f,
//                //三角形2
//                0f, 0f,
//                9f, 0f,
//                9f, 14f,
//                //直线
//                0f, 7f,
//                9f, 7f,
//                //2个木槌点
//                4.5f, 2f,
//                4.5f, 12f
//        };

        float[] tableVerticesWithTriangles = {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f,  0.5f,
                -0.5f,  0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f,  0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f,  0.25f
        };

        //分配本地内存
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTE_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);



    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //读取着色器代码
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertext_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);

        //编译着色器
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        //链接程序
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            //验证程序
            ShaderHelper.validateProgram(program);
        }

        //使用程序
        glUseProgram(program);

        //获取uniform的位置，来设置颜色
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        //获取属性的位置
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        //从开头读取数据
        vertexData.position(0);
        //在vertexData中找到a_Position对应的数据
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, vertexData);
        //使能顶点数组
        glEnableVertexAttribArray(aPositionLocation);

    }
x
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Draw the table.
        //更新着色器代码中u_Color的值
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        /**
         * 2个三角形，6个顶点
         * first - 从哪里开始读取
         * count - 读入多少个顶点
         */
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // Draw the center dividing line.
        /**
         * 绘制分隔线
         */
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        // Draw the first mallet blue.
        /**
         * 第一个木槌
         */
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        // Draw the second mallet red.
        /**
         * 第二个木槌
         */
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
