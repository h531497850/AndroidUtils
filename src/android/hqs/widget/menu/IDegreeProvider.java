package android.hqs.widget.menu;

/**
 * 各卫星深度回调接口
 * 
 * @author Siyamed SINIR
 *
 */
public interface IDegreeProvider {
	public float[] getDegrees(int count, float totalDegrees);
}
