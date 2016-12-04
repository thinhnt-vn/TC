/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import java.util.ArrayList;

/**
 *
 * @author thinhnt
 */
public class DataSet extends ArrayList<Document> {

    private DataSetMetaData metaData;
    
    public DataSet getSubSet(int totalPart, int part) {
        DataSet rs = new DataSet();
        int partSize = size() / totalPart + 1;
        int startPos = (part - 1) * partSize;
        int endPos = part == totalPart ? size() : part * partSize;
        for (int i = startPos; i < endPos; i++) {
            rs.add(get(i));
        }
        return rs;
    }

    public void setMetaData(DataSetMetaData metaData){
        this.metaData = metaData;
    }

    public DataSetMetaData getMetaData() {
        return metaData;
    }
}
