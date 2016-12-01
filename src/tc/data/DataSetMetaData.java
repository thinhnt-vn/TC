/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thinhnt
 */
public class DataSetMetaData implements Serializable{
    private List<String> featureWords;
    private Map<String, Integer> docCount;
    private int totalDoc;

    public DataSetMetaData(List<String> featureWords,int totalDoc, Map<String, Integer> docCount) {
        this.featureWords = featureWords;
        this.totalDoc = totalDoc;
        this.docCount = docCount;
    }

    public List<String> getFeatureWords() {
        return featureWords;
    }

    public void setFeatureWords(List<String> featureWords) {
        this.featureWords = featureWords;
    }

    public Map<String, Integer> getDocCount() {
        return docCount;
    }

    public void setDocCount(Map<String, Integer> docCount) {
        this.docCount = docCount;
    }

    public int getTotalDoc() {
        return totalDoc;
    }
}
