package com.MovieJunit.Movieproject.repository;

import com.MovieJunit.Movieproject.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
}
