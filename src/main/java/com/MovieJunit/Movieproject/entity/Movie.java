package com.MovieJunit.Movieproject.entity;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;


@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "tbl_movies")
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String genera;

    public Movie(long id, String name, String genera) {
        this.id = id;
        this.name = name;
        this.genera = genera;
    }
// private LocalDate releaseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenera() {
        return genera;
    }

    public void setGenera(String genera) {
        this.genera = genera;
    }

//    public LocalDate getReleaseDate() {
//        return releaseDate;
//    }
//
//    public void setReleaseDate(LocalDate releaseDate) {
//        this.releaseDate = releaseDate;
//    }
}