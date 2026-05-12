package pt.project.suggestions_service_v2;

import java.util.List;

public class Movie {
    private int id;
    private List<Integer> genreIds;
    private int directorId;
    private int watchCount;

    public Movie(int id, List<Integer> genreIds, int directorId, int watchCount) {
        this.id = id;
        this.genreIds = genreIds;
        this.directorId = directorId;
        this.watchCount = watchCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenres(List<Integer> genres) {
        this.genreIds = genres;
    }

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirector(Integer directorId) {
        this.directorId = directorId;
    }

    public int getWatchCount() {
        return watchCount;
    }
    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }
}
