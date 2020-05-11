package android.hqs.widget.siyamed;

/**
 * 各卫星菜单的默认弧度。<br>
 * 如果是3个菜单，根据总弧度数将菜单放在中间；如果多于3个，均匀使用最小最大弧度。
 *  
 * @author Siyamed SINIR
 *
 */
public class DefaultDegreeProvider implements IDegreeProvider {
	
	public float[] getDegrees(int count, float totalDegrees){
		if(count < 1)
        {
            return new float[]{};
        }

        float[] result = null;
        int tmpCount = 0;
        if(count < 4){
            tmpCount = count+1;
        }else{
            tmpCount = count-1;
        }
        
        result = new float[count];
        float delta = totalDegrees / tmpCount;
        
        for(int index=0; index<count; index++){
            int tmpIndex = index;
            if(count < 4){
                tmpIndex = tmpIndex+1;
            }
            result[index] = tmpIndex * delta;
        }
        
        return result;
	}
	
}
