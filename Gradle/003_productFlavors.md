# productFlavors

`productFlavors`用户创建不同的app版本，例如free和paid版本

在官方文档[ProductFlavor](https://developer.android.com/reference/tools/gradle-api/4.2/com/android/build/api/dsl/ProductFlavor)中，介绍说：

> When using Android plugin 3.0.0 and higher, *[each flavor must belong to a `dimension`](https://developer.android.com/reference/tools/gradle-api/4.2/com/android/build/api/dsl/ProductFlavor#dimension:kotlin.String)*.
>
> 在Android plugin 3.0.0及以上版本中，需要制定dimension

[dimension](https://developer.android.com/reference/tools/gradle-api/4.2/com/android/build/api/dsl/ProductFlavor#dimension)中有如下的说明：

> Specifies the flavor dimension that this product flavor belongs to.
>
> When configuring product flavors with Android plugin 3.0.0 and higher, you must specify at least one flavor dimension, using the `flavorDimensions` property, and then assign each flavor to a dimension. Otherwise, you will get the following build error:
>
> 在Android plugin 3.0.0 and higher，必须指定一个flavor dimension，使用 `flavorDimensions` 属性。否则的话，会有如下的错误
>
> ```groovy
> Error:All flavors must now belong to a named flavor dimension.
> The flavor 'flavor_name' is not assigned to a flavor dimension.
> ```
>
> By default, when you specify only one dimension, all flavors you configure automatically belong to that dimension. If you specify more than one dimension, you need to manually assign each flavor to a dimension, as shown in the sample below:
>
> 默认情况下，你只指定一个dimension，所有的flavors都自动的属于这个dimension
>
> ```groovy
> android {
>     ...
>     // Specifies the flavor dimensions you want to use. The order in which you
>     // list each dimension determines its priority, from highest to lowest,
>     // when Gradle merges variant sources and configurations. You must assign
>     // each product flavor you configure to one of the flavor dimensions.
>     flavorDimensions 'api', 'version'
> 
>     productFlavors {
>         demo {
>             // Assigns this product flavor to the 'version' flavor dimension.
>             dimension 'version'
>             ...
>         }
> 
>         full {
>             dimension 'version'
>             ...
>         }
> 
>         minApi24 {
>             // Assigns this flavor to the 'api' dimension.
>             dimension 'api'
>             minSdkVersion '24'
>             versionNameSuffix "-minApi24"
>             ...
>         }
> 
>         minApi21 {
>             dimension "api"
>             minSdkVersion '21'
>             versionNameSuffix "-minApi21"
>             ...
>         }
>     }
> }
> ```
>
> 



参考[Android Build Types and Product Flavors](https://www.journaldev.com/21533/android-build-types-product-flavors)中的内容，如下的设置：

```groovy
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        debug {
            applicationIdSuffix ".debug"
            versionNameSuffix "-debug"
            buildConfigField "String", "TYPE", '"I AM A DEBUG NINJA"'
        }
        beta {
            signingConfig signingConfigs.release
            applicationIdSuffix ".beta"
            versionNameSuffix "-beta"
            buildConfigField "String", "TYPE", '"I AM A BETA NINJA"'
            resValue "string", "my_name", "Anupam Beta"
        }
    }
    
    flavorDimensions "default"
    productFlavors{
        free{
            applicationId "com.example.broadcastdemo.free"
        }

        paid{
            applicationId "com.example.broadcastdemo.paid"
        }
    }
```

此时build varaints可选择的内容如下：

![010](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/010.png)





























