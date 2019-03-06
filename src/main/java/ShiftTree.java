import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class ShiftTree {

	//constants for reasonable minimum and maximum values
	public static final int MIN_KEY = 100;
	public static final int MAX_KEY = 1000000;
	
	public static final double LOG_BASE = Math.E;
	
	//TODO remove this in production
	//private static final long RANDOM_GENERATOR_SEED = 1234567890;

	private final Random randomGen = new Random();	//can use RANDOM_GENERATOR_SEED for testing

	private static boolean DEBUG = false;
	
	private ShiftTreeNode root;
	private int currNodeCount;
	private ArrayList<Integer> relOffsetList = new ArrayList<Integer>();
	private int offsetIndex;
	private double minOffsetRange;
	private double logBasePower;
	
	public ShiftTree(ShiftTreeNode root) {
		this.root = root;
		this.currNodeCount = 1;
		this.offsetIndex = 0;
		this.logBasePower = 1;	//LOG_BASE ^ 0 = 1;
		int relOffset0 = randomGen.nextInt(root.getMaxKey() - root.getMinKey());
		relOffsetList.add(relOffset0);
		int offset0 = root.getMinKey() + relOffset0;
		root.setOffset(offset0);
		minOffsetRange = Math.min(offset0 - root.getMinKey(), root.getMaxKey() - offset0);
		if (DEBUG) {
			System.out.println(root);
		}
	}
	
	public ShiftTreeNode getRoot() {
		return root;
	}
	
	public ShiftTreeNode addValue(int i) {
		return recursiveAddNode(root, i);
	}

	private ShiftTreeNode recursiveAddNode(ShiftTreeNode node, int i) {
		int nodeValue = node.getValue();
		if (nodeValue == i) {
			//in the obfuscation application this case should not occur, 
			//as we would not try add a value that has been obfuscated already 
			//(that should be looked up and found in the hashmap)
			return node;
		}
		else if (i < nodeValue) {
			//add smaller values to the left branch
			Optional<ShiftTreeNode> leftChild = node.getLeftChild();
			if (leftChild.isPresent()) {
				return recursiveAddNode(leftChild.get(), i);
			}
			else {
				updateNodeCountAndOffsetIndex(node, i);
				
				ShiftTreeNode newNode = new ShiftTreeNode(i, node.getMinKey(), node.getOffset(), offsetIndex);
				int parentAppliedOffsetIndex = node.getAppliedOffsetIndex();
				newNode.setOffset((offsetIndex == parentAppliedOffsetIndex ? node.getOffset() : node.getOffset() - relOffsetList.get(offsetIndex)));
				updateMinimumOffsetRange(newNode, i);
				node.addLeftChild(newNode);
				if (DEBUG) {
					System.out.println(newNode);
				}
				return newNode;
			}
		}
		else {
			//add greater values to the right branch
			Optional<ShiftTreeNode> rightChild = node.getRightChild();
			if (rightChild.isPresent()) {
				return recursiveAddNode(rightChild.get(), i);
			}
			else {
				updateNodeCountAndOffsetIndex(node, i);
				
				ShiftTreeNode newNode = new ShiftTreeNode(i, node.getOffset(), node.getMaxKey(), offsetIndex);
				int parentAppliedOffsetIndex = node.getAppliedOffsetIndex();
				newNode.setOffset((offsetIndex == parentAppliedOffsetIndex ? node.getOffset() : node.getOffset() + relOffsetList.get(offsetIndex)));
				updateMinimumOffsetRange(newNode, i);
				node.addRightChild(newNode);
				if (DEBUG) {
					System.out.println(newNode);
				}
				return newNode;
			}
		}
	}

	private void updateNodeCountAndOffsetIndex(ShiftTreeNode node, int i) {
		currNodeCount++;
		if (Math.floor(logBasePower) <= currNodeCount) {
			logBasePower = logBasePower * LOG_BASE;
			offsetIndex++;
			relOffsetList.add(randomGen.nextInt( (int) Math.round((Math.floor(minOffsetRange))) + 1));
			if (DEBUG) {
				System.out.println(String.format("  Added relative offset %d: %d", offsetIndex, relOffsetList.get(offsetIndex)));
			}
		}
	}

	private void updateMinimumOffsetRange(ShiftTreeNode node, int i) {
		minOffsetRange = Math.min(minOffsetRange, Math.min(node.getOffset()-node.getMinKey(), node.getMaxKey()-node.getOffset()));
		if (DEBUG) {
			System.out.println("  Minimum offset range (for value " + i + "): " + minOffsetRange);
		}
	}
	
	
}
