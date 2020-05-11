package android.hqs.widget.siyamed;

/**
 * 各卫星深度回调接口
 * 
 * @author Siyamed SINIR
 *
 */
public interface IDegreeProvider {
	public float[] getDegrees(int count, float totalDegrees);
}
