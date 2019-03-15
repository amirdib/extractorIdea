package extractor.main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WorldClimDownloader {

    private List<String> downloadedVariables = new ArrayList<>();

    public WorldClimDownloader(){

    }

    /**
     * Download all climatic variable and creates folder for each
     */
    public void downLoadAll(){
        try {
            File downloadConfig = new File("downloadConfig/worldClim2/downloadLinks.TXT");
            List<String> lines = FileUtils.readLines(downloadConfig,"UTF-8");

            for (String s : lines){
                String url = s.substring(0,s.indexOf(" "));
                String path = s.substring(s.indexOf("=") + 2);
                File folder = new File(path.substring(0,path.indexOf(".zip") -5));
                downloadedVariables.add(folder.getName());

                if(!(folder.exists() && folder.list().length == 13)){
                    System.out.println("Downloading "
                            + url.substring(url.indexOf("m_") + 2, url.indexOf(".zip"))
                            + " from " + path.substring(0, path.indexOf("/")));

                    WebPageDownloader webDwn = new WebPageDownloader(url);
                    webDwn.downloadAsFile(path);

                    System.out.println("Downloaded "
                            + url.substring(url.indexOf("m_") + 2,url.indexOf(".zip"))
                            + " from " + path.substring(0,path.indexOf("/")));

                    Zip zip = new Zip(path);
                    zip.unzip();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<String> getDownloadedVariables() {
        return downloadedVariables;
    }
}
