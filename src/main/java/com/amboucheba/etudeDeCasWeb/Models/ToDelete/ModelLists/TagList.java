package com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Tag;

import java.util.List;

public class TagList {

    private List<Tag> tags;
    private int count;

    public TagList(List<Tag> tags) {
        this.tags = tags;
        this.count = tags.size();
    }

    public TagList() {
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
