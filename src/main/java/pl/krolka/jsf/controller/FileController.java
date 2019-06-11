package pl.krolka.jsf.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named
@SessionScoped
public class FileController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Part file;

	public FileController() {
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	} 

	public String upload() throws IOException {
		
		String path = "C:\\Users\\Admin\\eclipse-workspace\\WeTransferClone\\src\\main\\resources\\Files\\" + getFilename(file);
		File checkFile = new File(path);
		
		if(checkFile.exists()) {
			String extention = path.substring(path.length() - 4);
			Calendar calendar = Calendar.getInstance();
			
			path = path.substring(0, path.length() - 4);
			path = path + "_" + calendar.getTimeInMillis();
			path = path + extention;
		}
		
		file.write(path);
		return "success";
	}

	private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
}
}
