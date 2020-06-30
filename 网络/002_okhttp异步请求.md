# okhttp异步请求

参考：

+ [Calls](https://square.github.io/okhttp/calls/#calls_1)
+ [OkHttp Android Example Tutorial](https://www.journaldev.com/13629/okhttp-android-example-tutorial)

> **Asynchronous** Calling is the recommneded way since it supports native cancelling, tagging multiple requests and canceling them all with a single method call (by invoking the cancel on the Acitivty instance inside the **onPause** or **onDestroy** method).
>
> 异步请求是推荐的方式，它支持cancelling，tagging（在**onPause**或者**onDestroy**调用cancel）



如下的异步请求：

```java
    //异步请求的方式
    private void asycnRequest() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(myResponse);
                    }
                });

            }
        });

    }

```

