package com.lyw.avproject.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 功能描述:
 * Created on 2021/6/19.
 *
 * @author lyw
 */
public class FileUtils {

    public static void writeByte(byte[] byteData, String pcmFileName) {
        FileOutputStream writer = null;
        try {
            File file = new File(pcmFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new FileOutputStream(pcmFileName, true);
            writer.write(byteData);
            writer.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
