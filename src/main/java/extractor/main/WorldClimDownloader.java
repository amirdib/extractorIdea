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

    public WorldClimDownloader(){
        try {
            File downloadConfig = new File("downloadConfig/worldClim2/downloadLinks.TXT");
            List<String> lines = FileUtils.readLines(downloadConfig,"UTF-8");

            for (String s : lines){
                String url = s.substring(0,s.indexOf(" "));
                String path = s.substring(s.indexOf("=") + 2);
                File folder = new File(path.substring(0,path.indexOf(".zip") -5));

                if(!(folder.exists() && folder.list().length == 13)){
                    downloadFile(url,path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(String url, String path){
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Downloading "
                    + url.substring(url.indexOf("m_") + 2, url.indexOf(".zip"))
                    + " from " + path.substring(0, path.indexOf("/")));

            try{
                URL urlDownload = new URL(url);
                FileUtils.copyURLToFile(urlDownload, file);
                unzip(file.getPath());
            }catch(Exception e){

            }

        }


        System.out.println("Downloaded "
                + url.substring(url.indexOf("m_") + 2,url.indexOf(".zip"))
                + " from " + path.substring(0,path.indexOf("/")));

    }

    private void unzip(String path){
        byte[] buffer = new byte[1024];

        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path));
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                StringBuilder stringBuilder = new StringBuilder(path);

                stringBuilder.reverse()
                        .delete(0,stringBuilder.indexOf(File.separator))
                        .reverse();

                File file = new File(stringBuilder.toString() + zipEntry.getName());

                FileOutputStream fos = new FileOutputStream(file);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();


            }

            zipInputStream.closeEntry();
            zipInputStream.close();
            FileUtils.deleteQuietly(new File(path));
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
