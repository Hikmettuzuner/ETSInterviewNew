package com.dev.controller;

import java.io.File;
import java.nio.file.FileSystemException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dev.dao.DashboardEtsFileDao;
import com.dev.dao.DashboardEtsFileDaoImp;
import com.dev.model.FileInformation;
import com.dev.utility.ConversationUtil;

@Controller
public class HomeDashboardController {

	@Autowired
	ServletContext servletContext;

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		List<FileInformation> mainList = new ArrayList<FileInformation>();
		DashboardEtsFileDao dashboardEtsFileDao = new DashboardEtsFileDaoImp();
		mainList = dashboardEtsFileDao.findAll();

		if (mainList.isEmpty()) {
			mainList.add(new FileInformation("EMPTY LIST"));
			modelAndView.addObject("showFileInformationList", mainList);
			return modelAndView;
		}

		modelAndView.addObject("showFileInformationList", mainList);
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		return modelAndView;
	}

	@RequestMapping(value = "/dashboardFileUpload", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView uploadEtsFile(@RequestParam("fileName") String fileName,
			@RequestParam("etsFile") MultipartFile etsFile) throws FileSystemException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		boolean isSuccess = false;
		

		
		ModelAndView modelAndView = new ModelAndView("home");
		List<FileInformation> mainList = new ArrayList<FileInformation>();
		DashboardEtsFileDao dashboardEtsFileDao = new DashboardEtsFileDaoImp();
		mainList = dashboardEtsFileDao.findAll();
		
		if(fileName.trim().equals(" ") || fileName.trim().equals("")) {
			modelAndView.addObject("showFileInformationList", mainList);
			return modelAndView.addObject("textEmpty", "Text area empty!");
		}
		

		// File Format Checking
		String etsFileExtension = ConversationUtil.getFileExtension(etsFile);
		List<String> allowedExtensions = Arrays.asList("pdf");
		if (!allowedExtensions.contains(etsFileExtension.toLowerCase())) {
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			modelAndView.addObject("showFileInformationList", mainList);
			return modelAndView.addObject("fileType", "Invalid file extension! pdf extension is supported!");
		}

		// File Size Checking
		boolean result = ConversationUtil.getFileSize(etsFile);
		if (!result) { // 5 MB
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			modelAndView.addObject("showFileInformationList", mainList);
			return modelAndView.addObject("fileSize", "it should not be more than 5mb");

		}

		// File Current File Version
		int fileVersion = dashboardEtsFileDao.findFileVersion(fileName);

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
				isSuccess = dashboardEtsFileDao.save(fileInformation);
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
				isSuccess = dashboardEtsFileDao.update(fileInformation);
			} catch (Exception e) {
				System.out.println("Unfortunatelly : " + e.getMessage());
			}
		}

		if (!isSuccess) {
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			modelAndView.addObject("showFileInformationList", mainList);
			return modelAndView.addObject("fail", "File could not be saved successfully!");
		}
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		modelAndView.addObject("showFileInformationList", mainList);
		return modelAndView.addObject("success", "File saved to database successfully!");

	}

	@RequestMapping(value = "/dashboardFileDelete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteEtsFile(@RequestParam("fileName") String fileName) throws FileSystemException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		ModelAndView modelAndView = new ModelAndView("home");
		List<FileInformation> mainList = new ArrayList<FileInformation>();
		DashboardEtsFileDao dashboardEtsFileDao = new DashboardEtsFileDaoImp();
		mainList = dashboardEtsFileDao.findAll();

		JSONObject obj = new JSONObject();
		JSONArray sendingList = new JSONArray();

		if (fileName.equals("null")) {
			obj.put("msg", "3");
			obj.put("deletedError", mainList);
			sendingList.add(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return sendingList.toJSONString();
		}

		if (mainList.isEmpty()) {
			mainList.add(new FileInformation("EMPTY LIST"));
			obj.put("msg", "3");
			obj.put("deletedError", mainList);
			sendingList.add(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return sendingList.toJSONString();
		}

		String folderPath = "C:\\Temp\\ETS\\" + fileName;
		File folder = new File(folderPath);
		boolean fileLocation = ConversationUtil.deleteFolder(folder);
		if (!fileLocation) {
			obj.put("msg", "3");
			obj.put("deletedError", mainList);
			sendingList.add(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return sendingList.toJSONString();
		}

		boolean isSuccess = dashboardEtsFileDao.delete(fileName);
		if (!isSuccess) {
			obj.put("msg", "3");
			obj.put("deletedError", mainList);
			sendingList.add(obj);
			System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
					+ formatter.format(date));
			return sendingList.toJSONString();
		}

		obj.put("msg", "1");
		obj.put("deletedData", "File was deleted successfully!");
		sendingList.add(obj);
		System.out.println(">> " + Thread.currentThread().getStackTrace()[1] + " Server Log " + " date: "
				+ formatter.format(date));
		return sendingList.toJSONString();
	}

}
