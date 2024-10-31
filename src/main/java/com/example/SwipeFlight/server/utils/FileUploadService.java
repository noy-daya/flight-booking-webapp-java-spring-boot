package com.example.SwipeFlight.server.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * The class is a service which handles File Upload.
 * (in which user selects a file from his device, and uploads into the project).
 */
@Service
public class FileUploadService {

	// returns a string which consists path
	// the check if the size of the file does not exceeds maximum is in the javascript file fileUpload.js
	/**
	 * The method handles file Upload by the user.
	 * @param file - the file to upload
	 * @param targetEntityName - a string consisting the name of target entity.
	 * 		  for example, in case user uploads a picture for a city, value will be "City".
	 * @param result - binding result to attach validation errors to.
	 * @return if the file was uploaded relative path of the file is returned.
	 * 			otherwise- null.
	 */
	public String fileUpload(MultipartFile file, String targetEntityName, BindingResult result)
	{
		String UPLOAD_FOLDER = "src/main/resources/static/",
				UPLOAD_SUB_FOLDER = "", fullPathStr = "", pathStr = "";

		if (targetEntityName == "City")
			UPLOAD_SUB_FOLDER = "/images/cities/";

		if (!file.isEmpty())
		{
			// --- check file extensions ---
			String pattern = ".(jpg|jpeg|png|gif|JPG|JPEG|PNG|GIF)$";
			String extention = getFileExtension(file);
			// illegal extension
			if (!Pattern.matches(pattern, extention))
			{
				result.rejectValue("imgUrl", "error.city", "The selected file type is not supported. Please upload a valid image file (JPG, JPEG, PNG, GIF).");
			}
			// legal extension
			else
			{
				try {
					// read and write the file to the selected location
					byte[] bytes = file.getBytes();
					// picture name will be the same as original
					pathStr = UPLOAD_SUB_FOLDER + file.getOriginalFilename();
					fullPathStr = UPLOAD_FOLDER + pathStr;
					Path path = Paths.get(fullPathStr);
					Files.write(path, bytes);

				} catch (Exception e) {
					result.rejectValue("imgUrl", "error.city", "File upload failed. Please try again.");
					pathStr = null;
				}
			}
		}
		return pathStr; // whether if it
	}

	/**
	 * The method is a helper, returning file's extension.
	 * @param file
	 * @return a string consisting extension
	 */
	private String getFileExtension(MultipartFile file) {
		String name = file.getOriginalFilename();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return name.substring(lastIndexOf);
	}

}
