# fork进程

参考：

+ [The fork() System Call](https://www.csl.mtu.edu/cs4411.ck/www/NOTES/process/fork/create.html)
+ [Android C++ 系列：Linux 进程 (二)](https://xie.infoq.cn/article/63e7bea358c58d98fee659da2)



`fork()`用来创建进程，无参数，返回进程id，创建新的进程会成为caller的子进程

返回追：

+ 负值，表示创建子进程未成功
+ 0，表示是新创建的子进程
+ 正值， the **process ID** of the child process, to the parent



使用`fork()`，需要导入`<unistd.h>`

`pid_t` 定义在`sys/types.h`中



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

假设上面的程序执行到了 `fork()`这儿：

![065](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/065.jpeg)

如果 `fork()`执行成功了，Unix会：

+ 制作两个相同的地址空间副本，一个给parent，另一个给 child
+ 两个进程都将在 `fork()` 调用之后的下一条语句开始执行。 在这种情况下，两个进程都将在赋值语句处开始执行，如下所示：

![066](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/066.jpeg)



> 两个进程在系统调用 `fork()` 之后立即开始执行。 由于两个进程具有相同但独立的地址空间，因此在 `fork()` 调用之前初始化的那些变量在两个地址空间中具有相同的值。 由于每个进程都有自己的地址空间，任何修改都将独立于其他进程。 换句话说，如果父进程改变了它的变量值，修改只会影响父进程地址空间中的变量。 由 `fork()` 调用创建的其他地址空间不会受到影响，即使它们具有相同的变量名。
>
> 使用 `write` 而不是 `printf` 的原因是什么？ 这是因为 `printf()` 是“缓冲的”，这意味着 `printf()` 会将进程的输出组合在一起。 在缓冲父进程的输出的同时，子进程也可能使用 `printf` 打印出一些信息，这些信息也会被缓冲。 因此，由于输出不会立即发送到屏幕，您可能无法获得预期结果的正确顺序。 更糟糕的是，两个进程的输出可能会以奇怪的方式混合在一起。 要克服这个问题，您可以考虑使用“无缓冲”写入。



如下的代码区分父进程和子进程：

```c++
#include  <stdio.h>
#include  <string.h>
#include  <sys/types.h>
#include  <unistd.h>

#define   MAX_COUNT  200

void  ChildProcess(void);                /* child process prototype  */
void  ParentProcess(void);               /* parent process prototype */

int main(int argc, const char * argv[]) {
    pid_t  pid;
    
    pid = fork();
    if (pid == 0)
        ChildProcess();
    else
        ParentProcess();
}

void  ChildProcess(void)
{
    int   i;
    
    for (i = 1; i <= MAX_COUNT; i++)
    printf("   This line is from child, value = %d\n", i);
    printf("   *** Child process is done ***\n");
}

void  ParentProcess(void)
{
    int   i;
    
    for (i = 1; i <= MAX_COUNT; i++)
    printf("This line is from parent, value = %d\n", i);
    printf("*** Parent is done ***\n");
}
```





































