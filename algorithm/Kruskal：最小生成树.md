# [USACO3.1] 最短网络 Agri-Net

## 题目背景

Farmer John 被选为他们镇的镇长！他其中一个竞选承诺就是在镇上建立起互联网，并连接到所有的农场。当然，他需要你的帮助。

## 题目描述

FJ 已经给他的农场安排了一条高速的网络线路，他想把这条线路共享给其他农场。为了用最小的消费，他想铺设最短的光纤去连接所有的农场。

你将得到一份各农场之间连接费用的列表，你必须找出能连接所有农场并所用光纤最短的方案。每两个农场间的距离不会超过 $10^5$。

## 输入格式

第一行农场的个数 $N$（ $3 \leq N \leq 100$ ）。

接下来是一个 $N \times N$ 的矩阵，表示每个农场之间的距离。理论上，他们是 $N$ 行，每行由 $N$ 个用空格分隔的数组成，实际上，由于每行 $80$ 个字符的限制，因此，某些行会紧接着另一些行。当然，对角线将会是 $0$，因为不会有线路从第 $i$ 个农场到它本身。

## 输出格式

只有一个输出，其中包含连接到每个农场的光纤的最小长度。

## 样例 #1

### 样例输入 #1

```
4
0 4 9 21
4 0 8 17
9 8 0 16
21 17 16 0
```

### 样例输出 #1

```
28
```

---
## 思路
将所有的边导入**优先队列**，从小到大排序。
然后从小往大 pop edge，如果一个 edge 联通新的节点（就是当并查集判定这两个点当前不联通时）就将这个 edge 加入最小生成树，用并查集把这两个点**union**一下（表示把这条边连上了），同时将没访问过的节点数减一，当没被访问过的节点数为 1 时，代表所有节点都被访问过，因此最小生成树完成。

### 注意：
实际上有**路径压缩**就没必要**按秩合并**了，因为路压会改变一个节点子树 size，因此两个优化同时有是没有意义的（因为 size 都不准确了）。代码中有这个小冗余是时代的眼泪 233.

## 代码
本代码写于 2023 年 1 月，谨以纪念。

```java
import java.io.*;
import java.util.*;

public class Main {
    static class Edge{
        int from,to,w;
        Edge(int a, int b, int c){
            from=a;
            to=b;
            w=c;
        }
    }
    static PriorityQueue<Edge> graph= new PriorityQueue<Edge>(11, (a,b) -> a.w-b.w);
    static int parent[], size[];
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        int n=in.nextInt(); //农场（节点）数量
        parent=new int[n];
        size=new int[n];
        for(int i=0; i<n; i++){
            parent[i]=i; //每个父亲初始化成自己
            size[i]=1;
            for(int j=0; j<n; j++){
                graph.add(new Edge(i,j,in.nextInt())); //加边
            }
        }
        int c=n, min=0;
        while(graph.size()>0){
            Edge e=graph.poll();
            if(c==1) break;
            if(find(e.from)!=find(e.to)){ //如果这个边联通一个之前不联通的点
                c--; //每当联通一个新点时，就将 c 减一，当 c=1时，所有点都联通
                union(e.from,e.to);
                min+=e.w; //最小权总和
            }
        }
        System.out.print(min);
    }
    static void union(int a, int b){
        int pa=find(a), pb=find(b);
        if(pa==pb) return;
        if(size[pa]<size[pb]){ //实际上已经路径压缩就没必要秩合并了，因为路压会改变一个节点子树 size。
            parent[pa]=pb;
            size[pb]+=size[pa];
        }
        else{
            parent[pb]=pa;
            size[pa]+=size[pb];
        }
    }
    static int find(int a){
        if(parent[a]==a) return a;
        return parent[a]=find(parent[a]); //路径压缩
    }
}
```
