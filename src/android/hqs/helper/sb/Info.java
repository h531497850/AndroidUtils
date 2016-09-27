package android.hqs.helper.sb;

/**
 * 记录{@link SeekBar}中各种图片的索引以及id
 * @author hqs2063594
 */
public class Info {
	private int index;
	private int id;
	
	public Info(int index, int id){
		this.index = index;
		this.id = id;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Info [index=" + index + ", id=" + id + "]";
	}
	
}