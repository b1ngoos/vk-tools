package com.b1ngoos.walllikes.domain;

public class UserLike {

    private int id;
    private int likes;

    public UserLike() {
    }

    public UserLike(int id) {
        this.id = id;
        likes++;
    }

    public int getLikes() {
        return likes;
    }

    public void incLikes() {
        likes++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserLike{" +
                "id=" + id +
                ", likes=" + likes +
                '}';
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserLike other = (UserLike) obj;
        if (id != other.id)
            return false;

        other.likes++;
        return true;
    }
}
