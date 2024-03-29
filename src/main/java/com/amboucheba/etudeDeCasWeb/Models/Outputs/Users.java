package com.amboucheba.etudeDeCasWeb.Models.Outputs;

import com.amboucheba.etudeDeCasWeb.Models.Entities.User;

import java.util.List;
import java.util.Objects;

public class Users {

    private List<User> users;

    public Users(List<User> users) {
        this.users = users;
    }

    public Users() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users1 = (Users) o;
        return users.equals(users1.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }

}
