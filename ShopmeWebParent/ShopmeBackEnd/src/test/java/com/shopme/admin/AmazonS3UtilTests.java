package com.shopme.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AmazonS3UtilTests {
    @Test
    public void testListFolder() {
        String folderName = "product-images/1";
        List<String> listKey = AmazonS3Util.listFolder(folderName);
        listKey.forEach(System.out::println);
    }

    @Test
    public void testUpload() throws FileNotFoundException {
        String folderName = "Test-upload/11/1";
        String fileName = "lol-diamon.png";
        String dirPath = "D:\\" + fileName;

        InputStream inputStream = new FileInputStream(dirPath);

        AmazonS3Util.uploadFile(folderName,fileName,inputStream);


    }

    @Test
    public void testDeleteFile() throws FileNotFoundException {
        String fileName = "Test-upload/lol-diamon.png";
        AmazonS3Util.deleteFile(fileName);
    }

    @Test
    public void testRemoveFolder() {
        String folderName = "Test-upload";
        AmazonS3Util.removeFolder(folderName);
    }
}
