/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import Jama.Matrix;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author thinhnt
 */
public class Document {

    private String name;
    private BufferedReader reader;
    private Map<String, Integer> tokensInDoc;
    private int docLength;
    private Matrix label;
    private Matrix v;

    public Document(String name, BufferedReader reader) {
        this.name = name;
        this.reader = reader;
        this.tokensInDoc = new HashMap<>();
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String getName() {
        return name;
    }
    
    public int getDocLeng(){
        return docLength;
    }
    
    public Map<String, Integer> getTokensInDoc(){
        return tokensInDoc;
    }
    
    public void setVector(Matrix v){
        this.v = v;
    }
    
    public void addToken(String token) {
        docLength++;
        int oldVal = tokensInDoc.containsKey(token) ? tokensInDoc.get(token) : 0;
        if (oldVal == 0) {
            DocumentLoader.increaseDocCount(token);
        }
        tokensInDoc.put(token, oldVal + 1);
    }

    public void setLabel(Matrix label) {
        this.label = label;
    }
}
