package com.galvanize.GMDBapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.galvanize.GMDBapi.model.Movie;
import com.galvanize.GMDBapi.repository.MovieRepository;
import com.galvanize.GMDBapi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/api/movies")
    public List<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }

    @GetMapping("/api/movies/{title}")
    public ResponseEntity<Object> getMovieByTitle(@PathVariable String title) throws JsonProcessingException {

        Movie movie = movieService.findByTitle(title);
        if(movie != null){
            return new ResponseEntity<>(movie, HttpStatus.OK);
        }
       else {
           return new ResponseEntity<>(this.buildResponseEntity(), HttpStatus.NOT_FOUND);
        }
    }

    private String buildResponseEntity() throws JsonProcessingException {
        //forming json to return in the response;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode incorrectTitle = mapper.createObjectNode();
        incorrectTitle.put("status", "Movie with this title does not exist");
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(incorrectTitle);
    }


}
