package pt.project.microservice.db;
import pt.project.microservice.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:C:/Users/Tudor/Desktop/facultate/year_3/SD/microservice/movie_service/identifier.sqlite";

    private Connection connection;

    public DatabaseManager() {
        connect();
    }

    private void connect() {

        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public List<Movie> getMovieByTitle(String title) {
        System.out.println("Getting movie by title: " + title);
        String[] tokens = title.trim().split("\\s+");
        StringBuilder query = new StringBuilder();
        for (String token : tokens) {
            query.append(token).append("* ");
        }


        String sql = """
                SELECT m.id, m.title, m.description, m.watch_count, g.name AS genre, d.name AS director_name FROM movies_fts f
                JOIN movies m ON m.id = f.rowid
                JOIN directors d on m.director_id = d.id
                JOIN movies_genres mg ON m.id = mg.movie_id
                JOIN genres g ON g.id = mg.genre_id
                WHERE movies_fts MATCH ?;
                """;
        List<Movie> movies = new  ArrayList<>();
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, query.toString().trim());
            ResultSet result = statement.executeQuery();

            Movie movie = null;
            List<String> genres = new ArrayList<>();
            int currentMovieId = -1;

            while (result.next()) {
                if(movie == null || currentMovieId != result.getInt("id")){
                    if(movie != null){
                        movies.add(movie);
                    }
                    currentMovieId = result.getInt("id");

                    genres = new ArrayList<>();
                    movie = new Movie(
                            result.getInt("id"),
                            result.getString("title"),
                            result.getString("description"),
                            genres,
                            result.getString("director_name"),
                            result.getInt("watch_count")
                    );

                }

                String genre = result.getString("genre");

                if (genre != null) {
                    genres.add(genre);
                }
            }
            if (movie != null) {
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
        return movies;
    }

    public List<Movie> getMoviesByIds(List<Integer> ids) {

        List<Movie> movies = new ArrayList<>();

        if (ids == null || ids.isEmpty()) {
            return movies;
        }

        StringBuilder placeholders = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {

            placeholders.append("?");

            if (i < ids.size() - 1) {
                placeholders.append(",");
            }
        }

        String sql = """
        SELECT
            m.id,
            m.title,
            m.description,
            m.watch_count,
            g.name AS genre,
            d.name AS director_name
        FROM movies m
        JOIN directors d ON m.director_id = d.id
        LEFT JOIN movies_genres mg ON m.id = mg.movie_id
        LEFT JOIN genres g ON g.id = mg.genre_id
        WHERE m.id IN (
        """ + placeholders + """
        );
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                statement.setInt(i + 1, ids.get(i));
            }

            ResultSet result = statement.executeQuery();

            Movie currentMovie = null;
            int currentMovieId = -1;
            List<String> genres = null;

            while (result.next()) {

                int movieId = result.getInt("id");

                if (currentMovie == null || movieId != currentMovieId) {

                    genres = new ArrayList<>();

                    currentMovie = new Movie(
                            movieId,
                            result.getString("title"),
                            result.getString("description"),
                            genres,
                            result.getString("director_name"),
                            result.getInt("watch_count")
                    );

                    movies.add(currentMovie);

                    currentMovieId = movieId;
                }

                String genre = result.getString("genre");

                if (genre != null && !genres.contains(genre)) {

                    genres.add(genre);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Query failed",e);
        }

        return movies;
    }

    public List<Movie> getTopWatchedMovies(){
        String sql = """
                SELECT DISTINCT m.id, m.title, m.description, m.watch_count, g.name AS genre, d.name AS director_name FROM movies_fts f
                JOIN movies m ON m.id = f.rowid
                JOIN directors d on m.director_id = d.id
                JOIN movies_genres mg ON m.id = mg.movie_id
                JOIN genres g ON g.id = mg.genre_id
                ORDER BY watch_count DESC;
                """;
        List<Movie> movies = new  ArrayList<>();
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            Movie movie = null;
            List<String> genres = new ArrayList<>();
            int currentMovieId = -1;
            int nbOfMovies = 0;
            while (result.next()) {
                if(movie == null || currentMovieId != result.getInt("id")){

                    if(nbOfMovies >= 5){
                        break;
                    }
                    nbOfMovies++;

                    if(movie != null){
                        movies.add(movie);
                    }
                    currentMovieId = result.getInt("id");

                    genres = new ArrayList<>();
                    movie = new Movie(
                            result.getInt("id"),
                            result.getString("title"),
                            result.getString("description"),
                            genres,
                            result.getString("director_name"),
                            result.getInt("watch_count")
                    );


                }

                String genre = result.getString("genre");

                if (genre != null) {
                    genres.add(genre);
                }
            }
            if (movie != null) {
                movies.add(movie);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
        return movies;
    }

    public void addWatchedMovie(int movieId){
        String sql = """
                    UPDATE movies SET watch_count = watch_count + 1 WHERE id = ?;
                """;

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, movieId);
            stmt.execute();
        }catch(SQLException e){
            System.out.println("addWatchedMovie SQLException");
        }
    }

}
