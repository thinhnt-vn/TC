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
    
}
