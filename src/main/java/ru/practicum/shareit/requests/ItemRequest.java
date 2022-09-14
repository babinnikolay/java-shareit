package ru.practicum.shareit.requests;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "request")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id")
    private Long id;
    @Column(name = "request_description")
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created;
}
