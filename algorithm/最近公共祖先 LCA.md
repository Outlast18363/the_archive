# 【模板】最近公共祖先（LCA）

## 题目描述

如题，给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。

## 输入格式

第一行包含三个正整数 $N,M,S$，分别表示树的结点个数、询问的个数和树根结点的序号。

接下来 $N-1$ 行每行包含两个正整数 $x, y$，表示 $x$ 结点和 $y$ 结点之间有一条直接连接的边（数据保证可以构成树）。

接下来 $M$ 行每行包含两个正整数 $a, b$，表示询问 $a$ 结点和 $b$ 结点的最近公共祖先。

## 输出格式

输出包含 $M$ 行，每行包含一个正整数，依次为每一个询问的结果。

## 样例 #1

### 样例输入 #1

```
5 5 4
3 1
2 4
5 1
1 4
2 4
3 2
3 5
1 2
4 5
```

### 样例输出 #1

```
4
4
1
4
4
```

## 提示

对于 $30\%$ 的数据， $N \leq 10$， $M\leq 10$。

对于 $70\%$ 的数据， $N \leq 10000$， $M\leq 10000$。

对于 $100\%$ 的数据， $1 \leq N,M\leq 500000$， $1 \leq x, y,a ,b \leq N$，**不保证** $a \neq b$。


样例说明：

该树结构如下：

 ![](https://cdn.luogu.com.cn/upload/pic/2282.png) 

第一次询问： $2, 4$ 的最近公共祖先，故为 $4$。

第二次询问： $3, 2$ 的最近公共祖先，故为 $4$。

第三次询问： $3, 5$ 的最近公共祖先，故为 $1$。

第四次询问： $1, 2$ 的最近公共祖先，故为 $4$。

第五次询问： $4, 5$ 的最近公共祖先，故为 $4$。

故输出依次为 $4, 4, 1, 4, 4$。

---

##  思路 & 关于为什么可以正好跳到 LCA 前一层的原因
<img width="757" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/4dd65ef9-1096-41c8-94a7-66c6ebd07f28">


## 题目代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<Integer> adj[];
    static int dep[], fa[][];
    static Kattio in;
    public static void main(String[] args) throws IOException {
        in = new Kattio();
        int n = in.nextInt(), m = in.nextInt(), s = in.nextInt(); //节点数，询问数，根节点标号
        adj = new ArrayList[n+1];
        for (int i = 0; i <= n; i++) adj[i] = new ArrayList<>();
        dep = new int[n+1]; //dep[i]: 节点 i 的深度
        fa = new int[n+1][20]; //fa[i][k]表示从节点 i 往上跳 2^k 步的父节点

        for(int i=1; i<n; i++){
            int a = in.nextInt(), b = in.nextInt();
            adj[a].add(b);
            adj[b].add(a);
        }

        dfs(s, 0); //以 0 为超级根节点 （根节点的 father）
        while(m-->0){
            int a = in.nextInt(), b = in.nextInt();
            if(m>0) in.println(lca(a, b));
            else in.print(lca(a, b));
        }
        in.close();
    }
    static void dfs(int u, int father){
        dep[u] = dep[father] + 1;

        fa[u][0] = father;
        for(int i=1; i<=19; i++) fa[u][i] = fa[fa[u][i-1]][i-1]; //对这个节点倍增求它的所有父亲
        
        for(int v : adj[u]) { //对 u 的子节点继续 dfs
            if(v != father) dfs(v, u);
        }
    }
    static int lca(int u, int v){
        if(dep[u] < dep[v]){
            int temp = u; u = v; v = temp;
        }

        //两个点先跳到同一层 (开始倍增)
        for(int i=19; i>=0; i--){
            if(dep[fa[u][i]] >= dep[v]) u = fa[u][i];
        }

        if (u==v) return u; //v 就是 u 的父节点（所以 u 和 v 的 LCA 就是 v）

        //一起往上继续找 (倍增)
        for(int i=19; i>=0; i--){
            //当祖先不是一样的时候继续跳，这样保证调到 LCA 的前一层
            //(在树中一个子节点不可能有多个父节点，所以 father 一样代表 LCA 已经过了，不能跳)
            if(fa[u][i] != fa[v][i]){ 
                u = fa[u][i]; //没过 LCA 在的层数就继续跳
                v = fa[v][i];
            }
        }
        return fa[u][0];
    }
}

class Kattio extends PrintWriter {
    private BufferedReader r;
    private StringTokenizer st;
    // standard input
    public Kattio() { this(System.in,System.out); }
    public Kattio(InputStream i, OutputStream o) {
        super(o);
        r = new BufferedReader(new InputStreamReader(i));
    }
    // USACO-style file input
    public Kattio(String problemName) throws IOException {
        super(problemName+".out");
        r = new BufferedReader(new FileReader(problemName+".in"));
    }
    // returns null if no more input
    public String next() {
        try {
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(r.readLine());
            return st.nextToken();
        } catch (Exception e) {}
        return null;
    }
    public String nextLine() {
        try {return r.readLine();} catch (Exception e) {}
        return null;
    }
    public int nextInt() { return Integer.parseInt(next()); }
    public double nextDouble() { return Double.parseDouble(next()); }
    public long nextLong() { return Long.parseLong(next()); }
}
```

