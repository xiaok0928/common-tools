package com.tools.security.base64;

import com.tools.common.Validate;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Description : Base64辅助类
 * <br>Created By : xiaok0928@hotmail.com 
 * <br>Creation Time : 2018年5月14日 上午11:19:20
 */
public class Base64 {
	private static final int BASELENGTH = 256;
	private static final int LOOKUPLENGTH = 256;
	private static final int TWENTYFOURBITGROUP = 24;
	private static final int EIGHTBIT = 8;
	private static final int SIXTEENBIT = 16;
	private static final int FOURBYTE = 4;
	private static final int SIGN = -128;
	private static final char PAD = '=';
	private static final boolean fDebug = false;
	private static final byte[] base64Alphabet = new byte[BASELENGTH];
	private static final char[] lookUpBase64Alphabet = new char[LOOKUPLENGTH];
	
	static {
        for (int i = 0; i < BASELENGTH; ++i) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;

        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';
        lookUpBase64Alphabet[64] = (char) '=';

    }
	
	/**
	 * Description : 编码字符串
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:23:57 
	 * 
	 * @param data
	 * @return
	 */
	public static String encodeStringToString(String data) {
		return new String(encodeByteToString(data.getBytes()));
	}

	/**
	 * Description : 解码字符串
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:24:10 
	 * 
	 * @param data
	 * @return
	 */
	public static String decodeStringToString(String data) {
		return new String(decodeCharToByte(data.toCharArray()));
	}

	/**
	 * Description : 编码byte数组到String
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:46:50 
	 * 
	 * @param binaryData
	 * @return
	 */
	public static String encodeByteToString(byte[] binaryData) {
		
		if (binaryData == null) {
			return null;
		}

		int lengthDataBits = binaryData.length * EIGHTBIT;
		if (lengthDataBits == 0) {
			return "";
		}

		int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
		int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
		int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
		char encodedData[] = null;

		encodedData = new char[numberQuartet * 4];

		byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

		int encodedIndex = 0;
		int dataIndex = 0;
		if (fDebug) {
            System.out.println("number of triplets = " + numberTriplets);
        }

		for (int i = 0; i < numberTriplets; i++) {
			b1 = binaryData[(dataIndex++)];
			b2 = binaryData[(dataIndex++)];
			b3 = binaryData[(dataIndex++)];
			
			if (fDebug) {
                System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
            }

			l = (byte) (b2 & 0xF);
			k = (byte) (b1 & 0x3);

			byte val1 = (b1 & 0xFFFFFF80) == 0 ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xC0);
			byte val2 = (b2 & 0xFFFFFF80) == 0 ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 0xF0);
			byte val3 = (b3 & 0xFFFFFF80) == 0 ? (byte) (b3 >> 6) : (byte) (b3 >> 6 ^ 0xFC);
			
			if (fDebug) {
                System.out.println("val2 = " + val2);
                System.out.println("k4   = " + (k << 4));
                System.out.println("vak  = " + (val2 | (k << 4)));
            }

			encodedData[(encodedIndex++)] = lookUpBase64Alphabet[val1];
			encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(val2 | k << 4)];
			encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(l << 2 | val3)];
			encodedData[(encodedIndex++)] = lookUpBase64Alphabet[(b3 & 0x3F)];
		}

		if (fewerThan24bits == EIGHTBIT) {
			b1 = binaryData[dataIndex];
			k = (byte) (b1 & 0x3);
			if (fDebug) {
                System.out.println("b1=" + b1);
                System.out.println("b1<<2 = " + (b1 >> 2));
            }
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
		} else if (fewerThan24bits == SIXTEENBIT) {
			b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = PAD;
        }

        return new String(encodedData);
	}

	/**
	 * Description : 解码字符串到byte数组
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:47:43 
	 * 
	 * @param encoded
	 * @return
	 */
	public static byte[] decodeStringToByte(String encoded) {
		if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len % FOURBYTE != 0) {
            return null;//should be divisible by four
        }

        int numberQuadruple = (len / FOURBYTE);

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte decodedData[] = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];

        for (; i < numberQuadruple - 1; i++) {

            if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))
                || !isData((d3 = base64Data[dataIndex++]))
                || !isData((d4 = base64Data[dataIndex++]))) {
                return null;
            }//if found "no data" just return null

            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))) {
            return null;//if found "no data" just return null
        }

        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData((d3)) || !isData((d4))) {//Check if they are PAD characters
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xf) != 0)//last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0)//last 2 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { //No PAD e.g 3cQl
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }

        return decodedData;
	}

	/**
	 * Description : 编码byte数组到char数组
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:48:41 
	 * 
	 * @param data
	 * @return
	 */
	public static char[] encodeByteToChar(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;

			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = lookUpBase64Alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = lookUpBase64Alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = lookUpBase64Alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = lookUpBase64Alphabet[val & 0x3F];
		}
		return out;
	}

	/**
	 * Description : 解码Char数组到Byte
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:52:31 
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decodeCharToByte(char[] data) {
		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || base64Alphabet[data[ix]] < 0) {
				--tempLen; // ignore non-valid chars and padding
			}
		}

		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3) {
			len += 2;
		}
		if ((tempLen % 4) == 2) {
			len += 1;

		}
		byte[] out = new byte[len];

		int shift = 0;
		int accum = 0;
		int index = 0;

		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : base64Alphabet[data[ix]];

			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}

		if (index != out.length) {
			throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ")");
		}

		return out;
	}

	/**
	 * Description : 转码文件
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:54:10 
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void encodeFile(File file) throws Exception {
		Validate.isNotNull(file, "需要编码的文件不能为空!");
		byte[] decoded = readBytes(file);
		char[] encoded = encodeByteToChar(decoded);
		writeChars(file, encoded);
	}

	/**
	 * Description : 解码文件
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:54:30 
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void decodeFile(File file) throws Exception {
		Validate.isNotNull(file, "需要解码的文件不能为空!");
		char[] encoded = readChars(file);
		byte[] decoded = decodeCharToByte(encoded);
		writeBytes(file, decoded);
	}

	/**
	 * Description : 将文件读取成byte数组
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:55:23 
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static byte[] readBytes(File file) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = null;
		InputStream fis = null;
		InputStream is = null;
		try {
			fis = new FileInputStream(file);
			is = new BufferedInputStream(fis);
			int count = 0;
			byte[] buf = new byte[16384];
			while ((count = is.read(buf)) != -1) {
				if (count > 0) {
					baos.write(buf, 0, count);
				}
			}
			b = baos.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return b;
	}

	/**
	 * Description : 将文件读取成Char数组
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:56:24 
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static char[] readChars(File file) throws Exception {
		CharArrayWriter caw = new CharArrayWriter();
		Reader fr = null;
		Reader in = null;
		try {
			fr = new FileReader(file);
			in = new BufferedReader(fr);
			int count = 0;
			char[] buf = new char[16384];
			while ((count = in.read(buf)) != -1) {
				if (count > 0) {
					caw.write(buf, 0, count);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (caw != null) {
					caw.close();
				}
				if (in != null) {
					in.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return caw.toCharArray();
	}

	/**
	 * Description : 将byte数组转换成File文件并输出
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:57:29 
	 * 
	 * @param file
	 * @param data
	 * @throws Exception
	 */
	private static void writeBytes(File file, byte[] data) throws Exception {
		OutputStream fos = null;
		OutputStream os = null;
		try {
			fos = new FileOutputStream(file);
			os = new BufferedOutputStream(fos);
			os.write(data);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	/**
	 * Description : 将char数组转换成File文件并输出
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午9:57:55 
	 * 
	 * @param file
	 * @param data
	 * @throws IOException
	 */
	private static void writeChars(File file, char[] data) throws Exception {
		Writer fos = null;
		Writer os = null;
		try {
			fos = new FileWriter(file);
			os = new BufferedWriter(fos);
			os.write(data);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	private static boolean isWhiteSpace(char octect) {
		return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
	}

	private static boolean isPad(char octect) {
		return (octect == PAD);
	}

	private static boolean isData(char octect) {
		return (octect < BASELENGTH && base64Alphabet[octect] != -1);
	}

	private static int removeWhiteSpace(char[] data) {
		if (data == null) {
			return 0;
		}

		int newSize = 0;
		int len = data.length;
		for (int i = 0; i < len; i++) {
			if (!isWhiteSpace(data[i])) {
				data[(newSize++)] = data[i];
			}
		}
		return newSize;
	}
}