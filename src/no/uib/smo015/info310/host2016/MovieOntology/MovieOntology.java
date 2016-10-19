package no.uib.smo015.info310.host2016.MovieOntology;

import no.uib.smo015.info310.host2016.Cleaner.Cleaner;
import no.uib.smo015.info310.host2016.NameSpaces.MovieOntologyNS;
import no.uib.smo015.info310.host2016.Poster.Poster;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by sindremoldeklev on 14.10.2016.
 */
public class MovieOntology {
    private OntModel ontModel;
    private Map<String, Poster> posters;
    private Property year, title, caption, posterUrl;

    private static final String YEAR = "year", TITLE = "title", CAPTION = "caption", POSTER_URL = "poster_url";

    public MovieOntology(OntModel ontModel, Map<String, Poster> posters) {
        this.ontModel = ontModel;
        this.posters = posters;
        createProperties();
    }

    public OntModel parsePosters(Map<String, Poster> posters) {
        OntClass posterClass = ontModel.createClass(MovieOntologyNS.NS + "poster");
        for(Poster poster : posters.values()) {

            Individual posterIndividual = posterClass.createIndividual(MovieOntologyNS.NS + Cleaner.convertToUnderscore(poster.getTitle()));

            DatatypeProperty titleProp = ontModel.createDatatypeProperty(MovieOntologyNS.NS + "title");
                    titleProp.addDomain(posterClass);
                    titleProp.addRange(XSD.xstring);

            DatatypeProperty captionProp = ontModel.createDatatypeProperty(MovieOntologyNS.NS + "caption");
                    captionProp.addDomain(posterClass);
                    captionProp.addRange(XSD.xstring);

            DatatypeProperty urlProp = ontModel.createDatatypeProperty(MovieOntologyNS.NS + "posterUrl");
                    urlProp.addDomain(posterClass);
                    urlProp.addRange(XSD.xstring);

            DatatypeProperty largePosterUrlProp = ontModel.createDatatypeProperty(MovieOntologyNS.NS + "largePosterUrl");
                             largePosterUrlProp.addDomain(posterClass);
                             largePosterUrlProp.addRange(XSD.xstring);

            DatatypeProperty yearProp = ontModel.createDatatypeProperty(MovieOntologyNS.NS + "year");
                    yearProp.addDomain(posterClass);
                    yearProp.addRange(XSD.integer);


            Literal titleLiteral = ontModel.createTypedLiteral(poster.getTitle());
            Literal captionLiteral = ontModel.createTypedLiteral(poster.getCaption());
            Literal urlLiteral = ontModel.createTypedLiteral(poster.getPosterUrl());
            if(poster.getLargePosterUrl() != null) {
                Literal largePosterUrlLiteral = ontModel.createTypedLiteral(poster.getLargePosterUrl());
                posterIndividual.addProperty(largePosterUrlProp, largePosterUrlLiteral);
            }
            Literal yearLiteral = ontModel.createTypedLiteral(poster.getYear());

            posterIndividual.addProperty(titleProp, titleLiteral);
            posterIndividual.addProperty(captionProp, captionLiteral);
            posterIndividual.addProperty(urlProp, urlLiteral);
            posterIndividual.addProperty(yearProp, yearLiteral);
            posterIndividual.addProperty(RDFS.label, poster.getTitle());


            try {
                posterIndividual.addProperty(OWL.sameAs, MovieOntologyNS.DBPEDIA + URLEncoder.encode(poster.getTitle(), "UTF-8").replace("+", "_"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return this.ontModel;
    }

    private void createProperties() {
        this.year = (Property) this.ontModel.createProperty(MovieOntologyNS.NS + MovieOntology.YEAR)
                .addProperty(RDFS.domain, MovieOntologyNS.MOVIE)
                .addProperty(RDFS.range, XSD.integer)
                .addProperty(RDF.type, OWL.DatatypeProperty);

        this.title = (Property) this.ontModel.createProperty(MovieOntologyNS.NS + MovieOntology.TITLE)
                .addProperty(RDFS.domain, MovieOntologyNS.MOVIE)
                .addProperty(RDFS.range, XSD.xstring)
                .addProperty(RDF.type, OWL.FunctionalProperty)
                .addProperty(RDF.type, OWL.DatatypeProperty);

        this.caption = (Property) ontModel.createProperty(MovieOntologyNS.NS + MovieOntology.CAPTION)
                .addProperty(RDFS.domain, MovieOntologyNS.MOVIE)
                .addProperty(RDFS.range, XSD.xstring)
                .addProperty(RDF.type, OWL.FunctionalProperty)
                .addProperty(RDF.type, OWL.DatatypeProperty);


        this.posterUrl = (Property) ontModel.createProperty(MovieOntologyNS.NS + MovieOntology.POSTER_URL)
                .addProperty(RDFS.domain, MovieOntologyNS.MOVIE)
                .addProperty(RDFS.range, XSD.xstring)
                .addProperty(RDF.type, OWL.FunctionalProperty)
                .addProperty(RDF.type, OWL.InverseFunctionalProperty)
                .addProperty(RDF.type, OWL.DatatypeProperty);


    }

    public void prefixMapping(Map<String, String> prefixes) {
        for(Map.Entry<String, String> entry : prefixes.entrySet()) {
            this.ontModel.setNsPrefix(entry.getKey(), entry.getValue());
        }
    }

    public OntModel getOntModel() {
        return ontModel;
    }

    public void setOntModel(OntModel ontModel) {
        this.ontModel = ontModel;
    }
}
