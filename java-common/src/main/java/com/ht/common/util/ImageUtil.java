//package com.ht.common.util;
//
//import com.gif4j.GifDecoder;
//import com.gif4j.GifEncoder;
//import com.gif4j.GifImage;
//import com.gif4j.GifTransformer;
//import net.coobird.thumbnailator.Thumbnails;
//import net.coobird.thumbnailator.Thumbnails.Builder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
///**
// * @description 图像压缩
// * @author bb.h
// * @date 2016-12-10上午11:00:48
// */
//public class ImageUtil {
//
//	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
//
//	/**
//	 * 图片文件压缩
//	 * 
//	 * @param imgPath
//	 * @param zipImg
//	 * @param width
//	 * @param height
//	 * @param keepRatio
//	 *            是否按比例压缩
//	 * @return
//	 * @throws IOException
//	 */
//	public static boolean doZip(String imgPath, String zipImg, Integer width, Integer height, boolean keepRatio)
//			throws IOException {
//		if (keepRatio) {
//			Builder<File> builder = Thumbnails.of(new File(imgPath));
//			if (width == 0) {
//				builder.height(height).keepAspectRatio(keepRatio).toFile(zipImg);
//			}
//			if (height == 0) {
//				builder.width(width).keepAspectRatio(keepRatio).toFile(zipImg);
//			}
//		} else {
//			doZip(imgPath, zipImg, width, height);
//		}
//		return true;
//	}
//
//	/**
//	 * 按照固定width，height进行压缩，并生成文件到服务器
//	 * 
//	 * @param imgPath
//	 * @param zipImg
//	 * @param width
//	 * @param height
//	 * @return
//	 * @throws IOException
//	 */
//	private static boolean doZip(String imgPath, String zipImg, Integer width, Integer height) throws IOException {
//		Builder<File> builder = Thumbnails.of(new File(imgPath));
//		builder.forceSize(width, height);
//		builder.toFile(new File(zipImg));
//		return true;
//	}
//
//	/**
//	 * 动态图片gif图片压缩
//	 * 
//	 * @param imgPath
//	 * @param zipImg
//	 * @param width
//	 * @param height
//	 * @return
//	 */
//	public static boolean resizeGif(String imgPath, String zipImg, Integer width, Integer height, boolean keepRatio) {
//		boolean flag = false;
//		GifImage gifImage = null;
//		GifImage resizeIMG = null;
//		OutputStream os = null;
//		try {
//			// 根据width,height重新生成文件名
//			zipImg = getZipName(imgPath, width, height);
//			os = new FileOutputStream(zipImg);
//			gifImage = GifDecoder.decode(new File(imgPath));
//			if (keepRatio) {
//				if (width == null || width == 0) {
//					resizeIMG = GifTransformer.resize(gifImage, -1, height, true);
//				}
//				if (height == null || height == 0) {
//					resizeIMG = GifTransformer.resize(gifImage, width, -1, true);
//				}
//			} else {
//				resizeIMG = GifTransformer.resize(gifImage, width, height, true);
//			}
//			GifEncoder.encode(resizeIMG, os);
//			flag = true;
//		} catch (IOException e) {
//			logger.error("压缩异常 imgPath:{} width:{} height:{} keepRatio:{} ", e);
//		} finally {
//			try {
//				if (null != os) {
//					os.close();
//				}
//			} catch (IOException e) {
//				logger.error("关闭 OutputStream 异常", e);
//			}
//		}
//		return flag;
//	}
//
//	/**
//	 * 根据width，height生成压缩后的文件名
//	 * 
//	 * @param imgPath
//	 * @param width
//	 * @param height
//	 * @return
//	 */
//	public static String getZipName(String imgPath, Integer width, Integer height) {
//		File file = new File(imgPath);
//		String imgNameTemp = file.getName();
//		String imgExt = FileUtil.getExtName(imgNameTemp);
//		String imgName = imgNameTemp.substring(0, file.getName().lastIndexOf("."));
//		String basePath = file.getParent();
//		if (width != 0 || height != 0) {
//			imgName = imgName + "_" + width + "x" + height + "." + imgExt;
//		}
//		if ((width == null || width == 0) && (height == null || height == 0)) {
//			imgName = imgName + "." + imgExt;
//		}
//		return basePath + File.separator + imgName;
//	}
//
//	/**
//	 * 图片文件压缩
//	 * @param imgPath  图片源路径
//	 * @param width     压缩宽度
//	 * @param height    压缩高度
//	 * @return
//	 */
//
//	public static String compress(String imgPath, Integer width, Integer height) {
//		String zipImg = "";
//		try {
//			// 不压缩
//			if ((width==null || width == 0) && (height==null ||height == 0)) {
//				return imgPath;
//			}
//			if(height==null)  height=0;
//			if(width==null)  width=0;
//
//			// 根据width,height重新生成文件名, 并校验文件是否存在，如果存在则不用压缩直接返回
//			zipImg = getZipName(imgPath, width, height);
//			if (new File(zipImg).exists()) {
//				return zipImg;
//			}
//
//			boolean keepRatio = false;
//			// 如果存在wigth或height为空，则按照比例压缩
//			if ((width==null || width == 0) || (height==null ||height == 0)) {
//				keepRatio = true;
//			}
//			// 执行压缩
//			long time = System.currentTimeMillis();
//			//获取文件扩展名类型
//			File tempfile=new File(imgPath);
//			String extType=FileUtil.getExtName(tempfile.getName());
//			if("gif".equalsIgnoreCase(extType)){
//				//动态图片压缩
//				resizeGif(imgPath, zipImg, width, height, keepRatio);
//			}else{
//				doZip(imgPath, zipImg, width, height, keepRatio);
//			}
//			logger.info("压缩耗时:" + (System.currentTimeMillis() - time) + "ms");
//		} catch (Exception e) {
//			logger.error("压缩异常 imgPath:{} width:{} height:{} keepRatio:{}", e);
//		}
//		return zipImg;
//	}
//}
