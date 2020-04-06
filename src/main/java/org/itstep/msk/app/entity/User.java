package org.itstep.msk.app.entity;

import org.itstep.msk.app.enums.Role;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(columnDefinition = "BIT")
    private Boolean active = false;

    @Column(name = "confirm_code")
    private String confirmCode;

    @Column(name = "confirm_expired")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmExpired;

    @OneToOne(targetEntity = Upload.class)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Upload avatar;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<String> getStringRoles() {
        return roles.stream().map(Enum::toString).collect(Collectors.toSet());
    }

    public void setStringRoles(Set<String> stringRoles) {
        roles.clear();
        for (String stringRole : stringRoles) {
            roles.add(Role.valueOf(stringRole));
        }
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public Date getConfirmExpired() {
        return confirmExpired;
    }

    public void setConfirmExpired(Date confirmExpired) {
        this.confirmExpired = confirmExpired;
    }

    public Upload getAvatar() {
        return avatar;
    }

    public void setAvatar(Upload avatar) {
        this.avatar = avatar;
    }
}
