package cn.net.xyd.Test;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class Test {

    public static void main(String[] args) {
        int x = 100 / 5;
        int y = 100 / 3;
        int z = 100 * 3;
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        //公母小
        for (int i = 1; i < x; i++) {
            for (int j = 1; j < y; j++) {
                for (int k = 3; k < 333; k = k + 3) {
                    if ((i * 5 + j * 3 + k / 3) == 100 && (i+j+k==100)) {
                        System.out.println("公鸡：" + i + ";母鸡：" + j + ";小鸡：" + k+"共计："+(i+j+k));
                        count1++;
                    }
                }
            }
        }
        System.out.println("共计："+count1+"条");
        /*//公小母
        for (int i = 1; i < x; i++) {
            for (int k = 3; k < 333; k = k + 3) {
                for (int j = 1; j < y; j++) {
                    if ((i * 5 + j * 3 + k / 3) == 100) {
                        System.out.println("公鸡：" + i + ";母鸡：" + j + ";小鸡：" + k+"共计："+(i+j+k));
                        count2++;
                    }
                }
            }
        }
        System.out.println("共计："+count2+"条");
        //母公小
        for (int j = 1; j < y; j++) {
            for (int i = 1; i < x; i++) {
                for (int k = 3; k < 333; k = k + 3) {
                    if ((i * 5 + j * 3 + k / 3) == 100) {
                        System.out.println("公鸡：" + i + ";母鸡：" + j + ";小鸡：" + k+"共计："+(i+j+k));
                        count3++;
                    }
                }
            }
        }
        System.out.println("共计："+count2+"条");*/
        /*//母小公
        for (int j = 1; j < y; j++) {
            for (int k = 3; k < 333; k = k + 3) {
                for (int i = 1; i < x; i++) {
                    if ((i * 5 + j * 3 + k / 3) == 100) {
                        System.out.println("公鸡：" + i + ";母鸡：" + j + ";小鸡：" + k+"共计："+(i+j+k));
                    }
                }
            }
        }
        //小公母
        for (int k = 3; k < 333; k = k + 3) {
            for (int i = 1; i < x; i++) {
                for (int j = 1; j < y; j++) {
                    if ((i * 5 + j * 3 + k / 3) == 100) {
                        System.out.println("公鸡：" + i + ";母鸡：" + j + ";小鸡：" + k+"共计："+(i+j+k));
                    }
                }
            }
        }
        //小母公
        for (int k = 3; k < 333; k = k + 3) {
            for (int j = 1; j < y; j++) {
                for (int i = 1; i < x; i++) {
                    if ((i * 5 + j * 3 + k / 3) == 100) {
                        System.out.println("公鸡：" + i + ";母鸡：" + j + ";小鸡：" + k+"共计："+(i+j+k));
                    }
                }
            }
        }*/
    }
}
