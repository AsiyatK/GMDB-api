package com.galvanize.GMDBapi.service;

import com.galvanize.GMDBapi.model.Movie;
import com.galvanize.GMDBapi.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie findByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    public Movie updateMovieRatings(Movie movie) {
        int yourRating = Integer.parseInt(movie.getRating());
        List movieRatingList = movie.getRatings();
        movieRatingList.add(yourRating);
        movie.setRatings(movieRatingList);

        int avgRating =  movie.getRatings().stream().mapToInt(Integer::intValue).sum()/movie.getRatings().size();
        movie.setRating(String.valueOf(yourRating)); //Average Ratings
        movieRepository.save(movie);

        movie.setRating(avgRating +" (Your Rating: " + yourRating+")"); //User custom Message
        return movie;
    }

    public Movie updateMovieReviews(Movie movie) {
        return null;
    }
}
