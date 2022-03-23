# DataBinding

使用一段时间DataBinding后的一点总结

## Codelab

官方Codelab的的例子：[Data Binding in Android](https://developer.android.com/codelabs/android-databinding#0)

代码位于[android-databinding-master](https://github.com/winfredzen/Android-Basic/tree/master/%E6%9E%B6%E6%9E%84/code/android-databinding-master)，UI效果如下：

![055](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/055.png)

1.如果不使用DataBinding，实现界面的效果，需要获取到对应的view，设置文本内容，添加事件等，如`PlainOldActivity`中演示的那样

![056](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/056.png)

2.使用使用DataBinding+ViewModel+LiveData的形式来解决，则代码会更简洁

![057](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/057.png)

![058](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/058.png)

在xml中使用

![059](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/059.png)

![060](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/060.png)

使用到了自定义的Binding Adapter

```kotlin
/**
 * A Binding Adapter that is called whenever the value of the attribute `app:popularityIcon`
 * changes. Receives a popularity level that determines the icon and tint color to use.
 */
@BindingAdapter("app:popularityIcon")
fun popularityIcon(view: ImageView, popularity: Popularity) {

    val color = getAssociatedColor(popularity, view.context)

    ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color))

    view.setImageDrawable(getDrawablePopularity(popularity, view.context))
}

/**
 * A Binding Adapter that is called whenever the value of the attribute `android:progressTint`
 * changes. Depending on the value it determines the color of the progress bar.
 */
@BindingAdapter("app:progressTint")
fun tintPopularity(view: ProgressBar, popularity: Popularity) {

    val color = getAssociatedColor(popularity, view.context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        view.progressTintList = ColorStateList.valueOf(color)
    }
}

/**
 *  Sets the value of the progress bar so that 5 likes will fill it up.
 *
 *  Showcases Binding Adapters with multiple attributes. Note that this adapter is called
 *  whenever any of the attribute changes.
 */
@BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
    progressBar.progress = (likes * max / 5).coerceAtMost(max)
}

/**
 * Unused Binding Adapter to replace the Binding Converter that hides a view if the number
 * of likes is zero.
 */
@BindingAdapter("app:hideIfZero")
fun hideIfZero(view: View, number: Int) {
    view.visibility = if (number == 0) View.GONE else View.VISIBLE
}

private fun getAssociatedColor(popularity: Popularity, context: Context): Int {
    return when (popularity) {
        Popularity.NORMAL -> context.theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.colorForeground)
        ).getColor(0, 0x000000)
        Popularity.POPULAR -> ContextCompat.getColor(context, R.color.popular)
        Popularity.STAR -> ContextCompat.getColor(context, R.color.star)
    }
}

private fun getDrawablePopularity(popularity: Popularity, context: Context): Drawable? {
    return when (popularity) {
        Popularity.NORMAL -> {
            ContextCompat.getDrawable(context, R.drawable.ic_person_black_96dp)
        }
        Popularity.POPULAR -> {
            ContextCompat.getDrawable(context, R.drawable.ic_whatshot_black_96dp)
        }
        Popularity.STAR -> {
            ContextCompat.getDrawable(context, R.drawable.ic_whatshot_black_96dp)
        }
    }
}

```





























