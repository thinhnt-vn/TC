/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import Jama.Matrix;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import tc.training.Function;

/**
 *
 * @author thinhnt
 */
public class Utils {
    
    /**
     * apply a function to each element of matrix
     * @param input - imput maxtrix
     * @param f - function to apply
     * @return 
     */
    public static Matrix applyFunction(Matrix input, Function f) {
        Matrix rs = input.copy();
        int row = rs.getRowDimension();
        int col = rs.getColumnDimension();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                rs.set(i, j, f.f(input.get(i, j)));
            }
        }
        return rs;
    }
    
    public static String getFileExt(String fileName){
        int dotPos = fileName.lastIndexOf(".");
        if (dotPos == fileName.length() - 1){
            return "";
        }
        return fileName.substring(dotPos + 1);
    }
    
    public static void tfidf4Doc(Document doc, int totalDoc,
            Map<String, Integer> docCount, List<String> featureWords) {
        System.out.println("Compute: " + doc.getName());
        int length = featureWords.size() + 1;
        double[] v = new double[length];
        v[0] = 1.0;
        Map<String, Integer> tokensInDoc = doc.getTokensInDoc();
        double max = 0;
        Iterator<String> tokens = tokensInDoc.keySet().iterator();
        while (tokens.hasNext()) {
            String next = tokens.next();
            int val = tokensInDoc.get(next);
            if (max < val) {
                max = val;
            }
        }

        double s = 0;
        for (int i = 1; i < length; i++) {
            String word = featureWords.get(i - 1);
            if (!tokensInDoc.containsKey(word)) {
                v[i] = 0;
            } else {
                v[i] = tokensInDoc.get(word) * 1.0 / max
                        * Math.log10(totalDoc * 1.0 / docCount.get(word));
                s += v[i];
            }
        }
        Matrix m = new Matrix(new double[][]{v});
        m.times(1.0/s);
        m.set(0, 0, 1);
        doc.setVector(m);
    }
    
    public static String getTime(){
        String fm = "yyy-MM-ddd HH-mm-ss";
        DateFormat df = new SimpleDateFormat(fm);
        
        return  df.format(new Date());
    }
    
    /**
     * return index of max value in a array
     * @param arr
     * @return 
     */
    public static int max(double [] arr){
        int maxIndex = 0;
        double maxValue = arr[maxIndex];
        
        for (int i = 1; i < arr.length; i++) {
            if (maxValue < arr[i]){
                maxValue = arr[i];
                maxIndex = i;
            }
        }
        
        return maxIndex;
    }
  
}
