# OpenGL实现相机知识点







## SurfaceTexture

[SurfaceTexture](https://developer.android.com/reference/android/graphics/SurfaceTexture)官方的解释：

> Captures frames from an image stream as an OpenGL ES texture.
>
> 从图像流中捕获帧作为 OpenGL ES 纹理。
>
> The image stream may come from either camera preview or video decode. A `Surface` created from a `SurfaceTexture` can be used as an output destination for the `android.hardware.camera2`, `MediaCodec`, `MediaPlayer`, and `Allocation` APIs. When `updateTexImage()` is called, the contents of the texture object specified when the SurfaceTexture was created are updated to contain the most recent image from the image stream. This may cause some frames of the stream to be skipped.
>
> 图像流可能来自相机预览或视频解码。 从 `SurfaceTexture` 创建的 `Surface` 可用作 `android.hardware.camera2`、`MediaCodec`、`MediaPlayer` 和 `Allocation API` 的输出目的地。

而在[SurfaceTexture](https://source.android.google.cn/devices/graphics/arch-st?hl=zh-cn)中也有如下的解释：

> `SurfaceTexture` 是 Surface 和 [OpenGL ES (GLES)](https://www.khronos.org/opengles/) 纹理的组合。`SurfaceTexture` 实例用于提供输出到 GLES 纹理的接口。
>
> `SurfaceTexture` 包含一个以应用为使用方的 `BufferQueue` 实例。当生产方将新的缓冲区排入队列时，`onFrameAvailable()` 回调会通知应用。然后，应用调用 `updateTexImage()`，这会释放先前占用的缓冲区，从队列中获取新缓冲区并执行 [EGL](https://www.khronos.org/egl) 调用，从而使 GLES 可将此缓冲区作为外部纹理使用。
>
> 外部 GLES 纹理 (`GL_TEXTURE_EXTERNAL_OES`) 与传统 GLES 纹理 (`GL_TEXTURE_2D`) 的区别如下：
>
> - 外部纹理直接在从 `BufferQueue` 接收的数据中渲染纹理多边形。
> - 外部纹理渲染程序的配置与传统的 GLES 纹理渲染程序不同。
> - 外部纹理不一定可以执行所有传统的 GLES 纹理活动。
>
> 外部纹理的主要优势是它们能够直接从 `BufferQueue` 数据进行渲染。`SurfaceTexture` 实例在为外部纹理创建 `BufferQueue` 实例时将使用方用法标志设置为 `GRALLOC_USAGE_HW_TEXTURE`，以确保 GLES 可以识别该缓冲区中的数据。

参考：[Android图形系统之SurfaceTexture](https://juejin.cn/post/6844904161645953038)

> SurfaceTexture是离屏渲染和TextureView的核心，内部包含了一个BufferQueue，可以把Surface生成的图像流，转换为纹理，供业务方进一步加工使用。整个架构如下图所示：
>
> ![008](https://github.com/winfredzen/Android-Basic/blob/master/Camera/images/008.png)
>
> 1. 首先，通过Canvas、OpenGL、Camera或者Video Decoder生成图像流。
> 2. 接着，图像流通过Surface入队到BufferQueue，并通知到GLConsumer。
> 3. 然后，GLConsumer从BufferQueue获取图像流GraphicBuffer，并转换为纹理。
> 4. 最后，业务方可以对纹理进一步处理，例如：上特效或者上屏。































