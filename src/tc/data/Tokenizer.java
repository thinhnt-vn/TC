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
public abstract class Tokenizer extends TokenStream {

    protected Reader reader;

    
    public void setReader(Reader reader) {
        this.reader = reader;
    }
}