package com.example.sportsscheduling.scheduling;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The type Write to file.
 */
public class WriteToFile {
    /**
     * Create and write external file.
     *
     * @param context  the context
     * @param fileName the file name
     * @param content  the content
     */
    public static void createAndWriteExternalFile(Context context, String fileName, String content) {
        File file = new File(context.getExternalFilesDir(null), fileName); // 获取 External Storage 路径

        try (FileWriter writer = new FileWriter(file, false)) { // false = 覆盖写入
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}