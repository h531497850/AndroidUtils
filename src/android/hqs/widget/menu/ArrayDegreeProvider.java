package android.hqs.widget.menu;

/**
 * 各卫星弧度实现类，可由{@link #SatelliteMenu}提供参数。
 *  
 * @author Siyamed SINIR
 *
 */
public class ArrayDegreeProvider implements IDegreeProvider {
	private float[] degrees;
	
	public ArrayDegreeProvider(float[] degrees) {
		this.degrees = degrees;
	}
	
	/**
	 * 如果数据不为空，且请求数据个数与存储的相等，返回各菜单弧度。
	 */
	public float[] getDegrees(int count, float totalDegrees){
		if(degrees == null || degrees.length != count){
            throw new IllegalArgumentException("Provided delta degrees and the action count are not the same.");
        }
		return degrees; 
	}
}
