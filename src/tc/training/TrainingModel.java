/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

import java.io.Serializable;
import tc.data.Document;

/**
 *
 * @author thinhnt
 */
public interface TrainingModel extends Serializable{
    
    PredictResult predict(Document doc);
    
    String getDescription();
    
    void setDescription(String des);
    
}
