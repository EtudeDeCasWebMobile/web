package com.amboucheba.etudeDeCasWeb.Models.Inputs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class CollectionInput {

    @NotBlank(message = "Field 'tag' is required")
    @Size( min = 1, max = 50, message = "Tag length must be between 1 and 50")
    private String tag;

    public CollectionInput(@NotBlank(message = "Field 'tag' is required") @Size(min = 0, max = 50, message = "Tag length must be between 1 and 50") String tag) {
        this.tag = tag;
    }

    public CollectionInput() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionInput that = (CollectionInput) o;
        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
