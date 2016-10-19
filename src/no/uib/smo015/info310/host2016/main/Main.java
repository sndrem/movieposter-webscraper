package no.uib.smo015.info310.host2016.main;

import no.uib.smo015.info310.host2016.Cleaner.Cleaner;
import no.uib.smo015.info310.host2016.DAO.PosterDAO;
import no.uib.smo015.info310.host2016.MovieOntology.MovieOntology;
import no.uib.smo015.info310.host2016.NameSpaces.MovieOntologyNS;
import no.uib.smo015.info310.host2016.Poster.Poster;
import no.uib.smo015.info310.host2016.scraper.PosterScraper;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sindre on 12.10.2016.
 */
public class Main {
    private static final String URL = "http://www.impawards.com/2016/alpha1.html";
    private static final String GALLERY_URL = "http://www.impawards.com/gallery.html";

    public static void main(String[] args) {
        PosterDAO dao = PosterDAO.getInstance();

        PosterScraper scraper = new PosterScraper("http://www.impawards.com/", new ArrayList<>());
//        int[] years = new int[]{2015, 2014, 2013, 2012, 2011, 2010};
//        for(int year : years) {
//            List<Poster> posters = scraper.parseSite(Main.GALLERY_URL, year);
//            posters.forEach(poster -> dao.addPoster(poster));
//        }
//
//        int[] years = new int[]{2016, 2015, 2014, 2013, 2012, 2011, 2010, 2009, 2008};
//        for (int year : years) {
//
//        }
//        List<Poster> posters = scraper.parseSite(Main.GALLERY_URL, false);
//        posters.forEach(poster -> dao.updatePosterWithDirectorAndImdbLink(poster));
        Map<String, Poster> posters = Cleaner.cleanDuplicates(dao.getAllPosters());
        posters.values().forEach(p -> System.out.println(p.getCaption()));


        MovieOntology ontology = new MovieOntology(ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF), posters);
        Map<String, String> prefixes = new HashMap<>();
        prefixes.put("poster", MovieOntologyNS.NS);
        prefixes.put("xsd", XSD.getURI());
        prefixes.put("rdfs", RDFS.getURI());
        prefixes.put("rdf", RDF.getURI());

        ontology.prefixMapping(prefixes);
        ontology.parsePosters(posters);
        ontology.getOntModel().write(System.out, "TURTLE");
        try {
            ontology.getOntModel().write(new FileOutputStream("posters.ttl"), "TURTLE");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
