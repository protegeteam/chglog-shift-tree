import com.google.common.graph.Traverser;
import edu.stanford.protege.shifttree.ShiftTree;
import edu.stanford.protege.shifttree.ShiftTreeNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class ShiftTreeTest {

	public static void main(String[] args) {
		if (args.length > 0) {
			testWithInputFile(args[0]);
		}
		else {
			basicTest();
			//randomTreeTest(500, 1, 1000, 0);
			//randomTreeTest(500, 1, 1000, 100);
			//randomTreeTest(1000, Tree.MIN_KEY, Tree.MAX_KEY, 0);
			randomTreeTest(100, ShiftTree.MIN_KEY, ShiftTree.MAX_KEY, 23000);
		}
	}

	private static void testWithInputFile(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
			System.out.println("File " + fileName+ " does not exist");
		}
		else if (!f.canRead()) {
			System.out.println("Cannot read input file " + fileName);
		}
		else {
			try {
				BufferedReader reader = new BufferedReader( new FileReader(f) );
				String line = reader.readLine();
				if (line != null) {
					int value = Integer.parseInt(line);
					
					ShiftTree tree = new ShiftTree(new ShiftTreeNode(value, ShiftTree.MIN_KEY, ShiftTree.MAX_KEY, 0));
			        while ((line = reader.readLine()) != null) {     
			        	value = Integer.parseInt(line);
			        	tree.addValue(value);
					}
					
					printTree(tree);
				}				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void basicTest() {
		System.out.println("Basic test:");
		ShiftTree tree = new ShiftTree(new ShiftTreeNode(5, ShiftTree.MIN_KEY, ShiftTree.MAX_KEY, 0));
		tree.addValue(4);
		tree.addValue(8);
		tree.addValue(12);
		tree.addValue(6);
		tree.addValue(10);
		tree.addValue(1);
		tree.addValue(15);
		tree.addValue(2);

		printTree(tree);
	}

	private static void randomTreeTest(int nodeCount, int minKey, int maxKey, int maxValue) {
		System.out.println(String.format("Testing random tree generator: node count: %d, min key: %d, max key: %d", nodeCount, minKey, maxKey));
		Random random = new Random();
		ShiftTree tree = new ShiftTree(new ShiftTreeNode((maxValue > 0 ? random.nextInt(maxValue) : random.nextInt()), minKey, maxKey, 0));
		for (int i=1; i < nodeCount; i++) {
			tree.addValue((maxValue > 0 ? random.nextInt(maxValue) : random.nextInt()));
		}

		printTree(tree);
	}

	private static void printTree(ShiftTree tree) {
		System.out.println("\nSorted:");
		ShiftTreeNode root = tree.getRoot();
		var traverser = Traverser.forTree(ShiftTreeNode::getSuccessors);
		traverser.depthFirstPreOrder(root).forEach(t -> System.out.println(t.toString()));
	}
}
