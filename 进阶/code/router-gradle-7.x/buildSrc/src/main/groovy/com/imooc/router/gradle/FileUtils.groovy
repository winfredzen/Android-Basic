package com.imooc.router.gradle

class FileUtils {
    static void copyFolder(String srcFolder, String tarFolder) throws Exception {
        File tar = new File(tarFolder);
        if (!tar.exists()) {
            tar.mkdir();
        }
        //System.out.println(tar.getName());
        File src = new File(srcFolder);
        File[] srcFiles = src.listFiles();//遍历源文件
        for (File file : srcFiles
        ) {
            if (file.isFile()) {//如果是文件则拷贝
                copyFile(file, new File(tarFolder, file.getName()));
            } else {//如果不是文件，则新建文件夹，把当前文件拷贝到文件夹
                copyFolder(file.getAbsolutePath(),  new File(tarFolder, file.getName()).absolutePath);
            }
        }
    }

    static void copyFile(File srcFile, File tarPath) throws Exception {
        FileInputStream ins = new FileInputStream(srcFile);
        FileOutputStream out = new FileOutputStream(tarPath);
        int len = 0;
        byte[] temp = new byte[1024];
        while ((len = ins.read(temp)) != -1) {
            out.write(temp, 0, len);
            out.flush();
        }
        out.close();
        ins.close();
    }

}
