# ViewPager

参考：

+ [Creating swipe views with tabs](https://developer.android.com/training/implementing-navigation/lateral.html)

[ViewPager](<https://developer.android.com/reference/android/support/v4/view/ViewPager>)多配合[Fragment](https://developer.android.com/reference/android/app/Fragment.html)使用，ViewPager需要[PagerAdapter](https://developer.android.com/reference/android/support/v4/view/PagerAdapter.html)的支持，需要实现[PagerAdapter](https://developer.android.com/reference/android/support/v4/view/PagerAdapter.html)来生成展示view的页面

也可以使用PagerAdapter子类：

+ [FragmentPagerAdapter](https://developer.android.com/reference/android/support/v4/app/FragmentPagerAdapter.html) - This is best when navigating between sibling screens representing a fixed, small number of pages. 适合于固定数量，数量较小的页面
+ [FragmentStatePagerAdapter](https://developer.android.com/reference/android/support/v4/app/FragmentStatePagerAdapter.html) - This is best for paging across a collection of objects for which the number of pages is undetermined. It destroys fragments as the user navigates to other pages, minimizing memory usage. 适合于未确定页数的页面， 它会在用户导航到其他页面时销毁片段，从而最大限度地减少内存使用量。

> **FragmentStatePagerAdapter 与 FragmentPagerAdapter的区别**
>
> FragmentPagerAdapter是另外一种可用的PagerAdapter，其用法与FragmentStatePagerAdapter基本一致。唯一的区别在于，卸载不再需要的fragment时，各自采用的处理方法有所不同。 
>
> FragmentStatePagerAdapter会销毁不需要的fragment。事务提交后，activity的FragmentManager中的fragment会被彻底移除。FragmentStatePagerAdapter类名中的“state”表明:在 销毁fragment时，可在`onSaveInstanceState(Bundle)`方法中保存fragment的Bundle信息。用户切换回来时，保存的实例状态可用来生成新的fragment。 
>
> 相比之下，FragmentPagerAdapter有不同的做法。对于不再需要的fragment，FragmentPagerAdapter会选择调用事务的`detach(Fragment)`方法来处理它，而非`remove(Fragment)`方 法。也就是说，FragmentPagerAdapter只是销毁了而fragment的视图，而fragment实例还保留在 FragmentManager中。因此，FragmentPagerAdapter创建的fragment永远不会被销毁。 
>
> 选择哪种adapter取决于应用的要求。通常来说，使用FragmentStatePagerAdapter更节省内存。CriminalIntent应用需显示大量crime记录，每份记录最终还会包含图片。在内存中保存所有 
>
> 信息显然不合适，因此我们选择使用FragmentStatePagerAdapter 
>
> 另一方面，如果用户界面只需要少量固定的fragment，则FragmentPagerAdapter是安全、
> 合适的选择。最常见的例子为使用tab选项页显示用户界面。例如，某些应用的明细视图所含内
> 容较多，通常需分两页显示。这时就可以将这些明细信息分拆开来，以多页面的形式展现。显然，
> 为用户界面添加支持滑动切换的ViewPager，能增强应用的触摸体验。此外，将fragment保存在
> 内存中，更易于管理控制器层的代码。对于这种类型的用户界面，每个activity通常只有两三个
> fragment，基本不用担心有内存不足的风险。



## 自定义PagerAdapter

一般要重写如下的四个方法：

+ `getCount()` - 获取ViewPager实际绘制的列表项的数量
+ `instantiateItem(ViewGroup, int)` - 告诉pager adapter创建指定位置的列表项视图，然后将其添加给ViewGroup视图容器
+ `destroyItem(ViewGroup, int, Object)` - 告诉pager adapter销毁已建视图
+ `isViewFromObject(View, Object)` - 判断view和obj是否为同一个view

可参考：

+ [ViewPager Without Fragments](https://www.bignerdranch.com/blog/viewpager-without-fragments/)
+ [Android ViewPager Example Tutorial](https://www.journaldev.com/10096/android-viewpager-example-tutorial)



## FragmentStatePagerAdapter

简单的使用例子：

```java
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {

                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getId());

            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
```

设置显示的页面：`mViewPager.setCurrentItem(i);`

## 其它Demo例子

+ [ViewPager with FragmentPagerAdapter](https://guides.codepath.com/android/viewpager-with-fragmentpageradapter)

+ [Android 👆 Swipe Views using ViewPager • haerulmuttaqin](https://www.youtube.com/watch?v=UsXv6VRqZKs)
  ![效果001](https://github.com/winfredzen/Android-Basic/blob/master/images/001.png)

  
