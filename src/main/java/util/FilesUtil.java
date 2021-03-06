package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * 文件处理工具类
 * 
 * @author richfit
 * @version 1.0 Date: 2009-7-7
 */
public class FilesUtil {

	public static final int FLUSH_BUFFER_SIZE = 10240; // buffer size=10K

	/**
	 * 取文件扩展名
	 */
	public static String getFileExt(String clientFileName) {
		if (clientFileName == null || clientFileName.equals("")) {
			return "";
		}
		String[] arrFile = clientFileName.split("\\x2E"); // "\\x2E" == "."
		return arrFile[arrFile.length - 1];
	}

	/**
	 * 获取短文件名
	 */
	public static String getShortFileName(String fileName) {
		if (fileName == null || fileName.equals("")) {
			return "";
		}
		int index = indexOfLastSeparator(fileName);
		return fileName.substring(index + 1);
	}

	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		} else {
			int lastUnixPos = filename.lastIndexOf('/');
			int lastWindowsPos = filename.lastIndexOf('\\');
			return Math.max(lastUnixPos, lastWindowsPos);
		}
	}

	/**
	 * 获取文件的大小
	 */
	public static String getFileSize(long filesize) {
		double size = filesize * 1.0d;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);

		double sizeGB = size / 1024 / 1024 / 1024;
		if (sizeGB >= 1) {
			return nf.format(sizeGB) + " GB";
		}

		double sizeMB = size / 1024 / 1024;
		if (sizeMB >= 1) {
			return nf.format(sizeMB) + " MB";
		}

		double sizeKB = size / 1024;
		if (sizeKB >= 1) {
			return nf.format(sizeKB) + " KB";
		}
		return filesize + " bytes";
	}

	/**
	 * 获取文件的大小
	 */
	public static String getFileSize(File file) {
		return getFileSize(file.length());
	}

	/**
	 * 拷贝文件从目录source到目录target，包括文件名selectedFileNames中的文件
	 * 
	 * @throws IOException
	 */
	public static void copyFile(File source, File target, String[] fileNames) throws IOException {
		Arrays.sort(fileNames);
		File[] files = source.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (Arrays.binarySearch(fileNames, f.getName()) >= 0) {
				FileInputStream in = new FileInputStream(f);
				File t = new File(target.getAbsolutePath() + File.separator + f.getName());
				FileOutputStream out = new FileOutputStream(t);
				int c = -1;
				int buffsize = 0;
				while ((c = in.read()) != -1) {
					out.write(c);
					if (buffsize++ == FLUSH_BUFFER_SIZE) {
						out.flush();
						buffsize = 0;
					}
				}
				out.flush();
				out.close();
				in.close();
			}
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @throws IOException
	 *             IO异常
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		copyFile(srcFile, destFile, true);
	}

	/**
	 * 拷贝文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param preserveFileDate
	 *            是否保持时间戳
	 * @throws IOException
	 *             IO异常
	 */
	public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new IOException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		if (destFile.getParentFile() != null && !destFile.getParentFile().exists()
				&& !destFile.getParentFile().mkdirs()) {
			throw new IOException("Destination '" + destFile + "' directory cannot be created");
		}
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		} else {
			doCopyFile(srcFile, destFile, preserveFileDate);
		}
	}

	// 拷贝文件
	private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {}

	/**
	 * 默认方式清理指定目录下的文件：文件时间超过24小时者删除
	 * 
	 * @param directory
	 */
	public static void defaultClean(File directory) {
		if (!directory.exists()) {
			return;
		}
		long limit = 24 * 60 * 60 * 1000L; // 最大时限24小时
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			long time = System.currentTimeMillis() - f.lastModified() - limit;
			if (time >= 0) {
				if (f.isDirectory()) {
					defaultClean(f);
				}
				f.delete();
			}
		}
	}

	/**
	 * 删除目录下的超过时限的文件（不含目录）,并且文件的后缀为fileExt中的一个
	 * 
	 * @param directory
	 * @param andSubFolder
	 *            是否删除子目录下的文件
	 * @param fileExt
	 *            如"txt,exe,xls"
	 * @throws IOException
	 */
	public static void cleanDir(File directory, boolean andSubFolder, String fileExt) {
		long limit = 24 * 60 * 60 * 1000L; // 最大时限24小时
		String fileext = fileExt.toLowerCase();
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			long time = System.currentTimeMillis() - f.lastModified() - limit;
			if (time >= 0) {
				if (f.isDirectory() && andSubFolder) {
					cleanDir(f, andSubFolder, fileext);
				}
				String ext = getFileExt(f.getName()).toLowerCase();
				if (f.isFile() && !"".equals(ext) && fileext.indexOf(ext) >= 0) {
					f.delete();
				}
			}
		}
	}

	/**
	 * 判断目录及其子目录下是否有文件
	 * 
	 * @param directory
	 * @throws IOException
	 */
	public static boolean containFiles(File directory) throws IOException {
		if (directory == null || !directory.exists()) {
			return false;
		}
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				return true;
			}
			if (f.isDirectory()) {
				return containFiles(f);
			}
		}
		return false;
	}

	/**
	 * 删除目录下的所有文件或目录
	 * 
	 * @param directory
	 *            需要删除的目录
	 * @throws IOException
	 *             删除操作异常
	 */
	public static void deleteFiles(File directory) throws IOException {
		File[] files = directory.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				deleteFiles(f);
			}
			f.delete();
		}
		directory.delete();
	}
	/**
	 * 删除目录下的所有文件或目录(不包括目录)
	 * 
	 * @param directory
	 *            需要删除的目录
	 * @throws IOException
	 *             删除操作异常
	 */
	public static void deleteFilesNoDirectory(File directory) throws IOException {
		File[] files = directory.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				deleteFiles(f);
			}
			f.delete();
		}
	}

	/**
	 * 删除目录下的所有文件
	 * 
	 * @param directory
	 *            需要删除文件所属的目录
	 * @throws IOException
	 *             删除操作异常
	 */
	public static void cleanFiles(File directory) throws IOException {
		File[] files = directory.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				deleteFiles(f);
			}
			f.delete();
		}
	}
	
	/**
	 * 删除绝对路径文件
	 * 
	 * @param directory
	 *            需要删除文件所属的目录
	 * @throws IOException
	 *             删除操作异常
	 */
	public static void deleteFile(File file) throws IOException {
		if(file.isFile()){
			file.delete();
		}
	}

	/**
	 * 删除目录下所有文件（不含目录）,并且文件的后缀为fileExt中的一个
	 * 
	 * @param directory
	 *            需要删除的目录
	 * @param alsoSubFolder
	 *            是否删除子目录下的文件
	 * @param fileExt
	 *            文件后缀，用逗号分割，如"txt,exe,xls"
	 * @throws IOException
	 *             删除操作异常
	 */
	public static void deleteFiles(File directory, boolean alsoSubFolder, String fileExt) throws IOException {
		String fileext = fileExt.toLowerCase();
		File[] files = directory.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && alsoSubFolder) {
				deleteFiles(f, alsoSubFolder, fileext);
			}
			String ext = getFileExt(f.getName()).toLowerCase();
			if (f.isFile() && !"".equals(ext) && fileext.indexOf(ext) >= 0) {
				f.delete();
			}
		}
	}

	/**
	 * 创建一个指定编码的文件Writer
	 * 
	 * @param out
	 *            输出
	 * @param enc
	 *            编码
	 * @return Writer
	 * @throws UnsupportedEncodingException
	 *             不支持的编码
	 */
	public static Writer makeWriter(OutputStream out, String enc) throws UnsupportedEncodingException {
		if ("UTF-8".equals(enc)) {
			enc = "UTF8";
		}
		Writer writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(out), enc));
		return writer;
	}

	/**
	 * 创建一个指定编码的文件Reader
	 * 
	 * @param in
	 *            输入
	 * @param enc
	 *            编码
	 * @return Reader
	 * @throws UnsupportedEncodingException
	 *             不支持的编码
	 */
	public static Reader makeReader(InputStream in, String enc) throws UnsupportedEncodingException {
		if ("UTF-8".equals(enc)) {
			enc = "UTF8";
		}
		Reader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(in), enc));
		return reader;
	}

	/**
	 * 创建整个目录
	 * 
	 * @param path
	 *            目录路径
	 * @return 目录存在否
	 */
	public static boolean createDir(String path) {
		File file = new File(path);
		
		return file.exists() ? true : file.mkdirs();
	}

	public static boolean createNewFile(String path) {
		try {
			File file = new File(path);
			return file.exists() ? true : file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
