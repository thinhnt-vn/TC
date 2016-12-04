/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import Jama.Matrix;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import tc.assessment.AssessmentResult;
import tc.assessment.CrossValidateAssessor;
import tc.constances.TCConstances;
import tc.training.NeuralNetworkTrainer;
import tc.training.PredictResult;
import tc.training.TrainingModel;

/**
 * Build the files to datasets contain vectors for training
 *
 * @author thinhnt
 */
public class DataSetBuilder implements Serializable {

    private File dataDir;
    private DataSet[] dataSets;
    transient private Predicate<File> fileFilter;
    private List<String> featureWords;

    public DataSetBuilder(String dataDir, Predicate<File> fileFilter) {
        this.dataDir = new File(dataDir);
        this.dataSets = new DataSet[5];
        this.fileFilter = fileFilter;
        for (int i = 0; i < 5; i++) {
            dataSets[i] = new DataSet();
        }
    }

    public void build() {
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
            Utils.tfidf4Doc(doc, totalDoc, docCount, featureWords);
        }
    }

    public List<String> getFeatureWords() {
        return featureWords;
    }

    public DataSet[] getDataSets() {
        return dataSets;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        DataSetBuilder builder = new DataSetBuilder(TCConstances.Path.DATA_PATH, new Predicate<File>() {
//
//            @Override
//            public boolean test(File t) {
//                String name = t.getName();
//                return name.endsWith(".tt")
//                        || name.endsWith(".kt")
//                        || name.endsWith(".pl")
//                        || name.endsWith(".cn");
//            }
//        });
//        builder.build();
//
//        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("databuider"));
//        stream.writeObject(builder);
//        stream.close();

        ObjectInputStream stream = new ObjectInputStream(new FileInputStream("databuider"));
        DataSetBuilder builder = (DataSetBuilder) stream.readObject();
        stream.close();

        CrossValidateAssessor assessor = new CrossValidateAssessor(builder.getDataSets(), new NeuralNetworkTrainer(1200));
        AssessmentResult rs = assessor.assess();

        

//        DataSet data = new DataSet();
//        data.addAll(builder.getDataSets()[1]);
//        data.addAll(builder.getDataSets()[2]);
//        data.addAll(builder.getDataSets()[3]);
//        data.addAll(builder.getDataSets()[4]);
////        data.sort(new Comparator<Document>() {
////
////            @Override
////            public int compare(Document o1, Document o2) {
////                String firstExt = Utils.getFileExt(o1.getName());
////                String secondsExt = Utils.getFileExt(o2.getName());
////                return firstExt.compareTo(secondsExt);
////            }
////        });
//
//        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(1200);
//        TrainingModel model = trainer.train(data);
//
//        Document doc = builder.getDataSets()[1].get(70);
//        PredictResult rs = model.predict(doc);
////        trainer.showW1();
////        trainer.showW2();
//        System.out.println("Test: " + doc.getName());
//        rs.getLabel().print(15, 8);
//        doc.getLabel().print(15, 8);
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

        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(5);
        TrainingModel model = trainer.train(data);
        trainer.showW1();
        trainer.showW2();

        doc = new Document("tests", null);
        doc.setVector(new Matrix(new double[][]{{1., 5.8, 2.7, 3.9, 1.2}}));
        PredictResult rs = model.predict(doc);
        rs.getLabel().print(15, 8);
    }

}
