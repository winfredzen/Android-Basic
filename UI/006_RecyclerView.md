# RecyclerView

相关类：

+ LayoutManager
+ Adapter
+ ViewHolder

基本流程：

**数据** ->**适配器**->**布局管理器**



**Adapter**

+ `onCreateViewHolder` - 创建ViewHolder，并把创建的ViewHolder返回回去
+ `onBindViewHolder` - 把数据放入ViewHolder中
+ `getItemCount` - 数据数量



## 线性布局

如下的一个简单的线性布局

```java
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private Context context;

    private List<String> dataSource;

    public MyRecyclerViewAdapter(Context context) {
        this.context = context;
        this.dataSource = new ArrayList<>();
    }

    // 创建并返回viewholder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
    }

    // 通过viewholder来绑定数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(getIcon(position));
        holder.textView.setText(dataSource.get(position));
    }

    // 返回数据数量
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    private int getIcon(int postion) {
        switch (postion % 5) {
            case 0:
                return R.mipmap.a;
            case 1:
                return R.mipmap.b;
            case 2:
                return R.mipmap.c;
            case 3:
                return R.mipmap.d;
            case 4:
                return R.mipmap.e;
        }
        return 0;
    }

    public List<String> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<String> dataSource) {
        this.dataSource = dataSource;
        // 刷新
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv);
            textView = itemView.findViewById(R.id.tv);


        }
    }

}
```

设置LayoutManager和Adapter

```java
//线性布局
LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
mRecyclerView.setLayoutManager(linearLayoutManager);
```

![008](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/008.png)



其它设置

```java
//设置反向
linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//设置反向展示
linearLayoutManager.setReverseLayout(true);
```



## 网格布局

```java
GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
mRecyclerView.setLayoutManager(gridLayoutManager);
```

![009](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/009.png)



## 瀑布流布局

与网格布局相比，可能区别在于，item有不同的高度

在`MyRecyclerViewAdapter`的`onBindViewHolder`的方法中，设置动态高度

```java
    // 通过viewholder来绑定数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(getIcon(position));
        holder.textView.setText(dataSource.get(position));

        if (recyclerView.getLayoutManager().getClass() == StaggeredGridLayoutManager.class) {
            // 为瀑布流布局，使用随机高度
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getRandomHeight());
            holder.textView.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.textView.setLayoutParams(params);
        }
    }


    // 返回不同的高度
    private int getRandomHeight() {
        return (int) (Math.random() * 1000);
    }
```

设置布局管理器

```java
// 瀑布流布局
StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
```

效果如下：

![010](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/010.png)



## 点击事件

首先定义接口：

```java
/**
 * itemview点击事件回到接口
 */
interface OnItemClickListener {
    void onItemClick(int postion);
}
```

然后在adapter的`onBindViewHolder`方法中，给对应的view，添加点击事件，如下：

```java
    // 通过viewholder来绑定数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(getIcon(position));
        holder.textView.setText(dataSource.get(position));

        if (recyclerView.getLayoutManager().getClass() == StaggeredGridLayoutManager.class) {
            // 为瀑布流布局，使用随机高度
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getRandomHeight());
            holder.textView.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.textView.setLayoutParams(params);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接口的回调方法
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }
```



## ItemAnimator

作用是RecylerView中的item增加或者删除的时候，会提供一个过渡的动画

+ notifyItemInserted
+ notifyItemRemoved
+ notifyItemRangeChanged

















































