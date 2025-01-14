package by.tms.myonlinelibrary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@ToString
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private int pages;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private int rating;

    @Column(nullable = false)
    private String description;

    private String image;

   @Lob
   private byte[] content;

}