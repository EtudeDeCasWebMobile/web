package com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;

import java.util.List;

public class UserList {

    private List<Users> users;
    private int count;

    public UserList(List<Users> users) {
        this.users = users;
        this.count = users.size();
    }

    public UserList() {
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
