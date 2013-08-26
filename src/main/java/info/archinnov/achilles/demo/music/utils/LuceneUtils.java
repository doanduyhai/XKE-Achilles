package info.archinnov.achilles.demo.music.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class LuceneUtils {

    private static final Analyzer ANALYZER = new StandardAnalyzer(Version.LUCENE_44);

    public static List<String> tokenizeString(String string) {
        List<String> result = new ArrayList<String>();
        try {
            TokenStream stream = ANALYZER.tokenStream("field", new StringReader(string));

            stream.reset();
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
        return result;
    }
}
