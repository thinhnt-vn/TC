/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.gui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author thinhnt
 */
public abstract class TCWorker extends SwingWorker<Void, String> {

    private PrintStream originStream;

    @Override
    protected Void doInBackground() throws Exception {
        originStream = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {

            @Override
            public void print(String s) {
                super.print(s); //To change body of generated methods, choose Tools | Templates.
                publish(s);
            }

            @Override
            public void println(String x) {
                super.println(x); //To change body of generated methods, choose Tools | Templates.
                publish(x);
            }
        });
        doTask();
        return null;
    }
    
    protected abstract void doTask();

    @Override
    protected void done() {
        super.done(); //To change body of generated methods, choose Tools | Templates.
        System.setOut(originStream);
    }
}
