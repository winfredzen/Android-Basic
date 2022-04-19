#include <jni.h>
#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define LOG_TAG "libNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

/* [Vertex source] */
static const char glVertexShader[] =
        "attribute vec4 a_v4Position;\n"
        // a_Color：从外部传递进来的每个顶点的颜色值
        "attribute vec4 a_v4FillColor;\n"
        // v_Color：将每个顶点的颜色值传递给片段着色器
        "varying vec4 v_v4FillColor;\n"
        "void main()\n"
        "{\n"
        "  v_v4FillColor = a_v4FillColor;\n"
        "  gl_Position = a_v4Position;\n"
        "}\n";
/* [Vertex source] */

/* [Fragment source] */
static const char glFragmentShader[] =
        "precision mediump float;\n"
        // v_Color：从顶点着色器传递过来的颜色值
        "varying vec4 v_v4FillColor;\n"
        "void main()\n"
        "{\n"
        "  gl_FragColor = v_v4FillColor;\n"
        "}\n";
/* [Fragment source] */

/* [loadShader] */
GLuint loadShader(GLenum shaderType, const char *shaderSource) {
    GLuint shader = glCreateShader(shaderType);
    if (shader) {
        glShaderSource(shader, 1, &shaderSource, NULL);
        glCompileShader(shader);

        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);

        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);

            if (infoLen) {
                char *buf = (char *) malloc(infoLen);

                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    LOGE("Could not Compile Shader %d:\n%s\n", shaderType, buf);
                    free(buf);
                }

                glDeleteShader(shader);
                shader = 0;
            }
        }
    }

    return shader;
}
/* [loadShader] */

/* [createProgram] */
GLuint createProgram(const char *vertexSource, const char *fragmentSource) {
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, vertexSource);
    if (!vertexShader) {
        return 0;
    }

    GLuint fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentSource);
    if (!fragmentShader) {
        return 0;
    }

    GLuint program = glCreateProgram();

    if (program) {
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;

        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);

        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;

            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);

            if (bufLength) {
                char *buf = (char *) malloc(bufLength);

                if (buf) {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }

    return program;
}
/* [createProgram] */

/* [setupGraphics] */
GLuint simpleTriangleProgram;
GLint iLocPosition = -1;
GLint iLocFillColor = -1;

bool setupGraphics(int w, int h) {
    simpleTriangleProgram = createProgram(glVertexShader, glFragmentShader);

    if (!simpleTriangleProgram) {
        LOGE ("Could not create program");
        return false;
    }

    /* Positions. */
    iLocPosition = glGetAttribLocation(simpleTriangleProgram, "a_v4Position");

    /* Fill colors. */
    iLocFillColor = glGetAttribLocation(simpleTriangleProgram, "a_v4FillColor");

    glViewport(0, 0, w, h);

    return true;
}
/* [setupGraphics] */

/* [renderFrame] */
/* Simple triangle. */
const float triangleVertices[] =
        {
                0.0f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        };

/* Per corner colors for the triangle (Red, Green, Green). */
const float triangleColors[] =
        {
                1.0, 0.0, 0.0, 1.0,
                0.0, 1.0, 0.0, 1.0,
                0.0, 0.0, 1.0, 1.0,
        };

void renderFrame() {
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(simpleTriangleProgram);
    glVertexAttribPointer(iLocPosition, 3, GL_FLOAT, GL_FALSE, 0, triangleVertices);
    glEnableVertexAttribArray(iLocPosition);
    glVertexAttribPointer(iLocFillColor, 4, GL_FLOAT, GL_FALSE, 0, triangleColors);
    glEnableVertexAttribArray(iLocFillColor);

    glDrawArrays(GL_TRIANGLES, 0, 3);
}
/* [renderFrame] */

extern "C"
JNIEXPORT void JNICALL
Java_com_wz_triangle_1jni_NativeLibrary_init(JNIEnv *env, jclass clazz, jint width, jint height) {
    setupGraphics(width, height);
}



extern "C"
JNIEXPORT void JNICALL
Java_com_wz_triangle_1jni_NativeLibrary_step(JNIEnv *env, jclass clazz, jint width, jint height) {
    renderFrame();
}