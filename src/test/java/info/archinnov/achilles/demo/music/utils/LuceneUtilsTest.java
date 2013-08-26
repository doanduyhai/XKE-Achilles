package info.archinnov.achilles.demo.music.utils;

import static info.archinnov.achilles.demo.music.utils.LuceneUtils.*;
import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class LuceneUtilsTest {

    @Test
    public void should_split_sentence_into_tokens() throws Exception {

        assertThat(tokenizeString("this is a simple sentence")).containsExactly("simple", "sentence");
        assertThat(tokenizeString("the Beattles")).containsExactly("beattles");
        assertThat(tokenizeString("where are you now!")).containsExactly("where", "you", "now");
    }
}
