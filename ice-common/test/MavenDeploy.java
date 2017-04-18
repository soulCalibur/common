import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

/**
 * File Name:MavenDeploy.java
 * Package Name:
 * Date:2017-4-9下午1:08:22
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
 */

/**
 * ClassName:MavenDeploy <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017-4-9 下午1:08:22 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */

public class MavenDeploy {
	/** 
     * @param args 
	 * @throws ParserConfigurationException 
     */  
    public static void main(String[] args) throws Exception{  
        List<File> fileList = getFileList(new File("c:/Users/Administrator/.m2/repository"),"jar");  
      int num=0;
      String homePath="e:/work/ht/";
        File home=new File(homePath);
        File tempDir=new File(homePath+"/temp_jar");
        if(!tempDir.exists()||!tempDir.isDirectory()){
        	tempDir.mkdirs();
        }
      for (File temp : fileList) {
    	   String config=trimExtension(temp.getAbsolutePath())+".pom";
    	   File f = new File(config);   
    	   if(!f.exists()){
    		   continue;
    	   }
    	    InputStreamReader reader = new InputStreamReader(new FileInputStream(f)); // 建立一个输入流对象reader  
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
             StringBuffer content = new StringBuffer();  
             String line = br.readLine(); 
             content.append("\n");
             content.append(line);
             while (line != null) {  
                 line = br.readLine(); // 一次读入一行数据  
                 content.append("\n");
                 content.append(line);
             }
          
             String modelVersion = getStr(content.toString(),"<modelVersion>(.*)</modelVersion>");
    	     String groupId = getStr(content.toString(),"<groupId>(.*)</groupId>");
    	     String artifactId = getStr(content.toString(),"<artifactId>(.*)</artifactId>");
    	     String version = getStr(content.toString(),"<version>(.*)</version>");
    	     String type = getStr(content.toString(),"<version>(.*)</version>");
    	   String pathname= home.getAbsolutePath()+"\\"+groupId+"\\"+artifactId+"\\"+version+"\\"+ temp.getName();
    	   File file=new File(pathname);
    	   
    	   if(file.exists()){
    		   String deployJar = getMd5ByFile(file);
    		   String selfJar=getMd5ByFile(temp);
    		   if(deployJar.equals(selfJar)){
    			   System.out.println("\n 跳过[modelVersion="+modelVersion+"],[groupId="+groupId+"],[artifactId="+artifactId+"],[version="+version+"]");
    			   continue;
    		   }
    	   }
    	   copyFile(temp.getAbsolutePath(), tempDir.getAbsolutePath()+"\\"+temp.getName(), true);
    	   
    	   StringBuffer buffer=new StringBuffer("mvn deploy:deploy-file -Dpackaging=jar");
    	   buffer.append(" -DgroupId="+groupId);
    	   buffer.append(" -DartifactId="+artifactId);
    	   buffer.append(" -Dversion="+version);
    	   buffer.append(" -Dfile="+tempDir.getAbsolutePath()+"\\"+temp.getName().replace("\\", "/"));
    	   buffer.append(" -Durl=file:"+homePath);
    	  final String command=" cmd /c "+buffer.toString();
    	  System.out.println("\n"+command);
    	  Process call = Runtime.getRuntime().exec(command);
    	  call.waitFor();
    	  if(fileList.size()<num+5){
    		  System.out.println("\n over \n");
    	  }
    	}
     }
    public static String getMd5ByFile(File file){  
        String value = null;
        FileInputStream in=null;
    try {  
    	in = new FileInputStream(file);  
        MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        md5.update(byteBuffer);  
        BigInteger bi = new BigInteger(1, md5.digest());  
        value = bi.toString(16);  
    } catch (Exception e) {  
        e.printStackTrace();  
    } finally {  
            if(null != in) {  
                try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    return value;  
    }  
    private static String getStr(String content,String ref){
    	   Pattern p = Pattern.compile(ref);  
           Matcher m = p.matcher(content);
           m.find();
           return m.group(1);
    }
	/** 
     * 获取文件的扩展名 
     *  
     * @param filename 
     * @param defExt 
     * @return 
     */  
    public static String getExtension(String filename, String defExt) {  
        if ((filename != null) && (filename.length() > 0)) {  
            int i = filename.lastIndexOf('.');  
  
  
            if ((i > -1) && (i < (filename.length() - 1))) {  
                return filename.substring(i + 1);  
            }  
        }  
        return defExt;  
    }  
  
  
    public static String getExtension(String filename) {  
        return getExtension(filename, "");  
    }  
  
  
    /** 
     * 获取一个文件夹下的所有文件 要求：后缀名为txt (可自己修改) 
     *  
     * @param file 
     * @return 
     */  
    public static List<File> getFileList( File file,final String extension) {  
        List<File> result = new ArrayList<File>();  
        fillFileList(result, file, extension);
        return result;  
    }  
    public static void fillFileList(List<File> result,File file,final String extension) {  
            // 内部匿名类，用来过滤文件类型  
            File[] fileTemp = file.listFiles(new FileFilter() {  
                public boolean accept(File file) {  
                    if(file.isDirectory()||extension.toLowerCase().endsWith(getExtension( file.getName().toLowerCase()))){
                        return true;
                    }
                    return false;
                }
            }); 
            for (File temp : fileTemp) {
			if(temp.isDirectory()){
				fillFileList(result,temp, extension);
			}else {
				  result.add(temp);
			}
		}
    }  
  
  
  
    /** 
     * 去掉文件的扩展名 
     *  
     * @param filename 
     * @return 
     */  
    public static String trimExtension(String filename) {  
        if ((filename != null) && (filename.length() > 0)) {  
            int i = filename.lastIndexOf('.');  
            if ((i > -1) && (i < (filename.length()))) {  
                return filename.substring(0, i);  
            }  
        }  
        return filename;  
    }  
  
    /** 
     * 复制单个文件 
     *  
     * @param srcFileName 
     *            待复制的文件名 
     * @param descFileName 
     *            目标文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,   boolean overlay) {  
        File srcFile = new File(srcFileName);  
  
        // 判断源文件是否存在  
        if (!srcFile.exists()) {  
            return false;  
        } else if (!srcFile.isFile()) {  
            return false;  
        }  
  
        // 判断目标文件是否存在  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            // 如果目标文件存在并允许覆盖  
            if (overlay) {  
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
                new File(destFileName).delete();  
            }  
        } else {  
            // 如果目标文件所在目录不存在，则创建目录  
            if (!destFile.getParentFile().exists()) {  
                // 目标文件所在目录不存在  
                if (!destFile.getParentFile().mkdirs()) {  
                    // 复制文件失败：创建目标文件所在目录失败  
                    return false;  
                }  
            }  
        }  
  
        // 复制文件  
        int byteread = 0; // 读取的字节数  
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}

