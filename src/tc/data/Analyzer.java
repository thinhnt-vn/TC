/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import java.io.Reader;

/**
 *
 * @author thinhnt
 */
public abstract class Analyzer {

    private AnalysisComponents analysisComponents = null;
    
    public abstract AnalysisComponents createAnalysisComponents();
    
    public TokenStream tokenStream(Reader reader){
        if (analysisComponents == null){
            analysisComponents = createAnalysisComponents();
        }
        
        analysisComponents.setReader(reader);
        
        return analysisComponents.getTokenStream();
    }
}
