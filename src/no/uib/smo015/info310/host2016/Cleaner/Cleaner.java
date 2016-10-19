package no.uib.smo015.info310.host2016.Cleaner;

import no.uib.smo015.info310.host2016.Poster.Poster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sindremoldeklev on 13.10.2016.
 */
public class Cleaner {

    public static Map<String, Poster> cleanDuplicates(List<Poster> posters) {
        Map<String, Poster> mappedPosters = new HashMap<>();
        for(Poster poster : posters) {
            mappedPosters.put(poster.getPosterUrl(), poster);
        }
        return mappedPosters;
    }

    public static String convertToUnderscore(String element) {
        return element.trim().replace(" ", "_").replace(",", "_").replace("'", "");
    }
}
