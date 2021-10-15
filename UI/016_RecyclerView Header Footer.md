# RecyclerView Header Footer

这里的Header和Footer，类似于iOS中的TableView的tableHeader和tableFooter

在Android中显示header和footer，其实就是和item一样，只要区分不同的viewType就行了

如[Android RecyclerView With Header And Footer Example | Sticky Fixed Multi Column](https://demonuts.com/android-recyclerview-header/)中例子：

`HFAdapter`

```java
public class HFAdapter extends RecyclerView.Adapter<HFAdapter.MyViewHolder> {
    public static final int Header = 1;
    public static final int Normal = 2;
    public static final int Footer = 3;

    private LayoutInflater inflater;
    private ArrayList<HeaderModel> headerModelArrayList;;

    public HFAdapter(Context ctx, ArrayList<HeaderModel> headerModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.headerModelArrayList = headerModelArrayList;
    }

    @Override
    public int getItemViewType(int position) {

        if(headerModelArrayList.get(position).getViewType().equals("header")){
            return Header;
        }
        else if(headerModelArrayList.get(position).getViewType().equals("footer")){
            return Footer;
        }
        else {
            return Normal;
        }

    }

    @Override
    public HFAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        MyViewHolder holder;

        switch (viewType)
        {
            case Normal:
                view = inflater.inflate(R.layout.single_item, parent, false);
                holder = new MyViewHolder(view , Normal);
                break;

            case Header:
                view = inflater.inflate(R.layout.rv_header, parent, false);
                holder = new MyViewHolder(view , Header);
                break;
            case Footer:

                view = inflater.inflate(R.layout.rv_footer, parent, false);
                holder = new MyViewHolder(view , Footer);
                break;

            default:

                view = inflater.inflate(R.layout.single_item, parent, false);
                holder = new MyViewHolder(view , Normal);
                break;
        }




        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(headerModelArrayList.get(position).getViewType().equals("header")){
            //holder.tvProduct.setText(" Item No : " + headerModelArrayList.get(position).getText());
        }
        else if(headerModelArrayList.get(position).getViewType().equals("footer")){
            // holder.tvProduct.setText(" Item No : " + headerModelArrayList.get(position).getText());
        }
        else {
            holder.tvProduct.setText(" Item No : " + headerModelArrayList.get(position).getText());
        }


    }

    @Override
    public int getItemCount() {
        return headerModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvProduct;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);

            if(viewType == Normal){
                tvProduct = (TextView) itemView.findViewById(R.id.tv);
            }
        }

    }
}
```

![022](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/022.png)



在[How to add Header to Recyclerview in Android](https://www.skcript.com/svr/how-to-add-header-to-recyclerview-in-android/)，也有类似的例子，原理都是一样的

```java
public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private static final int TYPE_HEADER = 0;
   private static final int TYPE_ITEM = 1;

   private List<ViewItemsModel> viewitemlists;
   private Context context;

   public CustomAdapter(Context context, List<ViewItemsModel> viewitemlists) {
       this.context = context;
       this.viewitemlists = viewitemlists;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if (viewType == TYPE_ITEM) {
           // Here Inflating your recyclerview item layout
           View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listviewitem, parent, false);
           return new ItemViewHolder(itemView);
       } else if (viewType == TYPE_HEADER) {
           // Here Inflating your header view
           View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_headeritem, parent, false);
           return new HeaderViewHolder(itemView);
       }
       else return null;
   }

   @Override
   public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

       if (holder instanceof HeaderViewHolder){
           setheadersdata_flag = true;
           HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

           // You have to set your header items values with the help of model class and you can modify as per your needs
           
            headerViewHolder.txt_needsreview.setText(“YOUR _HEADERVIEW_STRING”);

        }
       else if (holder instanceof ItemViewHolder){

           final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

           // Following code give a row of header and decrease the one position from listview items
           final LatestTabModel.ViewItemsModel data = latestlists.get(position-1);

           // You have to set your listview items values with the help of model class and you can modify as per your needs
           
           itemViewHolder.title.setText(data.getTitle());
          
       }
   }

   @Override
   public int getItemViewType(int position) {
       if (position == 0) {
           return TYPE_HEADER;
       }
       return TYPE_ITEM;
   }


// getItemCount increasing the position to 1. This will be the row of header
   @Override
   public int getItemCount() {
       return latestlists.size()+1;
   }

  
 private class HeaderViewHolder extends RecyclerView.ViewHolder {
       TextView txt_needsreview,txt_planned,txt_inprogress,txt_completed;

       public HeaderViewHolder(View view) {
           super(view);
           txt_needsreview = (TextView) view.findViewById(R.id.review_needs_count_text);
           txt_planned = (TextView) view.findViewById(R.id.planned_requests_count_text);
           txt_inprogress = (TextView) view.findViewById(R.id.inprogress_count_text);
           txt_completed = (TextView) view.findViewById(R.id.completed_count_text);
       }
   }

   public class ItemViewHolder extends RecyclerView.ViewHolder {

       TextView title,description,upward_count,comment_count,status;
       LinearLayout latest_feed_layout;
       LikeButton upvote_image;
      public ItemViewHolder(View itemView) {
           super(itemView);

           title = (TextView) itemView.findViewById(R.id.latest_tab_title_text);
           description = (TextView) itemView.findViewById(R.id.latest_tab_descrip_text);
           upward_count = (TextView) itemView.findViewById(R.id.count_upward_text);
           comment_count = (TextView) itemView.findViewById(R.id.comment_count_text);
           status = (TextView) itemView.findViewById(R.id.latest_tab_status_text);
           latest_feed_layout = (LinearLayout)                     itemView.findViewById(R.id.latest_feed_layout);
           upvote_image = (LikeButton) itemView.findViewById(R.id.upward_image);

       }
   }
  }
```





























