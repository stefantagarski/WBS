package ukim.finki.mk.wbs;

import org.apache.jena.rdf.model.*;

public class RDFGraphCreation  {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();

        String vcardNS = "http://www.w3.org/2001/vcard-rdf/3.0#";
        String foafNS = "http://xmlns.com/foaf/0.1/";

        String myURI = "https://www.facebook.com/tagar.77.tagar/";
        Resource person = model.createResource(myURI);

        Property fn = model.createProperty(vcardNS + "fn");
        person.addProperty(fn, "Stefan Tagarski");

        Property givenName = model.createProperty(vcardNS + "given-name");
        Property familyName = model.createProperty(vcardNS + "family-name");
        Property email = model.createProperty(vcardNS + "email");
        Property nickname = model.createProperty(vcardNS + "nickname");

        Property foafName = model.createProperty(foafNS + "name");
        Property foafAge = model.createProperty(foafNS + "age");
        Property foafGender = model.createProperty(foafNS + "gender");
        Property foafKnows = model.createProperty(foafNS + "knows");

        person.addProperty(givenName, "Stefan");
        person.addProperty(familyName, "Tagarski");
        person.addProperty(email, "stefan@tagarski.com");
        person.addProperty(nickname, "tagar");
        person.addProperty(foafName, "Stefan");
        person.addProperty(foafAge, "22");
        person.addProperty(foafGender, "male");

        Resource friend = model.createResource("https://www.linkedin.com/in/david-hristov-0985a4176/");
        person.addProperty(foafKnows, friend);
        friend.addProperty(foafName, "David Hristov");


        System.out.println("Printing with model.listStatements():");
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            System.out.print(subject.getURI() + " - ");
            System.out.print(predicate.getURI() + " - ");

            if (object instanceof Resource) {
                System.out.println(((Resource) object).getURI());
            } else {
                System.out.println("\"" + object.toString() + "\"");
            }
        }

        System.out.println("\n\nPrinting with model.write(), in RDF/XML:");
        model.write(System.out, "RDF/XML");

        System.out.println("\n\nPrinting with model.write(), in RDF/XML-ABBREV:");
        model.write(System.out, "RDF/XML-ABBREV");

        System.out.println("\n\nPrinting with model.write(), in N-TRIPLES:");
        model.write(System.out, "N-TRIPLES");

        System.out.println("\n\nPrinting with model.write(), in Turtle:");
        model.write(System.out, "TURTLE");
    }
}