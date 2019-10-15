package com.steer.concurrent.lock;

public class SynchronizedThread  implements Runnable{
    private static int a = 0;

    //1.修饰非静态方法,锁定的是当前实例对象this
    synchronized void add(){
        a++;
    }

//    synchronized(this) void foo2(){
//
//
//    }

    //2.修饰静态方法,锁定的是当前类的Class对象
    synchronized static void add2(){
        a++;
    }

//    synchronized (SynchronizedThread.class) static void bar2(){
//
//
//    }

    //3.修饰代码块
    Object object = new Object();
    void add3(){
        synchronized (object){
            a++;
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 20000; i++) {
            add3();
        }
    }



    public int getValue(){
        return a;
    }
}
