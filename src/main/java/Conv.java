package main.java;

public class Conv {

    public static int to64From120(int location120) {
        return ((location120 - (location120/10)*2)-17);
    }

    public static int to120From64(int location64) {
        return location64 + 21 + ((location64/8)*2);
    }

    public static int to64RC(int rowpass, int colpass) {
        return (rowpass * 8) + colpass;
    }

    public static int to120RC(int rowpass, int colpass) {
        return to120From64(to64RC(rowpass, colpass));
    }

    public static int[] toRC120(int location120) {
        int[] RC = new int[2];
        RC[0] = to64From120(location120)/8;
        RC[1] = to64From120(location120)%8;
        return RC;
    }

}
