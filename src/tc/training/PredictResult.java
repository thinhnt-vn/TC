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
     * @return string describe 
     */
    public String str();
    
    /**
     * 
     * @return the result label
     */
    public Matrix getLabel();
}
