# [USACO04OPEN] MooFest G

## 题目背景

[P2345 [USACO04OPEN] MooFest G](https://www.luogu.com.cn/problem/P2345)

## 题目描述

约翰的 $n$ 头奶牛每年都会参加“哞哞大会”。

哞哞大会是奶牛界的盛事。集会上的活动很多，比如堆干草，跨栅栏，摸牛仔的屁股等等。

它们参加活动时会聚在一起，第 $i$ 头奶牛的坐标为 $x_i$，没有两头奶牛的坐标是相同的。

奶牛们的叫声很大，第 $i$ 头和第 $j$ 头奶牛交流，会发出
$\max\{v_i,v_j\}\times |x_i − x_j |$ 
的音量，其中 $v_i$ 和 $v_j$ 分别是第 $i$ 头和第 $j$ 头奶牛的听力。

假设每对奶牛之间同时都在说话，请计算所有奶牛产生的音量之和是多少。

## 输入格式

第一行：单个整数 $n$，$1\le n\le2\times 10^4$

第二行到第 $n + 1$ 行：第 $i + 1$ 行有两个整数 $v_i$ 和 $x_i$（$1\le v_i,x_i\le2\times 10^4$）。

## 输出格式

单个整数：表示所有奶牛产生的音量之和

## 样例 #1

### 样例输入 #1

```
4
3 1
2 5
2 6
4 3
```

### 样例输出 #1

```
57
```

## 题目代码


```Java
import java.io.*;
import java.util.*;

public class Main {
  	static Kattio in;
    static Cow[] cows;
	static long sum=0;
	static class Cow{
		int v;
		int x;
		public Cow(int v, int x){
			this.v=v; //牛的音量
			this.x=x; //牛的位置
		}
	}

    public static void main(String[] args) throws IOException {
        in = new Kattio();
        int n = in.nextInt();
		cows = new Cow[n];
		for(int i=0; i<n; i++){
			cows[i] = new Cow(in.nextInt(),in.nextInt());
		}
		Arrays.sort(cows, (a,b) -> a.v-b.v);
        merge(0,n-1);
        in.print(sum);
        in.close();
    }

    static void merge(int l, int r){
        if(r == l) return;
        int mid = (l + r) / 2;
        merge(l, mid);
        merge(mid + 1, r);

        //cdq 分治 计算贡献
        int sum1=0, sum2=0;
        
        //求坐标和 以及 计算锚点牛坐标
        for(int i=l; i<=mid; i++) sum1+=cows[i].x;

        for(int a=l, b=mid+1; b<=r; b++){ //每次选择一个 b 来计算贡献 （因为 b 右边的所有牛的 v 都一定比左边大）
            
            //以及调整坐标和

            while(a<=mid && cows[a].x < cows[b].x){
                sum1 -= cows[a].x; //坐标比 b 牛 大的坐标和
                sum2 += cows[a].x; //坐标比 b 牛 小的坐标和
                a++;
            }

            //（a 这里泛指左边 v 小的数组）（b 这里泛指右边 v 大的数组）

            //a，b 数组在 x 的意义上现在各自都有序，所以当上面 loop 中（cows[a].x < cows[b].x）条件破坏时，
            //就意味着在数组a 中，牛 a 以前的 x 坐标都比 选定的牛 b 小，后面的x 坐标都>= 牛b

            int cnt1 = a-l; //a 中坐标比 b 牛小的牛数量
            int cnt2 = mid-a+1; //a 中坐标比 b 牛大的牛数量

            //计算贡献
            sum += 1l*cows[b].v * (cnt1 * cows[b].x - sum2) + 1l*cows[b].v * (sum1 - cnt2 * cows[b].x);
        }

        //merge
        //merge 部分要放在 计算贡献后面 因为在计算的时候，需要两边数组各自有序，但是还没有合并时的状态
        Cow temp[] = new Cow[r-l+1];
        int lp=l, rp=mid+1, p=0;
        while(lp<=mid && rp<=r){
            if(cows[lp].x<cows[rp].x) temp[p]=cows[lp++];
            else temp[p] = cows[rp++];
            p++;
        }
        //把剩下那边，没放的放完
        while(lp<=mid) temp[p++] = cows[lp++];
        while(rp<=r) temp[p++] = cows[rp++];
        for(int i=0; i<temp.length; i++) cows[l+i]=temp[i];

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
