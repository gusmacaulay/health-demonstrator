package org.mccaughey.geotools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.mccaughey.util.HttpProxy_JRoller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Zip {

	static final Logger LOGGER = LoggerFactory.getLogger(Zip.class);
	private List<String> fileList;
	private File outputZipFile;
	private String sourceFolder;

	public Zip(File outputZipFile, String sourceFolder) {
		this.outputZipFile = outputZipFile;
		this.sourceFolder = sourceFolder;
		fileList = new ArrayList<String>();
	}

	public void createZip() throws IOException {

		generateFileList(new File(sourceFolder));
		zipIt(outputZipFile);
	}

	/**
	 * Zip it
	 * 
	 * @param zipFile
	 *            output ZIP file location
	 * @throws IOException 
	 */
	public void zipIt(File zipFile) throws IOException {

		byte[] buffer = new byte[1024];

		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);

		LOGGER.info("Output to Zip : " + zipFile.getAbsolutePath());

		for (String file : this.fileList) {

			System.out.println("File Added : " + file);
			ZipEntry ze = new ZipEntry(file);
			zos.putNextEntry(ze);

			FileInputStream in = new FileInputStream(sourceFolder
					+ File.separator + file);

			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			in.close();
		}

		zos.closeEntry();
		// remember close it
		zos.close();

	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}

	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private String generateZipEntry(String file) {
		return file.substring(sourceFolder.length() + 1, file.length());
	}
}
