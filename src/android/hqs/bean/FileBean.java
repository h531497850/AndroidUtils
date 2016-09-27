package android.hqs.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.hqs.widget.tree.TreeNodeId;
import android.hqs.widget.tree.TreeNodeLabel;
import android.hqs.widget.tree.TreeNodePid;
import android.util.Log;

public class FileBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@TreeNodeId(type = Integer.class)
	private int id;
	/**
	 * 指向哪个父节点
	 */
	@TreeNodePid(type = Integer.class)
	private int pId;
	@TreeNodeLabel(type = String.class)
	private String label;
	private String desc;
	
	private byte age;
	private long phone;
	private String name;
	
	private List<FileBean> friends;
	 
	public List<FileBean> getFriends() {
		return friends;
	}
	public void setFriends(List<FileBean> friends) {
		this.friends = friends;
	}
	public int getId() {
		return id;
	}
	public byte getAge() {
		return age;
	}
	public void setAge(byte age) {
		this.age = age;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	private FileBean createSrc(){
		FileBean src = new FileBean();  
		src.setName("Yaoming");  
		src.setAge((byte)30);  
		src.setPhone(13789878978L);  
		      
		FileBean f1 = new FileBean();  
		f1.setName("tmac");  
		f1.setAge((byte)32);  
		f1.setPhone(138999898989L);  
		FileBean f2 = new FileBean();  
		f2.setName("liuwei");  
		f2.setAge((byte)29);  
		f2.setPhone(138999899989L);  
		          
		List<FileBean> friends = new ArrayList<FileBean>();  
		friends.add(f1);  
		friends.add(f2);  
		src.setFriends(friends);
		
		return src;
	}
	
	public byte[] serial(){
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	    ObjectOutputStream os = null;
	    byte[] b = null;
		try {
			os = new ObjectOutputStream(bos);
			os.writeObject(createSrc());  
		    b = bos.toByteArray();  
		} catch (IOException e) {
			Log.e("Test", "serial", e);
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close(); 
				} catch (IOException e) {}  
			}
		    try {
				bos.close();
			} catch (IOException e) {}  
		}
		return b;
	}
	
	public FileBean reversedSerial(InputStream fis){
		ObjectInputStream ois = null;
		FileBean vo = null;
		try {
			ois = new ObjectInputStream(fis);
			vo = (FileBean) ois.readObject();  
		} catch (StreamCorruptedException e) {
			Log.e("Test", "reversedSerial", e);
		} catch (IOException e) {
			Log.e("Test", "reversedSerial", e);
		} catch (ClassNotFoundException e) {
			Log.e("Test", "reversedSerial", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {}  
			}
			try {
				fis.close();
			} catch (IOException e) {}
		}
		return vo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + id;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + pId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileBean other = (FileBean) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (id != other.id)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (pId != other.pId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FileBean [id=" + id + ", pId=" + pId + ", label=" + label
				+ ", desc=" + desc + "]";
	}

}
