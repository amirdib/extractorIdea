package extractor.main;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

public class WebPageDownloader {

    private final String urlOfWeb;
    public WebPageDownloader(String targetUrl){
        this.urlOfWeb = targetUrl;

    }

    public String download() {
        StringBuilder pageBuilder = new StringBuilder();
        try {
            URL url = new URL(urlOfWeb);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                pageBuilder.append(line);
            }
            reader.close();

        }catch(Exception e1) {
            System.out.println("nepovedlo se");
            e1.printStackTrace();
        }

        return pageBuilder.toString();
    }

    /**
     * Download     *
     * @param path - where to place
     */
    public void downloadAsFile( String path){
        File file = new File(path);
        if (!file.exists()) {
            try{
                URL urlDownload = new URL(urlOfWeb);
                FileUtils.copyURLToFile(urlDownload, file);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
