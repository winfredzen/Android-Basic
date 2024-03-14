package org.example;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String userName;
    public List<Address> addresses;

    public static class Address {
        public String street;
        public String city;
    }


    public static void main(String[] args) {

        User user = new User();
        user.userName = "tony";
        user.addresses = new ArrayList<>();
        User.Address address1 = new User.Address();
        address1.street = "人民路";
        address1.city = "苏州";
        user.addresses.add(address1);
        User.Address address2 = new User.Address();
        address2.street = "中山路";
        address2.city = "深圳";
        user.addresses.add(address2);

        Observable.just(user)
                .flatMap(new Function<User, ObservableSource<User.Address>>() {
                    @Override
                    public Observable<User.Address> apply(User user) throws Exception {
                        return Observable.fromIterable(user.addresses);
                    }
                }).subscribe(new Consumer<User.Address>() {
                    @Override
                    public void accept(User.Address address) throws Exception {
                        System.out.println(address.street);
                    }
                });

//        Observable.just(user)
//                .map(new Function<User, List<Address>>() {
//                    @Override
//                    public List<Address> apply(User user) throws Exception {
//                        return user.addresses;
//                    }
//                }).subscribe(new Consumer<List<Address>>() {
//                    @Override
//                    public void accept(List<Address> addresses) throws Exception {
//                        for (User.Address address:addresses) {
//                            System.out.println(address.street);
//                        }
//                    }
//                });
    }

}
