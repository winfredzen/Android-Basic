# CardView

参考：

+ [创建卡片式布局](https://developer.android.com/guide/topics/ui/layout/cardview?hl=zh-cn)

[`CardView`](https://developer.android.com/guide/topics/ui/layout/"/reference/androidx/cardview/widget/CardView.html"?hl=zh-cn) 微件是 [AndroidX](https://developer.android.com/jetpack/androidx?hl=zh-cn) 的一部分。继承自`FrameLayout`，方便作为其它控件容器，添加3D阴影和圆角效果

添加依赖项：

```groovy
    dependencies {
        implementation "androidx.cardview:cardview:1.0.0"
    }
```

常用属性：

+ `cardBackgroundColor` - 设置背景
+ `cardCornerRadius` - 设置圆角半径
+ `cardElevation` - 为卡片提供自定义高程。高程值越大，绘制的阴影越明显，高程值越小，阴影越淡

> Since padding is used to offset content for shadows, you cannot set padding on CardView. Instead, you can use content padding attributes in XML or `setContentPadding(int, int, int, int)` in code to set the padding between the edges of the CardView and children of CardView.
>
> 使用`contentPadding`设置内部的`padding`

> Due to expensive nature of rounded corner clipping, on platforms before Lollipop, CardView does not clip its children that intersect with rounded corners. Instead, it adds padding to avoid such intersection (See `setPreventCornerOverlap(boolean)` to change this behavior).
>
> Note that, if you specify exact dimensions for the CardView, because of the shadows, its content area will be different between platforms before Lollipop and after Lollipop. By using api version specific resource values, you can avoid these changes. Alternatively, If you want CardView to add inner padding on platforms Lollipop and after as well, you can call `setUseCompatPadding(boolean)` and pass `true`.
>
> `cardUseCompatPadding`默认为false，用于5.0及以上，true则添加额外的padding绘制阴影
>
> `cardPreventCornerOverlap`默认true，用于5.0以下，添加额外的padding，防止内容和圆角重叠



## 例子

如下的例子：

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cardCornerRadius="8dp"
        app:cardElevation="4dp"
        android:layout_marginLeft="16dp"
        android:layout_marginRight="16dp"
        android:layout_marginTop="8dp"
        android:layout_marginBottom="8dp"
        android:foreground="?attr/selectableItemBackground">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/id_img"
                android:layout_width="match_parent"
                android:layout_height="150dp"
                android:scaleType="centerCrop"
                android:background="@drawable/img01"
                />

            <TextView
                android:id="@+id/id_title_tv"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="8dp"
                android:textColor="#000000"
                android:textSize="16dp"
                android:textStyle="bold"
                tools:text="xxxxxxxxxxx"
                />

            <TextView
                android:id="@+id/id_content_tv"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginLeft="8dp"
                android:layout_marginRight="8dp"
                android:layout_marginBottom="8dp"
                tools:text="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                />

        </LinearLayout>

    </androidx.cardview.widget.CardView>

</FrameLayout>
```



使用ListView实现的列表：

```java
public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Message> mDatas;

    public MessageAdapter(Context context, List<Message> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Message getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_message, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mIvImg = convertView.findViewById(R.id.id_img);
            viewHolder.mTvTitle = convertView.findViewById(R.id.id_title_tv);
            viewHolder.mTvContent = convertView.findViewById(R.id.id_content_tv);
            //保存viewholder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message message = mDatas.get(position);
        viewHolder.mIvImg.setImageResource(message.getImgResId());
        viewHolder.mTvTitle.setText(message.getTitle());
        viewHolder.mTvContent.setText(message.getContent());

        return convertView;
    }

    public static class ViewHolder {
        ImageView mIvImg;
        TextView mTvTitle;
        TextView mTvContent;


    }
}

```



效果为：

![007](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/007.png)

























