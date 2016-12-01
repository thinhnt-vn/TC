/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

import Jama.Matrix;

/**
 *
 * @author thinhnt
 */
public interface PredictResult {
    
    /**
     * 
     * @return string describe this result 
     */
    public String getDesciption();
    
    /**
     * 
     * @return the result label
     */
    public Matrix getLabel();
}
