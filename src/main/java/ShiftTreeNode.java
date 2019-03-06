import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShiftTreeNode {
	
	private Optional<ShiftTreeNode> right = Optional.empty();
	private Optional<ShiftTreeNode> left = Optional.empty();

	private int value;
	private int minKey = 1;
	private int maxKey = Integer.MAX_VALUE;
	private int offset;
	private int appliedOffsetIndex;
	
	public ShiftTreeNode (int v, int minKey, int maxKey, int appliedOffsetIndex) {
		this.value = v;
		this.minKey = minKey;
		this.maxKey = maxKey;
		this.appliedOffsetIndex = appliedOffsetIndex;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public int getAppliedOffsetIndex() {
		return appliedOffsetIndex;
	}
	
	public void addRightChild(ShiftTreeNode node) {
		if (node != null) {
			this.right = Optional.of(node);
		}
	}

	public void addLeftChild(ShiftTreeNode node) {
		if (node != null) {
			this.left = Optional.of(node);
		}
	}

	public int getValue() {
		return value;
	}
	
	public int getMinKey() {
		return minKey;
	}

	public int getMaxKey() {
		return maxKey;
	}

	public Optional<ShiftTreeNode> getLeftChild() {
		return left;
	}

	public Optional<ShiftTreeNode> getRightChild() {
		return right;
	}

	public List<ShiftTreeNode> getSuccessors() {
		var result = new ArrayList<ShiftTreeNode>();
		left.ifPresent(result::add);
		right.ifPresent(result::add);
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("(%s, %d, [%s,%s]/offset-%s)", value, offset, minKey, maxKey, appliedOffsetIndex );
	}
}
