/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.servlets.createPerson;

/**
 *
 * @author azula
 */
public class S3Adapter {

    public static void initializeUserFolder(Person pers) {
        String clothoId = pers.getId();
        List<String> credentialsList = new ArrayList<String>();
        try {
            FileReader fr = new FileReader(new File("../../credentials.txt"));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("LINE :: " + line);
                credentialsList.add(line);
            }
            String username = credentialsList.get(0);
            String password = credentialsList.get(1);

            AWSCredentials credentials = new BasicAWSCredentials(username, password);
            AmazonS3 s3client = new AmazonS3Client(credentials);

            createS3Folder("users", clothoId, s3client);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(createPerson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(createPerson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void uploadFile(Person pers, String bucketName, String filePath) {
        String clothoId = pers.getId();
        List<String> credentialsList = new ArrayList<String>();
        try {
            FileReader fr = new FileReader(new File("../../credentials.txt"));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("LINE :: " + line);
                credentialsList.add(line);
            }
            String username = credentialsList.get(0);
            String password = credentialsList.get(1);
            //profile picture upload gets called with fileName == "profilePicture.jpg"
            AWSCredentials credentials = new BasicAWSCredentials(username, password);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            String fileName = clothoId + "/" + "profilePicture.jpg";
            s3client.putObject(new PutObjectRequest(bucketName, fileName,
                    new File(filePath))
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(createPerson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(createPerson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void createS3Folder(String bucketName, String folderName, AmazonS3 client) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + "/", emptyContent, metadata); //folder name should be clothoID
        // send request to S3 to create folder
        client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        
    }  
}
