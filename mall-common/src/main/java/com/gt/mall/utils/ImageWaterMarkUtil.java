package com.gt.mall.utils;

import net.sf.json.JSONArray;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * 水印工具类
 *
 * @author Ryan
 */
public class ImageWaterMarkUtil {

    private BufferedImage buffImg = null;

    private Image srcImg = null;

    @SuppressWarnings( "unused" )
    private ImageWaterMarkUtil() {
    }

    /**
     * 水印类构造器
     *
     * @param srcImgPath 需要加水印的图片路径
     *
     * @throws Exception
     */
    public ImageWaterMarkUtil( String srcImgPath ) throws Exception {
        srcImg = ImageIO.read( new File( srcImgPath ) );
        buffImg = new BufferedImage( this.getImgWidth(), this.getImgHeight(), BufferedImage.TYPE_INT_RGB );
    }

    /**
     * 获取图片宽度
     *
     * @return
     */
    public int getImgWidth() {
        return srcImg.getWidth( null );
    }

    /**
     * 获取图片高度
     *
     * @return
     */
    public int getImgHeight() {
        return srcImg.getHeight( null );
    }

    /**
     * 获取文字长度
     *
     * @param waterMarkContent 水印文字
     * @param font             字体
     *
     * @return
     */
    public int getWatermarkLength( String waterMarkContent, Font font ) {
        Graphics2D g = buffImg.createGraphics();
        g.setFont( font );
        return g.getFontMetrics( g.getFont() ).charsWidth( waterMarkContent.toCharArray(), 0, waterMarkContent.length() );
    }

    /**
     * 给图片添加水印、可设置水印图片位置与角度
     *
     * @param iconPath   水印图片路径
     * @param targerPath 目标图片路径
     * @param degree     水印图片旋转角度
     * @param alpha      水印图片透明度
     * @param x          位于源图x轴位置
     * @param y          位于源图y轴位置
     * @param w          缩放水印图的宽度
     * @param h          缩放水印图的高度
     */
    public void markImageByIcon( String iconPath, String targerPath, Integer degree,
        Float alpha, int x, int y, Integer w, Integer h ) {
        OutputStream os = null;
        try {
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );

            g.drawImage( srcImg.getScaledInstance( srcImg.getWidth( null ), srcImg.getHeight( null ), Image.SCALE_SMOOTH ), 0, 0, null );

            if ( null != degree ) {
                // 设置水印旋转
                g.rotate( Math.toRadians( degree ), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2 );
            }

            if ( alpha == null || alpha > 1 ) {
                // 设置水印透明度
                alpha = (float) 1;
            }
            // 得到Image对象。
            Image img = null;
            if ( w == null || h == null ) {
                // 水印图象的路径 水印一般为gif或者png的
                ImageIcon imgIcon = new ImageIcon( iconPath );
                img = imgIcon.getImage();
            } else {
                // 图片缩放
                img = zoomImage( iconPath, w, h );
            }
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_ATOP, alpha ) );
            // 表示水印图片的位置
            g.drawImage( img, x, y, null );
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER ) );
            g.dispose();
            os = new FileOutputStream( targerPath );
            // 生成图片
            ImageIO.write( buffImg, targerPath.substring( targerPath.lastIndexOf( "." ) + 1 ), os );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                if ( null != os ) {
                    os.close();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片添加文字水印
     *
     * @param targerPath       添加水印后图片输出路径
     * @param waterMarkContent 水印的文字
     * @param color            水印文字的颜色
     * @param font             水印文字的字体
     * @param alpha            水印文字的透明度
     * @param x                位于源图x轴位置
     * @param y                位于源图y轴位置
     */
    public void markImageByText( String targerPath, String waterMarkContent, Color color, Font font,
        Integer degree, Float alpha, int x, int y ) {
        OutputStream os = null;
        try {
            // 加水印
            Graphics2D g = buffImg.createGraphics();
            g.drawImage( srcImg.getScaledInstance( this.getImgWidth(), this.getImgHeight(), Image.SCALE_SMOOTH ), 0, 0, null );
            g.setColor( color ); //根据图片的背景设置水印颜色
            g.setFont( font );  // 设置字体
            if ( null != degree ) {
                // 设置水印旋转
                g.rotate( Math.toRadians( degree ), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2 );
            }
            if ( alpha == null || alpha > 1 ) {
                // 设置水印透明度
                alpha = (float) 1;
            }
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_ATOP, alpha ) ); // 设置透明度
            g.drawString( waterMarkContent, x, y );  // 设置位置
            g.dispose();
            // 输出图片
            os = new FileOutputStream( targerPath );
            ImageIO.write( buffImg, targerPath.substring( targerPath.lastIndexOf( "." ) + 1 ), os );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                if ( null != os ) {
                    os.close();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片缩放
     *
     * @param src 源文件
     * @param w   宽度
     * @param h   高度
     *
     * @throws Exception
     */
    public static Image zoomImage( String src, int w, int h ) throws Exception {
        double wr = 0, hr = 0;
        File srcFile = new File( src );
        BufferedImage bufImg = ImageIO.read( srcFile );
        Image Itemp = bufImg.getScaledInstance( w, h, bufImg.SCALE_SMOOTH );
        wr = w * 1.0 / bufImg.getWidth();
        hr = h * 1.0 / bufImg.getHeight();
        AffineTransformOp ato = new AffineTransformOp( AffineTransform.getScaleInstance( wr, hr ), null );
        Itemp = ato.filter( bufImg, null );
        return Itemp;
    }

    /**
     * 给图片添加水印、可设置水印图片位置与角度(多位置)
     *
     * @param iconPath   水印图片路径
     * @param targerPath 目标图片路径
     * @param degree     水印图片旋转角度
     * @param alpha      水印图片透明度
     * @param x          位于源图x轴位置
     * @param y          位于源图y轴位置
     * @param w          缩放水印图的宽度
     * @param h          缩放水印图的高度
     */
    public void markImageByIconMore( String iconPath, String targerPath, Integer degree,
        Float alpha, int[] x, int[] y, Integer w, Integer h ) {
        OutputStream os = null;
        try {
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );

            g.drawImage( srcImg.getScaledInstance( srcImg.getWidth( null ), srcImg.getHeight( null ), Image.SCALE_SMOOTH ), 0, 0, null );

            if ( null != degree ) {
                // 设置水印旋转
                g.rotate( Math.toRadians( degree ), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2 );
            }

            if ( alpha == null || alpha > 1 ) {
                // 设置水印透明度
                alpha = (float) 1;
            }
            // 得到Image对象。
            Image img = null;
            if ( w == null || h == null ) {
                // 水印图象的路径 水印一般为gif或者png的
                ImageIcon imgIcon = new ImageIcon( iconPath );
                img = imgIcon.getImage();
            } else {
                // 图片缩放
                img = zoomImage( iconPath, w, h );
            }
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_ATOP, alpha ) );
            // 表示水印图片的位置
            for ( int i = 0; i < x.length; i++ ) {
                g.drawImage( img, x[i], y[i], null );
            }
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER ) );
            g.dispose();
            os = new FileOutputStream( targerPath );
            // 生成图片
            ImageIO.write( buffImg, targerPath.substring( targerPath.lastIndexOf( "." ) + 1 ), os );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                if ( null != os ) {
                    os.close();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片添加文字水印 （多位置）
     *
     * @param targerPath       添加水印后图片输出路径
     * @param waterMarkContent 水印的文字
     * @param color            水印文字的颜色
     * @param font             水印文字的字体
     * @param alpha            水印文字的透明度
     * @param x                位于源图x轴位置
     * @param y                位于源图y轴位置
     */
    public void markImageByTextMore( String targerPath, String waterMarkContent, Color color, Font font,
        Integer degree, Float alpha, int[] x, int[] y ) {
        OutputStream os = null;
        try {
            // 加水印
            Graphics2D g = buffImg.createGraphics();
            g.drawImage( srcImg.getScaledInstance( this.getImgWidth(), this.getImgHeight(), Image.SCALE_SMOOTH ), 0, 0, null );
            g.setColor( color ); //根据图片的背景设置水印颜色
            g.setFont( font );  // 设置字体
            if ( null != degree ) {
                // 设置水印旋转
                g.rotate( Math.toRadians( degree ), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2 );
            }
            if ( alpha == null || alpha > 1 ) {
                // 设置水印透明度
                alpha = (float) 1;
            }
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_ATOP, alpha ) ); // 设置透明度
            for ( int i = 0; i < x.length; i++ ) {
                g.drawString( waterMarkContent, x[i], y[i] );  // 设置位置
            }
            g.dispose();

            // 输出图片
            os = new FileOutputStream( targerPath );
            ImageIO.write( buffImg, targerPath.substring( targerPath.lastIndexOf( "." ) + 1 ), os );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                if ( null != os ) {
                    os.close();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给图片添加水印、可设置水印图片位置与角度(多位置)
     *
     * @param iconPathStr 水印图片路径
     * @param targerPath  目标图片路径
     * @param degree      水印图片旋转角度
     * @param alpha       水印图片透明度
     * @param x           位于源图x轴位置
     * @param y           位于源图y轴位置
     * @param w           缩放水印图的宽度
     * @param h           缩放水印图的高度
     */
    public void markImageByIconMoreImage( String[] iconPathStr, String targerPath, Integer degree,
        Float alpha, int[] x, int[] y, int[] w, int[] h, String newPath ) {
        OutputStream os = null;
        try {
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );

            g.drawImage( srcImg.getScaledInstance( srcImg.getWidth( null ), srcImg.getHeight( null ), Image.SCALE_SMOOTH ), 0, 0, null );

            if ( null != degree ) {
                // 设置水印旋转
                g.rotate( Math.toRadians( degree ), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2 );
            }

            if ( alpha == null || alpha > 1 ) {
                // 设置水印透明度
                alpha = (float) 1;
            }
            // 得到Image对象。
            Image img = null;
            if ( iconPathStr != null && iconPathStr.length > 0 ) {
                for ( int i = 0; i < iconPathStr.length; i++ ) {
                    String iconPath = iconPathStr[i];
                    if ( w == null || h == null ) {
                        // 水印图象的路径 水印一般为gif或者png的
                        ImageIcon imgIcon = new ImageIcon( iconPath );
                        img = imgIcon.getImage();
                    } else {
                        // 图片缩放
                        img = zoomImage( iconPath, w[i], h[i] );
                    }
                    g.drawImage( img, x[i], y[i], null );
                }
            }

            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_ATOP, alpha ) );
            // 表示水印图片的位置
        /*for(int i = 0; i < x.length; i++){
		    g.drawImage(img, x[i], y[i], null);
	    }*/
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER ) );
            g.dispose();
            //System.out.println(newPath);
            File file = new File( newPath.substring( 0, newPath.lastIndexOf( "/" ) ) );
            if ( !file.exists() && !file.isDirectory() ) {
                boolean flag = file.mkdirs();
                if ( !flag ) {
                    System.out.println( "创建文件失败 = " );
                }
            }
            os = new FileOutputStream( newPath );
            // 生成图片
            ImageIO.write( buffImg, targerPath.substring( targerPath.lastIndexOf( "." ) + 1 ), os );

        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                if ( null != os ) {
                    os.close();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        try {
            ContinueFTP myFtp = new ContinueFTP();
            myFtp.upload( newPath );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    //    public static void main( String[] args ) {
    //	try {
    //	    String twoCodePath = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHT8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyZERDdUJHdEhmZzIxMDAwMHcwN3MAAgQ5g1xYAwQAAAAA";
    //
    //	    String headPath = "http://wx.qlogo.cn/mmopen/ajNVdqHZLLCCf4dqcCCe7dIRIOpVZvgQ5NZySGgPfsibxNiaQhunnusJMGfT7V5OZW0hvWCd6BdsE2ubLHibgVcOg/0";
    //
    //	    String path = "E:/images/bg-poster.png";
    //
    //	    String nowDate = DateTimeKit.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD );
    //
    //	    //String twoCodePath = seller.getQrCodePath();
    //	    String newPaths = PropertiesUtil.getResImagePath() + "/temp/mall/" + nowDate;
    //	    twoCodePath = URLConnectionDownloader.downloadRqcode( twoCodePath, newPaths, 240, 240 );
    //
    //	    //String headPath = member.getHeadimgurl();
    //	    headPath = URLConnectionDownloader.downloadRqcode( headPath, newPaths, 90, 90 );
    //	    System.out.println( "headPath:" + headPath );
    //
    //	    JSONArray arr = new JSONArray();
    //	    arr.add( twoCodePath );
    //	    if ( CommonUtil.isNotEmpty( headPath ) ) {
    //		arr.add( headPath );
    //	    }
    //	    String[] logoPathStr = (String[]) JSONArray.toArray( JSONArray.fromObject( arr ), String.class );
    //
    //			/*String imagePath = PropertiesUtil.getResImagePath();
    //			imagePath = imagePath.substring(0	, imagePath.indexOf("/upload"));*/
    //
    //	    String suffix = path.substring( path.lastIndexOf( "." ) + 1 );
    //
    //	    String newPath = PropertiesUtil.getResImagePath() + "/temp/mall";
    //	    newPath += "/" + nowDate + "/" + System.currentTimeMillis() + "." + suffix;
    //
    //	    File file = new File( path );
    //	    path = file.getCanonicalPath();
    //	    ImageWaterMarkUtil iwm = new ImageWaterMarkUtil( path );
    //
    //	    int[] w = new int[logoPathStr.length];//水印图片的宽度
    //	    int[] h = new int[logoPathStr.length];//水印图片的高度
    //	    int[] x = new int[2];//水印的x坐标
    //	    int[] y = new int[2];//水印的y左边
    //	    System.out.println( "logoPathStr:" + logoPathStr.length );
    //	    for ( int i = 0; i < logoPathStr.length; i++ ) {
    //		String logo = logoPathStr[i];
    //
    //		ImageWaterMarkUtil iwmIcon = new ImageWaterMarkUtil( logo );
    //
    //		w[i] = iwmIcon.getImgWidth();
    //		h[i] = iwmIcon.getImgHeight();
    //		System.out.println( "logo:" + logo );
    //		System.out.println( w[i] );
    //		System.out.println( h[i] );
    //	    }
    //
    //	    x[0] = (int) ( iwm.getImgWidth() * 0.13 );
    //	    y[0] = (int) ( iwm.getImgHeight() * 0.59 );
    //	    x[1] = (int) ( iwm.getImgWidth() * 0.5 );
    //	    y[1] = (int) ( iwm.getImgHeight() * 0.59 );
    //
    //	    Float myfloat;
    //	    try {
    //		myfloat = new Float( 100 / 100 );
    //	    } catch ( Exception e ) {
    //		myfloat = null;
    //	    }
    //	    iwm.markImageByIconMoreImage( logoPathStr, path, 0, myfloat, x, y, w, h, newPath );
    //
    //	    System.out.println( newPath );
    //
    //	    //删除下载的二维码、用户头像和背景图片
    //	    if ( logoPathStr != null && logoPathStr.length > 0 ) {
    //		for ( int i = 0; i < logoPathStr.length; i++ ) {
    //		    File files = new File( logoPathStr[i] );
    //		    if ( files.exists() ) {
    //			boolean flag = files.delete();//删除水印图片
    //			if ( !flag ) {
    //			    System.out.println( "删除图片失败 = " );
    //			}
    //		    }
    //		}
    //	    }
    //			/*File pathFile = new File(path);
    //			if(pathFile.exists()) {
    //				pathFile.delete();//删除背景图片
    //			}*/
    //
    //	} catch ( Exception e ) {
    //	    e.printStackTrace();
    //	}
    //    }

}
