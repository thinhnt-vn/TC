/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

import tc.data.Utils;
import Jama.Matrix;
import tc.data.DataSetMetaData;
import tc.data.Document;

/**
 *
 * @author thinhnt
 */
public class NeuralNetworkModel implements TrainingModel {

    private DataSetMetaData metaData;
    private Matrix w1;
    private Matrix w2;
    private TranferFunction tranferFunction;
    private String desciption;

    public NeuralNetworkModel(Matrix w1, Matrix w2, TranferFunction tranferFunction, DataSetMetaData metaData) {
        this.w1 = w1;
        this.w2 = w2;
        this.tranferFunction = tranferFunction;
        this.metaData = metaData;
    }

    @Override
    public PredictResult predict(Document doc) {
        Matrix outL1 = doc.getVector(); // Output of layer 1(input of network)
        Matrix outL2 = Utils.applyFunction(outL1.times(w1), // output of layer 2
                tranferFunction::tranfer);
//        outL2.set(0, 0, 1);
//        final Matrix outL3 = Utils.applyFunction(outL2.times(w2), // output of layer 3 (output of network)
//                tranferFunction::tranfer);
        return new PredictResult() {

            @Override
            public String getDesciption() {
                int maxIndex = Utils.max(outL2.getArray()[0]);
                switch (maxIndex) {
                    case 0:
                        return "Thể Thao";
                    case 1:
                        return "Kinh Tế";
                    case 2:
                        return "Pháp Luật";
                }
                return "Côn nghệ";
            }

            @Override
            public Matrix getLabel() {
                return outL2;
            }
        };
    }

    public void setDescription(String des) {
        this.desciption = des;
    }

    @Override
    public String getDescription() {
        return desciption;
    }

    public DataSetMetaData getMetaData() {
        return metaData;
    }
}
