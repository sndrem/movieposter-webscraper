package no.uib.smo015.info310.host2016.DAOInterface;

import no.uib.smo015.info310.host2016.Poster.Poster;

import java.util.List;

/**
 * Created by Sindre on 12.10.2016.
 */
public interface DAOInterface {
    void addPoster(Poster poster);
    void connect();
    List<Poster> getAllPosters();
    List<Poster> getAllPosters(int limit);
    List<Poster> getPostersByYear(int year);
    List<Poster> getPostersByName(String name);
    void updatePosterWithDirectorAndImdbLink(Poster poster);

}
