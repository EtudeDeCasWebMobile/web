package com.amboucheba.etudeDeCasWeb.Models.Outputs;

import java.util.Objects;

public class CollectionShare {

    private String link;
    private String token;

    public CollectionShare(String link, String token) {
        this.link = link;
        this.token = token;
    }

    public CollectionShare() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionShare that = (CollectionShare) o;
        return link.equals(that.link) &&
                token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, token);
    }
}
