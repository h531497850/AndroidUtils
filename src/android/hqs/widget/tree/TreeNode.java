package android.hqs.widget.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	private int id;

	/**
	 * 根节点的pid=0 
	 */
	private int pId = 0;
	private String name;
	/**
	 * 树的层级，自己计算
	 */
	private int level;
	/**
	 * 是否展开
	 */
	private boolean isExpand = false;
	private int icon;
	
	private TreeNode parent;
	private List<TreeNode> children = new ArrayList<TreeNode>();
	
	public TreeNode() {}
	
	public TreeNode(int id, int pId, String name) {
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public int getId() {
		return id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 得到当前节点的层级
	 * @return
	 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isExpand() {
		return isExpand;
	}
	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
		if (!isExpand) {
			for (TreeNode node : children) {
				node.setExpand(false);
			}
		}
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	/**
	 * 是否是根节点
	 * @return
	 */
	public boolean isRoot() {
		return parent == null;
	}
	
	/**
	 * 判断当前父节点的收缩状态
	 * @return
	 */
	public boolean isParentExpand(){
		if (parent == null) {
			return false;
		}
		return parent.isExpand();
	}
	
	/**
	 * 是否时叶子节点
	 * @return
	 */
	public boolean isLeaf() {
		return children.size() == 0;
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", pId=" + pId + ", name=" + name
				+ ", level=" + level + ", isExpand=" + isExpand + ", icon="
				+ icon + ", parent=" + parent + ", children=" + children + "]";
	}
	
}
