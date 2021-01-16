package com.galvanize.GMDBapi.unitTests;

import com.galvanize.GMDBapi.model.Movie;
import com.galvanize.GMDBapi.repository.MovieRepository;
import com.galvanize.GMDBapi.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class ServiceTest {
    private MovieService movieService;
    private MovieRepository mockMovieRepository;
    List<Movie> movies;
    Movie movie;
    List<String> reviews;

    @BeforeEach
    public void setup(){
        mockMovieRepository = mock(MovieRepository.class);
        movieService = new MovieService(mockMovieRepository);
    }

    @BeforeEach
    public void movieSetup(){
        movie = new Movie();
        movie.setTitle("The Avengers");
        movie.setDirector("Joss Whedon");
        movie.setActors("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth");
        movie.setRelease("2012");
        movie.setDescription("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.");
        //movie.setRating(null);
    }

    @Test
    public void getAllMoviesTest() {
        movies = new ArrayList<>();
        movies.add(movie);

        Movie superManReturns = new Movie();
        movie.setTitle("Superman Returns");
        movies.add(superManReturns);

        when(mockMovieRepository.findAll()).thenReturn(movies);

        List<Movie> actualMovies = movieService.getAllMovies();
        assertEquals(movies, actualMovies);
        assertEquals(2, actualMovies.size());
    }

    @Test
    public void findByTitle_returnsMovieWithGivenTitle(){

        when(mockMovieRepository.findByTitle(any())).thenReturn(movie);

        Movie fromService = movieService.findByTitle(movie.getTitle());
        assertEquals(movie, fromService);
    }

    @Test
    public void findByNonExistentTitle_returnsNull(){
        when(mockMovieRepository.findByTitle("hello")).thenReturn(null);

        Movie fromService = movieService.findByTitle("Hello");
        assertNull(fromService);

    }

    @Test
    public void updateMovieRatingsTest(){
        when(mockMovieRepository.findByTitle(any())).thenReturn(movie);

        Movie avengerReview1 = new Movie();
        avengerReview1.setTitle("The Avengers");
        avengerReview1.setRating("5");

        Movie fromService = movieService.updateMovieRatings(avengerReview1);
        assertEquals("5",fromService.getRating());

        when(mockMovieRepository.findByTitle(any())).thenReturn(fromService);

        Movie avengerReview2 = new Movie();
        avengerReview2.setTitle("The Avengers");
        avengerReview2.setRating("3");

        Movie fromService2 = movieService.updateMovieRatings(avengerReview2);
        assertEquals("4",fromService2.getRating());

    }

    @Test
    public void updateMovieReviewsTest() {
        reviews = new ArrayList<>();

        when(mockMovieRepository.findByTitle(any())).thenReturn(movie);

        Movie avengerReview1 = new Movie();
        reviews.add("liked!!");
        avengerReview1.setTitle("The Avengers");
        avengerReview1.setReviews(reviews);
        avengerReview1.setRating("5");

        Movie fromService = movieService.updateMovieReviews(avengerReview1);
        assertEquals(1, fromService.getReviews().size());
        assertEquals("5", fromService.getRating());

        reviews.clear();

        when(mockMovieRepository.findByTitle(any())).thenReturn(fromService);

        Movie avengerReview2 = new Movie();
        reviews.add("Really Cool movie!!");
        avengerReview2.setTitle("The Avengers");
        avengerReview2.setReviews(reviews);
        avengerReview2.setRating("3");

        Movie fromService2 = movieService.updateMovieReviews(avengerReview2);

        assertEquals(2, fromService2.getReviews().size());
        assertEquals("4",fromService2.getRating());

    }


}
