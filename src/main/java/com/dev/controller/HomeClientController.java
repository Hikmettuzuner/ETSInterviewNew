package com.dev.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dev.dao.ClientEtsFileDao;
import com.dev.dao.ClientEtsFileDaoImp;
import com.dev.model.FileInformation;
import com.dev.utility.ConversationUtil;
import com.google.gson.Gson;

@Controller
public class HomeClientController {

	@Autowired
	ServletContext servletContext;

	@RequestMapping(value = "/clientGetFileList", method = RequestMethod.POST)
	public ResponseEntity<String> clientGetFileList() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<FileInformation> mainList = new ArrayList<FileInformation>();
		ClientEtsFileDao clientEtsFileDao = new ClientEtsFileDaoImp();
		mainList = clientEtsFileDao.findAll();

		if (mainList.isEmpty()) {
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>("No Data ", HttpStatus.OK);
		}

		Gson gson = new Gson();
		String json = gson.toJson(mainList);

		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	@RequestMapping(value = "/clientDeleteFile", method = RequestMethod.POST)
	public ResponseEntity<String> clientDeleteFile(@RequestParam("fileName") String fileName) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		List<FileInformation> mainList = new ArrayList<FileInformation>();
		ClientEtsFileDao clientEtsFileDao = new ClientEtsFileDaoImp();
		mainList = clientEtsFileDao.findAll();

		JSONObject obj = new JSONObject();
		JSONArray sendingList = new JSONArray();
		Gson gson = new Gson();

		if (fileName.equals("null")) {
			obj.put("msg", "Please entry file name!");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);
		}

		if (mainList.isEmpty()) {
			obj.put("msg", "No exist files at file system!");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);
		}

		String folderPath = "C:\\Temp\\ETS\\" + fileName;
		File folder = new File(folderPath);
		boolean fileLocation = ConversationUtil.deleteFolder(folder);
		if (!fileLocation) {
			obj.put("msg", "Unfortunatelly, File location could not find!");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);
		}

		boolean isSuccess = clientEtsFileDao.delete(fileName);
		if (!isSuccess) {
			obj.put("msg", "Unfortunaltelly, File was not deleted!");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);
		}

		obj.put("msg", "File was deleted successfully!");
		String json = gson.toJson(obj);
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	@RequestMapping(value = "/clientUploadFile", method = RequestMethod.POST)
	public ResponseEntity<String> clientAddFile(@RequestParam("fileName") String fileName,
			@RequestParam("etsFile") MultipartFile etsFile) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		boolean isSuccess = false;
		JSONObject obj = new JSONObject();
		JSONArray sendingList = new JSONArray();
		Gson gson = new Gson();

		ModelAndView modelAndView = new ModelAndView("home");
		List<FileInformation> mainList = new ArrayList<FileInformation>();
		ClientEtsFileDao clientEtsFileDao = new ClientEtsFileDaoImp();
		mainList = clientEtsFileDao.findAll();

		if (fileName.trim().equals(" ") || fileName.trim().equals("")) {
			obj.put("msg", "Text area empty!");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);

		}

		// File Format Checking
		String etsFileExtension = ConversationUtil.getFileExtension(etsFile);
		List<String> allowedExtensions = Arrays.asList("pdf");
		if (!allowedExtensions.contains(etsFileExtension.toLowerCase())) {
			obj.put("msg", "Invalid file extension! pdf extension is supported!");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);
		}

		// File Size Checking
		boolean result = ConversationUtil.getFileSize(etsFile);
		if (!result) { // 5 MB
			obj.put("msg", "It should not be more than 5mb");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);
		}

		// File Current File Version
		int fileVersion = clientEtsFileDao.findFileVersion(fileName);

		File ETS_Folder = new File("C:\\Temp\\ETS");
		if (!ETS_Folder.exists()) {
			System.out.println("C:\\Temp\\ETS folder will be created!");
			ETS_Folder.mkdirs();
		}

		File ETS_File_Folder = new File(ETS_Folder + "\\" + fileName);
		if (!ETS_File_Folder.exists()) {
			System.out.println("C:\\Temp\\ETS\\File folder will be created!");
			ETS_File_Folder.mkdirs();
		}

		File ETS_Folder_PDF_FILE = new File(
				ETS_Folder + "\\" + fileName + "\\" + fileName + "_" + fileVersion + ".pdf");

		// İf file do not exit, it will be first time and save() function will be
		// started
		if (!ETS_Folder_PDF_FILE.exists()) {
			System.out.println(ETS_Folder_PDF_FILE.toString() + " pdf file will be created");
			try {
				System.out.println("File transfer has started.");
				etsFile.transferTo(new File(ETS_Folder_PDF_FILE.toString()));
				System.out.println("File transfer has completed.");
			} catch (Exception e) {
				System.out.println("Unfortunatelly : " + e.getMessage());
			}
			try {
				String fileLocation = ETS_Folder_PDF_FILE.getAbsolutePath();
				FileInformation fileInformation = new FileInformation();
				fileInformation.setFileName(fileName);
				fileInformation.setFileVersion(fileVersion);
				fileInformation.setFileType(".pdf");
				fileInformation.setFileSize(ConversationUtil.calculateFileSize(etsFile));
				fileInformation.setFilePath(fileLocation);
				isSuccess = clientEtsFileDao.save(fileInformation);
			} catch (Exception e1) {

				System.out.println("Unfortunatelly : " + e1.getMessage());

			}

		}

		else

		{
			// İf file exits, it will be first time and save() function will be started
			fileVersion = fileVersion + 1;
			System.out.println(ETS_Folder_PDF_FILE.toString() + " same name file exist, it will be increase once time");
			ETS_Folder_PDF_FILE = new File(ETS_Folder + "\\" + fileName + "\\" + fileName + "_" + fileVersion + ".pdf");
			try {
				System.out.println("File transfer has started.");
				etsFile.transferTo(new File(ETS_Folder_PDF_FILE.toString()));
				System.out.println("File transfer has completed.");
			} catch (Exception e) {
				System.out.println("Unfortunatelly : " + e.getMessage());

			}
			try {
				String fileLocation = ETS_Folder_PDF_FILE.getAbsolutePath();
				FileInformation fileInformation = new FileInformation();
				fileInformation.setFileName(fileName);
				fileInformation.setFileVersion(fileVersion);
				fileInformation.setFileType(".pdf");
				fileInformation.setFileSize(ConversationUtil.calculateFileSize(etsFile));
				fileInformation.setFilePath(fileLocation);
				isSuccess = clientEtsFileDao.update(fileInformation);
			} catch (Exception e) {
				System.out.println("Unfortunatelly : " + e.getMessage());
			}
		}

		if (!isSuccess) {
			obj.put("msg", "File could not be saved successfully!");
			String json = gson.toJson(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>(json, HttpStatus.OK);

		}

		obj.put("msg", "File saved to database successfully!");
		String json = gson.toJson(obj);
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		return new ResponseEntity<>(json, HttpStatus.OK);

	}

	@RequestMapping(value = "/clientGetByFileName", method = RequestMethod.POST)
	public ResponseEntity<String> clientGetByFileName(@RequestParam("fileName") String fileName) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<FileInformation> mainList = new ArrayList<FileInformation>();
		FileInformation fileInformation = new FileInformation();
		ClientEtsFileDao clientEtsFileDao = new ClientEtsFileDaoImp();
		fileInformation = clientEtsFileDao.findFileByName(fileName);

		if (fileInformation == null) {
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return new ResponseEntity<>("No Data ", HttpStatus.OK);
		}

		Gson gson = new Gson();
		String json = gson.toJson(fileInformation);

		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	@RequestMapping(value = "/fileDownload", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @RequestParam(value = "fileName") String fileName)
			throws IOException {
		System.out.println(">> fileDownload " + fileName);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		// File Current File Version
		ClientEtsFileDao clientEtsFileDao = new ClientEtsFileDaoImp();
		int fileVersion = clientEtsFileDao.findFileVersion(fileName);

		File downloadFile = new File("C:\\Temp\\ETS\\" + fileName + "\\" + fileName + "_" + fileVersion + ".pdf");
		if (!downloadFile.exists()) {
			System.out.println("Unfortunatelly, file does not exist!");
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
		}

		String newFileName = fileName + "_" + String.valueOf(fileVersion) + ".pdf";
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + newFileName;
		// String headerValue = "attachment; filename=" + applicationVersion;
		response.setHeader(headerKey, headerValue);
		ServletOutputStream outputStream = response.getOutputStream();
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(downloadFile));
		byte[] buffer = new byte[8192];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		System.out.println("<< fileDownload ");
		inputStream.close();
		outputStream.close();

	}

}
