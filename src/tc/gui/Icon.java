/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.gui;

import javax.swing.ImageIcon;

/**
 *
 * @author thinhnt
 */
public class Icon {
    
    public final static String IMPORT_ICON_PATH = "images/import.png";
    public final static String EXPORT_ICON_PATH = "images/export.png";
    public final static String TRAIN_ICON_PATH = "images/train.png";
    public final static String EXIT_ICON_PATH = "images/exit.png";
    public final static String ASSESSMENT_ICON_PATH = "images/assessment.png";

    public static ImageIcon createImageIcon(String path,
            String description) {
        return new ImageIcon(path, description);
    }
}
