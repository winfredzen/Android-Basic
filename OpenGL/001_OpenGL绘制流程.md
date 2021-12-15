# OpenGL绘制流程

参考如下的文档：

+ 《Opengl es 3.0 编程指南》
+ [Learn OpenGL ES中文版](https://learnopengl-cn.github.io/)
+ [OpenGL 系列---基础绘制流程](https://glumes.com/post/opengl/opengl-tutorial-draw-point/)



## Android OpenGL ES

Android官方的OpenGL ES文档：

+ [OpenGL ES](https://developer.android.com/guide/topics/graphics/opengl?hl=zh-cn)



Android 应用中使用 OpenGL，需使用：`GLSurfaceView` 和 `GLSurfaceView.Renderer`

`GLSurfaceView.Renderer` 接口方法如下：

+ `onSurfaceCreated()`：系统会在创建 `GLSurfaceView` 时调用一次此方法。使用此方法可执行仅需发生一次的操作，例如设置 OpenGL 环境参数或初始化 OpenGL 图形对象。
+ `onDrawFrame()`：系统会在每次重新绘制 `GLSurfaceView` 时调用此方法。请将此方法作为绘制（和重新绘制）图形对象的主要执行点。
+ `onSurfaceChanged()`：系统会在 `GLSurfaceView` 几何图形发生变化（包括 `GLSurfaceView` 大小发生变化或设备屏幕方向发生变化）时调用此方法。例如，系统会在设备屏幕方向由纵向变为横向时调用此方法。使用此方法可响应 `GLSurfaceView` 容器中的更改。



通过例子来说明，该例子来自《OpenGL ES应用开发实践指南  Android卷》一书



### 绘制纯色背景

1.将GLSurfaceView设置为Activity的contentView，如下

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new FirstOpenGLProjectRender());
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }
```

2.实现一个Render，名为`FirstOpenGLProjectRender`

```java
public class FirstOpenGLProjectRender implements GLSurfaceView.Renderer {


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 0.0f,0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
```

此时运行之后，就可以在屏幕上显示一个红色背景，如下：



















