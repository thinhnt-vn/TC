/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

import java.io.Serializable;

/**
 *
 * @author thinhnt
 */
public interface TranferFunction extends Serializable{

    double tranfer(double input);

    double derivative_net(double f);
}
