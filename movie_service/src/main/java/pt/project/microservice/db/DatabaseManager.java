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

    public Movie getMovieByTitle(String title) {
        String sql = """
                SELECT m.id, m.title, m.description, g.name AS genre, d.name AS director_name FROM movies_fts f
                JOIN movies m ON m.id = f.rowid
                JOIN directors d on m.director_id = d.id
                JOIN movies_genres mg ON m.id = mg.movie_id
                JOIN genres g ON g.id = mg.genre_id
                WHERE movies_fts MATCH ?;
                """;

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet result = statement.executeQuery();

            Movie movie = null;
            List<String> genres = new ArrayList<>();

            while (result.next()) {
                if (movie == null) {

                    movie = new Movie(
                            result.getInt("id"),
                            result.getString("title"),
                            result.getString("description"),
                            genres,
                            result.getString("director_name")
                    );
                }

                String genre = result.getString("genre");

                if (genre != null) {
                    genres.add(genre);
                }
            }
            return movie;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
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
                            result.getString("director_name")
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
}
