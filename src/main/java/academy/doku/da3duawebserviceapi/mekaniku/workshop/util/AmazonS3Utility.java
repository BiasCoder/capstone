package academy.doku.da3duawebserviceapi.mekaniku.workshop.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AmazonS3Utility {

    @Value("${storage.properties.bucket-name}")
    private String bucketName;

    @Value("${storage.properties.endpoint-url}")
    private String endpointUrl;

    private final AmazonS3 amazonS3client;

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file, String serviceName)
            throws SdkClientException, IOException {
        amazonS3client.putObject(new PutObjectRequest(bucketName + "/" + serviceName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void deleteFileFromS3Bucket(String fileUrl, String serviceName)
            throws SdkClientException, IOException {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3client.deleteObject(new DeleteObjectRequest(bucketName + "/" + serviceName, fileName));
    }

    public String uploadFile(MultipartFile multipartFile, String serviceName) {

        String fileUrl = "";
        try {
            File file = convertMultipartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + serviceName + "/" + fileName;
            uploadFileTos3bucket(fileName, file, serviceName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }
}
