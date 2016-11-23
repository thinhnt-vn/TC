/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

/**
 *
 * @author thinhnt
 */
public class Sigmoid implements TranferFunction {

    public Sigmoid() {
    }

    @Override
    public double tranfer(double input) {
        return 1.0 / (1 + Math.pow(Math.E, -input));
    }

    @Override
    public double derivative_net(double f) {
        return f * (1 - f);
    }

}
