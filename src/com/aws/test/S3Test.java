package com.aws.test;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;

public class S3Test
{
  S3Operations s3_Ops = null;
  
  @Test
  public void s3_Upload_Download_File_Verify_Checksum() throws IOException, NoSuchAlgorithmException, InterruptedException
  {  
	  Log.info("Starting a new test");
	  s3_Ops.UploadFile("aws-test", "560.bld", "test.txt", "C:\\test.txt");
	  s3_Ops.DownloadFile("aws-test", "560.bld", "test.txt", "C:\\test2.txt");
	  Assert.assertEquals(FileOperations.GetFileCheckSum("C:\\test.txt"), 
			  			  FileOperations.GetFileCheckSum("C:\\test2.txt"));
  }
 
  @Test(expectedExceptions = AmazonClientException.class)
  public void s3_Upload_Empty_File_Verify_Errors() throws IOException, AmazonClientException
  {
	  //Didn't find errs while uploading an empty file
	  s3_Ops.UploadFile("aws-test", "560.bld", "emptyFile.txt", "C:\\emptyFile.txt");
	  s3_Ops.DownloadFile("aws-test", "560.bld", "emptyFile.txt", "C:\\emptyFile2.txt");
  }
  
  @Test
  public void s3_Upload_Big_File_Verify_Checksum() throws IOException, NoSuchAlgorithmException
  {
	  s3_Ops.UploadFile("aws-test", "560.bld", "bigFile.txt", "C:\\bigFile.txt");
	  s3_Ops.DownloadFile("aws-test", "560.bld", "bigFile.txt", "C:\\bigFile2.txt");
	  Assert.assertEquals(FileOperations.GetFileCheckSum("C:\\bigFile.txt"), 
			  			  FileOperations.GetFileCheckSum("C:\\bigFile2.txt"));
  }
  
  @BeforeClass
  public void beforeAllTests()
  {
	  s3_Ops = new S3Operations(); 
	  s3_Ops.AuthenticateToAWS(Constants.ACCESS_KEY_ID, Constants.SECRET_ACCESS_KEY);
	  delete_all_test_files_in_S3();
  }

  @AfterClass
  public void afterAllTests()
  {
	  delete_all_test_files_in_S3();
  }
  
  private void delete_all_test_files_in_S3()
  {
	  s3_Ops.DeleteFile("aws-test", "test.txt");
	  s3_Ops.DeleteFile("aws-test", "emptyFile.txt");
	  s3_Ops.DeleteFile("aws-test", "bigFile.txt");
  }
}