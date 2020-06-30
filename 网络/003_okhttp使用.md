# okhttp使用

参考：

+ [OkHttp Android Example Tutorial](https://www.journaldev.com/13629/okhttp-android-example-tutorial)
+ [Recipes](https://square.github.io/okhttp/recipes/)



## Query Parameters

使用`HttpUrl.Builder`类来添加query参数：

```java
    //query param
    private void queryParamRequest() {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://httpbin.org/get").newBuilder();
        urlBuilder.addQueryParameter("website", "www.journaldev.com");
        urlBuilder.addQueryParameter("tutorials", "android");
        String url = urlBuilder.build().toString();

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

![002](https://github.com/winfredzen/Android-Basic/blob/master/网络/images/002.png)