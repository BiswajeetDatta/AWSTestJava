package com.aws.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class S3Operations
{
	private String accessKeyId;
	private String secretAccessKey;
	private AmazonS3 s3Client;
	
	public void AuthenticateToAWS(String accessKeyId, String secretAccessKey)
	{
		this.accessKeyId = accessKeyId;
		this.secretAccessKey = secretAccessKey;
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1);
		s3Client = s3ClientBuilder.withCredentials((AWSCredentialsProvider) new AWSStaticCredentialsProvider(credentials)).build();
	}
	
	public void UploadFile(String bucketName, String s3FolderName, String s3FileName, String localFilePath) throws IOException
	{
    	Log.info(S3Operations.class.getName(), "Uploading a new object to S3 from a file");
        File file = new File(localFilePath);
        s3Client.putObject(new PutObjectRequest(bucketName, s3FolderName + "/" + s3FileName, file));
    }
	
	public void DownloadFile(String bucketName, String s3FolderName, String s3FileName, String destFilePath) throws IOException
	{
    	Log.info(S3Operations.class.getName(), "Downloading an object from S3");
        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, s3FolderName + "/" + s3FileName));
        Log.info(S3Operations.class.getName(), "Content-Type: "  + s3object.getObjectMetadata().getContentType());
        saveToFile(s3object.getObjectContent(), destFilePath);      
	}
	
	public void DeleteFile(String bucketName, String s3FileName)
	{
		try
		{
			s3Client.deleteObject(bucketName, s3FileName);
		}
		catch (Exception ex)
		{}
	}
	
	private void saveToFile(S3ObjectInputStream inputStream, String fileName) throws IOException
	{
		InputStream reader = new BufferedInputStream(inputStream);
		File file = new File(fileName);      
		OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
		int read = -1;
		while ( ( read = reader.read() ) != -1 ) {
		    writer.write(read);
		}
		writer.flush();
		writer.close();
		reader.close();
	}
}