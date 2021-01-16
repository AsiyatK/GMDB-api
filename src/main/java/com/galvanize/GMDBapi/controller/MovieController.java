package com.galvanize.GMDBapi.controller;

import com.galvanize.GMDBapi.model.Movie;
import com.galvanize.GMDBapi.repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/api/movies")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
}
