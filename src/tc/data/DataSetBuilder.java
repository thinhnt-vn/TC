/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import Jama.Matrix;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Build the files to datasets contain vectors for training
 *
 * @author thinhnt
 */
public class DataSetBuilder {

    public final static String DATA_PATH = "data";
    private String dataPath;
    private DataSet[] dataSets;
    private Predicate<File> fileFilter;
    private List<String> featureWords;

    public DataSetBuilder(String dataDir, Predicate<File> fileFilter) {
        this.dataPath = dataDir;
        this.dataSets = new DataSet[5];
        this.fileFilter = fileFilter;
        for (int i = 0; i < 5; i++) {
            dataSets[i] = new DataSet();
        }
    }

    public void build() {
        File dataDir = new File(dataPath);
        DocumentLoader docLoader = new DocumentLoader(new TCAnalyzer());
        for (File f : dataDir.listFiles()) {
            if (fileFilter.test(f)) {
                try {
                    Document doc = new Document(f.getName(), new BufferedReader(new FileReader(f)));
                    docLoader.load(doc);
                    System.out.println("Load: " + doc.getName());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DataSetBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        // Tinh tfidf
        featureWords = new ArrayList<>();
        featureWords.addAll(DocumentLoader.docCount.keySet());

        DataSet sportSet = docLoader.getSportSet();
        DataSet economySet = docLoader.getEconomySet();
        DataSet lawSet = docLoader.getLawSet();
        DataSet techSet = docLoader.getTechSet();
        int totalDoc = sportSet.size() + economySet.size() + lawSet.size() + techSet.size();

        tfidf(sportSet, totalDoc, DocumentLoader.docCount);
        tfidf(economySet, totalDoc, DocumentLoader.docCount);
        tfidf(lawSet, totalDoc, DocumentLoader.docCount);
        tfidf(techSet, totalDoc, DocumentLoader.docCount);

        // Chia dl
        for (int i = 0; i < 5; i++) {
            dataSets[i].addAll(sportSet.getSubSet(5, i + 1));
            dataSets[i].addAll(economySet.getSubSet(5, i + 1));
            dataSets[i].addAll(lawSet.getSubSet(5, i + 1));
            dataSets[i].addAll(techSet.getSubSet(5, i + 1));
        }
        System.out.println("");
    }

    private void tfidf(DataSet set, int totalDoc, Map<String, Integer> docCount) {
        int length = featureWords.size();
        for (Document doc : set) {
            System.out.println("Compute: " + doc.getName());
            double[] v = new double[length];
            Map<String, Integer> tokensInDoc = doc.getTokensInDoc();
            for (int i = 0; i < length; i++) {
                String word = featureWords.get(i);
                if (!tokensInDoc.containsKey(word)) {
                    v[i] = 0;
                } else {
                    v[i] = tokensInDoc.get(word) * 1.0 / doc.getDocLeng()
                            * totalDoc * 1.0 / docCount.get(word);
                }
            }
            doc.setVector(new Matrix(new double[][]{v}));
        }
    }

    public static void main(String[] args) {
        DataSetBuilder builder = new DataSetBuilder(DATA_PATH, new Predicate<File>() {

            @Override
            public boolean test(File t) {
                String name = t.getName();
                return name.endsWith(".tt")
                        || name.endsWith(".kt")
                        || name.endsWith(".pl")
                        || name.endsWith(".cn");
            }
        });
        builder.build();

    }

}
