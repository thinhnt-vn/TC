/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

/**
 *
 * @author thinhnt
 */
public abstract class TokenStream {
    protected TokenStream stream;
    
    public TokenStream(){}
    
    public TokenStream(TokenStream stream){
        this.stream = stream;
    }
    
    public abstract String increaseToken();
}
