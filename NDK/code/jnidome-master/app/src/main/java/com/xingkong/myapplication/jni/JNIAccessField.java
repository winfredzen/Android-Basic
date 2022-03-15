package com.xingkong.myapplication.jni;

import com.glumes.cppso.utils.LogUtil;
import com.xingkong.myapplication.model.Animal;

public class JNIAccessField extends BaseOperation {
    final Animal animal= new Animal("animal");
    static int num= 1;
    @Override
    public void invoke() {
//        print(
//                accessInstanceFiled(animal),
//                accessStaticField(animal),
//                callInstanceMethod(animal),
//                callStaticMethod(animal)
//        );
        // 访问实例的字段
//        accessInstanceFiled(animal);
//        LogUtil.Companion.d("after access instance field,the name is " + animal.getName());
//        // 访问静态类的字段
//        accessStaticField(animal);
//        LogUtil.Companion.d("after access static field,the num is " + Animal.num);
//        staticAccessInstanceField();
//        LogUtil.Companion.d("--staticAccessInstanceField--,the num is " + JNIAccessField.num);
//        // 访问实例的方法
//        callInstanceMethod(animal);
//        // 访问静态类的方法
        callStaticMethod(animal);
    }

    private native int accessInstanceFiled(Animal animal);

    private native int accessStaticField(Animal animal);

    private native void callInstanceMethod(Animal animal);

    private native void callStaticMethod(Animal animal);

    public static native void staticAccessInstanceField();

}
