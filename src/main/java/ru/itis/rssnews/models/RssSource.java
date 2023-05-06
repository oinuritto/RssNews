package ru.itis.rssnews.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity(name = "sources")
public class RssSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String source;
}
