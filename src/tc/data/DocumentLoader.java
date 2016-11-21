/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import Jama.Matrix;
import java.util.HashMap;
import java.util.Map;

/**
 * Load data to document and count the words in document
 *
 * @author thinhnt
 */
public class DocumentLoader {

    public static Map<String, Integer> docCount = new HashMap<>();
    private final static double[][] KT = {{0, 1.0, 0, 0}};
    private final static double[][] PL = {{0, 0, 1.0, 0}};
    private final static double[][] CN = {{0, 0, 0, 1.0}};
    private Analyzer analyzer;
    public DataSet sportSet;
    public DataSet economySet;
    public DataSet lawSet;
    public DataSet techSet;
    private static double[][] TT = {{1.0, 0, 0, 0}};

    public DocumentLoader(Analyzer analyzer) {
        this.analyzer = analyzer;
        this.sportSet = new DataSet();
        this.economySet = new DataSet();
        this.lawSet = new DataSet();
        this.techSet = new DataSet();
    }

    public void load(Document doc) {
        TokenStream ts = analyzer.tokenStream(doc.getReader());
        String fileExt = doc.getName().split("\\.")[1];
        Matrix label = null;
        DataSet addedSet = null;
        switch (fileExt) {
            case "tt":
                label = new Matrix(TT);
                addedSet = sportSet;
                break;
            case "kt":
                label = new Matrix(KT);
                addedSet = economySet;
                break;
            case "pl":
                label = new Matrix(PL);
                addedSet = lawSet;
                break;
            case "cn":
                label = new Matrix(CN);
                addedSet = techSet;
        }

        doc.setLabel(label);
        String token = null;
        while ((token = ts.increaseToken()) != null) {
            doc.addToken(token);
        }
        addedSet.add(doc);
    }
    
    public static void increaseDocCount(String token){
        int oldVal = docCount.containsKey(token) ? docCount.get(token) : 0;
        docCount.put(token, oldVal + 1);
    }

    public DataSet getSportSet() {
        return sportSet;
    }

    public DataSet getEconomySet() {
        return economySet;
    }

    public DataSet getLawSet() {
        return lawSet;
    }

    public DataSet getTechSet() {
        return techSet;
    }
}
