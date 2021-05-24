# Keyboard

在官网[处理键盘输入](https://developer.android.com/training/keyboard-input)中对键盘有相关的介绍：

> 当界面中的文本字段获得焦点时，Android 系统会显示屏幕键盘（称为“软键盘输入法”）。为了提供最佳用户体验，您可以指定有关所需输入类型（例如，是电话号码还是电子邮件地址）以及输入法行为方式（例如，是否自动更正拼写错误）的特征。



**指定键盘类型**

> 通过[`android:inputType`](https://developer.android.com/reference/android/widget/TextView#attr_android:inputType) 属性来声明文本字段的输入法，如`phone`、`textPassword`
>
> `textAutoCorrect` - 自动拼写更正
>
> 也可组合使用，如(能够将句子的首字母大写，又能够自动更正拼写错误的文本字段)
>
> ```xml
> android:inputType=
>             "textCapSentences|textAutoCorrect"
> ```



**指定输入法操作**

> 通过[`android:imeOptions`](https://developer.android.com/reference/android/widget/TextView#attr_android:imeOptions) 指定键盘操作按钮
>
> 如
>
> ```xml
> android:imeOptions="actionSend"
> ```
>
> 通过 `TextView.OnEditorActionListener` 来监听操作按钮按下操作
>
> ```java
>     EditText editText = (EditText) findViewById(R.id.search);
>     editText.setOnEditorActionListener(new OnEditorActionListener() {
>         @Override
>         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
>             boolean handled = false;
>             if (actionId == EditorInfo.IME_ACTION_SEND) {
>                 sendMessage();
>                 handled = true;
>             }
>             return handled;
>         }
>     });
> ```



**自动填充**

> 参考[提供自动填充建议](https://developer.android.com/training/keyboard-input/style#AutoComplete), 使用 `AutoCompleteTextView`



**在 Activity 启动时显示输入法**

> 参考[在 Activity 启动时显示输入法](https://developer.android.com/training/keyboard-input/visibility)
>
> 如需在 Activity 启动时显示输入法，请将 [`android:windowSoftInputMode`](https://developer.android.com/guide/topics/manifest/activity-element#wsoft) 属性添加到值为 `"stateVisible"` 的 `<activity>` 元素中。例如：
>
> ```java
>     <application ... >
>         <activity
>             android:windowSoftInputMode="stateVisible" ... >
>             ...
>         </activity>
>         ...
>     </application>
> ```
>
> > 但我在自己的oppo设备上测试了下，发现这样做并没有在Activity启动时显示输入法
> >
> > 原因可能是没有使用系统的默认键盘，参考[android:windowSoftInputMode=“stateVisible” doesn't work](https://stackoverflow.com/questions/9760048/androidwindowsoftinputmode-statevisible-doesnt-work)



**显示键盘**

> ```java
>     public void showSoftKeyboard(View view) {
>         if (view.requestFocus()) {
>             InputMethodManager imm = (InputMethodManager)
>                     getSystemService(Context.INPUT_METHOD_SERVICE);
>             imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
>         }
>     }
> ```



**隐藏键盘**

> ```java
> public void hideSoftKeyboard(View view){
>   InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
>   imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
> }
> ```



**`Activity`的`android:windowSoftInputMode`属性**

> 当输入法出现在屏幕上时，它会减少应用界面的可用空间。
>
> 可参考：
>
> + [Android中Activity的android:windowSoftInputMode属性](https://blog.csdn.net/qiutiandepaomo/article/details/84028558)



**处理键盘操作**

> 参考[处理键盘操作](https://developer.android.com/training/keyboard-input/commands)
>
> > 当用户将焦点放在可修改的文本视图（例如 `EditText` 元素）上，并且已连接硬件键盘时，所有输入均由系统处理。但是，如果您想要拦截或直接自行处理键盘输入，可以通过从 `KeyEvent.Callback` 接口实现回调方法（例如 `onKeyDown()` 和 `onKeyMultiple()`）完成此操作。
> >
> > `Activity` 和 `View` 类都会实现 `KeyEvent.Callback` 接口
>
> 如需处理单个按键操作，请根据具体情况实现 `onKeyDown()` 或 `onKeyUp()`。通常，如果您想要确保只接收一个事件，应该使用 `onKeyUp()`。如果用户按住该按钮，系统会多次调用 `onKeyDown()`



**监听键盘出现和隐藏**

参考：

+ [Android 监听EditText输入框软键盘显示及隐藏](https://blog.csdn.net/csdn_aiyang/article/details/107025875)
+ [How to check visibility of software keyboard in Android?](https://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android?noredirect=1&lq=1)
+ [Soft keyboard open and close listener in an activity in Android](https://stackoverflow.com/questions/25216749/soft-keyboard-open-and-close-listener-in-an-activity-in-android)





## 其它

参考：

+ [Android软键盘](https://blog.csdn.net/ccpat/category_5620281.html)
+ [Android手动显示和隐藏软键盘方法总结](https://blog.csdn.net/ccpat/article/details/46717573)









