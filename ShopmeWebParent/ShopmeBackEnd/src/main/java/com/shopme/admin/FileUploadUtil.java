package com.shopme.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        //lấy file theo đường dẫn uploadDir
        Path uploadPath = Paths.get(uploadDir);
        //kiểm tra nếu file chưa tồn tại thì tạo đường dẫn theo uploadPath
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            //gắn file name trong đường dẫn
            Path filePath = uploadPath.resolve(fileName);
            //nhận file từ inputStream và gắn trong đường dẫn filePath, nếu nó tồn tại thì replace
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("could not save file: " + fileName,ex);
        }
    }

    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);
        if (Files.exists(dirPath)) {

            try {
                //duyệt từng file và thư mục trong đường dẫn
                Files.list(dirPath).forEach(file -> {
                    //nếu file không phải là thư mục thì xóa hết
                    if(!Files.isDirectory(file)) {
                        try {
                            Files.delete(file);
                        } catch (IOException ex) {
                            System.out.println("Could not delete file: " + file);
                        }
                    }
                });
            } catch (IOException ex) {
                System.out.println("Could not list directory: " + dirPath );
                ex.printStackTrace();
            }
        }
    }
    public static void removeDir(String dir) {
        cleanDir(dir);
        try {
            Files.delete(Paths.get(dir));
        } catch (IOException e) {
            System.out.println("Could not remove directory: " + dir);
        }
    }
}
