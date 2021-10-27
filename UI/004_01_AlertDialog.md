# AlertDialog

`AlertDialog`的继承关系如下：

![030](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/030.png)

先参考[Android Custom Dialog Example – Making Custom AlertDialog](https://www.simplifiedcoding.net/android-custom-dialog-example/)中例子

1.dialog的布局为`my_dialog.xml`

![031](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/031.png)

2.在Activity中显示个dialog

```java
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
```

效果如下：

![032](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/032.png)



































































