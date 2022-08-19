package com.wz.ipcdemo;

import com.wz.ipcdemo.model.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
