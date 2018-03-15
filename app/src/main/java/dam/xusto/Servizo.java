package dam.xusto;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by qexame13 on 01/03/2018.
 */

class Servizo {

    private static String urlBase = "http://10.0.2.2/xusto/";

    public static URL urlDescargaTendas() {
        String cadeaURL = urlBase + "buscartendas.php";
        return construirURL(cadeaURL);
    }

    private static URL construirURL(String cadeaUrl) {
        URL url = null;

        try {
            url = new URL(cadeaUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    private static String encode(String cadeaEntrada) {
        try {
            return URLEncoder.encode(cadeaEntrada, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
