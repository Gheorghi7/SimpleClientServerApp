package dev.lpa.URIBasics;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URIBasics {
    static void main() {
        URI baseSite = URI.create("https://learnprogramming.academy");
        URI timeSite = URI.create("courses/complete-java-masterclass");
        printURI(timeSite);

        try {
            URI masterClass = baseSite.resolve(timeSite);
            URI uri = new URI("http://user/pw@gmail.com:5000");
            printURI(uri);
            URL url = masterClass.toURL();
            System.out.println(url);
            printURL(url);

        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends URI> void printURI(T uri) {
        System.out.printf("""
                            ----------------------------------------
                            [schema:]schema-specific-part[#fragment]
                            ----------------------------------------
                            Schema: %s
                            Schema-specific-part: %s
                            Authority: %s
                                User info: %s
                                Host: %s
                                Port: %s
                                Path: %s
                                Query: %s
                            Fragment: %s
                        """,
                uri.getScheme(),
                uri.getSchemeSpecificPart(),
                uri.getAuthority(),
                uri.getUserInfo(),
                uri.getHost(),
                uri.getPort() == -1 ? 80 : uri.getPort(),
                uri.getPath(),
                uri.getQuery(),
                uri.getFragment());


    }

    private static <T extends URL> void printURL(T url) {
        System.out.printf("""
                            ----------------------------------------
                            Authority: %s
                                User info: %s
                                Host: %s
                                Port: %s
                                Path: %s
                                Query: %s
                            ----------------------------------------
                        """,

                url.getAuthority(),
                url.getUserInfo(),
                url.getHost(),
                url.getPort() == -1 ? 80 : url.getPort(),
                url.getPath(),
                url.getQuery());


    }
}
