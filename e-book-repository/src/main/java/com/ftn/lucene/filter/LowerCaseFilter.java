package com.ftn.lucene.filter;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Created by Jasmina on 16/08/2018.
 */
public class LowerCaseFilter extends TokenFilter {

    private CharTermAttribute termAttribute;

    public LowerCaseFilter(TokenStream input) {
        super(input);
        termAttribute = (CharTermAttribute)input.addAttribute(CharTermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        boolean success = false;
        if(input.incrementToken()){
            String text=termAttribute.toString();
            termAttribute.setEmpty();
            termAttribute.append(text.toLowerCase());
            success = true;
        }

        return success;
    }
}
