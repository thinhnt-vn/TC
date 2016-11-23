/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.training;

import Jama.Matrix;
import tc.data.DataSet;
import tc.data.Document;

/**
 *
 * @author thinhnt
 */
public class NeuralNetworkTrainer implements Trainable {

    protected double threshold;
    private double e;   // Loi
    private double n; // Toc do hoc
    protected TranferFunction tranferFunction;
    private Matrix w1;
    private Matrix w2;

    public NeuralNetworkTrainer() {
        threshold = 750;
        e = 0;
//        n = 0.1;
        n = 0.5;
        tranferFunction = new Sigmoid();
    }

    @Override
    public TrainingModel train(DataSet dataSet) {
        int rowW1 = dataSet.get(0).getVector().getColumnDimension();
//        w1 = new Matrix(new double[][]{{0.1,0,0},{0.2,0.1,-0.1}, {0.3,-0.1,0.2}, {0.4,-0.1,0.1}, {0.5, 0,-0.1}});
//        w2 = new Matrix(new double[][]{{0.1},{0.2},{-0.1}});
        w1 = Matrix.random(rowW1, 5);
        w2 = Matrix.random(5, 4);
        do {
            e = 0;
            for (Document doc : dataSet) {
                System.out.println("Train: " + doc.getName());
                Matrix outL1 = doc.getVector(); // Output of layer 1(input of network)
                Matrix outL2 = Utils.applyFunction(outL1.times(w1), // output of layer 2
                        tranferFunction::tranfer);
                outL2.set(0, 0, 1);
                Matrix outL3 = Utils.applyFunction(outL2.times(w2), // output of layer 3 (output of network)
                        tranferFunction::tranfer);
                
                Matrix delta = outL3.plus(doc.getLabel());
                e += 0.5 * delta.times(delta.transpose()).get(0, 0);

                Matrix composedErrL3 = doc.getLabel().minus(outL3);//1x4
                Matrix errL3 = err(outL3, composedErrL3);//1x4

                Matrix composedErrL2 = w2.times(errL3.transpose());//5x1
                Matrix errL2 = err(outL2, composedErrL2.transpose());//1x5
//            errL2.set(0, 0, 0);

                Matrix deltaW1 = deltaW(outL1, errL2);
                w1.plusEquals(deltaW1);

                Matrix deltaW2 = deltaW(outL2, errL3);
                w2.plusEquals(deltaW2);
            }
            System.out.println("Ket thuc chu ky: E = " + e);
        } while (e >= threshold);

        return new NeuralNetworkModel(w1, w2, tranferFunction);
    }

    /**
     * Mesuare the error of layer
     *
     * @param out - vector contain output of this layer's neural
     * @param composedErr - vector contain error that composed from err of next
     * layer
     * @return
     */
    private Matrix err(Matrix out, Matrix composedErr) {
        Matrix rs = out.copy();
        int row = out.getRowDimension();
        int col = out.getColumnDimension();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                double val = composedErr.get(i, j)
                        * tranferFunction.derivative_net(out.get(i, j));
                rs.set(i, j, val);
            }
        }
        return rs;
    }

    /**
     * Compute deltaW of matrix between 2 layer
     *
     * @param out - vector contain outputs of left layer 's neurals
     * @param errNext - vector contain error values of right layer 's neurals
     * @return
     */
    private Matrix deltaW(Matrix out, Matrix errNext) {
        int row = out.getColumnDimension();
        int col = errNext.getColumnDimension();
        Matrix rs = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                double val = out.get(0, i) * errNext.get(0, j) * n;
                rs.set(i, j, val);
            }
        }
        return rs;
    }

    public void showW1() {
        w1.print(15, 8);
    }

    public void showW2() {
        w2.print(15, 8);
    }
}
