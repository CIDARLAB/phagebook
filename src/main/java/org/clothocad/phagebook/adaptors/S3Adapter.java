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
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.clothocad.model.Person;

/**
 *
 * @author azula
 */
public class S3Adapter {

    public static void initializeUserFolder(Person pers) {
        String clothoId = pers.getId();

        AWSCredentials credentials = new BasicAWSCredentials(S3Credentials.getUsername(), S3Credentials.getPassword());
        AmazonS3 s3client = new AmazonS3Client(credentials);
        System.out.println("Login Complete");

        // list buckets
        for (Bucket bucket : s3client.listBuckets()) {
            System.out.println(" - " + bucket.getName());
        }

        System.out.println("Clotho id " + clothoId);
        createS3Folder("phagebookaws", clothoId, s3client);

        //------------TESTING BEFORE INTEGRATED INTO PROFILE------------
        String fileName = clothoId + "/" + "profilePicture.jpg";
        String picturePath = "C:\\Users\\azula\\Pictures\\AllisonDurkan.jpg";
        s3client.putObject(new PutObjectRequest("phagebookaws", fileName,
                new File(picturePath))
                .withCannedAcl(CannedAccessControlList.PublicRead));

    }

    public static void uploadProfilePicture(Person pers, String filePath) {
        String clothoId = pers.getId();
        AWSCredentials credentials = new BasicAWSCredentials(S3Credentials.getUsername(), S3Credentials.getPassword());
        System.out.println("Login Complete");

        AmazonS3 s3client = new AmazonS3Client(credentials);
        String fileName = clothoId + "/" + "profilePicture.jpg";
        s3client.putObject(new PutObjectRequest("phagebookaws", fileName,
                new File(filePath))
                .withCannedAcl(CannedAccessControlList.PublicRead));

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
