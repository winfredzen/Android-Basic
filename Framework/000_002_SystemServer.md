# SystemServer

参考：

+ [【Android开发】zygote启动systemServer进程的过程](https://www.bilibili.com/video/BV1134y157zo?spm_id_from=333.337.search-card.all.click&vd_source=308fc9b57cdc925a463da02262234ff6)
+ [第20章 systemserver服务分析](https://www.bilibili.com/video/BV1xL411x7PU?spm_id_from=333.337.search-card.all.click&vd_source=308fc9b57cdc925a463da02262234ff6)



SystemServer是Zygote孵化出来的第一个Android服务程序，而这个服务程序，在启动过程中，会启动系统所有的核心服务



![021](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/021.png)

![022](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/022.png)



**SystemServer进程时如何创建的？**

1.`ZygoteInit.java`中，创建`SystemServer`

![023](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/023.png)

```java
    /**
     * Prepare the arguments and forks for the system server process.
     *
     * @return A {@code Runnable} that provides an entrypoint into system_server code in the child
     * process; {@code null} in the parent.
     */
    private static Runnable forkSystemServer(String abiList, String socketName,
            ZygoteServer zygoteServer) {
        long capabilities = posixCapabilitiesAsBits(
                OsConstants.CAP_IPC_LOCK,
                OsConstants.CAP_KILL,
                OsConstants.CAP_NET_ADMIN,
                OsConstants.CAP_NET_BIND_SERVICE,
                OsConstants.CAP_NET_BROADCAST,
                OsConstants.CAP_NET_RAW,
                OsConstants.CAP_SYS_MODULE,
                OsConstants.CAP_SYS_NICE,
                OsConstants.CAP_SYS_PTRACE,
                OsConstants.CAP_SYS_TIME,
                OsConstants.CAP_SYS_TTY_CONFIG,
                OsConstants.CAP_WAKE_ALARM,
                OsConstants.CAP_BLOCK_SUSPEND
        );
        /* Containers run without some capabilities, so drop any caps that are not available. */
        StructCapUserHeader header = new StructCapUserHeader(
                OsConstants._LINUX_CAPABILITY_VERSION_3, 0);
        StructCapUserData[] data;
        try {
            data = Os.capget(header);
        } catch (ErrnoException ex) {
            throw new RuntimeException("Failed to capget()", ex);
        }
        capabilities &= ((long) data[0].effective) | (((long) data[1].effective) << 32);

        /* Hardcoded command line to start the system server */
        String[] args = {
                "--setuid=1000",
                "--setgid=1000",
                "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,1021,1023,"
                        + "1024,1032,1065,3001,3002,3003,3005,3006,3007,3009,3010,3011,3012",
                "--capabilities=" + capabilities + "," + capabilities,
                "--nice-name=system_server",
                "--runtime-args",
                "--target-sdk-version=" + VMRuntime.SDK_VERSION_CUR_DEVELOPMENT,
                "com.android.server.SystemServer",
        };
        ZygoteArguments parsedArgs;

        int pid;

        try {
            ZygoteCommandBuffer commandBuffer = new ZygoteCommandBuffer(args);
            try {
                parsedArgs = ZygoteArguments.getInstance(commandBuffer);
            } catch (EOFException e) {
                throw new AssertionError("Unexpected argument error for forking system server", e);
            }
            commandBuffer.close();
            Zygote.applyDebuggerSystemProperty(parsedArgs);
            Zygote.applyInvokeWithSystemProperty(parsedArgs);

            if (Zygote.nativeSupportsMemoryTagging()) {
                /* The system server has ASYNC MTE by default, in order to allow
                 * system services to specify their own MTE level later, as you
                 * can't re-enable MTE once it's disabled. */
                String mode = SystemProperties.get("arm64.memtag.process.system_server", "async");
                if (mode.equals("async")) {
                    parsedArgs.mRuntimeFlags |= Zygote.MEMORY_TAG_LEVEL_ASYNC;
                } else if (mode.equals("sync")) {
                    parsedArgs.mRuntimeFlags |= Zygote.MEMORY_TAG_LEVEL_SYNC;
                } else if (!mode.equals("off")) {
                    /* When we have an invalid memory tag level, keep the current level. */
                    parsedArgs.mRuntimeFlags |= Zygote.nativeCurrentTaggingLevel();
                    Slog.e(TAG, "Unknown memory tag level for the system server: \"" + mode + "\"");
                }
            } else if (Zygote.nativeSupportsTaggedPointers()) {
                /* Enable pointer tagging in the system server. Hardware support for this is present
                 * in all ARMv8 CPUs. */
                parsedArgs.mRuntimeFlags |= Zygote.MEMORY_TAG_LEVEL_TBI;
            }

            /* Enable gwp-asan on the system server with a small probability. This is the same
             * policy as applied to native processes and system apps. */
            parsedArgs.mRuntimeFlags |= Zygote.GWP_ASAN_LEVEL_LOTTERY;

            if (shouldProfileSystemServer()) {
                parsedArgs.mRuntimeFlags |= Zygote.PROFILE_SYSTEM_SERVER;
            }

            /* Request to fork the system server process */
            pid = Zygote.forkSystemServer(
                    parsedArgs.mUid, parsedArgs.mGid,
                    parsedArgs.mGids,
                    parsedArgs.mRuntimeFlags,
                    null,
                    parsedArgs.mPermittedCapabilities,
                    parsedArgs.mEffectiveCapabilities);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }

        /* For child process */
        if (pid == 0) {
            if (hasSecondZygote(abiList)) {
                waitForSecondaryZygote(socketName);
            }

            zygoteServer.closeServerSocket();
            return handleSystemServerProcess(parsedArgs);
        }

        return null;
    }
```



2.在`forkSystemServer`方法中调用`handleSystemServerProcess(parsedArgs)`



![024](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/024.png)

![025](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/025.png)



**SystemServer启动的服务**

![026](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/026.png)

![027](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/027.png)











