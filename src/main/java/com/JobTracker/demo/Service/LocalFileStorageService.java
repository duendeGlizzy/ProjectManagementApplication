package com.JobTracker.demo.Service;

import com.JobTracker.demo.Repository.FileStorageService;
import jakarta.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path rootLocation = Paths.get("uploaded-documents");

    @PostConstruct
    public void init(){
        try{
            Files.createDirectories(rootLocation);
        }catch (Exception e){
            throw new RuntimeException("Unable to create directory");
        }
    }

    @Override
    public String storeFile(MultipartFile file, String folder){
        try{
            if(file.isEmpty())throw new IllegalArgumentException("File is empty");

            String uniqueName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetFolder = this.rootLocation.resolve(folder);
            Files.createDirectories(targetFolder);

            Path targetFile = targetFolder.resolve(uniqueName);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            return folder + "/" + uniqueName;
        }catch (IOException e){
            throw new RuntimeException("Unable to store file");
        }
    }

    @Override
    public Resource loadFileAsResource(String fileKey){
        try{
            Path file = rootLocation.resolve(fileKey);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) return resource;
            else throw new RuntimeException("could not read file: "+fileKey);
        }catch(MalformedURLException e){
            throw new RuntimeException("could not read file: "+fileKey);
        }
    }

    @Override
    public void deleteFile(String fileKey){
        try{
            Path file = rootLocation.resolve(fileKey);
            Files.deleteIfExists(file);
        }catch (IOException e){
            System.err.println("could not delete file: "+fileKey);
        }
    }


}
