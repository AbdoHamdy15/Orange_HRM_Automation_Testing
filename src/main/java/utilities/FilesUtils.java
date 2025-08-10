package utilities;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FilesUtils {
    private FilesUtils() {
        super();
    }

    public static File getLatestFile(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            LogsUtil.warn("No files found in directory: " + folderPath);
            return null;
        }
        File latestFile = files[0];
        for (File file : files) {
            if (file.lastModified() > latestFile.lastModified()) {
                latestFile = file;
            }
        }
        return latestFile;
    }

    public static void deleteFiles(File dirPath) {
        if (dirPath == null || !dirPath.exists()) {
            LogsUtil.warn("Directory does not exist: " + dirPath);
            return;
        }

        LogsUtil.info("Deleting directory: " + dirPath.getPath());
        
        try {
            FileUtils.deleteDirectory(dirPath);
            LogsUtil.info("Directory deleted successfully: " + dirPath);
        } catch (IOException e) {
            LogsUtil.error("Failed to delete directory: " + dirPath + " - " + e.getMessage());
        }
    }


    public static void cleanDirectory(File file) {
        try {
            FileUtils.deleteQuietly(file);
        } catch (Exception exception) {
            LogsUtil.error(exception.getMessage());

        }
    }

    public static void renameFile(File oldName, File newName) {
        try {
            File targetFile = oldName.getParentFile().getAbsoluteFile();
            String targetDirectory = targetFile + File.separator + newName;
            FileUtils.copyFile(oldName, new File(targetDirectory));
            FileUtils.deleteQuietly(oldName);
            LogsUtil.info("Target File Path: \"" + oldName.getPath() + "\", file was renamed to \"" + newName.getName() + "\".");
        } catch (Exception e) {
            LogsUtil.error("Failed to rename file: " + e.getMessage());
        }
    }

    public static void createDirectory(File path) {
        if (!path.exists()) {
            try {
                Files.createDirectories(path.toPath());
                LogsUtil.info("Directory created: " + path);
            } catch (IOException e) {
                LogsUtil.error("Failed to create directory: " + e.getMessage());
            }
        } else {
            LogsUtil.info("Directory already exists: " + path);
        }
    }

    public static void copyDirectory(File sourceDir, File destDir) {
        try {
            if (sourceDir.exists() && sourceDir.isDirectory()) {
                FileUtils.copyDirectory(sourceDir, destDir);
                LogsUtil.info("Directory copied successfully from " + sourceDir + " to " + destDir);
            } else {
                LogsUtil.warn("Source directory does not exist or is not a directory: " + sourceDir);
            }
        } catch (IOException e) {
            LogsUtil.error("Failed to copy directory: " + e.getMessage());
        }
    }

    public static void copyFolder(File sourceDir, File destDir) {
        copyDirectory(sourceDir, destDir);
    }

    public static void deleteDirectory(File dir) {
        try {
            if (dir.exists()) {
                FileUtils.deleteDirectory(dir);
                LogsUtil.info("Directory deleted successfully: " + dir);
            } else {
                LogsUtil.warn("Directory does not exist: " + dir);
            }
        } catch (IOException e) {
            LogsUtil.error("Failed to delete directory: " + e.getMessage());
        }
    }
}
