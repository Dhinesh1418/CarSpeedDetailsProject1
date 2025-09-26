package com.polestar.carspeeddetails.data.repo;

import com.polestar.carspeeddetails.domain.model.User;

import java.util.List;

public class UserData {

    //Sample data
    private List<User> users = List.of(
            new User("001", "Dhinesh", 80f, "FIREBASE"),
            new User("002", "Nefari", 90f, "AWS")
    );

    public User getUser(String id){
       if(users.get(0).getId()==id){
           return users.get(0);
       }
        return users.get(1);
    }

}
