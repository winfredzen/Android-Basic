# LruCache

[LruCache](https://developer.android.com/reference/android/util/LruCache)官网介绍：

> A cache that holds strong references to a limited number of values. Each time a value is accessed, it is moved to the head of a queue. When a value is added to a full cache, the value at the end of that queue is evicted and may become eligible for garbage collection.
>
> If your cached values hold resources that need to be explicitly released, override `entryRemoved(boolean, K, V, V)`.
>
> If a cache miss should be computed on demand for the corresponding keys, override `create(K)`. This simplifies the calling code, allowing it to assume a value will always be returned, even when there's a cache miss.
>
> By default, the cache size is measured in the number of entries. Override `sizeOf(K, V)` to size the cache in different units. For example, this cache is limited to 4MiB of bitmaps:
>
> ```java
>    int cacheSize = 4 * 1024 * 1024; // 4MiB
>    LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
>        protected int sizeOf(String key, Bitmap value) {
>            return value.getByteCount();
>        }
>    }
> ```
>
> This class is thread-safe. Perform multiple cache operations atomically by synchronizing on the cache:
>
> ```java
>    synchronized (cache) {
>      if (cache.get(key) == null) {
>          cache.put(key, value);
>      }
>    }
> ```
>
> This class does not allow null to be used as a key or value. A return value of null from `get(K)`, `put(K, V)` or `remove(K)` is unambiguous: the key was not in the cache.
>
> This class appeared in Android 3.1 (Honeycomb MR1); it's available as part of [Android's Support Package](http://developer.android.com/sdk/compatibility-library.html) for earlier releases.

参考：

+ [彻底解析Android缓存机制——LruCache](https://www.jianshu.com/p/b49a111147ee)
+ [LruCache 使用及原理](https://www.jianshu.com/p/e09870b60046)

> 一般来说，缓存策略主要包含缓存的添加、获取和删除这三类操作。如何添加和获取缓存这个比较好理解，那么为什么还要删除缓存呢？这是因为不管是内存缓存还是硬盘缓存，它们的缓存大小都是有限的。当缓存满了之后，再想其添加缓存，这个时候就需要删除一些旧的缓存并添加新的缓存。
>
> 因此LRU(Least Recently Used)缓存算法便应运而生，LRU是近期最少使用的算法，它的核心思想是当缓存满时，会优先淘汰那些近期最少使用的缓存对象。采用LRU算法的缓存有两种：LrhCache和DisLruCache，分别用于实现内存缓存和硬盘缓存，其核心思想都是LRU缓存算法。















