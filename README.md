## GMDB API
- - - - -

### Stories and Acceptance Criteria

Tip: When reading through stories, try focusing on writing the test that will fulfill the criteria first. Stoires and their critera outline behavior, not code.

As a user, I should see a list of movies when I visit GMDB.
````
When I visit GMDB
Then I can see a list of all movies.
````

As a user, I can browse each movie so I can learn all the details.

````
Rule: Movie details include title, director, actors, release year, description and star rating.

Given an existing movie
When I visit that title
Then I can see all the movie details.

Given a non-existing movie
When I visit that title
Then I receive a friendly message that it doesn't exist.
````

As a user, I can give a star rating to a movie so that I can share my experiences with others.
````
Given an existing movie
When I submit a 5 star rating
Then I can see it in the movie details.

Given a movie with one 5 star rating and one 3 star rating
When I view the movie details
Then I expect the star rating to be 4.
````

As a user, I can review a movie so that I can share my thoughts about it.
````
Given an existing movie
When I submit a star rating and text review
Then I can see my contribution on the movie details.

Given an existing movie
When I submit a text review without a star rating
Then I receive a friendly message that a star rating is required.
````

### **API Specification**

| URL | Request Method | Description | Http Status |
|-----|----------------|:------------|------------:|
|/api/movies|GET|Get all the Movies from the GMDB| 200|
|/api/movies/{title}|GET|Get Movie by Title|200|
|/api/movies/rate|PUT|Update and return the Movie Object with ratings|200|
|/api/movies/review|PUT|Update and return the Movie Object with Review|200|

### **Sample Request and Response**
GET /api/movies
````
[
  {
    "title": "The Avengers",
    "director": "Joss Whedon",
    "actors": "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth",
    "release": "2012",
    "description": "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.",
    "rating": null
  },
  {
    "title": "Superman Returns",
    "director": "Bryan Singer",
    "actors": "Brandon Routh, Kate Bosworth, Kevin Spacey, James Marsden",
    "release": "2006",
    "description": "Superman returns to Earth after spending five years in space examining his homeworld Krypton. But he finds things have changed while he was gone, and he must once again prove himself important to the world.",
    "rating": null
  }
 ]
````
GET /api/movies/The Avengers

````
  {
    "title": "The Avengers",
    "director": "Joss Whedon",
    "actors": "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth",
    "release": "2012",
    "description": "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.",
    "rating": null
  }
````
PUT /api/movies/rate

Request
````
  {
    "title": "The Avengers",
    "director": "Joss Whedon",
    "actors": "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth",
    "release": "2012",
    "description": "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.",
    "rating": 5
  }
````
Response
````
  {
    "title": "The Avengers",
    "director": "Joss Whedon",
    "actors": "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth",
    "release": "2012",
    "description": "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.",
    "rating": "5"
  }
````

PUT /api/movies/review

Request
````
  {
    "title": "The Avengers",
    "director": "Joss Whedon",
    "actors": "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth",
    "release": "2012",
    "description": "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.",
    "rating": "5"
    "reviews":[
        "liked!!"
    ]
  }
````
Response
````
  {
    "title": "The Avengers",
    "director": "Joss Whedon",
    "actors": "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth",
    "release": "2012",
    "description": "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.",
    "rating": "5",
    "reviews":[
        "Really Cool movie!!", 
        "liked!!"
    ]
  }
````

