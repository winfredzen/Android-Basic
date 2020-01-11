# RecyclerView

参考：

+ [使用 RecyclerView 创建列表](https://developer.android.com/guide/topics/ui/layout/recyclerview)

## 如何使用

1.需要在xml布局中添加`RecyclerView`

```xml
  <android.support.v7.widget.RecyclerView
      android:id="@+id/creatureRecyclerView"
      android:layout_width="0dp"
      android:layout_height="0dp"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintEnd_toEndOf="parent"
      app:layout_constraintStart_toStartOf="parent"
      app:layout_constraintTop_toTopOf="parent"
      />
```

2.需要单个list item布局`list_item_creature.xml`

3.视图持有者`RecyclerView.ViewHolder`

4.视图持有者对象由适配器管理，通过扩展`RecyclerView.Adapter`来实现Adapter。Adapter根据需要创建ViewHolder，将ViewHolder绑定到相应的数据，通过`onBindViewHolder()`方法

5.显示的数据项发生变化时，调用 `RecyclerView.Adapter.notify…()` 方法通知适配器

6.布局管理器

### 如何处理点击事件

RecyclerView的点击事件要自己去注册

如下的例子，展示一个完整的Adapter，并注册了item view的点击事件

`CreatureAdpater.kt`

```kotlin
class CreatureAdpater(private val creatures: MutableList<Creature>) : RecyclerView.Adapter<CreatureAdpater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //创建ViewHolder，这是使用的是ViewGroup上的一个扩展方法
        /*
        fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
            return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
        }
         */
        return ViewHolder(parent.inflate(R.layout.list_item_creature))

    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //在这里ViewHolder绑定数据
        holder.bind(creatures[position])
    }

    fun updateCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var creature: Creature

        init {
            //注册点击事件
            itemView.setOnClickListener(this)
        }

        //绑定数据
        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            //itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            val resId = context.resources.getIdentifier(creature.uri, null, context.packageName)
            var drawable = context.getDrawable(resId)
            itemView.creatureImage.setImageDrawable(drawable)
            itemView.fullName.text = creature.fullName

            itemView.nickName.text = creature.nickname
        }

        //实现点击事件方法
        override fun onClick(view: View) {
            val context = view.context
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }

    }

}
```

设置`adapter`和`layoutManager`

```kotlin
    creatureRecyclerView.layoutManager = LinearLayoutManager(activity)
    creatureRecyclerView.adapter = adpater
```



## Nested RecyclerViews

RecyclerView的嵌套，如下所示，list_item中又嵌入了一个水平滚动的RecyclerView，滑动起来有点卡顿

![012](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/012.png)

可以使用[RecycledViewPool](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.RecycledViewPool)来提高滚动的流程性

> RecycledViewPool lets you share Views between multiple RecyclerViews.
>
> RecycledViewPool可在多个RecyclerViews间共享view
>
> If you want to recycle views across RecyclerViews, create an instance of RecycledViewPool and use `setRecycledViewPool(RecycledViewPool)`.
>
> RecyclerView automatically creates a pool for itself if you don't provide one.

还可使用[SnapHelper](https://developer.android.com/reference/android/support/v7/widget/SnapHelper.html)，SnapHelper是个抽象类，可使用其子类[LinearSnapHelper](https://developer.android.com/reference/android/support/v7/widget/LinearSnapHelper)

参考：

+ [让你明明白白的使用RecyclerView——SnapHelper详解](https://www.jianshu.com/p/e54db232df62)
+ [SnapHelper学习记录](https://juejin.im/entry/59bdc3575188256bce40dec6)

>RecyclerView在24.2.0版本中新增了SnapHelper这个辅助类，用于辅助RecyclerView在滚动结束时将Item对齐到某个位置。特别是列表横向滑动时，很多时候不会让列表滑到任意位置，而是会有一定的规则限制，这时候就可以通过SnapHelper来定义对齐规则了。
>
>SnapHelper是一个抽象类，官方提供了一个LinearSnapHelper的子类，可以让RecyclerView滚动停止时相应的Item停留中间位置。25.1.0版本中官方又提供了一个PagerSnapHelper的子类，可以使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示。
>
>这两个子类使用方式也很简单，只需要创建对象之后调用`attachToRecyclerView()`附着到对应的RecyclerView对象上就可以了。
>
>
>
>**Fling操作**
>
>首先来了解一个概念，手指在屏幕上滑动RecyclerView然后松手，RecyclerView中的内容会顺着惯性继续往手指滑动的方向继续滚动直到停止，这个过程叫做**Fling**。Fling操作从手指离开屏幕瞬间被触发，在滚动停止时结束。

>LinearSnapHelper&PagerSnapHelper是抽象类SnapHelper的具体实现。
>区别在于：
>LinerSnapHelper，可滑动多页，居中显示；
>PagerSnapHelper，每次只能滑动一页，居中显示；

![013](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/013.png)



## 布局管理器

Android 支持库包含三个标准布局管理器

+ [LinearLayoutManager](https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager?hl=en)
+ [GridLayoutManager](https://developer.android.com/reference/android/support/v7/widget/GridLayoutManager) - 网格
+ [StaggeredGridLayoutManager](https://developer.android.com/reference/android/support/v7/widget/StaggeredGridLayoutManager?hl=en) - 类似于Pinterest



### LinearLayoutManager

可实现竖直和水平布局

如，实现水平布局：

```kotlin
foodRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
```

![011](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/011.png)



###GridLayoutManager

[GridLayoutManager](https://developer.android.com/reference/android/support/v7/widget/GridLayoutManager)实现网格布局

简单的例子，指定一行有多少个span，下面的例子，2列

```kotlin
creatureRecyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
```

![014](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/014.png)

> By default, each item occupies 1 span. You can change it by providing a custom `GridLayoutManager.SpanSizeLookup` instance via `setSpanSizeLookup(SpanSizeLookup)`.
>
> 默认，每个item占据一个span。可以通过`GridLayoutManager.SpanSizeLookup` 实例的 `setSpanSizeLookup(SpanSizeLookup)`提供一个自定义的span

如下面的例子

```kotlin
    val layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return if((position + 1) % 3 == 0) 2 else 1
      }

    }
    creatureRecyclerView.layoutManager = layoutManager
    creatureRecyclerView.adapter = adpater
```

![015](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/015.png)



### StaggeredGridLayoutManager

[StaggeredGridLayoutManager](https://developer.android.com/reference/android/support/v7/widget/StaggeredGridLayoutManager)可实现瀑布流式的布局

```kotlin
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        creatureRecyclerView.layoutManager = layoutManager
        creatureRecyclerView.adapter = adpater
```

![016](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/016.png)

























