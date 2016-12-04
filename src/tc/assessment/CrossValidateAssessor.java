/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.assessment;

import tc.data.DataSet;
import tc.data.Document;
import tc.data.Utils;
import tc.training.PredictResult;
import tc.training.Trainable;
import tc.training.TrainingModel;

/**
 *
 * @author thinhnt
 */
public class CrossValidateAssessor {

    public final static int PRECISION_INDEX = 0;
    public final static int RECALL_INDEX = 1;
    public final static int TP_INDEX = 0;
    public final static int FP_INDEX = 1;
    public final static int FN_INDEX = 2;
    private DataSet[] dataSet;
    private Trainable trainer;

    public CrossValidateAssessor(DataSet[] dataSet, Trainable trainer) {
        this.dataSet = dataSet;
        this.trainer = trainer;
    }

    /**
     *
     * @return precision and recall value. That is average of the values on all
     * of times assess
     */
    public AssessmentResult assess() {
        int length = dataSet.length;
        double[][][] tmp = new double[length][][]; // contain precision and recall value from length times assess
        for (int i = 0; i < length; i++) {
            DataSet trainingSet = getTrainingSet(i);
            double[][] pr = assess(dataSet[i], trainingSet);
            tmp[i] = pr;
        }

        double[][] rs = new double[2][dataSet[0].size()];
        for (int i = 0; i < rs[0].length; i++) {
            for (int j = 0; j < length; j++) {
                rs[PRECISION_INDEX][i] += tmp[j][PRECISION_INDEX][i];
                rs[RECALL_INDEX][i] += tmp[j][RECALL_INDEX][i];
            }
            if (i >= dataSet[dataSet.length - 1].size()) {
                rs[PRECISION_INDEX][i] /= (length - 1);
                rs[RECALL_INDEX][i] /= (length - 1);
            } else {
                rs[PRECISION_INDEX][i] /= length;
                rs[RECALL_INDEX][i] /= length;
            }
        }
        return new AssessmentResult() {

            @Override
            public double[] getPrecision() {
                return rs[PRECISION_INDEX];
            }

            @Override
            public double[] getRecall() {
                return rs[RECALL_INDEX];
            }
        };
    }

    /**
     *
     * @param testData
     * @param trainingData
     * @return array of precision and recall values. array[0] contain precision
     * values, array[1] contain recall values. Each of these values is result
     * assessment when we increase the test data 'size
     */
    private double[][] assess(DataSet testData, DataSet trainingData) {
        TrainingModel model = trainer.train(trainingData);
        double[][] rs = new double[2][dataSet[0].size()];
        int length = testData.size();
        int[][][] tmp = new int[testData.get(0).getLabel().getColumnDimension()][3][length];

        double[][][] pr = new double[testData.get(0).getLabel().getColumnDimension()][2][length];
        count(testData, 0, model, tmp);
        computeP(tmp, pr, rs, 0);

        for (int i = 1; i < length; i++) {
            for (int j = 0; j < tmp.length; j++) {
                for (int k = 0; k < tmp[0].length; k++) {
                    tmp[j][k][i] = tmp[j][k][i - 1];
                }
            }
            count(testData, i, model, tmp);
            computeP(tmp, pr, rs, i);
        }

        for (int i = 0; i < length; i++) {
            computeR(tmp, pr, rs, i);
        }

        int tp = 0;
        int fp = 0;
        int fn = 0;
        for (int i = 0; i < tmp.length; i++) {
            tp += tmp[i][TP_INDEX][tmp[i][TP_INDEX].length - 1];
            fp += tmp[i][FP_INDEX][tmp[i][FP_INDEX].length - 1];
            fn += tmp[i][FN_INDEX][tmp[i][FN_INDEX].length - 1];
        }
        int tn = testData.size() - tp - fp - fn;
        
        System.out.println("Acc: " + (tp + tn) * 1.0 / testData.size());

        return rs;

    }

    /**
     * Compute tp, fp, fn values for data set from 0 to i
     *
     * @param teDataSet
     * @param i
     * @param model
     * @param tmp
     */
    private void count(DataSet testDataSet, int i, TrainingModel model, int[][][] tmp) {
        Document doc = testDataSet.get(i);
        PredictResult result = model.predict(doc);
        int desireIndex = Utils.max(doc.getLabel().getArray()[0]);
        int resultIndex = Utils.max(result.getLabel().getArray()[0]);
        if (desireIndex == resultIndex) {
            tmp[desireIndex][TP_INDEX][i] += 1;
        } else {
            tmp[desireIndex][FN_INDEX][i] += 1;
            tmp[resultIndex][FP_INDEX][i] += 1;
        }
    }

    /**
     * Conpute precision value for data set from 0 to i
     *
     * @param tpfpfn
     * @param pr - precision and value for each class
     * @param rs - precision and value for all class
     * @param i
     */
    private void computeP(int[][][] tpfpfn, double[][][] pr, double[][] rs, int i) {
        int length = tpfpfn.length;
        for (int j = 0; j < length; j++) {
            int tp = tpfpfn[j][TP_INDEX][i];
            int fp = tpfpfn[j][FP_INDEX][i];
            int fn = tpfpfn[j][FN_INDEX][i];

            if (tp + fp == 0) {
                pr[j][PRECISION_INDEX][i] = 1;
            } else {
//                pr[j][PRECISION_INDEX][i] = tp * 1.0 / (tp + fp);
                pr[j][PRECISION_INDEX][i] = tp * 1.0 / (i + 1);
            }

//            if (tp + fn == 0) {
//                pr[j][RECALL_INDEX][i] = 1;
//            } else {
//                pr[j][RECALL_INDEX][i] = tp * 1.0 / (tp + fn);
//            }
            rs[PRECISION_INDEX][i] += pr[j][PRECISION_INDEX][i];
//            rs[RECALL_INDEX][i] += pr[j][RECALL_INDEX][i];
        }
        rs[PRECISION_INDEX][i] /= length;
//        rs[RECALL_INDEX][i] /= length;
    }

    /**
     * Conpute recall value for data set from 0 to i
     *
     * @param tpfpfn
     * @param pr - precision and value for each class
     * @param rs - precision and value for all class
     * @param i
     */
    private void computeR(int[][][] tpfpfn, double[][][] pr, double[][] rs, int i) {
        int length = tpfpfn.length;
        for (int j = 0; j < length; j++) {
            int tp = tpfpfn[j][TP_INDEX][i];
            int m = tpfpfn[j][TP_INDEX][tpfpfn[j][TP_INDEX].length - 1]
                    + tpfpfn[j][FN_INDEX][tpfpfn[j][FN_INDEX].length - 1];

            pr[j][RECALL_INDEX][i] = tp * 1.0 / m;

            rs[RECALL_INDEX][i] += pr[j][RECALL_INDEX][i];
        }
        rs[RECALL_INDEX][i] /= length;
//        rs[RECALL_INDEX][i] /= length;
    }

    /**
     * Retrieve training set
     *
     * @param testIndex index of testing Set
     * @return
     */
    private DataSet getTrainingSet(int testIndex) {
        DataSet rs = new DataSet();
        for (int i = 0; i < dataSet.length; i++) {
            if (i == testIndex) {
                continue;
            }

            rs.addAll(dataSet[i]);
        }
        return rs;
    }

}
