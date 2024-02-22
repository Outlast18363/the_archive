import java.io.*;

import static java.lang.Math.*;
import static java.lang.System.in;
import static java.lang.System.out;

public class Main {


	static final PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
	static final Writer writer = new BufferedWriter(pr);
	static final BufferedReader buff = new BufferedReader(new InputStreamReader(in));
	static Read read = new Read();


	public static final void main(String[] args) throws Exception {
		AVLTree<Integer> tree = new AVLTree<>();

		//
		int t = read.nextInt();
		while (t-- > 0) {
			int op = read.nextInt();
			int x = read.nextInt();
			if (op == 1)
				tree.insert(x);
			else if (op == 2)
				tree.delete(x);
			else if (op == 3)
				pr.println(tree.getRank(x));
			else if (op == 4)
				pr.println(tree.kth(x));
			else if (op == 5)
				pr.println(tree.getPre(x));
			else
				pr.println(tree.getNext(x));
		}


		pr.close();
	}

	/**
	 * 语言: Java
	 *
	 * @author Qing
	 * @date 2023/11/07
	 */

	static class AVLTree<T extends Comparable<T>> {
		public AVLTreeNode<T> root;    // 根结点

		// AVL树的节点(内部类)
		static class AVLTreeNode<T extends Comparable<T>> {
			T key;                // 关键字(键值)
			int height;         // 高度
			AVLTreeNode<T> left;    // 左孩子
			AVLTreeNode<T> right;    // 右孩子

			int count;//相同权值出现的次数
			int size;

			private AVLTreeNode(T key, AVLTreeNode<T> left, AVLTreeNode<T> right) {
				this.key = key;
				this.left = left;
				this.right = right;
				this.height = 0;
			}
		}

		// 构造函数
		public AVLTree() {
			root = null;
		}

		private int height(AVLTreeNode<T> tree) {
			if (tree != null)
				return tree.height;
			return 0;
		}

		/**
		 * 求结点的高度
		 *
		 * @return
		 */
		private int height() {
			return height(root);
		}


		/**
		 * 返回最大值
		 *
		 * @param a
		 * @param b
		 * @return
		 */
		private int max(int a, int b) {
			return a > b ? a : b;
		}


		/**
		 * (递归实现)查找"AVL树x"中键值为key的节点
		 *
		 * @param x
		 * @param key
		 * @return
		 */
		private AVLTreeNode<T> search(AVLTreeNode<T> x, T key) {
			if (x == null)
				return x;
			int cmp = key.compareTo(x.key);
			if (cmp < 0)
				return search(x.left, key);
			else if (cmp > 0)
				return search(x.right, key);
			else
				return x;
		}

		private AVLTreeNode<T> search(T key) {
			return search(root, key);
		}


		/**
		 * 查找最小结点
		 *
		 * @param tree
		 * @return 返回tree为根结点的AVL树的最小结点。
		 */
		private AVLTreeNode<T> minimum(AVLTreeNode<T> tree) {
			if (tree == null)
				return null;

			while (tree.left != null)
				tree = tree.left;
			return tree;
		}

		private T minimum() {
			AVLTreeNode<T> p = minimum(root);
			if (p != null)
				return p.key;

			return null;
		}


		/**
		 * 查找最大结点
		 *
		 * @return 返回tree为根结点的AVL树的最大结点。
		 */
		private AVLTreeNode<T> maximum(AVLTreeNode<T> tree) {
			if (tree == null)
				return null;

			while (tree.right != null)
				tree = tree.right;
			return tree;
		}

		/**
		 * @return 最大键值
		 */
		private T maximum() {
			AVLTreeNode<T> p = maximum(root);
			if (p != null)
				return p.key;

			return null;
		}


		/**
		 * LL: 左左对应的情况(左单旋转)。
		 *
		 * @param k2
		 * @return 旋转后的根节点
		 */
		private AVLTreeNode<T> SL(AVLTreeNode<T> k2) {
			AVLTreeNode<T> k1;

			k1 = k2.left;
			k2.left = k1.right;
			k1.right = k2;

			k2.height = max(height(k2.left), height(k2.right)) + 1;
			k1.height = max(height(k1.left), k2.height) + 1;
			update(k1);
			update(k2);

			return k1;
		}


		/**
		 * 右右对应的情况(右单旋转)。
		 *
		 * @param k1
		 * @return 旋转后的根节点
		 */
		private AVLTreeNode<T> SR(AVLTreeNode<T> k1) {
			AVLTreeNode<T> k2;

			k2 = k1.right;
			k1.right = k2.left;
			k2.left = k1;

			k1.height = max(height(k1.left), height(k1.right)) + 1;
			k2.height = max(height(k2.right), k1.height) + 1;
			update(k1);
			update(k2);

			return k2;
		}


		/**
		 * LR: 左右对应的情况(左双旋转)。
		 *
		 * @param k3
		 * @return 旋转后的根节点
		 */
		private AVLTreeNode<T> LR(AVLTreeNode<T> k3) {
			k3.left = SR(k3.left);

			return SL(k3);
		}


		/**
		 * RL: 右左对应的情况(右双旋转)。
		 *
		 * @param k1
		 * @return
		 */
		private AVLTreeNode<T> RL(AVLTreeNode<T> k1) {
			k1.right = SL(k1.right);

			return SR(k1);
		}


		/**
		 * @param tree:AVL树的根结点
		 * @param key:插入的结点的键值
		 * @return 根节点
		 */
		private AVLTreeNode<T> insert(AVLTreeNode<T> tree, T key) {
			if (tree == null) {
				// 新建节点
				tree = new AVLTreeNode<T>(key, null, null);
				tree.count = 1;
			} else {
				int cmp = key.compareTo(tree.key);
				if (cmp < 0) {    // 应该将key插入到"tree的左子树"的情况
					tree.left = insert(tree.left, key);
					// 插入节点后，若AVL树失去平衡，则进行相应的调节。
					if (height(tree.left) - height(tree.right) == 2) {
						if (key.compareTo(tree.left.key) < 0)
							tree = SL(tree);
						else
							tree = LR(tree);
					}
				} else if (cmp > 0) {    // 应该将key插入到"tree的右子树"的情况
					tree.right = insert(tree.right, key);
					// 插入节点后，若AVL树失去平衡，则进行相应的调节。
					if (height(tree.right) - height(tree.left) == 2) {
						if (key.compareTo(tree.right.key) > 0)
							tree = SR(tree);
						else
							tree = RL(tree);
					}
				} else {
					tree.count++;
				}
			}

			tree.height = max(height(tree.right), height(tree.left)) + 1;
			update(tree);
			return tree;
		}

		public void insert(T key) {
			root = insert(root, key);
		}

		private int getSize(AVLTreeNode<T> tree) {
			return tree == null ? 0 : tree.size;
		}

		private void update(AVLTreeNode<T> tree) {
			if (tree == null) return;
			tree.size = getSize(tree.left) + getSize(tree.right) + tree.count;

		}

		private int kth(AVLTreeNode<T> tree, int k) {
			if (tree == null) return 0;
			if (getSize(tree.left) >= k)
				return kth(tree.left, k);
			else if (getSize(tree.left) + tree.count >= k)
				return (Integer) tree.key;
			else
				return kth(tree.right, k - getSize(tree.left) - tree.count);
		}

		public int kth(int k) {
			return kth(root, k);
		}

		int Rank(AVLTreeNode root, Integer val) {
			if (root == null) return 1;
			int cmp = val.compareTo((Integer) root.key);
			if (cmp <= 0)
				return Rank(root.left, val);
			else {
				int a = (root.left == null ? 0 : root.left.size);
				int b = root.count;
				int c = Rank(root.right, val);
				int ans = a + b + c;
				return ans;
			}
		}

		public int getRank(Integer key) {
			return Rank(root, key);
		}

		private int getPre(AVLTreeNode<T> tree, Integer key) {
			if (tree == null) return Integer.MIN_VALUE;
			int cmp = key.compareTo((Integer) tree.key);
			if (cmp <= 0) {
				return getPre(tree.left, key);
			} else {
				return max((Integer) tree.key, getPre(tree.right, key));
			}
		}

		int getPre(Integer key) {
			return getPre(root, key);
		}

		int getNext(AVLTreeNode<T> tree, Integer key) {
			if (tree == null) return Integer.MAX_VALUE;
			int cmp = key.compareTo((Integer) tree.key);
			if (cmp >= 0)
				return getNext(tree.right, key);
			else
				return Math.min((Integer) tree.key, getNext(tree.left, key));
		}

		int getNext(int key) {
			return getNext(root, key);
		}


		/**
		 * @param tree:    AVL树的根结点
		 * @param z:待删除的结点
		 * @return:返回根节点
		 */
		private AVLTreeNode<T> delete(AVLTreeNode<T> tree, AVLTreeNode<T> z) {
			// 根为空 或者 没有要删除的节点，直接返回null。
			if (tree == null || z == null)
				return null;

			int cmp = z.key.compareTo(tree.key);
			if (cmp < 0) {        // 待删除的节点在"tree的左子树"中
				tree.left = delete(tree.left, z);
				// 删除节点后，若AVL树失去平衡，则进行相应的调节。
				if (height(tree.right) - height(tree.left) == 2) {
					AVLTreeNode<T> r = tree.right;
					if (height(r.left) > height(r.right))
						tree = RL(tree);
					else
						tree = SR(tree);
				}
			} else if (cmp > 0) {    // 待删除的节点在"tree的右子树"中
				tree.right = delete(tree.right, z);
				// 删除节点后，若AVL树失去平衡，则进行相应的调节。
				if (height(tree.left) - height(tree.right) == 2) {
					AVLTreeNode<T> l = tree.left;
					if (height(l.right) > height(l.left))
						tree = LR(tree);
					else
						tree = SL(tree);
				}
			} else {
				// tree是对应要删除的节点。
				if (tree.count != 1) {
					tree.count--;
					//更新大小
					update(tree);
					tree.height = max(height(tree.left), height(tree.right)) + 1;
					return tree;
				}
				// tree的左右孩子都非空
				if ((tree.left != null) && (tree.right != null)) {
					if (height(tree.left) > height(tree.right)) {
						// 如果tree的左子树比右子树高；
						// 则(01)找出tree的左子树中的最大节点
						//   (02)将该最大节点的值赋值给tree。
						//   (03)删除该最大节点。
						// 这类似于用"tree的左子树中最大节点"做"tree"的替身；
						// 采用这种方式的好处是: 删除"tree的左子树中最大节点"之后，AVL树仍然是平衡的。
						AVLTreeNode<T> max = maximum(tree.left);
						tree.key = max.key;
						tree.left = delete(tree.left, max);
					} else {
						// 如果tree的左子树不比右子树高(即它们相等，或右子树比左子树高1)
						// 则(01)找出tree的右子树中的最小节点
						//   (02)将该最小节点的值赋值给tree。
						//   (03)删除该最小节点。
						// 这类似于用"tree的右子树中最小节点"做"tree"的替身；
						// 采用这种方式的好处是: 删除"tree的右子树中最小节点"之后，AVL树仍然是平衡的。
						AVLTreeNode<T> min = minimum(tree.right);
						tree.key = min.key;
						tree.right = delete(tree.right, min);
					}
				} else {
					AVLTreeNode<T> tmp = tree;
					tree = (tree.left != null) ? tree.left : tree.right;
					tmp = null;

				}
			}

			if (tree != null)
				tree.height = max(height(tree.left), height(tree.right)) + 1;
			update(tree);
			return tree;
		}

		public void delete(T key) {
			AVLTreeNode<T> z;
			if ((z = search(root, key)) != null) root = delete(root, z);
		}


	}


	static class Read {
		private InputStream stream = in;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int numChars;
		private SpaceCharFilter filter;

		private int read() throws IOException {
			if (curChar >= numChars) {
				curChar = 0;
				numChars = stream.read(buf);
				if (numChars <= 0) return -1;
			}
			return buf[curChar++];
		}

		public int nextInt() throws IOException {
			int c = read();
			while (isSpaceChar(c)) c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			int res = 0;
			do {
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res * sgn;
		}


		public double nextDouble() throws IOException {
			int c = read();
			while (isSpaceChar(c)) c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			double res = 0;
			while (!isSpaceChar(c) && c != '.') {
				if (c == 'e' || c == 'E') return res * pow(10, nextInt());
				res *= 10;
				res += c - '0';
				c = read();
			}
			if (c == '.') {
				c = read();
				double m = 1;
				while (!isSpaceChar(c)) {
					if (c == 'e' || c == 'E') return res * pow(10, nextInt());
					m /= 10;
					res += (c - '0') * m;
					c = read();
				}
			}
			return res * sgn;
		}

		private char nextChar() throws IOException {
			int c = read();
			while (isSpaceChar(c)) c = read();
			return (char) c;
		}


		private boolean isSpaceChar(int c) {
			if (filter != null) return filter.isSpaceChar(c);
			return isWhitespace(c);
		}

		private boolean isSpaceChar2(int c) {
			if (filter != null) {
				return filter.isSpaceChar2(c);
			}
			return isWhitespace2(c);
		}

		private static boolean isWhitespace(int c) {
			return c == '\n' || c == '\r' || c == '\t' || c == -1 || c == ' ';
		}

		private static boolean isWhitespace2(int c) {
			return c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		private interface SpaceCharFilter {
			public boolean isSpaceChar(int ch);

			public boolean isSpaceChar2(int ch);
		}
	}
}
