package ukim.finki.mk.wbs;

import org.apache.jena.rdf.model.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class RDFGraphReading {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();

        try {
            InputStream in = new FileInputStream("src/main/java/ukim/finki/mk/wbs/lab1/info_friends.ttl");
            model.read(in, null, "TURTLE");
            in.close();

            System.out.println("Reading model from file input:");
            model.write(System.out, "TURTLE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}