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





























