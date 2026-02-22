package dev.lpa.URIBasics;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class URIBasics {
    static void main() {
        URI timeSite = URI.create("https://learnprogramming.academy:5000/courses/complete-java-masterclass?pass=dalsda?log=dsadsda");
        printURI(timeSite);
        URL url = null;
        try {
            url = timeSite.toURL();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
        }
        assert url != null;
        printURL(url);
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
