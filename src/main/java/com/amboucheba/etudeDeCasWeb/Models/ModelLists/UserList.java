package com.amboucheba.etudeDeCasWeb.Models.ModelLists;

import com.amboucheba.etudeDeCasWeb.Models.User;

import java.util.List;

public class UserList {

    private List<User> users;
    private int count;

    public UserList(List<User> users) {
        this.users = users;
        this.count = users.size();
    }

    public UserList() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
