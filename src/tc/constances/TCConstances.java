/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.constances;

/**
 *
 * @author thinhnt
 */
public class TCConstances {

    public final static String URL_REG = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    
    public static class Path {

        public static final String DATA_PATH = "data";
        public static final String MODEL_PATH = "tcmodel";
        
    }
    
    public static class File{
        public static final String MODEL_FILE_EXTENSION = "tcmodel";
    }
}
