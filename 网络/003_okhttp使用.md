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



## Headers

> Typically HTTP headers work like a `Map<String, String>`: each field has one value or none. But some headers permit multiple values, like Guava’s [Multimap](http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Multimap.html). For example, it’s legal and common for an HTTP response to supply multiple `Vary` headers. OkHttp’s APIs attempt to make both cases comfortable.
>
> 通常情况下headers类似于 `Map<String, String>`，每一个field有一个value或没有。但有些headers允许有多个value。OkHttp兼容2种
>
> When writing request headers, use `header(name, value)` to set the only occurrence of `name` to `value`. If there are existing values, they will be removed before the new value is added. Use `addHeader(name, value)` to add a header without removing the headers already present.
>
> 写入headers，使用 `header(name, value)`设置唯一值。如果已存在值，它将会在设置新值之前被移除。使用 `addHeader(name, value)`来添加一个header，而不用移除已存在的header
>
> When reading response a header, use `header(name)` to return the *last* occurrence of the named value. Usually this is also the only occurrence! If no value is present, `header(name)` will return null. To read all of a field’s values as a list, use `headers(name)`.
>
> 使用 `header(name)` 返回最后出现的value。如果没有value，返回null。要读取一个field的所有的值，使用`headers(name)`
>
> To visit all headers, use the `Headers` class which supports access by index.
>
> 使用`Headers` 类来获取所有的headers，它支持通过索引来获取

```java
  private final OkHttpClient client = new OkHttpClient();

  public void run() throws Exception {
    Request request = new Request.Builder()
        .url("https://api.github.com/repos/square/okhttp/issues")
        .header("User-Agent", "OkHttp Headers.java")
        .addHeader("Accept", "application/json; q=0.5")
        .addHeader("Accept", "application/vnd.github.v3+json")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      System.out.println("Server: " + response.header("Server"));
      System.out.println("Date: " + response.header("Date"));
      System.out.println("Vary: " + response.headers("Vary"));
    }
  }
```







































