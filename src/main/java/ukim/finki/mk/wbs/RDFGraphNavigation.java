package ukim.finki.mk.wbs;

import org.apache.jena.rdf.model.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class RDFGraphNavigation {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();

        try {
            InputStream in = new FileInputStream("src/main/java/ukim/finki/mk/wbs/lab1/info_friends.ttl");
            model.read(in, null, "TURTLE");
            in.close();

            String myURI = "https://www.facebook.com/tagar.77.tagar/";
            Resource person = model.getResource(myURI);

            String vcardNS = "http://www.w3.org/2001/vcard-rdf/3.0#";
            String foafNS = "http://xmlns.com/foaf/0.1/";

            Property fn = model.getProperty(vcardNS + "fn");
            if (person.hasProperty(fn)) {
                String fullName = person.getProperty(fn).getString();
                System.out.println("Full name: " + fullName);
            }

            Property givenName = model.getProperty(vcardNS + "given-name");
            if (person.hasProperty(givenName)) {
                String firstName = person.getProperty(givenName).getString();
                System.out.println("Name: " + firstName);
            }

            Property familyName = model.getProperty(vcardNS + "family-name");
            if (person.hasProperty(familyName)) {
                String lastName = person.getProperty(familyName).getString();
                System.out.println("Surname: " + lastName);
            }

            Property foafKnows = model.getProperty(foafNS + "knows");
            if (person.hasProperty(foafKnows)) {
                Resource friend = person.getProperty(foafKnows).getResource();
                System.out.println("Knows: " + friend.getURI());

                Property foafName = model.getProperty(foafNS + "name");
                if (friend.hasProperty(foafName)) {
                    String friendName = friend.getProperty(foafName).getString();
                    System.out.println("Friend's name: " + friendName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
