package org.darccona.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {

    @Id

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "SUB")
    private ArrayList<String> sub;

    @Column(name = "LIKERECORD")
    private ArrayList<Long> like;

    public UserEntity() {}
    public UserEntity(String name, String password) {
        this.name = name;
        this.password = password;
        this.role = "USER";
        this.sub = new ArrayList<>();
        this.like = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public void setSub(String name) {
        if (!sub.contains(name)) {
            this.sub.add(name);
        }
    }
    public ArrayList<String> getSub() {
        return sub;
    }
    public void removeSub(String name) {
        getSub().remove(name);
    }

    public void setLikeRecord(Long id) {
        this.like.add(id);
    }
    public ArrayList<Long> getLikeRecord() {
        return like;
    }
    public void removeLikeRecord(Long id) {
        getLikeRecord().remove(id);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<RecordEntity> record = new HashSet<>();

    public void setRecord(Set<RecordEntity> record) {
        this.record = record;
    }
    public Set<RecordEntity> getRecord() {
        return record;
    }
    public void removeRecord(RecordEntity record) {
        getRecord().remove(record);
    }
}
