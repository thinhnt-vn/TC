/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

import tc.data.DataSet;

/**
 *
 * @author thinhnt
 */
public interface Trainable {
    
    TrainingModel train(DataSet dataSet);
}
