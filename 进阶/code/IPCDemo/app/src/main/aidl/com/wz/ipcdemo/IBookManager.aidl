// IBookManager.aidl
package com.wz.ipcdemo;

// Declare any non-default types here with import statements

import com.wz.ipcdemo.model.Book;
import com.wz.ipcdemo.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}