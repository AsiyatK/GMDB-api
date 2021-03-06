package com.galvanize.GMDBapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.galvanize.GMDBapi.model.Movie;
import com.galvanize.GMDBapi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
           return new ResponseEntity<>(this.buildResponseEntity("Movie with this title does not exist"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/movies/rate")
    public Movie updateMovieRatings(@RequestBody Movie movie){
        return movieService.updateMovieRatings(movie);
    }

    @PutMapping("/api/movies/review")
    public ResponseEntity<Object> updateMovieReviews(@RequestBody Movie movie) throws JsonProcessingException {
        if(movie.getRating()==null)
            return new ResponseEntity<>(this.buildResponseEntity("Please Submit the Ratings along with the Review"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(movieService.updateMovieReviews(movie), HttpStatus.OK);
    }



    private String buildResponseEntity(String message) throws JsonProcessingException {
        //forming json to return in the response;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode incorrectTitle = mapper.createObjectNode();
        incorrectTitle.put("status", message);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(incorrectTitle);
    }

}
