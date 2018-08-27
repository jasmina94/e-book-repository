package com.ftn.lucene.analyzer;


import com.ftn.lucene.filter.CyrillicToLatinFilter;
import com.ftn.lucene.filter.LowerCaseFilter;
import com.ftn.lucene.stemmer.SimpleSerbianStemmer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by Jasmina on 29/07/2018.
 */
public class SerbianAnalyzer extends Analyzer {

    private static final Version version = Version.LUCENE_4_9;


    private static final String[] STOP_WORDS = {
            "biti", "jesam", "jeste", "jesmo", "jesu", "sam",
            "si", "je", "smo", "ste", "su",
            "nisam", "niste", "nismo", "nisu",
            "hoću", "hoćeš", "hoće", "hoćemo", "hoćete", "hoće",
            "ću", "ćeš", "će", "ćemo", "ćete",
            "hocu", "hoces", "hoce", "hocemo", "hocete", "hoce",
            "ce", "ces", "ce", "cemo", "cete",
            "budem", "budeš", "bude", "budemo", "budete", "budu",
            "bio", "bili", "bih", "bi", "bismo", "biste",
            "i", "ili", "pa", "te", "ni", "a", "ali", "na", "u", "po", "li", "da", "sa", "dok"
    };

    @Override
    protected TokenStreamComponents createComponents(String s, Reader reader) {
        TokenStreamComponents tokenStreamComponents = null;
        Tokenizer tokenizer = new StandardTokenizer(version, reader);
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);

        tokenStream = new CyrillicToLatinFilter(tokenStream);
        tokenStream = new StopFilter(version, tokenStream, StopFilter.makeStopSet(version, STOP_WORDS));
        tokenStream = new SnowballFilter(tokenStream, new SimpleSerbianStemmer());
        tokenStream = new ASCIIFoldingFilter(tokenStream);
        tokenStreamComponents = new TokenStreamComponents(tokenizer, tokenStream);

        return tokenStreamComponents;
    }
}
