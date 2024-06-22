package com.sportsLog.sportsLog.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String nickname;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @Builder
    public static User createUser(String email, String password, LocalDate birthdate, String role,
        String nickname, UserStatus userStatus) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.birthdate = birthdate;
        user.nickname = nickname;
        user.role = role;
        user.userStatus = userStatus;
        return user;
    }

    public void addPost(Post post, Board board) {
        posts.add(post);
        post.setUser(this);
        post.setBoard(board);
        board.incrementPostCount();
    }

    public void removePost(Post post, Board board) {
        posts.remove(post);
        post.setUser(null);
        post.setBoard(null);
        board.decrementPostCount();
    }
}
