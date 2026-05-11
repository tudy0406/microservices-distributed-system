package pt.project.microservice.model;

import java.util.List;

public class Movie {

    private int id;
    private String title;
    private String description;
    private List<String> genres;
    private String director;

    public Movie(int id, String title, String description,  List<String> genres, String director) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.director = director;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
