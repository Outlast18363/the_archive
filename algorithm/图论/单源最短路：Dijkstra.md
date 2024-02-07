# 【模板】单源最短路径

## 题目描述

给定一个 $n$ 个点， $m$ 条有向边的带非负权图，请你计算从 $s$ 出发，到每个点的距离。

数据保证你能从 $s$ 出发到任意点。

## 输入格式

第一行为三个正整数 $n, m, s$。
第二行起 $m$ 行，每行三个非负整数 $u_i, v_i, w_i$，表示从 $u_i$ 到 $v_i$ 有一条权值为 $w_i$ 的有向边。

## 输出格式

输出一行 $n$ 个空格分隔的非负整数，表示 $s$ 到每个点的距离。

## 样例 #1

### 样例输入 #1

```
4 6 1
1 2 2
2 3 2
2 4 1
1 3 5
3 4 3
1 4 4
```

### 样例输出 #1

```
0 2 4 3
```

## 提示

样例解释请参考 [数据随机的模板题](https://www.luogu.org/problemnew/show/P3371)。

$1 \leq n \leq 10^5$；

$1 \leq m \leq 2\times 10^5$；

$s = 1$；

$1 \leq u_i, v_i\leq n$；

$0 \leq w_i \leq 10 ^ 9$,

$0 \leq \sum w_i \leq 10 ^ 9$。

---

## 算法思路
<img width="1034" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/1f3454f2-f1bd-48a8-9ee1-d43245dfdbcf">
每次选出离白点集合最近的蓝点，将这个蓝点变成白点，然后更新这个点相邻的所有点的距离（可以理解为只更新蓝点距离，因为白点已是最短距离）。

## 代码
```java
import java.io.*;
import java.util.*;

public class Main {
    static class Edge{
        int to, w; //去到的点，权重
        Edge(int t, int w){
            to = t; this.w = w;
        }
    }
    static ArrayList<Edge> graph[];
    static PriorityQueue<Edge> q = new PriorityQueue<>((a, b) -> a.w - b.w); //按权从小到大
    static int dis[], inf = (int) 1e9 + 5;
    static boolean vis[];
    static Kattio in;

    public static void main(String[] args) throws IOException {
        in = new Kattio();
        int n = in.nextInt(), m = in.nextInt(), s = in.nextInt(); //n： 点个数，m：边个数，s：源点编号
        graph = new ArrayList[n+1];
        dis = new int[n+1]; //dis[i]：i 点到原点的距离
        vis = new boolean[n+1]; //一个点是否已被标记为白点
        for(int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
            dis[i] = inf;
        }

        //建图
        for(int i = 0; i < m; i++){
            int u = in.nextInt(), v = in.nextInt(), w = in.nextInt(); //u: from, v: to, w: weight
            graph[u].add(new Edge(v, w)); //加入权边
        }

        dij(s);

        for(int i = 1; i <= n; i++) in.print(dis[i] + " ");
        in.close();
    }

     static void dij(int s){
        dis[s] = 0;
        q.add(new Edge(s, 0));

        while(!q.isEmpty()){
            Edge e = q.poll();
            int u = e.to;
            if(vis[u]) continue; //如果已经是白点就下一个
            vis[u] = true; //标记为白点

            //更新这个白点相邻节点的距离
            for(Edge ed : graph[u]){
                int next = ed.to; int d = ed.w;
                if(dis[u] + d < dis[next]){
                    dis[next] = dis[u] + d; //松弛化（更新最短距离）
                    q.add(new Edge(next, dis[next]));
                }
            }
        }
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
