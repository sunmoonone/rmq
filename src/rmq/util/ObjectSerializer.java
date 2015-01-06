package rmq.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectSerializer {
	/**
	 * de-serialize
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object de(byte[] bytes) throws IOException,
			ClassNotFoundException {
		if (bytes == null) {
			return null;
		}
		ClassLoadingObjectInputStream ois = null;
		// ObjectInputStream ois = null;
		try {
			ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
			ois = new ClassLoadingObjectInputStream(bs);
			// ois = new ObjectInputStream(bs);
			return ois.readObject();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * serialize
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] se(Object obj) throws IOException{
		ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bs);
            oos.writeObject(obj);
            oos.flush();
            return  bs.toByteArray();
            
        }  finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
            }
        }
	}
	
}
