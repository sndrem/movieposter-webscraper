package no.uib.smo015.info310.host2016.NameSpaces;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

/**
 * Created by sindremoldeklev on 14.10.2016.
 */
public class MovieOntologyNS {
    private static Model model = ModelFactory.createDefaultModel();
    public static final String NS = "http://uib.no/info310/movieOntology#";
    public static final String LMD = "http://data.linkedmdb.org/resource/movie/";
    public static final String DBPEDIA = "http://dbpedia.org/resource/";
    public static final Resource MOVIE = model.createResource(MovieOntologyNS.NS + "poster");
}
