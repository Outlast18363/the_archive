# 硬币问题

## 题目描述

今有面值为 1、5、11 元的硬币各无限枚。

想要凑出 $n$ 元，问需要的最少硬币数量。

## 输入格式

仅一行，一个正整数 $n$。

## 输出格式

仅一行，一个正整数，表示需要的硬币个数。

## 样例 #1

### 样例输入 #1

```
15
```

### 样例输出 #1

```
3
```

## 样例 #2

### 样例输入 #2

```
12
```

### 样例输出 #2

```
2
```

## 提示

#### 样例解释

对于样例数据 1，最佳方案是 $15=5+5+5$，使用到 3 枚硬币。

对于样例数据 2，最佳方案是 $12=11 + 1$，使用到 2 枚硬币。

#### 数据规模与约定

对于 $100\%$ 的数据，保证 $n\leq 10^6$。

## 算法思想
定义 $d[i]$ 为凑成 i 元需要用到的最小硬币数量，有以下状态转移方程：

$$d[i] = \min(d[i-1], d[i-5], d[i-11]) + 1$$

这里硬币数量加一，分别对应着金钱数量变化 1，5，和 11，对应着用一枚这三种硬币中的一种。每次都从三种方案中取最小解，然后加上对应的钱数。

## 题目代码

```java
import java.io.*;
import java.util.*;

public class Main {
    static FastIO in;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        in = new FastIO();
        int n = in.nextInt();
        int dp[] = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            if(i >= 11){
                dp[i] = Math.min(Math.min(dp[i - 11], dp[i - 5]), dp[i - 1]) + 1;
                continue;
            }
            if(i >= 5){
                dp[i] = Math.min(dp[i - 5], dp[i - 1]) + 1;
                continue;
            }
            dp[i] = dp[i - 1] + 1;
        }
        in.print(dp[n]);
        in.close();
    }
}

class FastIO extends PrintWriter {
    private InputStream stream;
    private byte[] buf = new byte[1 << 16];
    private int curChar, numChars;

    // standard input
    public FastIO() { this(System.in, System.out); }

    public FastIO(InputStream i, OutputStream o) {super(o); stream = i; }

    // file input
    public FastIO(String i, String o) throws IOException { //i: in || o: out
        super(new FileWriter(o)); stream = new FileInputStream(i);
    }

    // throws InputMismatchException() if previously detected end of file
    private int nextByte() {
        if (numChars == -1) { throw new InputMismatchException(); }
        if (curChar >= numChars) { curChar = 0; try { numChars = stream.read(buf);
        } catch (IOException e) { throw new InputMismatchException(); }
            if (numChars == -1) return -1;  // end of file
        } return buf[curChar++];
    }

    // to read in entire lines, replace c <= ' '
    // with a function that checks whether c is a line break
    public String next() {
        int c; do { c = nextByte(); } while (c <= ' ');
        StringBuilder res = new StringBuilder();
        do { res.appendCodePoint(c); c = nextByte(); } while (c > ' ');
        return res.toString();
    }

    public int nextInt() {
        int c, sgn = 1, res = 0;
        do { c = nextByte(); } while (c <= ' ');
        if (c == '-') { sgn = -1; c = nextByte(); }
        do { if (c < '0' || c > '9') { throw new InputMismatchException(); }
            res = 10 * res + c - '0'; c = nextByte(); } while (c > ' ');
        return res * sgn;
    }

    public long nextLong() {
        int c, sgn = 1; long res = 0;
        do { c = nextByte(); } while (c <= ' ');
        if (c == '-') { sgn = -1; c = nextByte(); }
        do { if (c < '0' || c > '9') { throw new InputMismatchException(); }
            res = 10 * res + c - '0'; c = nextByte(); } while (c > ' ');
        return 1l * res * sgn;
    }

    public double nextDouble() { return Double.parseDouble(next()); }
}
```
