/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Date;

/**
 * This analyzer includes Vietnamese tokenizer, Stopword filter, and lower case
 * filter
 *
 * @author thinhnt
 */
public class TCAnalyzer extends Analyzer {

    public TCAnalyzer() {
    }

    @Override
    public AnalysisComponents createAnalysisComponents() {
        Tokenizer source = new TCTokenizer();
        TokenStream stopwordFilter = new StopwordFilter(source,
                StopwordFilter.loadStopwords(Paths.get(StopwordFilter.STOP_WORD_FILE)));
        TokenStream lowercaseFilter = new LowerCaseFilter(stopwordFilter);
        return new AnalysisComponents(source, lowercaseFilter);
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        Analyzer analyzer = new TCAnalyzer();
        TokenStream ts = analyzer.tokenStream(new BufferedReader(new FileReader("data/0.tt")));
        String token = null;
        while ((token = ts.increaseToken()) != null) {            
            System.out.print(token + "|");
        }
    }
    
}
