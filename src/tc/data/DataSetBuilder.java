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
import tc.training.NeuralNetworkTrainer;
import tc.training.PredictResult;
import tc.training.TrainingModel;

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

        // Tinh tfidf4Set
        featureWords = new ArrayList<>();
        featureWords.addAll(DocumentLoader.docCount.keySet());

        DataSet sportSet = docLoader.getSportSet();
        DataSet economySet = docLoader.getEconomySet();
        DataSet lawSet = docLoader.getLawSet();
        DataSet techSet = docLoader.getTechSet();
        int totalDoc = sportSet.size() + economySet.size() + lawSet.size() + techSet.size();

        tfidf4Set(sportSet, totalDoc, DocumentLoader.docCount);
        tfidf4Set(economySet, totalDoc, DocumentLoader.docCount);
        tfidf4Set(lawSet, totalDoc, DocumentLoader.docCount);
        tfidf4Set(techSet, totalDoc, DocumentLoader.docCount);

        // Chia dl
        for (int i = 0; i < 5; i++) {
            dataSets[i].addAll(sportSet.getSubSet(5, i + 1));
            dataSets[i].addAll(economySet.getSubSet(5, i + 1));
            dataSets[i].addAll(lawSet.getSubSet(5, i + 1));
            dataSets[i].addAll(techSet.getSubSet(5, i + 1));
        }
        System.out.println("");
    }

    private void tfidf4Set(DataSet set, int totalDoc, Map<String, Integer> docCount) {
        for (Document doc : set) {
            tfidf4Doc(doc, totalDoc, docCount, featureWords);
        }
    }

    public static void tfidf4Doc(Document doc, int totalDoc,
            Map<String, Integer> docCount, List<String> featureWords) {
        System.out.println("Compute: " + doc.getName());
        int length = featureWords.size() + 1;
        double[] v = new double[length];
        v[0] = 1.0;
        Map<String, Integer> tokensInDoc = doc.getTokensInDoc();
        double s = 0;
        for (int i = 1; i < length; i++) {
            String word = featureWords.get(i - 1);
            if (!tokensInDoc.containsKey(word)) {
                v[i] = 0;
            } else {
                v[i] = tokensInDoc.get(word) * 1.0 / doc.getDocLeng()
                        * totalDoc * 1.0 / docCount.get(word);
                s += v[i] * v[i];
            }
        }
        Matrix m = new Matrix(new double[][]{v});
        m = m.times(1.0 / s);
        m.set(0, 0, 1);
        doc.setVector(m);
    }

    public DataSet[] getDataSets() {
        return dataSets;
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

        DataSet data = new DataSet();
        data.addAll(builder.getDataSets()[1]);
        data.addAll(builder.getDataSets()[2]);
        data.addAll(builder.getDataSets()[3]);
        data.addAll(builder.getDataSets()[4]);

        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer();
        TrainingModel model = trainer.train(data);
        
        Document doc = builder.getDataSets()[0].get(102);
        PredictResult rs = model.predict(doc);
        rs.getLabel().print(15, 8);
        trainer.showW1();
        trainer.showW2();
        System.out.println("Test: " + doc.getName());
        rs.getLabel().print(15, 8);
        doc.getLabel().print(15, 8);

//        testScilab();
    }

    public static void testScilab() {
        DataSet data = new DataSet();
        Document doc = new Document("1", null);
        doc.setVector(new Matrix(new double[][]{{1., 4.7, 3.2, 1.3, 0.2}}));
        doc.setLabel(new Matrix(new double[][]{{0}}));
        data.add(doc);

        doc = new Document("2", null);
        doc.setVector(new Matrix(new double[][]{{1., 6.1, 2.8, 4.7, 1.2}}));
        doc.setLabel(new Matrix(new double[][]{{1}}));
        data.add(doc);

        doc = new Document("3", null);
        doc.setVector(new Matrix(new double[][]{{1., 5.6, 3.6, 4.1, 1.3}}));
        doc.setLabel(new Matrix(new double[][]{{1}}));
        data.add(doc);

        doc = new Document("4", null);
        doc.setVector(new Matrix(new double[][]{{1., 5.8, 2.7, 5.1, 1.9}}));
        doc.setLabel(new Matrix(new double[][]{{0}}));
        data.add(doc);

        doc = new Document("5", null);
        doc.setVector(new Matrix(new double[][]{{1., 6.5, 3.2, 5.1, 2}}));
        doc.setLabel(new Matrix(new double[][]{{0}}));
        data.add(doc);

        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer();
        TrainingModel model = trainer.train(data);
        trainer.showW1();
        trainer.showW2();

        doc = new Document("tests", null);
        doc.setVector(new Matrix(new double[][]{{1., 5.8, 2.7, 3.9, 1.2}}));
        PredictResult rs = model.predict(doc);
        rs.getLabel().print(15, 8);
    }

}
