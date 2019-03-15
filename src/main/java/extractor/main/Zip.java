package extractor.main;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zip {
    private final String path;

    Zip(String path){
        this.path = path;
    }

    public void unzip(){
        byte[] buffer = new byte[1024];

        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(this.path));
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                StringBuilder stringBuilder = new StringBuilder(this.path);

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
            FileUtils.deleteQuietly(new File(this.path));
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
