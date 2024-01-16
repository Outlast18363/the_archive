# LCS 最长公共子序列



### 题目要求：

给出 String a 和 String b，求 a 和 b 的最长公共子序列。



首先设定 $LCS[m][n]$为（a 的 m 字符长度的前缀）和 （b 的 n 字符长度的前缀）的 LCS



因为我们最终想要求出a 整个和b 整个的 LCS，所以我们最终想知道$LCS[a.length()][b.length()]$

**举例**

```java
String a = "abc123";
"abc"	//a 的 3 字符长度的前缀
```



我们在递推的过程中有两种情况：

第一

如果a.charAt(i) == b.charAt(j)，那么 $LCS[i][j]$一定是 $LCS[i-1][j-1] + b.charAt(j)$
<img width="767" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/8ffaf18e-4677-4380-9b33-945d86c2a005">



举例：

```java
String a = "abc";
String b = "abc";
//设：（a 的 2 字符长度的前缀）为："ab" (注意这里不是 LCS， 而是 a 的前 2 个字符组成的字串)
//设：（b 的 2 字符长度的前缀）为："ab"

//根据定义，LCS[i=2][j=2] = "ab"
//如果a.charAt(i) == b.charAt(j)
//那么LCS[i=3][j=3] = LCS[i=2][j=2] + 'c';

```

<img width="406" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/4d310691-de20-4dd4-a415-f96dfae8973b">


图片注释：a，b的前缀 LCS 不变，然后 a，b的 LCS 肯定是在a，b的前缀 LCS的基础上加入他们最后一个相同的字符。（这里的例子是当只剩最后一个字符没有递推到时的情况）



第二

<img width="708" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/c621aba1-61fd-4407-955a-2e9f16365980">


<img width="399" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/54778e4f-d741-4913-aa14-f931d230c128">


图片注释：

在 a，b字符串最后一个 char 不相等的情况下，要不就是取左边黄色中两个字串的 LCS，要不就是取右边黄色中两个字串的 LCS，因为后面的LCS 长度不会变短，所以现在取LCS 肯定是两种情况中最长的最好。



## 代码：

```java
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String a = "abec", b = "abaec";
        int n = a.length()+1, m = b.length()+1;
        String LCS[][] = new String[a.length()+1][b.length()+1];
        
        // Initialize the dp array
        for(int i = 0; i <= a.length(); i++){
            for(int j = 0; j <= b.length(); j++){
                LCS[i][j] = "";
            }
        }

        // Fill the dp table
        for(int i = 1; i <= a.length(); i++){
            for(int j = 1; j <= b.length(); j++){
                if(a.charAt(i-1) == b.charAt(j-1)) {
                    LCS[i][j] = LCS[i-1][j-1] + a.charAt(i-1);
                } else if(LCS[i][j-1].length() > LCS[i-1][j].length()) { //就是肯定要从选a[i]的 LCS 和不选a[i]选b[i]的 LCS 里选一个
                    LCS[i][j] = LCS[i][j-1];
                } else {
                    LCS[i][j] = LCS[i-1][j];
                }
            }
        }

        // Print the result
        System.out.print(LCS[a.length()][b.length()]);
    }
}
```

