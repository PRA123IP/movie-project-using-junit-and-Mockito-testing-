package com.MovieJunit.Movieproject.service;

import com.MovieJunit.Movieproject.entity.Movie;
import com.MovieJunit.Movieproject.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public  Movie getMovieById(Long id, Movie updateDetails) {
        Movie movie = movieRepository.findById(id).get();
        if(movie!=null)
        {
            movie.setName(updateDetails.getName());
            movie.setGenera(updateDetails.getGenera());
         return    movieRepository.save(movie);
        }

        return null;
    }

    public Movie    createmovie(Movie movie) {
        log.info("service "+movie);
       return movieRepository.save(movie);

    }

    public List<Movie> getMovieDetails() {

        List<Movie> movieList = movieRepository.findAll();
        return movieList;
    }

    public void deletebyId(Long id) {
        movieRepository.deleteById(id);
    }

    public Movie getbyId(Long id) {
       return movieRepository.findById(id).get();


    }
}
