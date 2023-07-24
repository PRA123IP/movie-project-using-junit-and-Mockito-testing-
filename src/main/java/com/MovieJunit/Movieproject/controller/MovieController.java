package com.MovieJunit.Movieproject.controller;

import com.MovieJunit.Movieproject.entity.Movie;
import com.MovieJunit.Movieproject.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie/api")
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;
    @PostMapping("/save")
    public ResponseEntity<Movie> create(@RequestBody Movie movie)
    {
        try {
        Movie createdMovie = movieService.createmovie(movie);
        log.info("kk"+createdMovie);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);

    } catch (Exception e) {
            log.info("error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @GetMapping("/get")
    public ResponseEntity<List<Movie>> getMovieDetails()
    {
        try {


            List<Movie> movieDetails = movieService.getMovieDetails();
            log.info(movieDetails.toString());
            return ResponseEntity.status(HttpStatus.OK).body(movieDetails);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
@PutMapping("/post/{id}")
    public ResponseEntity<Movie> updateDetailsById(@PathVariable Long id , @RequestBody Movie updateDetails) {
    try {
        Movie movieById = movieService.getMovieById(id,updateDetails);
        if (movieById == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(updateDetails);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@DeleteMapping("/delete/{id}")
    public ResponseEntity<String>  deleteById(@PathVariable Long id ){

        try
        {
            movieService.deletebyId(id);
            return ResponseEntity.ok("delete movies details successfully ");
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
}

@GetMapping("/getbyid/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id)
{
    try {
        Movie movie = movieService.getbyId(id);
        if(movie==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }
    catch (Exception e)
    {
        return ResponseEntity.internalServerError().build();
    }

}



}
