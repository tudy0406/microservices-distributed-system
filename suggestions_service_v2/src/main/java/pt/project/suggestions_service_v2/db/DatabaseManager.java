package pt.project.suggestions_service_v2.db;

import pt.project.suggestions_service_v2.Movie;

import javax.naming.ldap.PagedResultsControl;
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

    public Movie getMovieById(int id){
        Movie movie = null;
        String sql = """
                SELECT m.* , g.id AS genre_id FROM movies m
                JOIN movies_genres mg ON mg.movie_id = m.id
                JOIN genres g ON g.id = mg.genre_id
                WHERE m.id = ?;
                """;

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            List<Integer> genreIds = new ArrayList<>();
            while(rs.next()){
                if(movie == null) {
                    movie = new Movie(
                            rs.getInt("id"),
                            genreIds,
                            rs.getInt("director_id"),
                            rs.getInt("watch_count")
                    );
                }
                int genre_id = rs.getInt("genre_id");
                genreIds.add(genre_id);
            }
            return movie;

        }catch(SQLException e){
            System.out.println(e);
        }
        return movie;
    }

    public List<Integer> getSuggestions(String attribute, List<Integer> attributeIds, int requestedMovieId) {

        List<Integer> suggestions = new ArrayList<>();

        String sql;

        if ("director".equals(attribute)) {

            sql = """
                SELECT DISTINCT id, watch_count
                FROM movies
                WHERE director_id = ? AND id != ?
                ORDER BY watch_count DESC
                LIMIT 5;
              """;

        } else if ("genres".equals(attribute)) {

            String placeholders = String.join(
                    ",",
                    java.util.Collections.nCopies(attributeIds.size(), "?")
            );

            sql = """
                SELECT DISTINCT m.id, m.watch_count
                FROM movies m
                JOIN movies_genres mg
                    ON mg.movie_id = m.id
                WHERE mg.genre_id IN (%s) AND m.id != ?
                ORDER BY m.watch_count DESC
                LIMIT 5;
              """.formatted(placeholders);

        } else {
            return suggestions;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            int paramIndex = 1;

            for (Integer id : attributeIds) {
                stmt.setInt(paramIndex++, id);
            }

            stmt.setInt(paramIndex, requestedMovieId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    suggestions.add(rs.getInt("id"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch suggestions", e);
        }

        return suggestions;
    }
}
