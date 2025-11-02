package ukim.finki.mk.wbs;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class HIFMDatasetAnalysis {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();

        try {
            InputStream in = new FileInputStream("src/main/java/ukim/finki/mk/wbs/lab1/hifm-dataset.ttl");
            model.read(in, null, "TURTLE");
            in.close();

            String hifmOntNS = "http://purl.org/net/hifm/ontology#";

            System.out.println("=== Сите лекови (по азбучен редослед) ===");
            Set<String> drugNames = new TreeSet<>();

            StmtIterator iter = model.listStatements(null, RDFS.label, (RDFNode) null);
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                if (stmt.getObject().isLiteral()) {
                    drugNames.add(stmt.getObject().asLiteral().getString());
                }
            }

            for (String name : drugNames) {
                System.out.println(name);
            }
            System.out.println("Вкупно: " + drugNames.size() + " уникатни лекови\n");

            System.out.println("=== Сите релации за избран лек ===");
            Resource selectedDrug = null;
            StmtIterator drugIter = model.listStatements(null, RDFS.label, (RDFNode) null);
            while (drugIter.hasNext()) {
                Statement stmt = drugIter.nextStatement();
                if (stmt.getObject().isLiteral() &&
                        stmt.getObject().asLiteral().getString().equals("Fluconazole")) {
                    selectedDrug = stmt.getSubject();
                    break;
                }
            }

            if (selectedDrug != null) {
                System.out.println("Лек: " + selectedDrug.getURI());
                StmtIterator propIter = selectedDrug.listProperties();
                while (propIter.hasNext()) {
                    Statement stmt = propIter.nextStatement();
                    System.out.println("  " + stmt.getPredicate().getLocalName() + ": " + stmt.getObject());
                }
            }

            System.out.println("\n=== Слични лекови ===");
            Property similarTo = model.getProperty(hifmOntNS + "similarTo");

            if (selectedDrug != null && selectedDrug.hasProperty(RDFS.label)) {
                String drugName = selectedDrug.getProperty(RDFS.label).getString();
                System.out.println("Лекови слични на: " + drugName);

                int count = 0;
                StmtIterator simIter = selectedDrug.listProperties(similarTo);
                while (simIter.hasNext()) {
                    Statement stmt = simIter.nextStatement();
                    if (stmt.getObject().isResource()) {
                        Resource similarDrug = stmt.getResource();
                        if (similarDrug.hasProperty(RDFS.label)) {
                            count++;
                            System.out.println("  " + count + ". " + similarDrug.getProperty(RDFS.label).getString());
                        }
                    }
                }

                if (count == 0) {
                    System.out.println("  (Нема слични лекови)");
                }
            }

            System.out.println("\n=== Споредба на цени ===");
            Property refPrice = model.getProperty(hifmOntNS + "refPriceWithVAT");

            if (selectedDrug != null) {
                String drugName = selectedDrug.getProperty(RDFS.label).getString();
                System.out.println("Лек: " + drugName);

                if (selectedDrug.hasProperty(refPrice)) {
                    String price = selectedDrug.getProperty(refPrice).getString();
                    System.out.println("Цена: " + price + " МКД");
                } else {
                    System.out.println("Цена: Нема податок");
                }

                System.out.println("\nСлични лекови и нивни цени:");
                StmtIterator simIter = selectedDrug.listProperties(similarTo);
                int count = 0;

                while (simIter.hasNext()) {
                    Statement stmt = simIter.nextStatement();
                    if (stmt.getObject().isResource()) {
                        Resource similarDrug = stmt.getResource();

                        if (similarDrug.hasProperty(RDFS.label)) {
                            count++;
                            String simName = similarDrug.getProperty(RDFS.label).getString();
                            System.out.print("  " + count + ". " + simName);

                            if (similarDrug.hasProperty(refPrice)) {
                                String simPrice = similarDrug.getProperty(refPrice).getString();
                                System.out.println(": " + simPrice + " МКД");
                            } else {
                                System.out.println(": Нема податок за цена");
                            }
                        }
                    }
                }

                if (count == 0) {
                    System.out.println("  (Нема слични лекови)");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}