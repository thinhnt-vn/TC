/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

import Jama.Matrix;
import tc.data.Document;

/**
 *
 * @author thinhnt
 */
public class NeuralNetworkModel implements TrainingModel {

    private Matrix w1;
    private Matrix w2;
    private TranferFunction tranferFunction;

    public NeuralNetworkModel(Matrix w1, Matrix w2, TranferFunction tranferFunction) {
        this.w1 = w1;
        this.w2 = w2;
        this.tranferFunction = tranferFunction;
    }

    @Override
    public PredictResult predict(Document doc) {
        Matrix outL1 = doc.getVector(); // Output of layer 1(input of network)
        Matrix outL2 = Utils.applyFunction(outL1.times(w1), // output of layer 2
                tranferFunction::tranfer);
        outL2.set(0, 0, 1);
        final Matrix outL3 = Utils.applyFunction(outL2.times(w2), // output of layer 3 (output of network)
                tranferFunction::tranfer);
        return new PredictResult() {
            
            @Override
            public String str() {
                return null;
            }

            @Override
            public Matrix getLabel() {
                return outL3;
            }
        };
    }

}
