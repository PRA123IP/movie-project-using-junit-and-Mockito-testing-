package com.MovieJunit.Movieproject;

import com.MovieJunit.Movieproject.controller.MovieController;
import com.MovieJunit.Movieproject.entity.Movie;
import com.MovieJunit.Movieproject.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();
    @MockBean
    private MovieService movieService;
    @InjectMocks
    private MovieController movieController;

  Movie record = new Movie(1L,"Don21","Your Movie 1991");
    Movie record1 = new Movie(2L,"ram","Your Movie 1991");
    Movie record2 = new Movie(3L,"KGF","Your Movie 2019");

    @Before("")
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    public void getMovieDetails_test() throws Exception {
        List<Movie> list = new ArrayList<>(Arrays.asList(record,record1, record2));
        when(movieService.getMovieDetails()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8081/movie/api/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
         .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Don21"));


    }

    @Test
    public void getById_success()
    {
        when(movieService.getbyId(record.getId())).thenReturn(record);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("http://localhost:8081/movie/api/getbyid/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void create_Succes() throws Exception {
        Movie record=Movie.builder()
                .id(4L)
                .name("ram sita")
                .genera("this is 1990")
                .build();


        when(movieService.createmovie(record)).thenReturn(record);
        //object convert into string
        String content=objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("http://localhost:8081/movie/api/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()));
    }



    @Test
    public void testUpdateExistingMovie() throws Exception {
        long movieId = 1L;
        Movie existingMovie = Movie.builder()
                .id(movieId)
                .name("Don21")
                .genera("Your M")
                .build();

        // Mock the service behavior to return the existing movie when updating
        Mockito.when(movieService.getMovieById(eq(movieId), any())).thenReturn(existingMovie);

        // Define the updated details
        Movie updatedDetails = Movie.builder()
                .id(movieId)
                .name("Don21")
                .genera("Action, Thriller")
                .build();

        // Convert the updated movie object to JSON
        String content = new ObjectMapper().writeValueAsString(updatedDetails);

        // Perform the PUT request using MockMvc
        MockHttpServletRequestBuilder put = MockMvcRequestBuilders.put("http://localhost:8081/movie/api/post/" + movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        // Perform the request and validate the response
        mockMvc.perform(put)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movieId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Don21"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genera").value("Action, Thriller"));

        // Verify that the movieService's getMovieById method was called with the correct parameters
        verify(movieService, times(1)).getMovieById(eq(movieId), eq(updatedDetails));

        // Retrieve the existingMovie after the update request
        existingMovie = movieService.getMovieById(movieId, updatedDetails);

        // Check if the existingMovie fields match the updatedDetails fields and print a message if they do
        if (!existingMovie.getName().equals(updatedDetails.getName())) {
            System.out.println("Test Failed: Name mismatch in existingMovie.");
        }
        if (!existingMovie.getGenera().equals(updatedDetails.getGenera())) {
            System.out.println("Test Failed: Genera mismatch in existingMovie.");
        }

        // Additional validation to check if the updated movie fields are not empty
        assertNotEquals("", existingMovie.getName());
        assertNotEquals("", existingMovie.getGenera());
    }

    @Test
    public void delete_Success() throws Exception
    {
 // Mockito.doNothing().when(movieService.deletebyId(record.getId()));
        Mockito.doNothing().when(movieService).deletebyId(record.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("http://localhost:8081/movie/api/delete/"+record.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("delete movies details successfully "));


    }

}
