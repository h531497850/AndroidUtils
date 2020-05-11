package android.hqs.widget.tree;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 1、将用户的数据通过反射+注解的方法转换为树形数据
 *    （如果考虑到效率的问题，注解可以被命名规范所替代，如，id --> nodeId; pId --> nodePid）
 * 2、设置节点间的关联关系
 * 3、排序
 * 4、过滤出需要显示的数据
 * @author hqs2063594
 *
 */
public class TreeHelper {

	/**
	 * 1、将用户的数据通过反射+注解的方法转换为树形数据
	 * 2、设置节点间的关联关系
	 * @param datas
	 * @return
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static <T> List<TreeNode> convertDatas2Nodes(List<T> datas) throws IllegalAccessException, IllegalArgumentException{
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		TreeNode node = null;
		for (T t:datas) {
			node = new TreeNode();
			int id = -1;
			int pid = -1;
			String label = null;
			Class<? extends Object> clazz = t.getClass();
			Field[] fields = clazz.getDeclaredFields();
			// 注解定位，反射取值
			for (Field field : fields) {
				
				TreeNodeId annotation = field.getAnnotation(TreeNodeId.class);
				if (annotation != null) {
					Class<?> type = annotation.type();
					if (type == Integer.class) {
						field.setAccessible(true);
						id = field.getInt(t);
					}
				}
				
				// 下面两个反射同理
				if (field.getAnnotation(TreeNodePid.class) != null) {
					field.setAccessible(true);
					pid = field.getInt(t);
				}
				if (field.getAnnotation(TreeNodeLabel.class) != null) {
					field.setAccessible(true);
					label = (String)field.get(t);
				}
			}
			node = new TreeNode(id, pid, label);
			nodes.add(node);
		}
		
		// 设置节点间的关联关系
		for (int i = 0; i < nodes.size(); i++) {
			TreeNode n = nodes.get(i);
			for (int j = 0; j < nodes.size(); j++) {
				TreeNode m = nodes.get(j);
				if (m.getId() == n.getId()) {
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getpId()) {
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}
		
		for (TreeNode n:nodes) {
			setNodeIcon(n);
		}
		
		return nodes;
	}
	
	/**
	 * 排序
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static  <T> List<TreeNode> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException, IllegalArgumentException{
		List<TreeNode> result = new ArrayList<TreeNode>();
		List<TreeNode> nodes = convertDatas2Nodes(datas);
		
		List<TreeNode> roots = getRoootNodes(nodes);
		
		for (TreeNode node : roots) {
			addNode(result, node, defaultExpandLevel, 1);
		}
		
		return result;
	}
	
	/**
	 * 过滤出需要显示的数据
	 * @param nodes
	 * @return
	 */
	public static List<TreeNode> filterVisibleNodes(List<TreeNode> nodes) {
		List<TreeNode> result = new ArrayList<TreeNode>();
		
		for (TreeNode node : nodes) {
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		
		return result;
	}

	/**
	 * 把一个节点的所有子节点都放入result
	 * @param result
	 * @param node
	 * @param defaultExpandLevel
	 * @param i
	 */
	private static void addNode(List<TreeNode> result, TreeNode node,
			int defaultExpandLevel, int currentLevel) {
		result.add(node);
		if (defaultExpandLevel >= currentLevel) {
			node.setExpand(true);
		}
		if (node.isLeaf()) {
			return;
		}
		for (int i = 0; i < node.getChildren().size(); i++) {
			addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
		}
	}

	/**
	 * 从所有节点中过滤出根节点
	 * @param nodes
	 * @return
	 */
	private static List<TreeNode> getRoootNodes(List<TreeNode> nodes) {
		List<TreeNode> root = new ArrayList<TreeNode>();
		for (TreeNode node : nodes) {
			if (node.isRoot()) {
				root.add(node);
			}
		}
		return root;
	}

	/**
	 * 为Node设置图标
	 * @param n
	 */
	private static void setNodeIcon(TreeNode n) {
		if (n.getChildren().size() > 0 && n.isExpand()) {
			// 展开
			//n.setIcon(icon);
		} else if (n.getChildren().size() > 0 && !n.isExpand()) {
			// 不展开
			//n.setIcon(icon);
		} else {
			// 无图标
			n.setIcon(-1);
		}
	}

}
