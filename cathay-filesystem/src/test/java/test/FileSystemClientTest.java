package test;

import java.util.Map;

import com.cathay.filesystem.FileSystemClient;
import com.cathay.filesystem.UploadTokenParam;

public class FileSystemClientTest {

	public static void main(String[] args) {
		UploadTokenParam param = new UploadTokenParam();
		Map<String, Object> token = FileSystemClient.getClient("xxxx2").createUploadToken(param);
		
		System.out.println(token);
	}

}
