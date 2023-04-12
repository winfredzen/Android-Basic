# fork进程

参考：

+ [The fork() System Call](https://www.csl.mtu.edu/cs4411.ck/www/NOTES/process/fork/create.html)
+ [Android C++ 系列：Linux 进程 (二)](https://xie.infoq.cn/article/63e7bea358c58d98fee659da2)



**fork()**用来创建进程，无参数，返回进程id，创建新的进程会成为caller的子进程

返回追：

+ 负值，表示创建子进程未成功
+ 0，表示是新创建的子进程
+ 正值， the ***process ID\*** of the child process, to the parent



使用`fork()`，需要导入`<unistd.h>`

**pid_t** 定义在**sys/types.h**中



如下的例子：

```c
#include  <stdio.h>
#include  <string.h>
#include  <sys/types.h>
#include  <unistd.h>


#define   MAX_COUNT  200
#define   BUF_SIZE   100

int main(int argc, const char * argv[]) {
    pid_t  pid;
    int    i;
    char   buf[BUF_SIZE];
    
    fork();
    pid = getpid();
    for (i = 1; i <= MAX_COUNT; i++) {
        sprintf(buf, "This line is from pid %d, value = %d\n", pid, i);
        write(1, buf, strlen(buf));
    }
    return 0;
}
```

部分输出如下：

```c
This line is from pid 7307, value = 199
This line is from pid 7307, value = 200
This line is from pid 7309, value = 1
This line is from pid 7309, value = 2
This line is from pid 7309, value = 3
```

可见有2个进程



**说明：**

假设上面的程序执行到了 **fork()**这儿：

![065](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/065.jpeg)

如果 **fork()**执行成功了，Unix会：

+ 制作两个相同的地址空间副本，一个给parent，另一个给 child
+ 两个进程都将在 `fork()` 调用之后的下一条语句开始执行。 在这种情况下，两个进程都将在赋值语句处开始执行，如下所示：

![066](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/066.jpeg)









































