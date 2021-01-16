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
        Movie fromDatabase = movieRepository.findByTitle(movie.getTitle());

        int avgRating = getAvgRatingValue(movie, fromDatabase);
        fromDatabase.setRating(String.valueOf(avgRating)); //Average Ratings
        movieRepository.save(movie);

        return fromDatabase;
    }

    public Movie updateMovieReviews(Movie movie) {
        Movie fromDatabase = movieRepository.findByTitle(movie.getTitle());

        int avgRating = getAvgRatingValue(movie, fromDatabase);
        fromDatabase.setRating(String.valueOf(avgRating)); //Average Ratings

        // Add Review to the List in the Movie Object
        List<String> movieReviewList = fromDatabase.getReviews();
        movieReviewList.addAll(movie.getReviews());
        fromDatabase.setReviews(movieReviewList);

        movieRepository.save(fromDatabase); //save

        return fromDatabase;
    }

    private Integer getAvgRatingValue(Movie inputMovie, Movie movieFromDatabase) {
        Integer yourRating = Integer.parseInt(inputMovie.getRating());
        List<Integer> movieRatingList = movieFromDatabase.getRatings();
        movieRatingList.add(yourRating);
        movieFromDatabase.setRatings(movieRatingList);
        return movieFromDatabase.getRatings().stream().mapToInt(Integer::intValue).sum()/movieFromDatabase.getRatings().size();
    }
}
