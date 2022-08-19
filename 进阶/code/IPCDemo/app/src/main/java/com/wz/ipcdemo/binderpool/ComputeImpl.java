package com.wz.ipcdemo.binderpool;

import android.os.RemoteException;

import com.wz.ipcdemo.ICompute;

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

}
